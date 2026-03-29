package com.itsjeel01.finsiblefrontend.common

import android.content.Context
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

interface LocaleProvider {
    fun currentLocale(): Locale
}

@Singleton
class DeviceLocaleProvider @Inject constructor(
    private val context: Context,
) : LocaleProvider {
    override fun currentLocale(): Locale {
        return context.resources.configuration.locales.get(0) ?: Locale.getDefault()
    }
}


