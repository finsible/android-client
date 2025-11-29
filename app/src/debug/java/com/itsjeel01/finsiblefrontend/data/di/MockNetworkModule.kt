package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import com.itsjeel01.finsiblefrontend.data.remote.interceptor.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Debug-only module to provide MockInterceptor. */
@Module
@InstallIn(SingletonComponent::class)
object MockNetworkModule {

    @Provides
    @Singleton
    fun provideMockInterceptor(
        @ApplicationContext context: Context,
        testPrefs: TestPreferenceManager
    ): MockInterceptor {
        return MockInterceptor(context, testPrefs)
    }
}