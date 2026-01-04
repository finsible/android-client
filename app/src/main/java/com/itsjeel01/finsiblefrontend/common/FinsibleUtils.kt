package com.itsjeel01.finsiblefrontend.common

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import java.util.Calendar
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

        fun String.toPeriodBounds(): Pair<Long, Long> {
            val parts = this.split("-")
            val year = parts[0].toInt()
            val month = parts[1].toInt() - 1

            val start = Calendar.getInstance().apply {
                set(year, month, 1, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val end = Calendar.getInstance().apply {
                set(year, month, getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            return start to end
        }
    }
}