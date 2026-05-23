package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData
import com.itsjeel01.finsiblefrontend.ui.mapper.toUiModel
import com.itsjeel01.finsiblefrontend.ui.model.state.AccountListItem
import com.itsjeel01.finsiblefrontend.ui.model.state.AccountsUIState
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.StatEntryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountLocalRepository: AccountLocalRepository,
    private val accountGroupLocalRepository: AccountGroupLocalRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _selectedGroupId = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<AccountsUIState> = combine(
        accountLocalRepository.getAccountsFlow(),
        accountGroupLocalRepository.getAccountGroupsFlow(),
        _selectedGroupId,
        preferenceManager.defaultCurrencyCodeFlow
    ) { accounts, groups, selectedGroupId, currencyCode ->
        withContext(Dispatchers.Default) {
            computeUiState(accounts, groups, selectedGroupId, currencyCode)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountsUIState(isLoading = true)
    )

    fun selectGroupFilter(groupId: Long?) {
        _selectedGroupId.value = groupId
    }

    private fun computeUiState(
        accounts: List<AccountEntity>,
        groups: List<AccountGroupEntity>,
        selectedGroupId: Long?,
        currencyCode: String
    ): AccountsUIState {

        val (totalAssetCentis, totalLiabilityCentis) = calculateTotals(accounts)
        val netWorthCentis = totalAssetCentis - totalLiabilityCentis

        val cards = if (accounts.isEmpty()) emptyList() else buildList {
            add(createNetWorthCard(netWorthCentis, totalAssetCentis, totalLiabilityCentis, currencyCode))

            val assetStats = buildGroupedStatistics(accounts, { it >= 0L }, { it.balanceCentis }, currencyCode)
            if (assetStats.isNotEmpty()) add(createAssetsCard(totalAssetCentis, assetStats, currencyCode))

            val liabilityStats = buildGroupedStatistics(accounts, { it < 0L }, { -it.balanceCentis }, currencyCode)
            if (liabilityStats.isNotEmpty()) add(createLiabilitiesCard(totalLiabilityCentis, liabilityStats, currencyCode))
        }

        val filteredAccounts = if (selectedGroupId == null) accounts
        else accounts.filter { it.accountGroup.target?.id == selectedGroupId }

        val listItems = filteredAccounts
            .groupBy { it.accountGroup.target?.name ?: context.getString(R.string.others) }
            .flatMap { (groupName, accountsInGroup) ->
                buildList {
                    if (selectedGroupId == null) add(AccountListItem.Header(groupName))
                    addAll(accountsInGroup.map { AccountListItem.Account(it.toUiModel(currencyFormatter, currencyCode)) })
                }
            }

        return AccountsUIState(
            tileCards = cards.toPersistentList(),
            listItems = listItems.toPersistentList(),
            accountGroups = groups.map { it.toUiModel() }.toPersistentList(),
            selectedGroupId = selectedGroupId,
            isLoading = false
        )
    }

    private fun calculateTotals(accounts: List<AccountEntity>): Pair<Long, Long> {
        var assets = 0L
        var liabilities = 0L
        for (account in accounts) {
            if (account.balanceCentis >= 0L) assets += account.balanceCentis
            else liabilities += -account.balanceCentis
        }
        return assets to liabilities
    }

    private fun createNetWorthCard(netWorthCentis: Long, assetCentis: Long, liabilityCentis: Long, currencyCode: String) = FinsibleTileCardData(
        title = context.getString(R.string.net_worth),
        heroText = currencyFormatter.format(centis = netWorthCentis, currencyCode = currencyCode),
        statistics = listOf(
            StatEntryUIModel(
                context.getString(R.string.assets),
                currencyFormatter.formatCompact(centis = assetCentis, currencyCode = currencyCode)
            ),
            StatEntryUIModel(
                context.getString(R.string.liabilities),
                currencyFormatter.formatCompact(centis = liabilityCentis, currencyCode = currencyCode)
            )
        ).toPersistentList()
    )

    private fun createAssetsCard(totalAssetCentis: Long, statistics: List<StatEntryUIModel>, currencyCode: String) = FinsibleTileCardData(
        title = context.getString(R.string.total_assets),
        heroText = currencyFormatter.format(centis = totalAssetCentis, currencyCode = currencyCode),
        statistics = statistics.toPersistentList()
    )

    private fun createLiabilitiesCard(totalLiabilityCentis: Long, statistics: List<StatEntryUIModel>, currencyCode: String) = FinsibleTileCardData(
        title = context.getString(R.string.total_liabilities),
        heroText = currencyFormatter.format(centis = totalLiabilityCentis, currencyCode = currencyCode),
        statistics = statistics.toPersistentList()
    )

    private fun buildGroupedStatistics(
        accounts: List<AccountEntity>,
        includePredicate: (Long) -> Boolean,
        valueSelector: (AccountEntity) -> Long,
        currencyCode: String
    ): List<StatEntryUIModel> {
        val matchingAccounts = accounts.filter { includePredicate(it.balanceCentis) }
        if (matchingAccounts.isEmpty()) return emptyList()

        val (orphanAccounts, groupedAccounts) = matchingAccounts.partition { it.accountGroup.target == null }
        val orphanTotal = orphanAccounts.sumOfCentis(valueSelector)

        val namedGroups = groupedAccounts
            .groupBy { it.accountGroup.target!!.name }
            .map { (name, groupAccounts) -> name to groupAccounts.sumOfCentis(valueSelector) }
            .sortedByDescending { it.second }

        return buildList {
            namedGroups.take(2).forEach { (name, totalCentis) ->
                add(StatEntryUIModel(name, currencyFormatter.formatCompact(centis = totalCentis, currencyCode = currencyCode)))
            }

            var othersTotalCentis = orphanTotal
            if (namedGroups.size > 2) othersTotalCentis += namedGroups.drop(2).sumOf { it.second }

            if (othersTotalCentis > 0L) {
                add(
                    StatEntryUIModel(
                        context.getString(R.string.others),
                        currencyFormatter.formatCompact(centis = othersTotalCentis, currencyCode = currencyCode)
                    )
                )
            }
        }
    }

    private fun List<AccountEntity>.sumOfCentis(selector: (AccountEntity) -> Long): Long =
        fold(0L) { acc, account -> acc + selector(account) }
}