package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import java.math.BigDecimal

/** Daily summary of transaction amounts grouped by type. */
@Immutable
data class TransactionDailySummary(
    val income: BigDecimal = BigDecimal.ZERO,
    val expense: BigDecimal = BigDecimal.ZERO,
    val count: Long = 0
) {
    val net: BigDecimal
        get() = income.subtract(expense)
}