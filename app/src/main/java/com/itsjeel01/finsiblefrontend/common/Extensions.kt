package com.itsjeel01.finsiblefrontend.common

import android.icu.math.BigDecimal
import java.util.Locale

/** UTC to Local Time */
fun Long.convertUTCToLocal(): Long {
    val timeZone = java.util.TimeZone.getDefault()
    val offset = timeZone.getOffset(this)
    return this + offset
}

/** Local Time to UTC */
fun Long.convertLocalToUTC(): Long {
    val timeZone = java.util.TimeZone.getDefault()
    val offset = timeZone.getOffset(this)
    return this - offset
}

fun Long.toReadableDate(): String {
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(java.util.Date(this))
}

/** Format amount with currency symbol: `₹1,234` or `-₹1,234`. Use for balances, account totals. */
fun BigDecimal.toFormattedCurrency(formatter: CurrencyFormatter): String = formatter.format(this)

/** Format String amount with currency symbol. */
fun String.toFormattedCurrency(formatter: CurrencyFormatter): String = formatter.format(this)

/** Format amount with abbreviated suffixes (K, L, Cr, etc.): `₹1.5Cr`. Use for statistics/summaries. */
fun BigDecimal.toCompactCurrency(formatter: CurrencyFormatter): String = formatter.formatCompact(this)

/** Format String amount with abbreviated suffixes. */
fun String.toCompactCurrency(formatter: CurrencyFormatter): String = formatter.formatCompact(this)

/** Format amount with sign and spacing: `+ ₹1,234` or `- ₹1,234`. Use for transaction lists. */
fun BigDecimal.toSignedCurrency(formatter: CurrencyFormatter): String = formatter.formatForUI(this)

/** Format String amount with sign and spacing. */
fun String.toSignedCurrency(formatter: CurrencyFormatter): String = formatter.formatForUI(this)

/** Format amount without sign: `1,234`. Use when adding custom signs/symbols. */
fun BigDecimal.toAmountOnly(formatter: CurrencyFormatter): String = formatter.formatWithoutSign(this)

/** Format String amount without sign. */
fun String.toAmountOnly(formatter: CurrencyFormatter): String = formatter.formatWithoutSign(this)
