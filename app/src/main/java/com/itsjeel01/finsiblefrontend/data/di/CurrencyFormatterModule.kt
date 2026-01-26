package com.itsjeel01.finsiblefrontend.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/** Provides CurrencyFormatter instance using Hilt entry point for Compose usage. */
@Composable
fun hiltCurrencyFormatter(): CurrencyFormatter {
    val context = LocalContext.current
    val appContext = context.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(
            appContext,
            CurrencyFormatterEntryPoint::class.java
        ).currencyFormatter()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CurrencyFormatterEntryPoint {
    fun currencyFormatter(): CurrencyFormatter
}

