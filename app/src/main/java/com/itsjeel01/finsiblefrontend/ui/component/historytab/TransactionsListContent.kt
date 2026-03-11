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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.GroupAggregates
import com.itsjeel01.finsiblefrontend.ui.model.TransactionListState
import com.itsjeel01.finsiblefrontend.ui.model.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun TransactionListContent(
    uiState: TransactionListState,
    showGrouping: Boolean,
    dateFilterModes: Map<String, DateFilterMode>,
    hasActiveFiltersOrSearch: Boolean,
    listState: LazyListState,
    currencyFormatter: CurrencyFormatter,
    onToggleDateFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (showGrouping) {
            // GROUPED MODE
            uiState.groupedTransactions.forEach { (dateHeader, transactionsForDate) ->
                stickyHeader(key = "header_$dateHeader") {
                    val filterMode: DateFilterMode = dateFilterModes.filterModeFor(dateHeader)
                    val groupAggregates: GroupAggregates =
                        remember(transactionsForDate, hasActiveFiltersOrSearch) {
                            if (hasActiveFiltersOrSearch) {
                                computeGroupAggregates(transactionsForDate)
                            } else {
                                uiState.dateAggregates[dateHeader]?.let {
                                    GroupAggregates(it.incomeSumCentis, it.expenseSumCentis, it.netSumCentis)
                                } ?: GroupAggregates.ZERO
                            }
                        }

                    Box(modifier = Modifier.background(FinsibleTheme.colors.primaryBackground)) {
                        DateHeader(
                            dateText = dateHeader,
                            filterMode = filterMode,
                            incomeSumCentis = groupAggregates.incomeSumCentis,
                            expenseSumCentis = groupAggregates.expenseSumCentis,
                            netSumCentis = groupAggregates.netSumCentis,
                            onToggleFilter = { onToggleDateFilter(dateHeader) },
                            currencyFormatter = currencyFormatter,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = FinsibleTheme.dimes.d12, top = FinsibleTheme.dimes.d16)
                        )
                    }
                }

                val filterMode: DateFilterMode = dateFilterModes.filterModeFor(dateHeader)
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
                        Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d16))
                    }
                }
            }
        } else {
            // FLAT MODE
            itemsIndexed(
                items = uiState.transactions,
                key = { _, item -> item.id }
            ) { _, transaction ->
                Box(modifier = Modifier.padding(vertical = FinsibleTheme.dimes.d6)) {
                    FlatModeTransactionItem(transaction = transaction)
                }
            }
        }

        if (uiState.isLoading && uiState.transactions.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = FinsibleTheme.colors.brandAccent40
                    )
                }
            }
        }
    }
}

/** Compute income/expense/net aggregates from a list of transaction UI models. */
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

/** Resolve the DateFilterMode for a header, defaulting to NET. Explicit return type avoids Map.get() overload ambiguity. */
private fun Map<String, DateFilterMode>.filterModeFor(header: String): DateFilterMode =
    this[header] ?: DateFilterMode.NET
