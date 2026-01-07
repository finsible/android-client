package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.FinsibleUtils.Companion.toPeriodBounds
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity_
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import java.util.Calendar
import javax.inject.Inject

class TransactionLocalRepository @Inject constructor(
    override val box: Box<TransactionEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator
) : SyncableLocalRepository<Transaction, TransactionEntity>(
    box,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.TRANSACTION
    override fun idProperty(): Property<TransactionEntity> = TransactionEntity_.id
    override fun syncStatusProperty(): Property<TransactionEntity> = TransactionEntity_.syncStatus

    override fun addAll(data: List<Transaction>, additionalInfo: Any?) {
        super.addAll(data, additionalInfo)

        val entities = data.map { it.toEntity() }
        box.put(entities)
        Logger.Database.d("Added ${entities.size} transactions to local DB")
    }

    /** Get recent transactions with pagination support (for infinite scroll). */
    fun getRecentTransactions(offset: Long = 0, limit: Long = 50): List<TransactionEntity> {
        return box.query()
            .orderDesc(TransactionEntity_.transactionDate)
            .build()
            .find(offset, limit)
            .also { Logger.Database.d("Fetched ${it.size} transactions (offset=$offset, limit=$limit)") }
    }

    /** Get total count of all transactions (for knowing when to stop pagination). */
    fun getTotalTransactionCount(): Long {
        return box.count()
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

    /** Create transaction locally and queue for sync. Returns immediately with local entity. */
    fun createTransaction(
        type: TransactionType,
        totalAmount: String,
        transactionDate: Long,
        categoryId: Long,
        categoryName: String,
        fromAccountId: Long?,
        toAccountId: Long?,
        description: String?,
        currency: String = "INR"
    ): TransactionEntity {
        val localId = localIdGenerator.nextLocalId()

        val entity = TransactionEntity(
            id = localId,
            type = type,
            totalAmount = totalAmount,
            transactionDate = transactionDate,
            categoryId = categoryId,
            categoryName = categoryName,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            description = description,
            currency = currency,
            syncStatus = Status.PENDING
        )

        box.put(entity)

        queueCreate(
            localEntityId = localId,
            request = TransactionCreateRequest(
                type = type.name,
                totalAmount = totalAmount,
                transactionDate = transactionDate,
                categoryId = categoryId,
                description = description,
                currency = currency,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId
            )
        )

        Logger.Database.i("Created local transaction: id=$localId, type=$type, amount=$totalAmount")
        return entity
    }

    /** Update transaction locally and queue for sync (only for server-synced entities). */
    fun updateTransaction(
        id: Long,
        type: TransactionType? = null,
        totalAmount: String? = null,
        transactionDate: Long? = null,
        categoryId: Long? = null,
        categoryName: String? = null,
        fromAccountId: Long? = null,
        toAccountId: Long? = null,
        description: String? = null,
        currency: String? = null
    ): TransactionEntity? {
        val entity = box.get(id) ?: return null

        // Apply updates
        type?.let { entity.type = it }
        totalAmount?.let { entity.totalAmount = it }
        transactionDate?.let { entity.transactionDate = it }
        categoryId?.let { entity.categoryId = it }
        categoryName?.let { entity.categoryName = it }
        fromAccountId?.let { entity.fromAccountId = it }
        toAccountId?.let { entity.toAccountId = it }
        description?.let { entity.description = it }
        currency?.let { entity.currency = it }

        entity.syncStatus = Status.PENDING
        box.put(entity)

        // Only queue if server-synced (positive ID)
        if (id > 0) {
            queueUpdate(
                entityId = id,
                request = TransactionUpdateRequest(
                    type = type?.name,
                    totalAmount = totalAmount,
                    transactionDate = transactionDate,
                    categoryId = categoryId,
                    description = description,
                    currency = currency,
                    fromAccountId = fromAccountId,
                    toAccountId = toAccountId
                )
            )
        }

        Logger.Database.i("Updated transaction: id=$id")
        return entity
    }

    /** Delete transaction locally and queue for sync (server-synced) or remove immediately (local-only). */
    fun deleteTransaction(id: Long): Boolean = deleteSyncAware(id)
}