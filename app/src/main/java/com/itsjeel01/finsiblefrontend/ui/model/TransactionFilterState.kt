package com.itsjeel01.finsiblefrontend.ui.model

import androidx.compose.runtime.Immutable
import com.itsjeel01.finsiblefrontend.common.TransactionType
import java.math.BigDecimal

/** Enum representing the different time based filtering modes. */
enum class TimeFilterMode(val displayText: String) {
    ALL("All Time"),
    MONTH_YEAR("Month / Year"),
    CUSTOM("Custom Range");
}

/** Enum representing the different transaction sort options. */
enum class TransactionSortOption(val displayText: String) {
    NEWEST_FIRST("Newest First"),
    OLDEST_FIRST("Oldest First"),
    AMOUNT_HIGH_TO_LOW("Amount: High to Low"),
    AMOUNT_LOW_TO_HIGH("Amount: Low to High");

    val isDateBased: Boolean get() = this == NEWEST_FIRST || this == OLDEST_FIRST
}

/** Data class representing transaction view options (search, filter, sort). */
@Immutable
data class TransactionViewOptions(
    val searchQuery: String = "",
    val sortOption: TransactionSortOption = TransactionSortOption.NEWEST_FIRST,
    val accountIds: Set<Long> = emptySet(),
    val timeFilterMode: TimeFilterMode = TimeFilterMode.ALL,
    val selectedMonth: Int = -1,
    val selectedYear: Int = -1,
    val dateRangeStart: Long? = null,
    val dateRangeEnd: Long? = null,
    val amountMin: BigDecimal? = null,
    val amountMax: BigDecimal? = null,
    val transactionTypes: Set<TransactionType> = emptySet()
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
        get() = sortOption != TransactionSortOption.NEWEST_FIRST

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

    companion object {
        val DEFAULT = TransactionViewOptions()
    }
}

/** Summary of filtered transaction results. */
@Immutable
data class FilteredTransactionSummary(
    val totalCount: Int,
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal
) {
    val net: BigDecimal get() = totalIncome.subtract(totalExpense)

    companion object {
        val EMPTY = FilteredTransactionSummary(
            totalCount = 0,
            totalIncome = BigDecimal.ZERO,
            totalExpense = BigDecimal.ZERO
        )
    }
}
