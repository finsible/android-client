package com.itsjeel01.finsiblefrontend.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.di.CurrencyFormatterEntryPoint
import dagger.hilt.android.EntryPointAccessors

/**
 * Compose-friendly wrapper to obtain CurrencyFormatter via Hilt entry point.
 * Kept in UI layer so data/di no longer depends on Compose runtime.
 */
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

