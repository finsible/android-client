package com.itsjeel01.finsiblefrontend.common

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/** Centralized currency formatting utility that respects user's currency preference. */
@Singleton
class CurrencyFormatter @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    companion object {
        /** Locale configuration for different currencies. */
        private val CURRENCY_LOCALES = mapOf(
            Currency.INR to Locale.forLanguageTag("en-IN")
        )

        /** Grouping patterns for different currencies. */
        private val CURRENCY_PATTERNS = mapOf(
            Currency.INR to "##,##,##0.##"
        )

        /** Compact formatting thresholds for abbreviated display (K, L, Cr, etc.). */
        private val CURRENCY_THRESHOLDS = mapOf(
            Currency.INR to listOf(
                BigDecimal("1000000000000000") to "Pad",
                BigDecimal("10000000000000") to "Ne",
                BigDecimal("100000000000") to "Khar",
                BigDecimal("1000000000") to "Ar",
                BigDecimal("10000000") to "Cr",
                BigDecimal("100000") to "L",
                BigDecimal("1000") to "K"
            )
        )
    }

    /** Get the current user's currency from preferences. */
    fun getUserCurrency(): Currency = preferenceManager.getCurrency()

    /** Get locale for current user's currency. */
    private fun getLocaleForCurrency(currency: Currency = getUserCurrency()): Locale {
        return CURRENCY_LOCALES[currency] ?: Locale.getDefault()
    }

    /** Get pattern for current user's currency. */
    private fun getPatternForCurrency(currency: Currency = getUserCurrency()): String {
        return CURRENCY_PATTERNS[currency] ?: "###,###,##0.##"
    }

    /** Format amount with currency symbol and locale-specific formatting. */
    fun format(amount: BigDecimal, currency: Currency = getUserCurrency()): String {
        val locale = getLocaleForCurrency(currency)
        val pattern = getPatternForCurrency(currency)
        val formatter = DecimalFormat(pattern, DecimalFormatSymbols(locale)).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 0
            isGroupingUsed = true
        }

        val sign = if (amount.signum() < 0) "-" else ""
        val formattedValue = formatter.format(amount.abs())
        return "$sign${currency.getSymbol()}$formattedValue"
    }

    /** Format String amount with currency. */
    fun format(amount: String, currency: Currency = getUserCurrency()): String {
        return try {
            format(BigDecimal(amount), currency)
        } catch (_: NumberFormatException) {
            amount
        }
    }

    /** Format amount for UI display (with proper spacing and symbol). */
    fun formatForUI(amount: BigDecimal, currency: Currency = getUserCurrency()): String {
        val locale = getLocaleForCurrency(currency)
        val pattern = getPatternForCurrency(currency)
        val formatter = DecimalFormat(pattern, DecimalFormatSymbols(locale)).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 0
            isGroupingUsed = true
        }

        val sign = when {
            amount.signum() < 0 -> "- "
            amount.signum() > 0 -> "+ "
            else -> ""
        }
        val formattedValue = formatter.format(amount.abs())
        return "$sign${currency.getSymbol()}$formattedValue"
    }

    /** Format String amount for UI. */
    fun formatForUI(amount: String, currency: Currency = getUserCurrency()): String {
        return try {
            formatForUI(BigDecimal(amount), currency)
        } catch (_: NumberFormatException) {
            amount
        }
    }

    /** Format amount with abbreviated suffixes (K, L, Cr, etc.) for compact display in statistics/summaries. */
    fun formatCompact(amount: BigDecimal, currency: Currency = getUserCurrency()): String {
        val absoluteValue = amount.abs()
        val thresholds = CURRENCY_THRESHOLDS[currency] ?: emptyList()

        val (scaledValue, suffix) = thresholds.firstOrNull { absoluteValue >= it.first }?.let { threshold ->
            absoluteValue.divide(threshold.first, 4, MathContext.ROUND_HALF_EVEN) to threshold.second
        } ?: (absoluteValue to "")

        val locale = getLocaleForCurrency(currency)
        val pattern = getPatternForCurrency(currency)
        val formatter = DecimalFormat(pattern, DecimalFormatSymbols(locale)).apply {
            roundingMode = MathContext.ROUND_HALF_EVEN
            maximumFractionDigits = 2
            minimumFractionDigits = 0
            isGroupingUsed = true
        }

        val formatted = formatter.format(scaledValue.abs())
        val sign = if (amount.signum() < 0) "-" else ""
        return "$sign${currency.getSymbol()}$formatted$suffix"
    }

    /** Format String amount with abbreviated suffixes. */
    fun formatCompact(amount: String, currency: Currency = getUserCurrency()): String {
        return try {
            formatCompact(BigDecimal(amount), currency)
        } catch (_: NumberFormatException) {
            amount
        }
    }

    /** Format amount without sign (just formatted number). Use when caller needs to add custom signs. */
    fun formatWithoutSign(amount: BigDecimal, currency: Currency = getUserCurrency()): String {
        val locale = getLocaleForCurrency(currency)
        val pattern = getPatternForCurrency(currency)
        val formatter = DecimalFormat(pattern, DecimalFormatSymbols(locale)).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 0
            isGroupingUsed = true
        }

        return formatter.format(amount.abs())
    }

    /** Format String amount without sign. */
    fun formatWithoutSign(amount: String, currency: Currency = getUserCurrency()): String {
        return try {
            formatWithoutSign(BigDecimal(amount), currency)
        } catch (_: NumberFormatException) {
            amount
        }
    }
}
