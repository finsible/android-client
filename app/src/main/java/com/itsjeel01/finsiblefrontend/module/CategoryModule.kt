package com.itsjeel01.finsiblefrontend.module

import com.itsjeel01.finsiblefrontend.data.network.apis.CategoryApiService
import com.itsjeel01.finsiblefrontend.data.objectbox.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.objectbox.repositories.CategoryLocalRepository
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
    fun provideCategoryEntityBox(store: BoxStore): Box<CategoryEntity> {
        return store.boxFor(CategoryEntity::class.java)
    }


    @Provides
    @Singleton
    fun provideCategoryEntityLocalRepository(
        categoryEntityBox: Box<CategoryEntity>,
        categoryApiService: CategoryApiService,
    ): CategoryLocalRepository {
        return CategoryLocalRepository(categoryEntityBox, categoryApiService)
    }
}