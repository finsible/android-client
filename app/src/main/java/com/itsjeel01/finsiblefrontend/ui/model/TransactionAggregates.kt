package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable

/** Summary of filtered transaction results, amounts in centis (×100). */
@Immutable
data class FilteredTransactionSummary(
    val totalCount: Int,
    val totalIncomeCentis: Long,
    val totalExpenseCentis: Long
) {
    val netCentis: Long get() = totalIncomeCentis - totalExpenseCentis

    companion object {
        val EMPTY = FilteredTransactionSummary(
            totalCount = 0,
            totalIncomeCentis = 0L,
            totalExpenseCentis = 0L
        )
    }
}

/** Daily summary of transaction amounts grouped by type, stored as centis (×100). */
@Immutable
data class TransactionDailySummary(
    val incomeCentis: Long = 0L,
    val expenseCentis: Long = 0L,
    val count: Long = 0
) {
    val netCentis: Long get() = incomeCentis - expenseCentis
}

/** Aggregate financial data for a specific date, stored as centis (×100). */
@Immutable
data class DateAggregates(
    val startOfDayMs: Long,
    val endOfDayMs: Long,
    val incomeSumCentis: Long,
    val expenseSumCentis: Long,
    val netSumCentis: Long,
    val transactionCount: Long
) {
    companion object {
        /** Create DateAggregates with zero values. */
        fun zero(startOfDayMs: Long, endOfDayMs: Long): DateAggregates {
            return DateAggregates(
                startOfDayMs = startOfDayMs,
                endOfDayMs = endOfDayMs,
                incomeSumCentis = 0L,
                expenseSumCentis = 0L,
                netSumCentis = 0L,
                transactionCount = 0
            )
        }
    }
}

/** Lightweight aggregate holder for computed group summaries, amounts in centis (×100). */
@Immutable
data class GroupAggregates(
    val incomeSumCentis: Long,
    val expenseSumCentis: Long,
    val netSumCentis: Long
) {
    companion object {
        val ZERO = GroupAggregates(0L, 0L, 0L)
    }
}
