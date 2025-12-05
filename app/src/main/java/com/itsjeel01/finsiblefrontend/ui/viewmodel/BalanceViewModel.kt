package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.toLocaleCurrency
import com.itsjeel01.finsiblefrontend.common.toReadableCurrency
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.ui.model.AccountCardData
import com.itsjeel01.finsiblefrontend.ui.model.StatisticsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val accountLocalRepository: AccountLocalRepository
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<AccountEntity>>(emptyList())
    val accounts: StateFlow<List<AccountEntity>> = _accounts.asStateFlow()

    private val _totalAssets = MutableStateFlow(BigDecimal.ZERO.setScale(4, MathContext.ROUND_DOWN))
    val totalAssets: StateFlow<BigDecimal> = _totalAssets

    private val _totalLiabilities = MutableStateFlow(BigDecimal.ZERO.setScale(4, MathContext.ROUND_DOWN))
    val totalLiabilities: StateFlow<BigDecimal> = _totalLiabilities

    private val _accountCards = MutableStateFlow<List<AccountCardData>>(emptyList())
    val accountCards: StateFlow<List<AccountCardData>> = _accountCards.asStateFlow()

    init {
        viewModelScope.launch {
            _accounts.value = accountLocalRepository.getAll()
            recalculateTotals(_accounts.value)
            generateAccountCards(_accounts.value)
        }
    }

    private fun recalculateTotals(accounts: List<AccountEntity>) {
        var assets = BigDecimal.ZERO
        var liabilities = BigDecimal.ZERO
        for (account in accounts) {
            val bal: BigDecimal = account.balance
            if (bal.signum() >= 0) {
                assets = assets.add(bal)
            } else {
                liabilities = liabilities.add(bal)
            }
        }
        _totalAssets.value = assets.setScale(4, MathContext.ROUND_DOWN)
        _totalLiabilities.value = liabilities.abs().setScale(4, MathContext.ROUND_DOWN)
    }

    private fun generateAccountCards(accounts: List<AccountEntity>) {
        // Don't render cards for fresh accounts or insufficient data
        if (accounts.isEmpty() || totalAssets == BigDecimal.ZERO && totalLiabilities == BigDecimal.ZERO) {
            _accountCards.value = emptyList()
            return
        }

        val cards = mutableListOf<AccountCardData>()

        val totalAssets = _totalAssets.value
        val totalLiabilities = _totalLiabilities.value
        val netWorth = totalAssets.subtract(totalLiabilities)

        // Card 1: Net Worth - Always shows exactly 2 statistics (Assets & Liabilities)
        cards.add(
            AccountCardData(
                title = "Net Worth",
                largeText = netWorth.toLocaleCurrency(),
                statistics = listOf(
                    StatisticsModel("Assets", totalAssets.toReadableCurrency()),
                    StatisticsModel("Liabilities", totalLiabilities.toReadableCurrency())
                )
            )
        )

        // Card 2: Total Assets - Shows up to 3 statistics
        val assetStats = buildAssetStatistics(accounts)
        if (assetStats.isNotEmpty()) {
            cards.add(
                AccountCardData(
                    title = "Total Assets",
                    largeText = totalAssets.toLocaleCurrency(),
                    statistics = assetStats
                )
            )
        }

        // Card 3: Total Liabilities - Shows up to 3 statistics
        val liabilityStats = buildLiabilityStatistics(accounts)
        if (liabilityStats.isNotEmpty()) {
            cards.add(
                AccountCardData(
                    title = "Total Liabilities",
                    largeText = totalLiabilities.toLocaleCurrency(),
                    statistics = liabilityStats
                )
            )
        }

        _accountCards.value = cards
    }

    /** Build grouped statistics helper: top 2 groups + Others, handle orphans correctly. */
    private fun buildGroupedStatistics(
        accounts: List<AccountEntity>,
        includePredicate: (BigDecimal) -> Boolean,
        valueSelector: (AccountEntity) -> BigDecimal
    ): List<StatisticsModel> {
        val filteredAccounts = accounts.filter { includePredicate(it.balance) }
        if (filteredAccounts.isEmpty()) return emptyList()

        // Separate orphan and grouped accounts
        val orphanAccounts = filteredAccounts.filter { it.accountGroup.target == null }
        val groupedAccounts = filteredAccounts.filter { it.accountGroup.target != null }

        // Calculate orphan total (always goes into "Others")
        val orphanTotal = orphanAccounts.fold(BigDecimal.ZERO) { acc, account -> acc.add(valueSelector(account)) }

        // Group non-orphan accounts by group name and calculate totals
        val namedGroups = groupedAccounts
            .groupBy { it.accountGroup.target!!.name }
            .map { (groupName, groupAccounts) ->
                val groupTotal = groupAccounts.fold(BigDecimal.ZERO) { acc, account -> acc.add(valueSelector(account)) }
                groupName to groupTotal
            }
            .sortedByDescending { it.second }

        val stats = mutableListOf<StatisticsModel>()

        when {
            namedGroups.isEmpty() && orphanTotal > BigDecimal.ZERO -> {
                // All groups are empty, only orphan accounts have values - show only Others
                stats.add(StatisticsModel("Others", orphanTotal.toReadableCurrency()))
            }

            namedGroups.size == 1 -> {
                // Only 1 group - show it
                stats.add(StatisticsModel(namedGroups[0].first, namedGroups[0].second.toReadableCurrency()))
                // Add Others if orphans exist
                if (orphanTotal > BigDecimal.ZERO) {
                    stats.add(StatisticsModel("Others", orphanTotal.toReadableCurrency()))
                }
            }

            namedGroups.size == 2 -> {
                // Exactly 2 groups - show both
                namedGroups.forEach { (groupName, groupTotal) ->
                    stats.add(StatisticsModel(groupName, groupTotal.toReadableCurrency()))
                }
                // Add Others if orphans exist
                if (orphanTotal > BigDecimal.ZERO) {
                    stats.add(StatisticsModel("Others", orphanTotal.toReadableCurrency()))
                }
            }

            namedGroups.size >= 3 -> {
                // More than 2 groups - show top 2 + Others (orphans + remaining groups)
                namedGroups.take(2).forEach { (groupName, groupTotal) ->
                    stats.add(StatisticsModel(groupName, groupTotal.toReadableCurrency()))
                }
                val remainingGroupsTotal = namedGroups.drop(2).fold(BigDecimal.ZERO) { acc, (_, total) -> acc.add(total) }
                val othersTotal = orphanTotal.add(remainingGroupsTotal)
                stats.add(StatisticsModel("Others", othersTotal.toReadableCurrency()))
            }
        }

        return stats
    }

    /** Build asset statistics following the philosophy: top 2 groups + Others. */
    private fun buildAssetStatistics(accounts: List<AccountEntity>): List<StatisticsModel> {
        return buildGroupedStatistics(
            accounts = accounts,
            includePredicate = { it.signum() >= 0 },
            valueSelector = { it.balance }
        )
    }

    /** Build liability statistics following the philosophy: top 2 groups + Others. */
    private fun buildLiabilityStatistics(accounts: List<AccountEntity>): List<StatisticsModel> {
        return buildGroupedStatistics(
            accounts = accounts,
            includePredicate = { it.signum() < 0 },
            valueSelector = { it.balance.abs() }
        )
    }
}