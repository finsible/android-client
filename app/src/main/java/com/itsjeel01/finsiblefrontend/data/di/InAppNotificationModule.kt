package com.itsjeel01.finsiblefrontend.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InAppNotificationModule {

    /** Provides singleton NotificationManager for single notification display. */
    @Provides
    @Singleton
    fun provideNotificationManager(): InAppNotificationManager {
        return InAppNotificationManager()
    }
}

/** Provides NotificationManager instance using Hilt entry point for Compose usage. */
@Composable
fun hiltNotificationManager(): InAppNotificationManager {
    val context = LocalContext.current
    return remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            NotificationManagerEntryPoint::class.java
        ).notificationManager()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotificationManagerEntryPoint {
    fun notificationManager(): InAppNotificationManager
}