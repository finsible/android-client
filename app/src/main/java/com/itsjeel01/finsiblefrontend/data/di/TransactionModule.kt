package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
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
class TransactionModule {
    @Provides
    @Singleton
    fun provideTransactionBox(store: BoxStore): Box<TransactionEntity> {
        return store.boxFor(TransactionEntity::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionLocalRepository(
        transactionBox: Box<TransactionEntity>,
        pendingOperationBox: Box<PendingOperationEntity>,
        localIdGenerator: LocalIdGenerator
    ): TransactionLocalRepository {
        return TransactionLocalRepository(
            transactionBox,
            pendingOperationBox,
            localIdGenerator
        )
    }
}