package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AccountModule {

    @Provides
    @Singleton
    fun accountEntityBox(store: BoxStore): Box<AccountEntity> {
        return store.boxFor(AccountEntity::class.java)
    }

    @Provides
    @Singleton
    fun accountGroupEntityBox(store: BoxStore): Box<AccountGroupEntity> {
        return store.boxFor(AccountGroupEntity::class.java)
    }

    @Provides
    @Singleton
    fun accountLocalRepository(
        accountEntityBox: Box<AccountEntity>,
        syncMetadataBox: Box<SyncMetadataEntity>,
        pendingOperationBox: Box<PendingOperationEntity>,
        localIdGenerator: LocalIdGenerator
    ): AccountLocalRepository {
        return AccountLocalRepository(
            accountEntityBox,
            syncMetadataBox,
            pendingOperationBox,
            localIdGenerator
        )
    }

    @Provides
    @Singleton
    fun accountGroupLocalRepository(
        accountGroupEntityBox: Box<AccountGroupEntity>,
        syncMetadataBox: Box<SyncMetadataEntity>,
        pendingOperationBox: Box<PendingOperationEntity>,
        localIdGenerator: LocalIdGenerator
    ): AccountGroupLocalRepository {
        return AccountGroupLocalRepository(
            accountGroupEntityBox,
            syncMetadataBox,
            pendingOperationBox,
            localIdGenerator
        )
    }
}
