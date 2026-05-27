package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.data.local.entity.ExchangeRateEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExchangeRateModule {

    @Provides
    @Singleton
    fun exchangeRateEntityBox(store: BoxStore): Box<ExchangeRateEntity> {
        return store.boxFor(ExchangeRateEntity::class.java)
    }
}