package com.itsjeel01.finsiblefrontend

import android.app.Application
import com.itsjeel01.finsiblefrontend.data.local.ObjectBoxModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FinsibleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBoxModule.init(this)
    }
}
