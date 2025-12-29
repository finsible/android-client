package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SyncModule {
    @Provides
    @Singleton
    fun providePendingOperationBox(store: BoxStore): Box<PendingOperationEntity> {
        return store.boxFor(PendingOperationEntity::class.java)
    }

    @Provides
    @Singleton
    fun provideSyncMetadataBox(store: BoxStore): Box<SyncMetadataEntity> {
        return store.boxFor(SyncMetadataEntity::class.java)
    }
}