package com.itsjeel01.finsiblefrontend.common

import com.itsjeel01.finsiblefrontend.ui.util.DateUtils

fun Long.toReadableDate(): String {
    return DateUtils.readableDate(this)
}

/** Parse a user-typed decimal String to centis, returning 0 on invalid input. */
fun String.asCentisAmount(): Long {
    if (isBlank()) return 0L
    val parts = trim().split(".")
    val whole = parts[0].toLongOrNull() ?: return 0L
    val decimal = if (parts.size > 1) parts[1].take(2).padEnd(2, '0').toLongOrNull() ?: 0L else 0L
    return whole * 100L + decimal
}
