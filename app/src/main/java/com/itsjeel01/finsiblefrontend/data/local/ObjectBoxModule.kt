package com.itsjeel01.finsiblefrontend.data.local

import android.content.Context
import com.itsjeel01.finsiblefrontend.data.local.entity.MyObjectBox
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
    fun boxStore(): BoxStore {
        return store
    }
}
