package com.itsjeel01.finsiblefrontend.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/** Provides CurrencyRepository instance using Hilt entry point for Compose usage. */
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

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CurrencyRepositoryEntryPoint {
    fun currencyRepository(): CurrencyRepository
}

