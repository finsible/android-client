package com.itsjeel01.finsiblefrontend.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionCreateRequest(
    val type: String,
    val totalAmount: String,
    val transactionDate: Long,
    val categoryId: Long,
    val description: String? = null,
    val currency: String = "INR",
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val spaceId: Long? = null,
    val userShare: String? = null,
    val isSplit: Boolean = false,
    val paidByUserId: Long? = null
)