package com.itsjeel01.finsiblefrontend.ui.util

import com.itsjeel01.finsiblefrontend.R

/** Resolve a category icon name into a drawable resource id with fallbacks. */
fun resolveCategoryIcon(name: String?): Int {
    if (name.isNullOrBlank()) return R.drawable.ic_close
    val key = name.lowercase()
    val mapping = mapOf(
        // Simplified aliases
        "home" to R.drawable.ic_home_filled,
        "home_outlined" to R.drawable.ic_home_outlined,
        "settings" to R.drawable.ic_settings_filled,
        "settings_outlined" to R.drawable.ic_settings_outlined,
        "analytics" to R.drawable.ic_analytics_filled,
        "analytics_outlined" to R.drawable.ic_analytics_outlined,
        "transactions" to R.drawable.ic_transactions,
        "transfer" to R.drawable.ic_transfer,
        "piggy_bank" to R.drawable.ic_piggy_bank,
        "piggybank" to R.drawable.ic_piggy_bank,
        "piggybank_filled" to R.drawable.ic_piggybank_filled,
        "piggybank_outlined" to R.drawable.ic_piggybank_outlined,
        "stats" to R.drawable.ic_stats,
        "plus" to R.drawable.ic_plus,
        "calendar" to R.drawable.ic_calendar,
        "warning" to R.drawable.ic_warning,
        "error" to R.drawable.ic_error,
        "info" to R.drawable.ic_info,
        "close" to R.drawable.ic_close,
        // Direct ic_ names
        "ic_home_filled" to R.drawable.ic_home_filled,
        "ic_home_outlined" to R.drawable.ic_home_outlined,
        "ic_settings_filled" to R.drawable.ic_settings_filled,
        "ic_settings_outlined" to R.drawable.ic_settings_outlined,
        "ic_analytics_filled" to R.drawable.ic_analytics_filled,
        "ic_analytics_outlined" to R.drawable.ic_analytics_outlined,
        "ic_transactions" to R.drawable.ic_transactions,
        "ic_transfer" to R.drawable.ic_transfer,
        "ic_piggy_bank" to R.drawable.ic_piggy_bank,
        "ic_piggybank_filled" to R.drawable.ic_piggybank_filled,
        "ic_piggybank_outlined" to R.drawable.ic_piggybank_outlined,
        "ic_stats" to R.drawable.ic_stats,
        "ic_plus" to R.drawable.ic_plus,
        "ic_calendar" to R.drawable.ic_calendar,
        "ic_warning" to R.drawable.ic_warning,
        "ic_error" to R.drawable.ic_error,
        "ic_info" to R.drawable.ic_info,
        "ic_close" to R.drawable.ic_close
    )
    return mapping[key] ?: R.drawable.ic_close
}

