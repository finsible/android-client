package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.icu.math.BigDecimal
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.toCompactCurrency
import com.itsjeel01.finsiblefrontend.common.toFormattedCurrency
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.ui.model.AccountListItem
import com.itsjeel01.finsiblefrontend.ui.model.AccountUiModel
import com.itsjeel01.finsiblefrontend.ui.model.AccountsUiState
import com.itsjeel01.finsiblefrontend.ui.model.FlippableCardData
import com.itsjeel01.finsiblefrontend.ui.model.StatisticsModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val accountLocalRepository: AccountLocalRepository,
    private val accountGroupLocalRepository: AccountGroupLocalRepository,
    private val currencyFormatter: CurrencyFormatter
) : ViewModel() {

    private val _selectedGroupId = MutableStateFlow<Long?>(null)

    private val initialState: AccountsUiState by lazy {
        computeUiState(
            accounts = accountLocalRepository.getAll(),
            groups = accountGroupLocalRepository.getAll(),
            selectedGroupId = null
        )
    }

    val uiState: StateFlow<AccountsUiState> = combine(
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
    ): AccountsUiState {

        val (totalAssets, totalLiabilities) = calculateTotals(accounts)
        val netWorth = totalAssets.subtract(totalLiabilities)

        val cards = if (accounts.isEmpty()) {
            emptyList()
        } else {
            buildList {
                add(createNetWorthCard(netWorth, totalAssets, totalLiabilities))

                val assetStats = buildGroupedStatistics(
                    accounts,
                    includePredicate = { it.signum() >= 0 },
                    valueSelector = { it.balance }
                )
                if (assetStats.isNotEmpty()) add(createAssetsCard(totalAssets, assetStats))

                val liabilityStats = buildGroupedStatistics(
                    accounts,
                    includePredicate = { it.signum() < 0 },
                    valueSelector = { it.balance.abs() }
                )
                if (liabilityStats.isNotEmpty()) add(createLiabilitiesCard(totalLiabilities, liabilityStats))
            }
        }

        val filteredAccounts = if (selectedGroupId == null) {
            accounts
        } else {
            accounts.filter { it.accountGroup.target?.id == selectedGroupId }
        }

        val listItems = filteredAccounts
            .groupBy { it.accountGroup.target?.name ?: "Others" }
            .flatMap { (groupName, accountsInGroup) ->
                buildList {
                    if (selectedGroupId == null) {
                        add(AccountListItem.Header(groupName))
                    }
                    addAll(accountsInGroup.map { entity ->
                        AccountListItem.Account(
                            AccountUiModel(
                                id = entity.id,
                                name = entity.name,
                                description = entity.description,
                                icon = entity.icon,
                                formattedBalance = entity.balance.toFormattedCurrency(currencyFormatter),
                                groupColor = entity.accountGroup.target?.color,
                                isPositiveBalance = entity.balance.signum() >= 0
                            )
                        )
                    })
                }
            }

        return AccountsUiState(
            accountCards = cards.toPersistentList(),
            listItems = listItems.toPersistentList(),
            accountGroups = groups.toPersistentList(),
            selectedGroupId = selectedGroupId,
            isLoading = false
        )
    }

    private fun calculateTotals(accounts: List<AccountEntity>): Pair<BigDecimal, BigDecimal> {
        var assets = BigDecimal.ZERO
        var liabilities = BigDecimal.ZERO
        for (account in accounts) {
            val balance = account.balance
            if (balance.signum() >= 0) {
                assets = assets.add(balance)
            } else {
                liabilities = liabilities.add(balance.abs())
            }
        }
        return assets to liabilities
    }

    private fun createNetWorthCard(netWorth: BigDecimal, assets: BigDecimal, liabilities: BigDecimal) = FlippableCardData(
        title = "Net Worth",
        largeText = netWorth.toFormattedCurrency(currencyFormatter),
        statistics = listOf(
            StatisticsModel("Assets", assets.toCompactCurrency(currencyFormatter)),
            StatisticsModel("Liabilities", liabilities.toCompactCurrency(currencyFormatter))
        ).toPersistentList()
    )

    private fun createAssetsCard(totalAssets: BigDecimal, statistics: List<StatisticsModel>) = FlippableCardData(
        title = "Total Assets",
        largeText = totalAssets.toFormattedCurrency(currencyFormatter),
        statistics = statistics.toPersistentList()
    )

    private fun createLiabilitiesCard(totalLiabilities: BigDecimal, statistics: List<StatisticsModel>) = FlippableCardData(
        title = "Total Liabilities",
        largeText = totalLiabilities.toFormattedCurrency(currencyFormatter),
        statistics = statistics.toPersistentList()
    )

    private fun buildGroupedStatistics(
        accounts: List<AccountEntity>,
        includePredicate: (BigDecimal) -> Boolean,
        valueSelector: (AccountEntity) -> BigDecimal
    ): List<StatisticsModel> {
        val matchingAccounts = accounts.filter { includePredicate(it.balance) }
        if (matchingAccounts.isEmpty()) return emptyList()

        val (orphanAccounts, groupedAccounts) = matchingAccounts.partition {
            it.accountGroup.target == null
        }

        val orphanTotal = orphanAccounts.sumOfBigDecimal(valueSelector)

        val namedGroups = groupedAccounts
            .groupBy { it.accountGroup.target!!.name }
            .map { (name, groupAccounts) -> name to groupAccounts.sumOfBigDecimal(valueSelector) }
            .sortedByDescending { it.second }

        return buildList {
            namedGroups.take(2).forEach { (name, total) ->
                add(StatisticsModel(name, total.toCompactCurrency(currencyFormatter)))
            }

            var othersTotal = orphanTotal
            if (namedGroups.size > 2) {
                othersTotal = othersTotal.add(
                    namedGroups.drop(2).fold(BigDecimal.ZERO) { acc, (_, total) -> acc.add(total) }
                )
            }

            if (othersTotal > BigDecimal.ZERO) {
                add(StatisticsModel("Others", othersTotal.toCompactCurrency(currencyFormatter)))
            }
        }
    }

    private fun List<AccountEntity>.sumOfBigDecimal(selector: (AccountEntity) -> BigDecimal): BigDecimal =
        fold(BigDecimal.ZERO) { acc, account -> acc.add(selector(account)) }
}

