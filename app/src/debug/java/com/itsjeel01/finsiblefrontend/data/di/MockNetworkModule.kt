package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import com.itsjeel01.finsiblefrontend.data.remote.interceptor.MockInterceptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugNetworkInterceptorsProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val testPrefs: TestPreferenceManager
) : NetworkInterceptorsProvider {
    override fun interceptors(): List<Interceptor> {
        val mockInterceptor = MockInterceptor(context, testPrefs)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return listOf(mockInterceptor, loggingInterceptor)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MockNetworkModule {
    @Binds
    @Singleton
    abstract fun bindNetworkInterceptorsProvider(
        impl: DebugNetworkInterceptorsProvider
    ): NetworkInterceptorsProvider
}