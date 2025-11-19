package com.itsjeel01.finsiblefrontend.data.di

import android.content.Context
import android.util.Log
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.data.local.entity.MyObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.objectbox.BoxStore
import io.objectbox.android.Admin

@Module
@InstallIn(SingletonComponent::class)
object ObjectBoxModule {
    private lateinit var store: BoxStore

    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context)
            .build()

        if (BuildConfig.DEBUG) startAdmin(context)
    }

    fun startAdmin(context: Context) {
        val started = Admin(store).start(context)
        Log.d("ObjectBox", "Admin started: $started")
    }

    @Provides
    fun boxStore(): BoxStore {
        return store
    }
}