package com.itsjeel01.finsiblefrontend.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.itsjeel01.finsiblefrontend.data.di.LocaleEntryPoint
import dagger.hilt.android.EntryPointAccessors
import java.util.Locale

/**
 * Compose-friendly wrapper to obtain the user's locale via Hilt entry point.
 * Kept in UI layer so data/di no longer depends on Compose runtime.
 */
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

