package com.itsjeel01.finsiblefrontend.ui.model

import androidx.annotation.StringRes
import com.itsjeel01.finsiblefrontend.R

/** Enum representing the different time based filtering modes. */
enum class TimeFilterMode(@StringRes val displayText: Int) {
    ALL(R.string.filter_all_time),
    MONTH_YEAR(R.string.filter_month_year),
    CUSTOM(R.string.filter_custom_range);
}

/** Enum representing the different transaction sort options. */
enum class SortOption(@StringRes val displayText: Int) {
    NEWEST_FIRST(R.string.sort_newest_first),
    OLDEST_FIRST(R.string.sort_oldest_first),
    AMOUNT_HIGH_TO_LOW(R.string.sort_amount_high_to_low),
    AMOUNT_LOW_TO_HIGH(R.string.sort_amount_low_to_high);

    val isDateBased: Boolean get() = this == NEWEST_FIRST || this == OLDEST_FIRST
}

/** Filter mode for transaction date groups. */
enum class DateFilterMode {
    NET,
    INCOME,
    EXPENSE
}
