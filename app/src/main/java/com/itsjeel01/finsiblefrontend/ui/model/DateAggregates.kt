package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

/** Aggregate financial data for a specific date. */
@Immutable
data class DateAggregates(
    val dateHeader: String,
    val startOfDayMs: Long,
    val endOfDayMs: Long,
    val incomeSum: BigDecimal,
    val expenseSum: BigDecimal,
    val netSum: BigDecimal,
    val transactionCount: Long
) {
    companion object {
        /** Create DateAggregates with zero values. */
        fun zero(dateHeader: String, startOfDayMs: Long, endOfDayMs: Long): DateAggregates {
            return DateAggregates(
                dateHeader = dateHeader,
                startOfDayMs = startOfDayMs,
                endOfDayMs = endOfDayMs,
                incomeSum = BigDecimal.ZERO,
                expenseSum = BigDecimal.ZERO,
                netSum = BigDecimal.ZERO,
                transactionCount = 0
            )
        }
    }
}

