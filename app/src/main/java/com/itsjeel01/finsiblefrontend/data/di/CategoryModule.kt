package com.itsjeel01.finsiblefrontend.data.di

import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.CategoryLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.Box
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryModule {

    @Provides
    @Singleton
    fun categoryEntityBox(store: BoxStore): Box<CategoryEntity> {
        return store.boxFor(CategoryEntity::class.java)
    }


    @Provides
    @Singleton
    fun categoryLocalRepository(
        categoryEntityBox: Box<CategoryEntity>,
    ): CategoryLocalRepository {
        return CategoryLocalRepository(categoryEntityBox)
    }
}
