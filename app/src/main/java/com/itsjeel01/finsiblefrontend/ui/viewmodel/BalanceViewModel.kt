package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.icu.math.BigDecimal
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.toLocaleCurrency
import com.itsjeel01.finsiblefrontend.common.toReadableCurrency
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.ui.model.FlippableCardData
import com.itsjeel01.finsiblefrontend.ui.model.StatisticsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val accountLocalRepository: AccountLocalRepository,
    private val accountGroupLocalRepository: AccountGroupLocalRepository
) : ViewModel() {

    private var accounts: List<AccountEntity> = emptyList()
    private var totalAssets = BigDecimal.ZERO
    private var totalLiabilities = BigDecimal.ZERO

    private val _accountGroups = MutableStateFlow<List<AccountGroupEntity>>(emptyList())
    val accountGroups: StateFlow<List<AccountGroupEntity>> = _accountGroups.asStateFlow()

    private val _accountCards = MutableStateFlow<List<FlippableCardData>>(emptyList())
    val accountCards: StateFlow<List<FlippableCardData>> = _accountCards.asStateFlow()

    private val _selectedGroupId = MutableStateFlow<Long?>(null)
    val selectedGroupId: StateFlow<Long?> = _selectedGroupId.asStateFlow()

    private val _filteredAccounts = MutableStateFlow<List<AccountEntity>>(emptyList())
    val filteredAccounts: StateFlow<List<AccountEntity>> = _filteredAccounts.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            accounts = accountLocalRepository.getAll()
            _accountGroups.value = accountGroupLocalRepository.getAll()
            _filteredAccounts.value = accounts
            recalculateTotals()
            generateAccountCards()
        }
    }

    /** Updates the selected group filter and recalculates filtered accounts. */
    fun selectGroupFilter(groupId: Long?) {
        _selectedGroupId.value = groupId
        _filteredAccounts.value = if (groupId == null) {
            accounts
        } else {
            accounts.filter { it.accountGroup.target?.id == groupId }
        }
    }

    private fun recalculateTotals() {
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

        totalAssets = assets
        totalLiabilities = liabilities
    }

    private fun generateAccountCards() {
        if (accounts.isEmpty()) {
            _accountCards.value = emptyList()
            return
        }

        val netWorth = totalAssets.subtract(totalLiabilities)

        _accountCards.value = buildList {
            add(createNetWorthCard(netWorth))

            buildAssetStatistics()
                .takeIf { it.isNotEmpty() }
                ?.let { add(createAssetsCard(it)) }

            buildLiabilityStatistics()
                .takeIf { it.isNotEmpty() }
                ?.let { add(createLiabilitiesCard(it)) }
        }
    }

    private fun createNetWorthCard(netWorth: BigDecimal) = FlippableCardData(
        title = "Net Worth",
        largeText = netWorth.toLocaleCurrency(),
        statistics = listOf(
            StatisticsModel("Assets", totalAssets.toReadableCurrency()),
            StatisticsModel("Liabilities", totalLiabilities.toReadableCurrency())
        )
    )

    private fun createAssetsCard(statistics: List<StatisticsModel>) = FlippableCardData(
        title = "Total Assets",
        largeText = totalAssets.toLocaleCurrency(),
        statistics = statistics
    )

    private fun createLiabilitiesCard(statistics: List<StatisticsModel>) = FlippableCardData(
        title = "Total Liabilities",
        largeText = totalLiabilities.toLocaleCurrency(),
        statistics = statistics
    )

    /** Builds grouped statistics: top 2 groups + Others, handles orphans correctly. */
    private fun buildGroupedStatistics(
        includePredicate: (BigDecimal) -> Boolean,
        valueSelector: (AccountEntity) -> BigDecimal
    ): List<StatisticsModel> {
        val matchingAccounts = accounts.filter { includePredicate(it.balance) }
        if (matchingAccounts.isEmpty()) return emptyList()

        val (orphanAccounts, groupedAccounts) = matchingAccounts.partition {
            it.accountGroup.target == null
        }

        val orphanTotal = orphanAccounts.sumOf(valueSelector)

        val namedGroups = groupedAccounts
            .groupBy { it.accountGroup.target!!.name }
            .map { (name, groupAccounts) -> name to groupAccounts.sumOf(valueSelector) }
            .sortedByDescending { it.second }

        return buildList {
            // Add top 2 groups (or all if <= 2)
            namedGroups.take(2).forEach { (name, total) ->
                add(StatisticsModel(name, total.toReadableCurrency()))
            }

            // Calculate "Others" total: orphans + remaining groups beyond top 2
            val remainingGroupsTotal = namedGroups.drop(2).sumOfTotals()
            val othersTotal = orphanTotal.add(remainingGroupsTotal)

            if (othersTotal > BigDecimal.ZERO) {
                add(StatisticsModel("Others", othersTotal.toReadableCurrency()))
            }
        }
    }

    private fun List<AccountEntity>.sumOf(selector: (AccountEntity) -> BigDecimal): BigDecimal =
        fold(BigDecimal.ZERO) { acc, account -> acc.add(selector(account)) }

    private fun List<Pair<String, BigDecimal>>.sumOfTotals(): BigDecimal =
        fold(BigDecimal.ZERO) { acc, (_, total) -> acc.add(total) }

    /** Builds asset statistics: top 2 groups + Others. */
    private fun buildAssetStatistics(): List<StatisticsModel> =
        buildGroupedStatistics(
            includePredicate = { it.signum() >= 0 },
            valueSelector = { it.balance }
        )

    /** Builds liability statistics: top 2 groups + Others. */
    private fun buildLiabilityStatistics(): List<StatisticsModel> =
        buildGroupedStatistics(
            includePredicate = { it.signum() < 0 },
            valueSelector = { it.balance.abs() }
        )
}