package com.itsjeel01.finsiblefrontend.common

import android.icu.math.BigDecimal
import android.icu.math.MathContext
import android.icu.text.CompactDecimalFormat
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyFormatter @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    private data class FormatterCacheKey(
        val currencyCode: String,
        val localeTag: String,
        val compact: Boolean,
        val useGrouping: Boolean,
        val minFractionDigits: Int,
        val maxFractionDigits: Int,
    )

    enum class CurrencySymbolPosition { Prefix, Suffix }

    data class CurrencyFormatOptions(
        val includeCurrencySymbol: Boolean = true,
        val includeSpaceAfterCurrencySymbol: Boolean = false,
        val includeSign: Boolean = true,
        val includeSpaceAfterSign: Boolean = false,
        val showPositiveSign: Boolean = false,
        val useGrouping: Boolean = true,
        val minFractionDigits: Int = 0,
        val maxFractionDigits: Int = 2,
        val symbolPosition: CurrencySymbolPosition = CurrencySymbolPosition.Prefix,
    )

    private val threadLocalConfiguredFormatters = object : ThreadLocal<MutableMap<FormatterCacheKey, DecimalFormat>>() {
        override fun initialValue(): MutableMap<FormatterCacheKey, DecimalFormat> = mutableMapOf()
    }

    fun format(
        centis: Long,
        currencyCode: String,
        options: CurrencyFormatOptions = CurrencyFormatOptions(),
    ): String {
        val absCentis = if (centis < 0L) -centis else centis
        val currency = currencyRepository.getByIsoCode(currencyCode)
        val symbol = currency?.symbol ?: currencyCode
        val formatter = getConfiguredFormatter(currency = currency, compact = false, options = options)
        val formattedValue = formatter.format(centisToDecimal(absCentis))
        return composeValue(
            formattedAmount = formattedValue,
            isNegative = centis < 0L,
            symbol = symbol,
            options = options,
        )
    }

    fun formatCompact(
        centis: Long,
        currencyCode: String,
        options: CurrencyFormatOptions = CurrencyFormatOptions(),
    ): String {
        val absCentis = if (centis < 0L) -centis else centis
        val currency = currencyRepository.getByIsoCode(currencyCode)
        val symbol = currency?.symbol ?: currencyCode
        val compactFormatter = getConfiguredFormatter(currency = currency, compact = true, options = options)

        // ICU natively scales the number down AND appends K, M, L, Cr based on the locale!
        val formattedAmount = compactFormatter.format(centisToDecimal(absCentis))

        return composeValue(
            formattedAmount = formattedAmount,
            isNegative = centis < 0L,
            symbol = symbol,
            options = options,
        )
    }

    private fun getConfiguredFormatter(
        currency: Currency?,
        compact: Boolean,
        options: CurrencyFormatOptions,
    ): DecimalFormat {
        val normalized = normalizeOptions(options)

        // 1. Get the user's actual device language (e.g., "es" for Spanish)
        val userLocale = UserLocaleRegistry.currentLocale()

        // 2. Build the Hybrid Display Locale: User's Language + Currency's Native Region
        val resolvedLocale = currency?.localeTag?.let { tag ->
            val currencyRegion = Locale.forLanguageTag(tag).country

            Locale.Builder()
                .setLanguage(userLocale.language) // User can read this!
                .setRegion(currencyRegion)        // Region dictates the grouping (e.g. IN for Lakhs)
                .build()
        } ?: userLocale

        val key = FormatterCacheKey(
            currencyCode = currency?.code ?: "",
            localeTag = resolvedLocale.toLanguageTag(),
            compact = compact,
            useGrouping = normalized.useGrouping,
            minFractionDigits = normalized.minFractionDigits,
            maxFractionDigits = normalized.maxFractionDigits,
        )

        val cache = threadLocalConfiguredFormatters.get()!!
        return cache.getOrPut(key) {

            val formatter = if (compact) {
                CompactDecimalFormat.getInstance(
                    resolvedLocale,
                    CompactDecimalFormat.CompactStyle.SHORT
                ) as DecimalFormat
            } else {
                NumberFormat.getNumberInstance(resolvedLocale) as DecimalFormat
            }

            formatter.apply {
                isGroupingUsed = normalized.useGrouping
                minimumFractionDigits = normalized.minFractionDigits
                maximumFractionDigits = normalized.maxFractionDigits
                if (compact) roundingMode = MathContext.ROUND_HALF_EVEN
            }
        }
    }

    private fun normalizeOptions(options: CurrencyFormatOptions): CurrencyFormatOptions {
        val minFractionDigits = options.minFractionDigits.coerceAtLeast(0)
        val maxFractionDigits = options.maxFractionDigits.coerceAtLeast(minFractionDigits)
        return options.copy(minFractionDigits = minFractionDigits, maxFractionDigits = maxFractionDigits)
    }

    private fun composeValue(
        formattedAmount: String,
        isNegative: Boolean,
        symbol: String,
        options: CurrencyFormatOptions,
    ): String {
        val signPrefix = if (options.includeSign) {
            when {
                isNegative -> "-${if (options.includeSpaceAfterSign) " " else ""}"
                options.showPositiveSign -> "+${if (options.includeSpaceAfterSign) " " else ""}"
                else -> ""
            }
        } else {
            ""
        }

        val valueWithSymbol = if (!options.includeCurrencySymbol) {
            formattedAmount
        } else {
            val separator = if (options.includeSpaceAfterCurrencySymbol) " " else ""
            when (options.symbolPosition) {
                CurrencySymbolPosition.Prefix -> "$symbol$separator$formattedAmount"
                CurrencySymbolPosition.Suffix -> "$formattedAmount$separator$symbol"
            }
        }

        return "$signPrefix$valueWithSymbol"
    }

    private fun centisToDecimal(centis: Long): BigDecimal =
        BigDecimal(centis).movePointLeft(2)
}