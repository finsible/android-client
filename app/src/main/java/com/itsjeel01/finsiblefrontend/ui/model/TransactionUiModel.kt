package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.TransactionType

@Immutable
data class TransactionUiModel(
    val id: Long,
    val type: TransactionType,
    val title: String,
    val subtitle: String,
    val formattedAmount: String,
    val categoryIcon: String,
    val currency: Currency,
    val transactionDate: Long
)