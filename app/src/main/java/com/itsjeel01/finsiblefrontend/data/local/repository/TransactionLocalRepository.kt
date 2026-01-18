package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.EntityType
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
    localIdGenerator: LocalIdGenerator,
    private val categoryLocalRepository: CategoryLocalRepository
) : SyncableLocalRepository<Transaction, TransactionEntity>(
    box,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.TRANSACTION
    override fun idProperty(): Property<TransactionEntity> = TransactionEntity_.id
    override fun syncStatusProperty(): Property<TransactionEntity> = TransactionEntity_.syncStatus

    override fun toCreateRequest(entity: TransactionEntity) = TransactionCreateRequest(
        type = entity.type.name,
        totalAmount = entity.totalAmount,
        transactionDate = entity.transactionDate,
        categoryId = entity.categoryId,
        description = entity.description,
        currency = entity.currency,
        fromAccountId = entity.fromAccountId,
        toAccountId = entity.toAccountId
    )

    override fun toUpdateRequest(entity: TransactionEntity) = TransactionUpdateRequest(
        type = entity.type.name,
        totalAmount = entity.totalAmount,
        transactionDate = entity.transactionDate,
        categoryId = entity.categoryId,
        description = entity.description,
        currency = entity.currency,
        fromAccountId = entity.fromAccountId,
        toAccountId = entity.toAccountId
    )

    override fun addAll(data: List<Transaction>, additionalInfo: Any?) {
        super.addAll(data, additionalInfo)

        val entities = data.map { it.toEntity() }

        // Populate categoryIcon for each entity by looking up from CategoryLocalRepository
        entities.forEach { entity ->
            try {
                entity.categoryIcon = categoryLocalRepository.get(entity.categoryId).icon
            } catch (_: Exception) {
                entity.categoryIcon = ""
            }
        }

        box.put(entities)
        Logger.Database.d("Added ${entities.size} transactions to local DB")
    }

    fun getTotalTransactionCount(): Long {
        return box.count()
    }

    fun getTransactionsForDate(startOfDayMs: Long, endOfDayMs: Long): List<TransactionEntity> {
        return box.query()
            .between(TransactionEntity_.transactionDate, startOfDayMs, endOfDayMs)
            .orderDesc(TransactionEntity_.transactionDate)
            .build()
            .find()
            .also { Logger.Database.d("Fetched ${it.size} transactions for date range $startOfDayMs-$endOfDayMs") }
    }

    fun getAllUniqueDates(): List<Long> {
        return box.query()
            .orderDesc(TransactionEntity_.transactionDate)
            .build()
            .find()
            .map { transaction ->
                // Normalize to start of day
                val cal = Calendar.getInstance().apply {
                    timeInMillis = transaction.transactionDate
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                cal.timeInMillis
            }
            .distinct()
            .also { Logger.Database.d("Found ${it.size} unique dates in database") }
    }

    fun getTransactionsForDates(dateTimestamps: List<Long>): List<TransactionEntity> {
        if (dateTimestamps.isEmpty()) return emptyList()

        val allTransactions = mutableListOf<TransactionEntity>()

        dateTimestamps.forEach { dateTimestamp ->
            val cal = Calendar.getInstance().apply {
                timeInMillis = dateTimestamp
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = cal.timeInMillis
            cal.add(Calendar.DAY_OF_MONTH, 1)
            val endOfDay = cal.timeInMillis - 1

            val transactions = box.query()
                .between(TransactionEntity_.transactionDate, startOfDay, endOfDay)
                .orderDesc(TransactionEntity_.transactionDate)
                .build()
                .find()

            allTransactions.addAll(transactions)
        }

        return allTransactions
            .distinctBy { it.id }
            .sortedByDescending { it.transactionDate }
            .also { Logger.Database.d("Fetched ${it.size} transactions for ${dateTimestamps.size} dates") }
    }

    fun getAllDateAggregates(): Map<Long, Triple<String, String, Long>> {
        val allTransactions = box.query()
            .orderDesc(TransactionEntity_.transactionDate)
            .build()
            .find()

        return allTransactions
            .groupBy {
                // Normalize to start of day
                val cal = Calendar.getInstance().apply {
                    timeInMillis = it.transactionDate
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                cal.timeInMillis
            }
            .mapValues { (_, transactions) ->
                val incomeSum = transactions
                    .filter { it.type == TransactionType.INCOME }
                    .fold(java.math.BigDecimal.ZERO) { acc, t ->
                        acc + (t.totalAmount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO)
                    }

                val expenseSum = transactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .fold(java.math.BigDecimal.ZERO) { acc, t ->
                        acc + (t.totalAmount.toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO)
                    }

                val count = transactions.size.toLong()

                Triple(incomeSum.toString(), expenseSum.toString(), count)
            }
            .also { Logger.Database.d("Computed aggregates for ${it.size} unique dates") }
    }

    fun createTransaction(
        type: TransactionType,
        totalAmount: String,
        transactionDate: Long,
        categoryId: Long,
        categoryName: String,
        fromAccountId: Long?,
        toAccountId: Long?,
        description: String?,
        currency: Currency = Currency.INR
    ): TransactionEntity {
        return queueCreateEntity { localId ->
            TransactionEntity(
                id = localId,
                type = type,
                totalAmount = totalAmount,
                transactionDate = transactionDate,
                categoryId = categoryId,
                categoryName = categoryName,
                categoryIcon = try {
                    categoryLocalRepository.get(categoryId).icon
                } catch (_: Exception) {
                    ""
                },
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                description = description,
                currency = currency,
                syncStatus = Status.PENDING
            )
        }
    }

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
        currency: Currency? = null
    ): TransactionEntity? {
        val entity = box.get(id) ?: return null

        type?.let { entity.type = it }
        totalAmount?.let { entity.totalAmount = it }
        transactionDate?.let { entity.transactionDate = it }
        categoryId?.let {
            entity.categoryId = it
            entity.categoryIcon = try {
                categoryLocalRepository.get(it).icon
            } catch (_: Exception) {
                ""
            }
        }
        categoryName?.let { entity.categoryName = it }
        fromAccountId?.let { entity.fromAccountId = it }
        toAccountId?.let { entity.toAccountId = it }
        description?.let { entity.description = it }
        currency?.let { entity.currency = it }

        return queueUpdateEntity(entity)
    }

    fun deleteTransaction(id: Long): Boolean = queueDeleteEntity(id)
}