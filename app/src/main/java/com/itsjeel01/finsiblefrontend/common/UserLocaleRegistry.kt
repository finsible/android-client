package com.itsjeel01.finsiblefrontend.common

import java.util.Locale

object UserLocaleRegistry {
    @Volatile
    private var localeProvider: LocaleProvider? = null

    fun initialize(provider: LocaleProvider) {
        localeProvider = provider
    }

    fun currentLocale(): Locale {
        return localeProvider?.currentLocale() ?: Locale.getDefault()
    }
}

