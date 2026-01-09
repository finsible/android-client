package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.SyncState
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.PendingOperationRepository
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
    private val pendingOperationRepository: PendingOperationRepository,
    private val networkMonitor: NetworkMonitor,
    private val coroutineScope: CoroutineScope,
    private val syncHandlers: Map<EntityType, @JvmSuppressWildcards EntitySyncHandler>
) {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _pendingCount = MutableStateFlow(0)
    val pendingCount: StateFlow<Int> = _pendingCount.asStateFlow()

    private var isSyncing = false

    init {
        networkMonitor.initialize()
        observeNetworkAndSync()
        updatePendingCount()

        Logger.Sync.i("SyncManager initialized with ${syncHandlers.size} handlers: ${syncHandlers.keys}")
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
        if (isSyncing) {
            Logger.Sync.d("Already syncing, skipping")
            return
        }

        val pendingOps = pendingOperationRepository.getPending()
        if (pendingOps.isEmpty()) {
            _syncState.value = SyncState.Idle
            return
        }

        isSyncing = true
        _syncState.value = SyncState.Syncing(pendingOps.size)
        Logger.Sync.i("Processing sync queue: ${pendingOps.size} operations")

        var remaining = pendingOps.size

        for (operation in pendingOps) {
            // Check network before each operation
            if (!networkMonitor.isOnline.value) {
                Logger.Sync.w("Network lost, pausing sync")
                _syncState.value = SyncState.Idle
                isSyncing = false
                return
            }

            try {
                processOperation(operation)
                operation.status = Status.COMPLETED
                pendingOperationRepository.update(operation)
                remaining--
                _syncState.value = SyncState.Syncing(remaining)
            } catch (e: SyncException) {
                handleSyncError(operation, e)
            } catch (e: Exception) {
                handleSyncError(operation, SyncException("Unexpected: ${e.message}", true, e))
            }
        }

        pendingOperationRepository.removeCompleted()
        updatePendingCount()

        _syncState.value = SyncState.Idle
        isSyncing = false
        Logger.Sync.i("Sync queue complete")
    }

    private suspend fun processOperation(operation: PendingOperationEntity) {
        val handler = syncHandlers[operation.entityType]
            ?: throw SyncException("No handler for ${operation.entityType}", false)

        when (operation.operationType) {
            OperationType.CREATE -> handler.processCreate(operation)
            OperationType.UPDATE -> handler.processUpdate(operation)
            OperationType.DELETE -> handler.processDelete(operation)
            null -> throw SyncException("Operation type is null", false)
        }
    }

    private fun handleSyncError(operation: PendingOperationEntity, error: SyncException) {
        Logger.Sync.w("Sync error for op ${operation.localId}: ${error.message}")

        operation.retryCount++
        operation.lastError = error.message

        if (!error.isRetryable || operation.retryCount >= MAX_RETRIES) {
            operation.status = Status.FAILED
            Logger.Sync.e("Operation ${operation.localId} marked FAILED after ${operation.retryCount} attempts")
        }

        pendingOperationRepository.update(operation)
    }

    /** Retry all failed operations. */
    fun retryFailed() {
        coroutineScope.launch {
            val failed = pendingOperationRepository.getFailed()

            Logger.Sync.i("Retrying ${failed.size} failed operations")

            for (op in failed) {
                op.status = Status.PENDING
                op.retryCount = 0
                op.lastError = null
                pendingOperationRepository.update(op)
            }

            updatePendingCount()
            processQueue()
        }
    }

    private fun updatePendingCount() {
        _pendingCount.value = pendingOperationRepository.getPendingCount().toInt()
    }

    companion object {
        const val MAX_RETRIES = 3
    }
}