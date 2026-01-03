package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.SyncMetadataEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity_
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import java.util.Calendar
import javax.inject.Inject

class TransactionLocalRepository @Inject constructor(
    override val box: Box<TransactionEntity>,
    syncMetadataBox: Box<SyncMetadataEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator
) : SyncableLocalRepository<Transaction, TransactionEntity>(
    box,
    syncMetadataBox,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.TRANSACTION
    override fun idProperty(): Property<TransactionEntity> = TransactionEntity_.id
    override fun syncStatusProperty(): Property<TransactionEntity> = TransactionEntity_.syncStatus

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

    /** Replace all transactions for a period (full sync). Uses base replaceMatching method. */
    fun replaceAllForPeriod(periodKey: String, entities: List<TransactionEntity>) {
        val (startMs, endMs) = periodKey.toPeriodBounds()

        val queryBuilder = box.query()
            .between(TransactionEntity_.transactionDate, startMs, endMs)

        replaceMatching(queryBuilder, entities)

        Logger.Database.i("Replaced ${entities.size} transactions for period $periodKey")
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