package com.itsjeel01.finsiblefrontend.ui.mapper

import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.toAmountOnly
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.ui.model.TransactionUiModel

fun TransactionEntity.toUiModel(currencyFormatter: CurrencyFormatter): TransactionUiModel {
    return TransactionUiModel(
        id = this.id,
        type = this.type,
        title = this.description.takeUnless { it.isNullOrBlank() } ?: this.categoryName,
        subtitle = formatAccountLabel(this),
        formattedAmount = formatAmount(this, currencyFormatter),
        categoryIcon = this.categoryIcon,
        currency = this.currency,
        transactionDate = this.transactionDate
    )
}

private fun formatAccountLabel(transaction: TransactionEntity): String {
    val from = transaction.fromAccountName ?: "?"
    val to = transaction.toAccountName ?: "?"
    return when (transaction.type) {
        TransactionType.TRANSFER -> "$from -> $to"
        TransactionType.EXPENSE -> from
        TransactionType.INCOME -> to
    }
}

private fun formatAmount(transaction: TransactionEntity, currencyFormatter: CurrencyFormatter): String {
    val sign = when (transaction.type) {
        TransactionType.INCOME -> "+"
        TransactionType.EXPENSE -> "-"
        TransactionType.TRANSFER -> ""
    }
    val amountStr = transaction.totalAmount.toAmountOnly(currencyFormatter)
    return "$sign ${transaction.currency.getSymbol()}$amountStr"
}