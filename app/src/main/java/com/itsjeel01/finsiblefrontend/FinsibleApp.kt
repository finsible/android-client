package com.itsjeel01.finsiblefrontend

import android.app.Application
import com.itsjeel01.finsiblefrontend.common.logging.DebugLogTree
import com.itsjeel01.finsiblefrontend.common.logging.ReleaseLogTree
import com.itsjeel01.finsiblefrontend.data.di.ObjectBoxModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FinsibleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeLogging()
        ObjectBoxModule.init(this)
    }

    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugLogTree())
            Timber.d("Debug logging enabled")
        } else {
            Timber.plant(ReleaseLogTree())
        }
    }
}
