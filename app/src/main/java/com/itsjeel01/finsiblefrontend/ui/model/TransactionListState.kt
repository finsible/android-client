package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

/** UI state for lazy-loaded transaction list with day-based pagination. Immutable for Compose optimization. */
@Immutable
data class TransactionListState(
    val transactions: ImmutableList<TransactionUiModel> = persistentListOf(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val error: String? = null,
    val dateFilterModes: ImmutableMap<String, DateFilterMode> = persistentMapOf(),
    val dateAggregates: ImmutableMap<String, DateAggregates> = persistentMapOf(),
    val groupedTransactions: ImmutableMap<String, ImmutableList<TransactionUiModel>> = persistentMapOf(),
    val loadedDatesCount: Int = 0
)