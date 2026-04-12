package com.itsjeel01.finsiblefrontend.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.itsjeel01.finsiblefrontend.common.LocaleProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.util.Locale

@Composable
fun hiltUserLocale(): Locale {
    val context = LocalContext.current
    val appContext = context.applicationContext
    return remember(appContext) {
        EntryPointAccessors.fromApplication(
            appContext,
            LocaleEntryPoint::class.java,
        ).localeProvider().currentLocale()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocaleEntryPoint {
    fun localeProvider(): LocaleProvider
}

