package com.itsjeel01.finsiblefrontend.common

import android.content.Context
import android.icu.util.TimeZone
import android.telephony.TelephonyManager
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

interface LocaleProvider {
    fun currentLocale(): Locale
    fun currentRegionIso(): String
}

@Singleton
class DeviceLocaleProvider @Inject constructor(
    private val context: Context,
) : LocaleProvider {

    override fun currentLocale(): Locale {
        // Respects Android 13 Per-App Languages and Context overrides
        val configLocales = context.resources.configuration.locales
        val language = if (!configLocales.isEmpty) {
            configLocales.get(0).language
        } else {
            "en"
        }

        // Force region to match hardware/timezone to defeat OEM bugs
        val region = currentRegionIso()

        // Use Locale.Builder instead of the deprecated constructor
        return Locale.Builder()
            .setLanguage(language)
            .setRegion(region)
            .build()
    }

    override fun currentRegionIso(): String {
        // TimeZone is the most robust signal for physical region
        val tzId = TimeZone.getDefault().id
        val tzRegion = TimeZone.getRegion(tzId)
        if (tzRegion != null && tzRegion.length == 2) {
            return tzRegion.uppercase()
        }

        // SIM Country fallback
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        val simCountry = tm?.simCountryIso
        if (!simCountry.isNullOrBlank()) {
            return simCountry.uppercase()
        }

        // Context fallback
        val configLocales = context.resources.configuration.locales
        if (!configLocales.isEmpty) {
            val country = configLocales.get(0).country
            if (country.isNotBlank()) return country.uppercase()
        }

        return "US"
    }
}