package com.itsjeel01.finsiblefrontend.ui.model

import java.math.BigDecimal

/** Daily summary of transaction amounts grouped by type. */
data class TransactionDailySummary(
    val income: BigDecimal = BigDecimal.ZERO,
    val expense: BigDecimal = BigDecimal.ZERO,
    val count: Long = 0
) {
    val net: BigDecimal
        get() = income.subtract(expense)
}