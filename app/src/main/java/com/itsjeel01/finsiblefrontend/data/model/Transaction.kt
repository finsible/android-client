package com.itsjeel01.finsiblefrontend.data.model

import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Long,
    val type: String,
    val totalAmount: String,
    val transactionDate: String,
    val categoryId: Long,
    val categoryName: String,
    val description: String? = null,
    val currency: Currency,
    val fromAccountId: Long? = null,
    val fromAccountName: String? = null,
    val toAccountId: Long? = null,
    val toAccountName: String? = null,
    val spaceId: Long? = null,
    val userShare: String? = null,
    val isSplit: Boolean = false,
    val paidByUserId: Long? = null,
    val paidByUserName: String? = null
)

fun Transaction.toEntity(
    syncStatus: Status = Status.COMPLETED
): TransactionEntity = TransactionEntity(
    id = id,
    type = TransactionType.valueOf(type),
    totalAmount = totalAmount,
    transactionDate = transactionDate.toLongOrNull() ?: 0L,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryIcon = "", // Will be populated by repository
    description = description,
    currency = currency,
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