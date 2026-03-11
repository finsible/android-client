package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

/** UI state for lazy-loaded transaction list with unified pagination. Immutable for Compose optimization. */
@Immutable
data class TransactionListState(
    val transactions: ImmutableList<TransactionUIModel> = persistentListOf(),
    val groupedTransactions: ImmutableMap<String, ImmutableList<TransactionUIModel>> = persistentMapOf(),
    val dateAggregates: ImmutableMap<String, DateAggregates> = persistentMapOf(),

    val isLoading: Boolean = false,
    val hasMoreData: Boolean = true,
    val error: String? = null,

    val filteredSummary: FilteredTransactionSummary? = null
)