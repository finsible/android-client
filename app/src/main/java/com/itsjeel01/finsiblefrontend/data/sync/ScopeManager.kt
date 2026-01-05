package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject
import javax.inject.Singleton

/** Manages the lifecycle of the application-level CoroutineScope for sync operations. */
@Singleton
class ScopeManager @Inject constructor() {
    @Volatile
    private var job: Job = SupervisorJob()
    @Volatile
    private var _scope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)

    /** The application-level CoroutineScope for background sync operations. */
    val scope: CoroutineScope
        get() = _scope

    /** Cancels all ongoing coroutines and creates a new scope. Called on logout. */
    @Synchronized
    fun reset() {
        Logger.Sync.i("Resetting CoroutineScope - cancelling all ongoing operations")
        _scope.cancel()
        job = SupervisorJob()
        _scope = CoroutineScope(job + Dispatchers.IO)
    }

    /** Cancels all ongoing coroutines. Called on application termination. */
    @Synchronized
    fun shutdown() {
        Logger.Sync.i("Shutting down CoroutineScope - cancelling all operations")
        _scope.cancel()
    }
}
