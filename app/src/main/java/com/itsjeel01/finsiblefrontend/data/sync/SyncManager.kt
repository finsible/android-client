package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.SyncState
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity_
import io.objectbox.Box
import io.objectbox.kotlin.equal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/** Manages background synchronization of local changes to server. */
@Singleton
class SyncManager @Inject constructor(
    private val pendingOperationBox: Box<PendingOperationEntity>,
    private val networkMonitor: NetworkMonitor,
    private val coroutineScope: CoroutineScope
) {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _pendingCount = MutableStateFlow(0)
    val pendingCount: StateFlow<Int> = _pendingCount.asStateFlow()

    init {
        observeNetworkAndSync()
        updatePendingCount()
    }

    private fun observeNetworkAndSync() {
        coroutineScope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                if (isOnline) {
                    processQueue()
                }
            }
        }
    }

    suspend fun processQueue() {
        // TODO: Phase 3 implementation
        updatePendingCount()
    }

    private fun updatePendingCount() {
        val count = pendingOperationBox.query()
            .equal(
                PendingOperationEntity_.status,
                StatusConverter().convertToDatabaseValue(Status.PENDING)!!
            )
            .build()
            .count()
        _pendingCount.value = count.toInt()
    }

    companion object {
        const val MAX_RETRIES = 3
    }
}