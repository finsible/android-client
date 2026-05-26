package com.itsjeel01.finsiblefrontend.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.itsjeel01.finsiblefrontend.data.di.CurrencyRepositoryEntryPoint
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import dagger.hilt.android.EntryPointAccessors

/**
 * Compose-friendly wrapper to obtain CurrencyRepository via Hilt entry point.
 * Kept in UI layer so data/di no longer depends on Compose runtime.
 */
@Composable
fun hiltCurrencyRepository(): CurrencyRepository {
    val context = LocalContext.current
    val appContext = context.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(
            appContext,
            CurrencyRepositoryEntryPoint::class.java
        ).currencyRepository()
    }
}

