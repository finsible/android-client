package com.itsjeel01.finsiblefrontend.common

import java.text.SimpleDateFormat
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
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(java.util.Date(this))
}

/** Format centis with currency symbol: `₹1,234` or `-₹1,234`. */
fun Long.centisToFormattedCurrency(formatter: CurrencyFormatter): String =
    formatter.format(this)

/** Format centis with abbreviated suffixes (K, L, Cr, etc.): `₹1.5Cr`. */
fun Long.centisToCompactCurrency(formatter: CurrencyFormatter): String =
    formatter.formatCompact(this)

/** Format centis without sign: `1,234`. Use when adding custom signs/symbols. */
fun Long.centisToFormattedAmount(formatter: CurrencyFormatter): String =
    formatter.formatWithoutSign(this)

/** Format a user-typed decimal String with compact currency display. */
fun String.toCompactCurrency(formatter: CurrencyFormatter): String {
    val centis = toAmountCentisOrZero()
    return if (centis != 0L) formatter.formatCompact(centis) else this
}

/** Parse a user-typed decimal String to centis, returning 0 on invalid input. */
fun String.toAmountCentisOrZero(): Long {
    if (isBlank()) return 0L
    val parts = trim().split(".")
    val whole = parts[0].toLongOrNull() ?: return 0L
    val decimal = if (parts.size > 1) parts[1].take(2).padEnd(2, '0').toLongOrNull() ?: 0L else 0L
    return whole * 100L + decimal
}
