package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountCentis
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class Transaction(
    val id: Long,
    val type: String,
    val totalAmount: String,
    val transactionDate: Long,
    val categoryId: Long,
    val categoryName: String,
    val description: String? = null,
    @SerialName("currency") val currencyCode: String,
    val fromAccountId: Long? = null,
    val fromAccountName: String? = null,
    val toAccountId: Long? = null,
    val toAccountName: String? = null,
    val spaceId: Long? = null,
    val userShare: String? = null,
    val isSplit: Boolean = false,
    val paidByUserId: Long? = null,
    val paidByUserName: String? = null,
    val conversion: CurrencyConversion? = null
)

fun Transaction.toEntity(
    syncStatus: Status = Status.COMPLETED
): TransactionEntity = TransactionEntity(
    id = id,
    type = TransactionType.valueOf(type),
    totalAmount = totalAmount.toAmountCentis(),
    searchableText = buildSearchableText(description, categoryName),
    transactionDate = transactionDate,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryIcon = "", // Will be populated by repository
    description = description,
    currencyCode = currencyCode,
    conversionBaseCurrency = conversion?.baseCurrencyCode,
    conversionRate = conversion?.rate,
    conversionBaseAmountCentis = conversion?.baseAmount?.toAmountCentis(),
    isRateEstimated = conversion?.isEstimated ?: false,
    fromAccountId = fromAccountId,
    fromAccountName = fromAccountName,
    toAccountId = toAccountId,
    toAccountName = toAccountName,
    spaceId = spaceId,
    userShare = userShare,
    isSplit = isSplit,
    paidByUserId = paidByUserId,
    paidByUserName = paidByUserName,
    syncStatus = syncStatus
)

@Serializable
data class CurrencyConversion(
    val baseCurrencyCode: String,
    val rate: Double,
    val baseAmount: String,
    val isEstimated: Boolean
)

/** Builds pre-computed lowercase searchable text for efficient text search. */
fun buildSearchableText(description: String?, categoryName: String): String {
    return buildString {
        description?.lowercase(Locale.ROOT)?.let { append(it).append(" ") }
        append(categoryName.lowercase(Locale.ROOT))
    }.trim()
}