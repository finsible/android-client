package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.centisToCompactCurrency
import com.itsjeel01.finsiblefrontend.common.centisToFormattedCurrency
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
    private val currencyFormatter: CurrencyFormatter
) : ViewModel() {

    private val _selectedGroupId = MutableStateFlow<Long?>(null)

    private val initialState: AccountsUIState by lazy {
        computeUiState(
            accounts = accountLocalRepository.getAll(),
            groups = accountGroupLocalRepository.getAll(),
            selectedGroupId = null
        )
    }

    val uiState: StateFlow<AccountsUIState> = combine(
        accountLocalRepository.getAccountsFlow(),
        accountGroupLocalRepository.getAccountGroupsFlow(),
        _selectedGroupId
    ) { accounts, groups, selectedGroupId ->
        withContext(Dispatchers.Default) {
            computeUiState(accounts, groups, selectedGroupId)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialState
    )

    fun selectGroupFilter(groupId: Long?) {
        _selectedGroupId.value = groupId
    }

    private fun computeUiState(
        accounts: List<AccountEntity>,
        groups: List<AccountGroupEntity>,
        selectedGroupId: Long?
    ): AccountsUIState {

        val (totalAssetCentis, totalLiabilityCentis) = calculateTotals(accounts)
        val netWorthCentis = totalAssetCentis - totalLiabilityCentis

        val cards = if (accounts.isEmpty()) {
            emptyList()
        } else {
            buildList {
                add(createNetWorthCard(netWorthCentis, totalAssetCentis, totalLiabilityCentis))

                val assetStats = buildGroupedStatistics(
                    accounts,
                    includePredicate = { it >= 0L },
                    valueSelector = { it.balanceCentis }
                )
                if (assetStats.isNotEmpty()) add(createAssetsCard(totalAssetCentis, assetStats))

                val liabilityStats = buildGroupedStatistics(
                    accounts,
                    includePredicate = { it < 0L },
                    valueSelector = { -it.balanceCentis }
                )
                if (liabilityStats.isNotEmpty()) add(createLiabilitiesCard(totalLiabilityCentis, liabilityStats))
            }
        }

        val filteredAccounts = if (selectedGroupId == null) accounts
        else accounts.filter { it.accountGroup.target?.id == selectedGroupId }

        val listItems = filteredAccounts
            .groupBy { it.accountGroup.target?.name ?: context.getString(R.string.others) }
            .flatMap { (groupName, accountsInGroup) ->
                buildList {
                    if (selectedGroupId == null) add(AccountListItem.Header(groupName))
                    addAll(accountsInGroup.map { AccountListItem.Account(it.toUiModel(currencyFormatter)) })
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

    /** Returns (totalAssetCentis, totalLiabilityCentis) — liabilities are positive (magnitude). */
    private fun calculateTotals(accounts: List<AccountEntity>): Pair<Long, Long> {
        var assets = 0L
        var liabilities = 0L
        for (account in accounts) {
            if (account.balanceCentis >= 0L) assets += account.balanceCentis
            else liabilities += -account.balanceCentis
        }
        return assets to liabilities
    }

    private fun createNetWorthCard(netWorthCentis: Long, assetCentis: Long, liabilityCentis: Long) = FinsibleTileCardData(
        title = context.getString(R.string.net_worth),
        heroText = netWorthCentis.centisToFormattedCurrency(currencyFormatter),
        statistics = listOf(
            StatEntryUIModel(context.getString(R.string.assets), assetCentis.centisToCompactCurrency(currencyFormatter)),
            StatEntryUIModel(context.getString(R.string.liabilities), liabilityCentis.centisToCompactCurrency(currencyFormatter))
        ).toPersistentList()
    )

    private fun createAssetsCard(totalAssetCentis: Long, statistics: List<StatEntryUIModel>) = FinsibleTileCardData(
        title = context.getString(R.string.total_assets),
        heroText = totalAssetCentis.centisToFormattedCurrency(currencyFormatter),
        statistics = statistics.toPersistentList()
    )

    private fun createLiabilitiesCard(totalLiabilityCentis: Long, statistics: List<StatEntryUIModel>) = FinsibleTileCardData(
        title = context.getString(R.string.total_liabilities),
        heroText = totalLiabilityCentis.centisToFormattedCurrency(currencyFormatter),
        statistics = statistics.toPersistentList()
    )

    private fun buildGroupedStatistics(
        accounts: List<AccountEntity>,
        includePredicate: (Long) -> Boolean,
        valueSelector: (AccountEntity) -> Long
    ): List<StatEntryUIModel> {
        val matchingAccounts = accounts.filter { includePredicate(it.balanceCentis) }
        if (matchingAccounts.isEmpty()) return emptyList()

        val (orphanAccounts, groupedAccounts) = matchingAccounts.partition {
            it.accountGroup.target == null
        }

        val orphanTotal = orphanAccounts.sumOfCentis(valueSelector)

        val namedGroups = groupedAccounts
            .groupBy { it.accountGroup.target!!.name }
            .map { (name, groupAccounts) -> name to groupAccounts.sumOfCentis(valueSelector) }
            .sortedByDescending { it.second }

        return buildList {
            namedGroups.take(2).forEach { (name, totalCentis) ->
                add(StatEntryUIModel(name, totalCentis.centisToCompactCurrency(currencyFormatter)))
            }

            var othersTotalCentis = orphanTotal
            if (namedGroups.size > 2) {
                othersTotalCentis += namedGroups.drop(2).sumOf { it.second }
            }

            if (othersTotalCentis > 0L) {
                add(StatEntryUIModel(context.getString(R.string.others), othersTotalCentis.centisToCompactCurrency(currencyFormatter)))
            }
        }
    }

    private fun List<AccountEntity>.sumOfCentis(selector: (AccountEntity) -> Long): Long =
        fold(0L) { acc, account -> acc + selector(account) }
}
