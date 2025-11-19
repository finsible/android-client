package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.data.local.entity.AccountEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountGroupLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.AccountLocalRepository
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
    ): AccountLocalRepository {
        return AccountLocalRepository(accountEntityBox)
    }

    @Provides
    @Singleton
    fun accountGroupLocalRepository(
        accountGroupEntityBox: Box<AccountGroupEntity>,
    ): AccountGroupLocalRepository {
        return AccountGroupLocalRepository(accountGroupEntityBox)
    }
}
