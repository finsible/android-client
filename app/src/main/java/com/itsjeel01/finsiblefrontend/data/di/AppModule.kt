package com.itsjeel01.finsiblefrontend.data.di

import android.app.Application
import android.content.Context
import com.itsjeel01.finsiblefrontend.common.DeviceLocaleProvider
import com.itsjeel01.finsiblefrontend.common.LocaleProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun context(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun localeProvider(deviceLocaleProvider: DeviceLocaleProvider): LocaleProvider {
        return deviceLocaleProvider
    }
}
