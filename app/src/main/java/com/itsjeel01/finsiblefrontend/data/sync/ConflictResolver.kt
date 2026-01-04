package com.itsjeel01.finsiblefrontend.data.sync

import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionDelta
import com.itsjeel01.finsiblefrontend.ui.inappnotification.NotificationManager
import javax.inject.Inject

class ConflictResolver @Inject constructor(
    private val transactionLocalRepo: TransactionLocalRepository,
    private val notificationManager: NotificationManager
) {
    fun applyDeltaChanges(
        serverChanges: List<TransactionDelta>,
        localPending: List<PendingOperationEntity>
    ) {
        for (change in serverChanges) {
            val hasLocalPending = localPending.any {
                it.entityId == change.id && it.status == Status.PENDING
            }

            when {
                change.deleted -> {
                    // Server deleted this transaction
                    transactionLocalRepo.removeById(change.id)
                    if (hasLocalPending) {
                        // User edited something that's deleted
                        notificationManager.showWarning(
                            "A transaction you edited was deleted elsewhere"
                        )
                    }
                }

                hasLocalPending -> {
                    // Conflict: server and local both modified
                    // Server wins - update local, discard pending edit
                    change.transaction?.let { transactionLocalRepo.upsert(change.transaction.toEntity()) }
                    notificationManager.showInfo(
                        "A transaction was updated elsewhere. Your changes were overwritten."
                    )
                }

                else -> {
                    // Normal update, no conflict
                    change.transaction?.let { transactionLocalRepo.upsert(change.transaction.toEntity()) }
                }
            }
        }
    }
}