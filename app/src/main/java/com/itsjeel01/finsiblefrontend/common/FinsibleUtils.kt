package com.itsjeel01.finsiblefrontend.common

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import java.util.Locale


class FinsibleUtils {
    companion object {
        val INDIAN_CURRENCY_THRESHOLDS = listOf(
            BigDecimal("1000000000000000") to "Pad",
            BigDecimal("10000000000000") to "Ne",
            BigDecimal("100000000000") to "Khar",
            BigDecimal("1000000000") to "Ar",
            BigDecimal("10000000") to "Cr",
            BigDecimal("100000") to "L",
            BigDecimal("1000") to "K"
        )

        val INDIAN_CURRENCY_FORMATTER = DecimalFormat("##,##,##0.####", DecimalFormatSymbols(Locale.forLanguageTag("en-IN"))).apply {
            roundingMode = MathContext.ROUND_HALF_EVEN
            maximumFractionDigits = 2
            minimumFractionDigits = 0
            isGroupingUsed = true
        }
    }
}