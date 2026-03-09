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
    val preferenceManager: PreferenceManager
) {
    val userCurrency = preferenceManager.getCurrency()

    companion object {
        private val CURRENCY_LOCALES = mapOf(
            Currency.INR to Locale.forLanguageTag("en-IN")
        )

        private val CURRENCY_PATTERNS = mapOf(
            Currency.INR to "##,##,##0.##"
        )

        /** Data class representing a threshold for compact currency formatting. */
        private data class Threshold(
            val limit: Long,
            val divisor: BigDecimal,
            val suffix: String,
        )

        /** Compact formatting thresholds in centis (×100). */
        private val CURRENCY_THRESHOLDS_CENTIS = mapOf(
            Currency.INR to listOf(
                Threshold(1_00_00_00_00_00_00_00_000L, BigDecimal(1_00_00_00_00_00_00_00_000L), "Pad"),
                Threshold(1_00_00_00_00_00_00_000L, BigDecimal(1_00_00_00_00_00_00_000L), "Ne"),
                Threshold(1_00_00_00_00_00_000L, BigDecimal(1_00_00_00_00_00_000L), "Khar"),
                Threshold(1_00_00_00_00_000L, BigDecimal(1_00_00_00_00_000L), "Ar"),
                Threshold(1_00_00_00_000L, BigDecimal(1_00_00_00_000L), "Cr"),
                Threshold(1_00_00_000L, BigDecimal(1_00_00_000L), "L"),
                Threshold(1_00_000L, BigDecimal(1_00_000L), "K")
            )
        )
    }

    /** ThreadLocal cache for DecimalFormat instances. Prevents expensive instantiations on the hot path while maintaining thread safety. */
    private val threadLocalFormatters = object : ThreadLocal<MutableMap<Currency, DecimalFormat>>() {
        override fun initialValue(): MutableMap<Currency, DecimalFormat> = mutableMapOf()
    }

    private fun getFormatter(currency: Currency): DecimalFormat {
        val cache = threadLocalFormatters.get()!!

        return cache.getOrPut(currency) {
            val locale = CURRENCY_LOCALES[currency] ?: Locale.getDefault()
            val pattern = CURRENCY_PATTERNS[currency] ?: "###,###,##0.##"

            DecimalFormat(pattern, DecimalFormatSymbols(locale)).apply {
                maximumFractionDigits = 2
                minimumFractionDigits = 0
                isGroupingUsed = true
            }
        }
    }

    /** Format centis with currency symbol: `₹1,234` or `-₹1,234`. */
    fun format(centis: Long, currency: Currency = userCurrency): String {
        val absCentis = if (centis < 0L) -centis else centis
        val sign = if (centis < 0L) "- " else ""

        val formattedValue = getFormatter(currency).format(centisToDecimal(absCentis))
        return "$sign${currency.getSymbol()}$formattedValue"
    }

    /** Format centis with abbreviated suffixes (K, L, Cr, etc.) for compact display. */
    fun formatCompact(centis: Long, currency: Currency = userCurrency): String {
        val absCentis = if (centis < 0L) -centis else centis

        val (scaledDecimal, suffix) = CURRENCY_THRESHOLDS_CENTIS[currency]
            ?.firstOrNull { absCentis >= it.limit }
            ?.let { threshold ->
                val scaled = BigDecimal(absCentis).divide(threshold.divisor, 4, MathContext.ROUND_HALF_EVEN)
                scaled to threshold.suffix
            }
            ?: (centisToDecimal(absCentis) to "")

        val formatter = getFormatter(currency).apply { roundingMode = MathContext.ROUND_HALF_EVEN }
        val sign = if (centis < 0L) "- " else ""

        return "$sign${currency.getSymbol()}${formatter.format(scaledDecimal)}$suffix"
    }

    /** Format centis without sign: `1,234`. Use when caller needs to add custom signs/symbols. */
    fun formatWithoutSign(centis: Long, currency: Currency = userCurrency): String {
        val absCentis = if (centis < 0L) -centis else centis
        return getFormatter(currency).format(centisToDecimal(absCentis))
    }

    /** Convert centis Long to ICU BigDecimal for DecimalFormat. E.g., 12345L → 123.45. */
    private fun centisToDecimal(centis: Long): BigDecimal =
        BigDecimal(centis).divide(BigDecimal(100L))
}