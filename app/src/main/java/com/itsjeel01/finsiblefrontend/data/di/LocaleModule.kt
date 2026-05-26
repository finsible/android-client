package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.common.LocaleProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.util.Locale

/**
 * Non-Compose helper to resolve the application's user locale via Hilt entry-point.
 *
 * Note: Compose-specific helpers were moved to the UI layer. Data layer exposes a
 * Context-taking function to avoid depending on Compose runtime.
 */
fun hiltUserLocale(context: Context): Locale {
    val appContext = context.applicationContext
    return EntryPointAccessors.fromApplication(
        appContext,
        LocaleEntryPoint::class.java,
    ).localeProvider().currentLocale()
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleEntryPoint {
    fun localeProvider(): LocaleProvider
}

