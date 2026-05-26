package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Non-Compose helper to resolve CurrencyFormatter via Hilt entry-point.
 *
 * Compose wrappers were moved to the UI layer. Data layer exposes a Context-taking
 * function to avoid depending on Compose runtime.
 */
fun hiltCurrencyFormatter(context: Context): CurrencyFormatter {
    val appContext = context.applicationContext
    return EntryPointAccessors.fromApplication(
        appContext,
        CurrencyFormatterEntryPoint::class.java
    ).currencyFormatter()
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CurrencyFormatterEntryPoint {
    fun currencyFormatter(): CurrencyFormatter
}

