package com.itsjeel01.finsiblefrontend;

import android.app.Application
import com.itsjeel01.finsiblefrontend.module.ObjectBoxModule
import com.itsjeel01.finsiblefrontend.utils.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import io.objectbox.BoxStore

@HiltAndroidApp
class MyApplication : Application() {
    private lateinit var boxStore: BoxStore

    override fun onCreate() {
        super.onCreate()
        ObjectBoxModule.init(this)
    }

    fun tokenProvider(): String? {
        return PreferenceManager(this).getJwt()
    }
}