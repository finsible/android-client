package com.itsjeel01.finsiblefrontend.data.remote.model

import com.itsjeel01.finsiblefrontend.common.Currency
import kotlinx.serialization.Serializable

@Serializable
data class TransactionCreateRequest(
    val type: String,
    val totalAmount: String,
    val transactionDate: Long,
    val categoryId: Long,
    val description: String? = null,
    val currency: Currency = Currency.INR,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val spaceId: Long? = null,
    val userShare: String? = null,
    val isSplit: Boolean = false,
    val paidByUserId: Long? = null
)