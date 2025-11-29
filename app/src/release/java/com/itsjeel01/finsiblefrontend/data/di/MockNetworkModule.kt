package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.data.remote.interceptor.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Release module that provides null for MockInterceptor (not available in release builds). */
@Module
@InstallIn(SingletonComponent::class)
object MockNetworkModule {

    @Provides
    @Singleton
    fun provideMockInterceptor(): MockInterceptor? {
        return null
    }
}

