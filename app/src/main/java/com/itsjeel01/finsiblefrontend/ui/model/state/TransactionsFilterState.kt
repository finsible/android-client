package com.itsjeel01.finsiblefrontend.ui.model.state

import androidx.compose.runtime.Immutable
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.model.TimeFilterMode
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionsFilterViewModel

/** Data class representing transaction view options (search, filter, sort). Sheet-exclusive fields are orphan state used only by [TransactionsFilterViewModel]. */
@Immutable
data class TransactionsFilterState(
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NEWEST_FIRST,
    val accountIds: Set<Long> = emptySet(),
    val timeFilterMode: TimeFilterMode = TimeFilterMode.ALL,
    val selectedMonth: Int = -1,
    val selectedYear: Int = -1,
    val dateRangeStart: Long? = null,
    val dateRangeEnd: Long? = null,
    val amountMin: Long? = null,
    val amountMax: Long? = null,
    val transactionTypes: Set<TransactionType> = emptySet(),
    // Sheet-exclusive orphan fields — not used by HistoryViewModel
    val amountMinText: String = "",
    val amountMaxText: String = "",
    val showDateRangePicker: Boolean = false,
    val amountRangeError: Boolean = false
) {
    /** Check if any filter, search, or non-default sort is active. */
    val isActive: Boolean
        get() = hasActiveFiltersOrSearch || hasActiveSort

    /** Check if any filter or search is active (triggers filtered query mode). */
    val hasActiveFiltersOrSearch: Boolean
        get() = isSearchActive || hasActiveFilters

    /** Check if only search is active (for UI display purposes). */
    val isSearchActive: Boolean
        get() = searchQuery.isNotBlank()

    /** Check if any filter (excluding search and sort) is active. */
    val hasActiveFilters: Boolean
        get() = accountIds.isNotEmpty() ||
                dateRangeStart != null ||
                dateRangeEnd != null ||
                amountMin != null ||
                amountMax != null ||
                transactionTypes.isNotEmpty()

    /** Check if non-default sort is applied. */
    val hasActiveSort: Boolean
        get() = sortOption != SortOption.NEWEST_FIRST

    /** Count of active filter categories (for badge display). Excludes search and sort. */
    val activeFilterCount: Int
        get() = listOf(
            accountIds.isNotEmpty(),
            dateRangeStart != null || dateRangeEnd != null,
            amountMin != null || amountMax != null,
            transactionTypes.isNotEmpty()
        ).count { it }

    /** Show date grouping when sorting by date (regardless of filter state). */
    val shouldShowDateGrouping: Boolean get() = sortOption.isDateBased

    /** True if any non-default filter or sort option is active in the sheet (used by [TransactionsFilterViewModel]). */
    val hasAnySheetActive: Boolean
        get() = sortOption != SortOption.NEWEST_FIRST ||
                transactionTypes.isNotEmpty() ||
                dateRangeStart != null ||
                dateRangeEnd != null ||
                amountMinText.isNotBlank() ||
                amountMaxText.isNotBlank()

    companion object {
        val DEFAULT = TransactionsFilterState()
    }
}