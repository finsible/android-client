package com.itsjeel01.finsiblefrontend.common

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import java.util.Currency
import java.util.Locale

// UTC to Local Time
fun Long.convertUTCToLocal(): Long {
    val timeZone = java.util.TimeZone.getDefault()
    val offset = timeZone.getOffset(this)
    return this + offset
}

// Local Time to UTC
fun Long.convertLocalToUTC(): Long {
    val timeZone = java.util.TimeZone.getDefault()
    val offset = timeZone.getOffset(this)
    return this - offset
}

fun Long.toReadableDate(): String {
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(java.util.Date(this))
}

fun String.toReadableCurrency(): String {
    val locale = Locale.forLanguageTag("en-IN")
    val currencySymbol = Currency.getInstance(locale).symbol

    return try {
        val value = BigDecimal(this)
        val absoluteValue = value.abs()
        val thresholds = FinsibleUtils.INDIAN_CURRENCY_THRESHOLDS

        val (scaledValue, suffix) = thresholds.firstOrNull { absoluteValue >= it.first }?.let { threshold ->
            absoluteValue.divide(threshold.first, 4, MathContext.ROUND_HALF_EVEN) to threshold.second
        } ?: (absoluteValue to "")

        val formatted = FinsibleUtils.INDIAN_CURRENCY_FORMATTER.format(scaledValue.abs())
        val sign = if (value.signum() < 0) "-" else ""
        "$sign$currencySymbol$formatted$suffix"
    } catch (_: NumberFormatException) {
        this
    }
}

fun BigDecimal.toLocaleCurrency(): String {
    val locale = Locale.forLanguageTag("en-IN")
    val currencySymbol = Currency.getInstance(locale).symbol
    val sign = if (this.signum() < 0) "-" else ""
    val formatted = FinsibleUtils.INDIAN_CURRENCY_FORMATTER.format(this.abs())
    return "$sign$currencySymbol$formatted"
}

fun String.toLocaleCurrency(): String {
    return try {
        BigDecimal(this).toLocaleCurrency()
    } catch (_: NumberFormatException) {
        this
    }
}

fun BigDecimal.toReadableCurrency(): String = this.toString().toReadableCurrency()