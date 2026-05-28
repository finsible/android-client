package com.itsjeel01.finsiblefrontend.ui.mapper

import android.content.Context
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.TransactionUIModel

fun TransactionEntity.toUiModel(
    currencyFormatter: CurrencyFormatter,
    context: Context
): TransactionUIModel {
    return TransactionUIModel(
        id = this.id,
        type = this.type,
        title = this.description.takeUnless { it.isNullOrBlank() } ?: this.categoryName,
        subtitle = formatAccountLabel(this, context),
        formattedAmount = formatAmount(this, currencyFormatter),
        categoryIcon = this.categoryIcon,
        currencyCode = this.currencyCode,
        transactionDate = this.transactionDate,
        rawAmountCentis = this.totalAmount,
    )
}

private fun formatAccountLabel(transaction: TransactionEntity, context: Context): String {
    val from = transaction.fromAccountName ?: context.getString(R.string.account_unknown)
    val to = transaction.toAccountName ?: context.getString(R.string.account_unknown)
    return when (transaction.type) {
        TransactionType.TRANSFER -> context.getString(R.string.transfer_label_format, from, to)
        TransactionType.EXPENSE -> from
        TransactionType.INCOME -> to
    }
}

private fun formatAmount(
    transaction: TransactionEntity,
    currencyFormatter: CurrencyFormatter
): String {
    val signedCentis = when (transaction.type) {
        TransactionType.EXPENSE -> -transaction.totalAmount
        else -> transaction.totalAmount
    }
    return currencyFormatter.format(
        centis = signedCentis,
        currencyCode = transaction.currencyCode,
        options = CurrencyFormatter.CurrencyFormatOptions(
            includeSign = transaction.type != TransactionType.TRANSFER,
            includeSpaceAfterSign = transaction.type != TransactionType.TRANSFER,
            showPositiveSign = transaction.type == TransactionType.INCOME,
            includeCurrencySymbol = true,
            includeSpaceAfterCurrencySymbol = false,
        )
    )
}