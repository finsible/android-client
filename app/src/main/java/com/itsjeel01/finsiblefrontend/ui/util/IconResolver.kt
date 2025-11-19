package com.itsjeel01.finsiblefrontend.ui.util

import com.itsjeel01.finsiblefrontend.R

/** Central icon resolver mapping tokens to drawable resources. */
private val iconMap: MutableMap<String, Int> = mutableMapOf(
    // Generic/shared
    "home" to R.drawable.ic_home_filled,
    "home_outlined" to R.drawable.ic_home_outlined,
    "settings" to R.drawable.ic_settings_filled,
    "settings_outlined" to R.drawable.ic_settings_outlined,
    "analytics" to R.drawable.ic_analytics_filled,
    "analytics_outlined" to R.drawable.ic_analytics_outlined,
    "transactions" to R.drawable.ic_transactions,
    "transfer" to R.drawable.ic_transfer,
    "stats" to R.drawable.ic_stats,
    "plus" to R.drawable.ic_plus,
    "calendar" to R.drawable.ic_calendar,
    "warning" to R.drawable.ic_warning,
    "error" to R.drawable.ic_error,
    "info" to R.drawable.ic_info,
    "close" to R.drawable.ic_close,
    // Piggy bank variants
    "piggy_bank" to R.drawable.ic_piggy_bank,
    "piggybank" to R.drawable.ic_piggy_bank,
    "piggybank_filled" to R.drawable.ic_piggybank_filled,
    "piggybank_outlined" to R.drawable.ic_piggybank_outlined,
    // Account intent aliases
    "savings" to R.drawable.ic_piggy_bank,
    "wallet" to R.drawable.ic_piggy_bank,
    "bank" to R.drawable.ic_piggy_bank,
    // Direct ic_ names (aliases)
    "ic_home_filled" to R.drawable.ic_home_filled,
    "ic_home_outlined" to R.drawable.ic_home_outlined,
    "ic_settings_filled" to R.drawable.ic_settings_filled,
    "ic_settings_outlined" to R.drawable.ic_settings_outlined,
    "ic_analytics_filled" to R.drawable.ic_analytics_filled,
    "ic_analytics_outlined" to R.drawable.ic_analytics_outlined,
    "ic_transactions" to R.drawable.ic_transactions,
    "ic_transfer" to R.drawable.ic_transfer,
    "ic_stats" to R.drawable.ic_stats,
    "ic_plus" to R.drawable.ic_plus,
    "ic_calendar" to R.drawable.ic_calendar,
    "ic_warning" to R.drawable.ic_warning,
    "ic_error" to R.drawable.ic_error,
    "ic_info" to R.drawable.ic_info,
    "ic_close" to R.drawable.ic_close,
    "ic_piggy_bank" to R.drawable.ic_piggy_bank,
    "ic_piggybank_filled" to R.drawable.ic_piggybank_filled,
    "ic_piggybank_outlined" to R.drawable.ic_piggybank_outlined
)

/** Resolve an icon given a token; returns fallback if not found. */
fun resolveIcon(token: String?, fallbackIcon: Int): Int {
    val key = token?.lowercase()?.takeIf { it.isNotBlank() } ?: return fallbackIcon
    iconMap[key]?.let { return it }
    
    // Try prefix normalization.
    if (key.startsWith("ic_")) {
        val stripped = key.removePrefix("ic_")
        iconMap[stripped]?.let { return it }
    } else {
        iconMap["ic_$key"]?.let { return it }
    }
    return fallbackIcon
}