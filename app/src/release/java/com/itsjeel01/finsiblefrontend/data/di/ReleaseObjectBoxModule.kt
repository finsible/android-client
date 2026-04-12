package com.itsjeel01.finsiblefrontend.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReleaseObjectBoxModule {
    @Binds
    @Singleton
    abstract fun bindObjectBoxAdminStarter(
        impl: ReleaseObjectBoxAdminStarter
    ): ObjectBoxAdminStarter
}

