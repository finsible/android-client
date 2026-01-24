package com.itsjeel01.finsiblefrontend.data.remote.model

import com.itsjeel01.finsiblefrontend.common.Currency
import kotlinx.serialization.Serializable

/** Partial update - all fields nullable. Only non-null fields are updated. */
@Serializable
data class TransactionUpdateRequest(
    val type: String? = null,
    val totalAmount: String? = null,
    val transactionDate: Long? = null,
    val categoryId: Long? = null,
    val description: String? = null,
    val currency: Currency? = null,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val spaceId: Long? = null,
    val userShare: String? = null,
    val isSplit: Boolean? = null,
    val paidByUserId: Long? = null
)