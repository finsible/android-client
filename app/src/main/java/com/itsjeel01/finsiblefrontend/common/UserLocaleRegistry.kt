package com.itsjeel01.finsiblefrontend.common

import java.util.Currency
import java.util.Locale

object UserLocaleRegistry {
    @Volatile
    private var localeProvider: LocaleProvider? = null

    fun initialize(provider: LocaleProvider) {
        localeProvider = provider
    }

    /** For formatting dates/numbers (Hybrid: Context Language + Real Region). */
    fun currentLocale(): Locale {
        return localeProvider?.currentLocale() ?: Locale.getDefault(Locale.Category.FORMAT)
    }

    /** For guessing the local currency based on hardware/timezone. */
    fun currentGeographicCurrencyCode(): String? {
        val regionIso = localeProvider?.currentRegionIso() ?: return null

        // Construct targeted region locale without deprecated constructor
        val regionLocale = Locale.Builder().setRegion(regionIso).build()

        return runCatching {
            Currency.getInstance(regionLocale).currencyCode
        }.getOrNull()
    }
}