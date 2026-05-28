package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleLoader
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleLoaderDefaults
import com.itsjeel01.finsiblefrontend.ui.model.DateAggregates
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.GroupAggregates
import com.itsjeel01.finsiblefrontend.ui.model.state.TransactionListState
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils

@Composable
fun TransactionListContent(
    uiState: TransactionListState,
    showGrouping: Boolean,
    dateFilterModes: Map<Long, DateFilterMode>,
    hasActiveFiltersOrSearch: Boolean,
    listState: LazyListState,
    defaultCurrencyCode: String,
    currencyFormatter: CurrencyFormatter,
    onToggleDateFilter: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (showGrouping) {
            // GROUPED MODE
            uiState.groupedTransactions.forEach { (startOfDayMs, transactionsForDate) ->
                stickyHeader(key = startOfDayMs) {

                    val aggregates = uiState.dateAggregates[startOfDayMs]
                        ?: DateAggregates.zero(startOfDayMs, DateUtils.getEndOfDayMs(startOfDayMs))
                    val filterMode = dateFilterModes[startOfDayMs] ?: DateFilterMode.NET

                    Box(modifier = Modifier.background(FinsibleTheme.colors.surfaceBase)) {
                        DailySummaryHeader(
                            timestampMs = startOfDayMs,
                            filterMode = filterMode,
                            incomeSumCentis = aggregates.incomeSumCentis,
                            expenseSumCentis = aggregates.expenseSumCentis,
                            netSumCentis = aggregates.netSumCentis,
                            onToggleFilter = { onToggleDateFilter(startOfDayMs) },
                            defaultCurrencyCode = defaultCurrencyCode,
                            currencyFormatter = currencyFormatter,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = FinsibleTheme.spacing.gapMd, top = FinsibleTheme.spacing.insetLg)
                        )
                    }
                }

                val filterMode: DateFilterMode = dateFilterModes.filterModeFor(startOfDayMs)
                val visibleTransactions = when (filterMode) {
                    DateFilterMode.NET -> transactionsForDate
                    DateFilterMode.INCOME -> transactionsForDate.filter { it.type == TransactionType.INCOME }
                    DateFilterMode.EXPENSE -> transactionsForDate.filter { it.type == TransactionType.EXPENSE }
                }

                itemsIndexed(
                    items = visibleTransactions,
                    key = { _, item -> item.id }
                ) { index, transaction ->
                    GroupedTransactionItem(
                        transaction = transaction,
                        isFirst = index == 0,
                        isLast = index == visibleTransactions.lastIndex
                    )
                    if (index == visibleTransactions.lastIndex) {
                        Spacer(modifier = Modifier.height(FinsibleTheme.spacing.insetLg))
                    }
                }
            }
        } else {
            // FLAT MODE
            itemsIndexed(
                items = uiState.transactions,
                key = { _, item -> item.id }
            ) { _, transaction ->
                Box(modifier = Modifier.padding(vertical = FinsibleTheme.spacing.insetSm)) {
                    FlatModeTransactionItem(transaction = transaction)
                }
            }
        }

        if (uiState.isLoading && uiState.transactions.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(FinsibleTheme.spacing.insetLg),
                    contentAlignment = Alignment.Center
                ) {
                    FinsibleLoader(
                        modifier = Modifier.size(FinsibleTheme.spacing.inset2xl),
                        size = FinsibleSize.ExtraSmall,
                        colors = FinsibleLoaderDefaults.colors(
                            ballColor = FinsibleTheme.colors.brandTint,
                            barColor = FinsibleTheme.colors.contentTertiary
                        )
                    )
                }
            }
        }
    }
}

private fun computeGroupAggregates(transactions: List<TransactionUIModel>): GroupAggregates {
    var income = 0L
    var expense = 0L
    for (t in transactions) {
        when (t.type) {
            TransactionType.INCOME -> income += t.rawAmountCentis
            TransactionType.EXPENSE -> expense += t.rawAmountCentis
            TransactionType.TRANSFER -> { /* transfers don't affect daily summary */
            }
        }
    }
    return GroupAggregates(incomeSumCentis = income, expenseSumCentis = expense, netSumCentis = income - expense)
}

private fun Map<Long, DateFilterMode>.filterModeFor(timestamp: Long): DateFilterMode =
    this[timestamp] ?: DateFilterMode.NET
