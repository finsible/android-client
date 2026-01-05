package com.itsjeel01.finsiblefrontend

import android.app.Application
import com.itsjeel01.finsiblefrontend.common.logging.DebugLogTree
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.common.logging.ReleaseLogTree
import com.itsjeel01.finsiblefrontend.data.di.ObjectBoxModule
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FinsibleApp : Application() {
    @Inject
    lateinit var scopeManager: ScopeManager

    override fun onCreate() {
        super.onCreate()
        initializeLogging()
        ObjectBoxModule.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        scopeManager.shutdown()
    }

    private fun initializeLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugLogTree())
            Logger.App.d("Debug logging enabled")
        } else {
            Timber.plant(ReleaseLogTree())
        }
    }
}
