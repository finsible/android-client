package com.itsjeel01.finsiblefrontend.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReleaseNetworkModule {
    @Binds
    @Singleton
    abstract fun bindNetworkInterceptorsProvider(
        impl: ReleaseNetworkInterceptorsProvider
    ): NetworkInterceptorsProvider
}

