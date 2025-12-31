package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.EntityTypeConverter
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity_
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import io.objectbox.Box
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.notEqual
import java.util.Calendar
import javax.inject.Inject

class TransactionLocalRepository @Inject constructor(
    override val box: Box<TransactionEntity>,
    private val syncMetadataBox: Box<SyncMetadataEntity>
) : BaseLocalRepository<Transaction, TransactionEntity> {

    override fun addAll(data: List<Transaction>, additionalInfo: Any?, ttlMinutes: Long?) {
        super.addAll(data, additionalInfo, ttlMinutes)

        val entities = data.map { transaction ->
            transaction.toEntity().apply {
                updateCacheTime(ttlMinutes)
            }
        }

        box.put(entities)
        Logger.Database.d("Added ${entities.size} transactions to local DB")
    }

    override fun syncToServer(entity: TransactionEntity) {
        Logger.Database.w("syncToServer called directly - use SyncManager instead")
    }

    fun getTransactionsForPeriod(periodKey: String): List<TransactionEntity> {
        val (startMs, endMs) = periodKey.toPeriodBounds()

        return box.query()
            .between(TransactionEntity_.transactionDate, startMs, endMs)
            .orderDesc(TransactionEntity_.transactionDate)
            .build()
            .find()
            .also { Logger.Database.d("Fetched ${it.size} transactions for period $periodKey") }
    }

    fun getCurrentMonthTransactions(): List<TransactionEntity> {
        val now = Calendar.getInstance()
        val periodKey = "${now.get(Calendar.YEAR)}-${
            (now.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        }"
        return getTransactionsForPeriod(periodKey)
    }

    fun isPeriodStale(periodKey: String): Boolean {
        val (startMs, endMs) = periodKey.toPeriodBounds()

        val sample = box.query()
            .between(TransactionEntity_.transactionDate, startMs, endMs)
            .build()
            .findFirst()

        return sample?.isStale() ?: true
    }

    fun getTransactionsForAccount(accountId: Long): List<TransactionEntity> {
        val fromAccount = box.query()
            .equal(TransactionEntity_.fromAccountId, accountId)
            .build()
            .find()

        val toAccount = box.query()
            .equal(TransactionEntity_.toAccountId, accountId)
            .build()
            .find()

        return (fromAccount + toAccount)
            .distinctBy { it.id }
            .sortedByDescending { it.transactionDate }
            .also { Logger.Database.d("Fetched ${it.size} transactions for account $accountId") }
    }

    fun getPendingTransactions(): List<TransactionEntity> {
        return box.query()
            .equal(TransactionEntity_.syncStatus, StatusConverter().convertToDatabaseValue(Status.PENDING)!!)
            .build()
            .find()
    }

    fun getFailedTransactions(): List<TransactionEntity> {
        return box.query()
            .equal(TransactionEntity_.syncStatus, StatusConverter().convertToDatabaseValue(Status.FAILED)!!)
            .build()
            .find()
    }

    fun getLocalOnlyTransactions(): List<TransactionEntity> {
        return box.query()
            .less(TransactionEntity_.id, 0)
            .build()
            .find()
    }

    fun updateSyncStatus(id: Long, status: Status, error: String? = null) {
        val entity = box.get(id)
        entity?.let {
            it.syncStatus = status
            it.syncError = error
            box.put(it)
            Logger.Database.d("Updated sync status for transaction $id: $status")
        }
    }

    /** Remap local ID to server ID after successful sync. */
    fun remapId(oldId: Long, newId: Long, updatedEntity: TransactionEntity) {
        box.remove(oldId)

        updatedEntity.syncStatus = Status.COMPLETED
        updatedEntity.syncError = null
        box.put(updatedEntity)

        Logger.Database.i("Remapped transaction ID: $oldId → $newId")
    }

    /** Upsert (update or insert) a transaction. */
    fun upsert(entity: TransactionEntity) {
        box.put(entity)
        Logger.Database.d("Upserted transaction ${entity.id}")
    }

    /** Remove a transaction by ID. */
    fun remove(id: Long) {
        box.remove(id)
        Logger.Database.d("Removed transaction $id")
    }

    /** Replace all transactions for a period (full sync). */
    fun replaceAllForPeriod(periodKey: String, entities: List<TransactionEntity>) {
        val (startMs, endMs) = periodKey.toPeriodBounds()

        box.query()
            .between(TransactionEntity_.transactionDate, startMs, endMs)
            .notEqual(TransactionEntity_.syncStatus, StatusConverter().convertToDatabaseValue(Status.PENDING)!!)
            .build()
            .remove()

        box.put(entities)

        Logger.Database.i("Replaced ${entities.size} transactions for period $periodKey")
    }


    fun getLastSyncTime(periodKey: String?): Long? {
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, periodKey)
        return syncMetadataBox.query(SyncMetadataEntity_.syncKey equal syncKey)
            .build()
            .findFirst()
            ?.lastSyncTime
    }

    fun updateLastSyncTime(periodKey: String?, serverTime: Long) {
        val syncKey = SyncMetadataEntity.buildSyncKey(EntityType.TRANSACTION, periodKey)

        val existing = syncMetadataBox.query(SyncMetadataEntity_.syncKey equal syncKey)
            .build()
            .findFirst()

        val metadata = existing ?: SyncMetadataEntity.forScope(EntityType.TRANSACTION, periodKey)
        metadata.lastSyncTime = serverTime
        syncMetadataBox.put(metadata)

        Logger.Database.d("Updated sync time for $syncKey: $serverTime")
    }

    fun clearAll() {
        box.removeAll()
        syncMetadataBox.query()
            .equal(SyncMetadataEntity_.entityType, EntityTypeConverter().convertToDatabaseValue(EntityType.TRANSACTION)!!)
            .build()
            .remove()
        Logger.Database.i("Cleared all transaction data")
    }
}

private fun String.toPeriodBounds(): Pair<Long, Long> {
    val parts = this.split("-")
    val year = parts[0].toInt()
    val month = parts[1].toInt() - 1

    val start = Calendar.getInstance().apply {
        set(year, month, 1, 0, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val end = Calendar.getInstance().apply {
        set(year, month, getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
        set(Calendar.MILLISECOND, 999)
    }.timeInMillis

    return start to end
}