package com.itsjeel01.finsiblefrontend.ui.util

import com.itsjeel01.finsiblefrontend.R
import com.composables.icons.lucide.R as LucideR
import com.composables.icons.materialicons.filled.R as MaterialFilledIcons
import com.composables.icons.materialsymbols.outlined.R as MaterialSymbolsOutlinedIcons
import com.composables.icons.tabler.outline.R as TablerOutlineIcons

fun resolveIcon(token: String?, fallbackIcon: Int): Int {
    if (token.isNullOrBlank()) return fallbackIcon

    // Normalize input: lowercase, remove "ic_", remove separators
    val key = token.trim().lowercase()
        .removePrefix("ic_")
        .replace("_", "")
        .replace("-", "")

    return when (key) {
        "transactions" -> R.drawable.ic_transactions
        "transfer" -> R.drawable.ic_transfer
        "stats" -> R.drawable.ic_stats
        "plus" -> R.drawable.ic_plus
        "calendar" -> R.drawable.ic_calendar
        "warning" -> R.drawable.ic_warning
        "error" -> R.drawable.ic_error
        "info" -> R.drawable.ic_info
        "close" -> R.drawable.ic_close
        "piggybank" -> R.drawable.ic_piggybank_outlined
        "foodgroup" -> MaterialFilledIcons.drawable.materialicons_ic_restaurant_menu_filled
        "groceries" -> LucideR.drawable.lucide_ic_shopping_basket
        "restauranttakeaway" -> MaterialFilledIcons.drawable.materialicons_ic_fastfood_filled
        "utilitiesgroup" -> MaterialFilledIcons.drawable.materialicons_ic_receipt_long_filled
        "rent" -> MaterialFilledIcons.drawable.materialicons_ic_home_filled
        "home" -> MaterialFilledIcons.drawable.materialicons_ic_home_filled
        "utilities" -> MaterialFilledIcons.drawable.materialicons_ic_receipt_filled
        "internet" -> MaterialFilledIcons.drawable.materialicons_ic_wifi_filled
        "transportgroup" -> MaterialFilledIcons.drawable.materialicons_ic_commute_filled
        "fuel" -> LucideR.drawable.lucide_ic_fuel
        "shoppinggroup" -> MaterialFilledIcons.drawable.materialicons_ic_shopping_cart_filled
        "clothingaccessories" -> LucideR.drawable.lucide_ic_shopping_bag
        "electronics" -> LucideR.drawable.lucide_ic_monitor
        "healthgroup" -> MaterialFilledIcons.drawable.materialicons_ic_health_and_safety_filled
        "medical" -> MaterialFilledIcons.drawable.materialicons_ic_medical_services_filled
        "gym" -> MaterialFilledIcons.drawable.materialicons_ic_fitness_center_filled
        "entertainmentleisuregroup" -> TablerOutlineIcons.drawable.tabler_ic_confetti_outline
        "movies" -> MaterialFilledIcons.drawable.materialicons_ic_movie_filled
        "trip" -> MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_flight_outlined
        "subscription" -> MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_subscriptions_outlined
        "financialgroup" -> LucideR.drawable.lucide_ic_credit_card
        "insurance" -> MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_shield_with_heart_outlined
        "tax" -> TablerOutlineIcons.drawable.tabler_ic_receipt_tax_outline
        "jobgroup" -> MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_work_outlined
        "salary" -> LucideR.drawable.lucide_ic_calendar_arrow_down
        "bonus" -> LucideR.drawable.lucide_ic_award
        "businessgroup" -> TablerOutlineIcons.drawable.tabler_ic_coins_outline
        "freelance" -> MaterialFilledIcons.drawable.materialicons_ic_person_filled
        "business" -> MaterialFilledIcons.drawable.materialicons_ic_business_filled
        "investmentgroup" -> LucideR.drawable.lucide_ic_chart_no_axes_combined
        "dividend" -> LucideR.drawable.lucide_ic_percent
        "capitalgain" -> LucideR.drawable.lucide_ic_trending_up
        "propertygroup" -> TablerOutlineIcons.drawable.tabler_ic_buildings_outline
        "otherincome" -> TablerOutlineIcons.drawable.tabler_ic_cash_outline
        "giftcashbackrefund" -> LucideR.drawable.lucide_ic_gift
        "banktobank" -> R.drawable.ic_transfer
        "bankwallet" -> R.drawable.ic_transfer
        "creditcardpayment" -> TablerOutlineIcons.drawable.tabler_ic_credit_card_pay_outline
        "loanpayment" -> LucideR.drawable.lucide_ic_calendar_arrow_up
        "savingsdeposit" -> R.drawable.ic_piggybank_outlined
        "atm" -> MaterialFilledIcons.drawable.materialicons_ic_atm_filled
        "wallettopup" -> MaterialSymbolsOutlinedIcons.drawable.materialsymbols_ic_wallet_outlined
        "investmenttransfer" -> R.drawable.ic_transfer
        "fd" -> TablerOutlineIcons.drawable.tabler_ic_building_bank_outline
        else -> fallbackIcon
    }
}