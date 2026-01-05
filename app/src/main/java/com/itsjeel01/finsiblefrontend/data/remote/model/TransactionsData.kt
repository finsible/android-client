package com.itsjeel01.finsiblefrontend.data.remote.model

import com.itsjeel01.finsiblefrontend.data.model.Transaction
import kotlinx.serialization.Serializable

/** Wrapper for transaction list responses. */
@Serializable
data class TransactionsData(
    val transactions: List<Transaction>,
    val totalElements: Int = transactions.size,
    val totalPages: Int = 1,
    val currentPage: Int = 0
)

/** Delta sync response - contains only modified transactions since last sync. */
@Serializable
data class TransactionsDeltaData(
    val changes: List<TransactionDelta>,
    val serverTime: Long,
    val hasMore: Boolean = false
)

@Serializable
data class TransactionDelta(
    val id: Long,
    val deleted: Boolean = false,
    val transaction: Transaction? = null  // null if deleted=true
)