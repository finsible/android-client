package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Inject
import javax.inject.Singleton

/** Manages the lifecycle of the application-level CoroutineScope for background operations. */
@Singleton
class ScopeManager @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    @Volatile
    private var _scope: CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    /** The application-level CoroutineScope. */
    val scope: CoroutineScope
        get() = _scope

    /** Cancels all ongoing coroutines and creates a new scope. Called on logout. */
    @Synchronized
    fun reset() {
        Logger.Sync.i("Resetting CoroutineScope - cancelling all ongoing operations")
        _scope.cancel()
        _scope = CoroutineScope(SupervisorJob() + ioDispatcher)
    }

    /** Cancels all ongoing coroutines. */
    @Synchronized
    fun shutdown() {
        Logger.Sync.i("Shutting down CoroutineScope - cancelling all operations")
        _scope.cancel()
    }
}