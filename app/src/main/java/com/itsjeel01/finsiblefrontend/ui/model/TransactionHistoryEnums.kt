package com.itsjeel01.finsiblefrontend.ui.model

/** Enum representing the different time based filtering modes. */
enum class TimeFilterMode(val displayText: String) {
    ALL("All Time"),
    MONTH_YEAR("Month / Year"),
    CUSTOM("Custom Range");
}

/** Enum representing the different transaction sort options. */
enum class SortOption(val displayText: String) {
    NEWEST_FIRST("Newest First"),
    OLDEST_FIRST("Oldest First"),
    AMOUNT_HIGH_TO_LOW("Amount: High to Low"),
    AMOUNT_LOW_TO_HIGH("Amount: Low to High");

    val isDateBased: Boolean get() = this == NEWEST_FIRST || this == OLDEST_FIRST
}

/** Filter mode for transaction date groups. */
enum class DateFilterMode {
    NET,
    INCOME,
    EXPENSE
}
