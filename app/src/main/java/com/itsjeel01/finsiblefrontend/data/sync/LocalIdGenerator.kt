package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/** Generates unique negative IDs for local-only entities not yet synced to server. */
@Singleton
class LocalIdGenerator @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    private val mutex = Mutex()
    private var isInitialized = false
    private var idCounter = 0L

    suspend fun nextLocalId(): Long = mutex.withLock {
        if (!isInitialized) {
            idCounter = preferenceManager.getLocalIdCounter()
            isInitialized = true
        }
        val next = --idCounter
        preferenceManager.saveLocalIdCounter(next)
        next
    }

    fun isLocalId(id: Long): Boolean = id < 0
}
