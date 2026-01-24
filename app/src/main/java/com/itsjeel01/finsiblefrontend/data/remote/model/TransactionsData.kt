package com.itsjeel01.finsiblefrontend.data.remote.model

import com.itsjeel01.finsiblefrontend.data.model.Transaction
import kotlinx.serialization.Serializable

/** Wrapper for transaction list responses matching Spring Page format. */
@Serializable
data class TransactionsData(
    val transactions: List<Transaction>,
    val pageable: Pageable? = null,
    val last: Boolean = false,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val first: Boolean = true,
    val size: Int = 0,
    val number: Int = 0,
    val sort: Sort? = null,
    val numberOfElements: Int = 0,
    val empty: Boolean = true
)

/** Pageable metadata from server response. */
@Serializable
data class Pageable(
    val pageNumber: Int = 0,
    val pageSize: Int = 0,
    val sort: Sort? = null,
    val offset: Int = 0,
    val unpaged: Boolean = false,
    val paged: Boolean = true
)

/** Sort metadata from server response. */
@Serializable
data class Sort(
    val empty: Boolean = true,
    val unsorted: Boolean = true,
    val sorted: Boolean = false
)