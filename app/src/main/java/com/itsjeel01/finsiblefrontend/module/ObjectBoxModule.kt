package com.itsjeel01.finsiblefrontend.module

import android.content.Context
import com.itsjeel01.finsiblefrontend.data.objectbox.entity.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore

@Module
@InstallIn(SingletonComponent::class)
object ObjectBoxModule {
    private lateinit var store: BoxStore

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context)
            .build()
    }

    @Provides
    fun provideBoxStore(): BoxStore {
        return store
    }
}