package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

/** Generates unique negative IDs for local-only entities not yet synced to server. */
@Singleton
class LocalIdGenerator @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    private var isInitialized = false
    private val idCounter = AtomicLong(0)

    suspend fun nextLocalId(): Long {
        if (!isInitialized) {
            idCounter.set(preferenceManager.getLocalIdCounter())
            isInitialized = true
        }

        val next = idCounter.decrementAndGet()
        preferenceManager.saveLocalIdCounter(next)
        return next  // Returns -1, -2, -3, ...
    }

    fun isLocalId(id: Long): Boolean = id < 0
}