package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import com.itsjeel01.finsiblefrontend.data.sync.AccountGroupSyncHandler
import com.itsjeel01.finsiblefrontend.data.sync.AccountSyncHandler
import com.itsjeel01.finsiblefrontend.data.sync.CategorySyncHandler
import com.itsjeel01.finsiblefrontend.data.sync.EntitySyncHandler
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import com.itsjeel01.finsiblefrontend.data.sync.TransactionSyncHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

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

    @Provides
    @Singleton
    fun provideCoroutineScope(scopeManager: ScopeManager): CoroutineScope {
        return scopeManager.scope
    }

    @Provides
    @Singleton
    fun provideSyncHandlers(
        transactionSyncHandler: TransactionSyncHandler,
        accountGroupSyncHandler: AccountGroupSyncHandler,
        accountSyncHandler: AccountSyncHandler,
        categorySyncHandler: CategorySyncHandler
    ): Map<EntityType, EntitySyncHandler> {
        return mapOf(
            EntityType.TRANSACTION to transactionSyncHandler,
            EntityType.ACCOUNT_GROUP to accountGroupSyncHandler,
            EntityType.ACCOUNT to accountSyncHandler,
            EntityType.CATEGORY to categorySyncHandler
        )
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
        }
    }
}

