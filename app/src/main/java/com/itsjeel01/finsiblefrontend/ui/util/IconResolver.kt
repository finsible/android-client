package com.itsjeel01.finsiblefrontend.ui.util

import com.itsjeel01.finsiblefrontend.R
import com.composables.icons.lucide.R as LucideR
import com.composables.icons.materialicons.filled.R as MaterialFilledIcons
import com.composables.icons.materialsymbols.outlined.R as MaterialSymbolsOutlinedIcons
import com.composables.icons.tabler.outline.R as TablerOutlineIcons

/** Central icon resolver mapping tokens to drawable resources. */
private val iconMap: MutableMap<String, Int> = mutableMapOf(
    "transactions" to R.drawable.ic_transactions,
    "transfer" to R.drawable.ic_transfer,
    "stats" to R.drawable.ic_stats,
    "plus" to R.drawable.ic_plus,
    "calendar" to R.drawable.ic_calendar,
    "warning" to R.drawable.ic_warning,
    "error" to R.drawable.ic_error,
    "info" to R.drawable.ic_info,
    "close" to R.drawable.ic_close,
    "piggy_bank" to R.drawable.ic_piggybank_outlined,
    "food_group" to MaterialFilledIcons.drawable.materialicons_ic_restaurant_menu_filled,
    "groceries" to LucideR.drawable.lucide_ic_shopping_basket,
    "restaurant_takeaway" to MaterialFilledIcons.drawable.materialicons_ic_fastfood_filled,
    "utilities_group" to MaterialFilledIcons.drawable.materialicons_ic_receipt_long_filled,
    "rent" to MaterialFilledIcons.drawable.materialicons_ic_home_filled,
    "home" to MaterialFilledIcons.drawable.materialicons_ic_home_filled,
    "utilities" to MaterialFilledIcons.drawable.materialicons_ic_receipt_filled,
    "internet" to MaterialFilledIcons.drawable.materialicons_ic_wifi_filled,
    "transport_group" to MaterialFilledIcons.drawable.materialicons_ic_commute_filled,
    "fuel" to LucideR.drawable.lucide_ic_fuel,
    "shopping_group" to MaterialFilledIcons.drawable.materialicons_ic_shopping_cart_filled,
    "clothing_accessories" to LucideR.drawable.lucide_ic_shopping_bag,
    "electronics" to LucideR.drawable.lucide_ic_monitor,
    "health_group" to MaterialFilledIcons.drawable.materialicons_ic_health_and_safety_filled,
    "medical" to MaterialFilledIcons.drawable.materialicons_ic_medical_services_filled,
    "gym" to MaterialFilledIcons.drawable.materialicons_ic_fitness_center_filled,
    "entertainment_leisure_group" to TablerOutlineIcons.drawable.tabler_ic_confetti_outline,
    "movies" to MaterialFilledIcons.drawable.materialicons_ic_movie_filled,
    "trip" to MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_flight_outlined,
    "subscription" to MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_subscriptions_outlined,
    "financial_group" to LucideR.drawable.lucide_ic_credit_card,
    "insurance" to MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_shield_with_heart_outlined,
    "tax" to TablerOutlineIcons.drawable.tabler_ic_receipt_tax_outline,
    "job_group" to MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_work_outlined,
    "salary" to LucideR.drawable.lucide_ic_calendar_arrow_down,
    "bonus" to LucideR.drawable.lucide_ic_award,
    "business_group" to TablerOutlineIcons.drawable.tabler_ic_coins_outline,
    "freelance" to MaterialFilledIcons.drawable.materialicons_ic_person_filled,
    "business" to MaterialFilledIcons.drawable.materialicons_ic_business_filled,
    "investment_group" to LucideR.drawable.lucide_ic_chart_no_axes_combined,
    "dividend" to LucideR.drawable.lucide_ic_percent,
    "capital_gain" to LucideR.drawable.lucide_ic_trending_up,
    "property_group" to TablerOutlineIcons.drawable.tabler_ic_buildings_outline,
    "rent" to MaterialFilledIcons.drawable.materialicons_ic_home_filled,
    "other_income" to TablerOutlineIcons.drawable.tabler_ic_cash_outline,
    "gift_cashback_refund" to LucideR.drawable.lucide_ic_gift,
    "bank_to_bank" to R.drawable.ic_transfer,
    "bank_wallet" to R.drawable.ic_transfer,
    "credit_card_payment" to TablerOutlineIcons.drawable.tabler_ic_credit_card_pay_outline,
    "loan_payment" to LucideR.drawable.lucide_ic_calendar_arrow_up,
    "savings_deposit" to R.drawable.ic_piggybank_outlined,
    "atm" to MaterialFilledIcons.drawable.materialicons_ic_atm_filled,
    "wallet_topup" to MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_wallet_outlined,
    "investment_transfer" to R.drawable.ic_transfer,
    "fd" to TablerOutlineIcons.drawable.tabler_ic_building_bank_outline
)

/** Resolve an icon given a token; returns fallback if not found. */
fun resolveIcon(token: String?, fallbackIcon: Int): Int {
    val key = token?.trim()?.lowercase()?.takeIf { it.isNotBlank() } ?: return fallbackIcon

    val variants = linkedSetOf<String>()

    variants.add(key)
    variants.add(key.replace('_', '-'))
    variants.add(key.replace("_", ""))
    variants.add(key.replace('-', '_'))
    variants.add(key.replace("-", ""))

    // Add ic_ prefixed / stripped variants
    val snapshot = variants.toList()
    for (v in snapshot) {
        if (v.startsWith("ic_")) {
            variants.add(v.removePrefix("ic_"))
        } else {
            variants.add("ic_$v")
        }
    }

    for (candidate in variants) {
        iconMap[candidate]?.let { return it }
    }

    return fallbackIcon
}