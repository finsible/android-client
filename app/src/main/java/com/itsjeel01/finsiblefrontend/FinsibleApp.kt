package com.itsjeel01.finsiblefrontend

import android.app.Application
import com.itsjeel01.finsiblefrontend.common.logging.DebugLogTree
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.common.logging.ReleaseLogTree
import com.itsjeel01.finsiblefrontend.data.di.ObjectBoxModule
import com.itsjeel01.finsiblefrontend.data.sync.NetworkMonitor
import com.itsjeel01.finsiblefrontend.data.sync.ScopeManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FinsibleApp : Application() {
    @Inject
    lateinit var scopeManager: ScopeManager

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate() {
        super.onCreate()
        initializeLogging()
        ObjectBoxModule.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        // Note: onTerminate() is only called in emulated environments, not on actual devices.
        // This cleanup is primarily for testing. Production apps should rely on the logout
        // cleanup path in AuthRepository for proper resource management.
        networkMonitor.cleanup()
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
