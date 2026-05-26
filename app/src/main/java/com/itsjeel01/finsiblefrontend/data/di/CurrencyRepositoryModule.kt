package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Non-Compose helper to resolve CurrencyRepository via Hilt entry-point.
 *
 * Compose wrappers were moved to the UI layer. Data layer exposes a Context-taking
 * function to avoid depending on Compose runtime.
 */
fun hiltCurrencyRepository(context: Context): CurrencyRepository {
    val appContext = context.applicationContext
    return EntryPointAccessors.fromApplication(
        appContext,
        CurrencyRepositoryEntryPoint::class.java
    ).currencyRepository()
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CurrencyRepositoryEntryPoint {
    fun currencyRepository(): CurrencyRepository
}

