package com.itsjeel01.finsiblefrontend

import android.app.Application
import android.content.Context
import com.itsjeel01.finsiblefrontend.utils.NavControllerWrapper
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
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideNavControllerWrapper(): NavControllerWrapper {
        return NavControllerWrapper()
    }
}