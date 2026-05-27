package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.asCentisAmount
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.OperationTypeConverter
import com.itsjeel01.finsiblefrontend.data.local.TransactionTypeConverter
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountString
import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.model.buildSearchableText
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.model.TransactionDailySummary
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.query.QueryBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class TransactionLocalRepository @Inject constructor(
    override val box: Box<TransactionEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator,
    private val json: Json,
    private val categoryLocalRepository: CategoryLocalRepository,
    private val accountLocalRepository: AccountLocalRepository
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
        totalAmount = entity.totalAmount.toAmountString(),
        transactionDate = entity.transactionDate,
        categoryId = entity.categoryId,
        description = entity.description,
        currencyCode = entity.currencyCode,
        fromAccountId = entity.fromAccountId,
        toAccountId = entity.toAccountId
    )

    override fun toUpdateRequest(entity: TransactionEntity) = TransactionUpdateRequest(
        type = entity.type.name,
        totalAmount = entity.totalAmount.toAmountString(),
        transactionDate = entity.transactionDate,
        categoryId = entity.categoryId,
        description = entity.description,
        currencyCode = entity.currencyCode,
        fromAccountId = entity.fromAccountId,
        toAccountId = entity.toAccountId
    )

    override fun addAll(data: List<Transaction>, additionalInfo: Any?) {
        super.addAll(data, additionalInfo)

        val entities = data.map { it.toEntity() }

        val categoryIds = entities.map { it.categoryId }.distinct()
        val categoryMap = categoryLocalRepository.getCategories(categoryIds)

        entities.forEach { entity ->
            entity.categoryIcon = categoryMap[entity.categoryId]?.icon ?: ""
        }

        box.put(entities)
        Logger.Database.d("Added ${entities.size} transactions to local DB")
    }

    /** Returns total number of transactions. */
    fun getTotalTransactionCount(): Long {
        return box.count()
    }

    /** Computes daily aggregates for all transactions. */
    fun getAllDateAggregates(): Map<Long, TransactionDailySummary> {
        val converter = TransactionTypeConverter()
        val incomeTypeInt = converter.convertToDatabaseValue(TransactionType.INCOME) ?: -1
        val expenseTypeInt = converter.convertToDatabaseValue(TransactionType.EXPENSE) ?: -1

        return box.store.callInReadTx {
            val transactions = box.query()
                .orderDesc(TransactionEntity_.transactionDate)
                .build()
                .use { it.find() }

            val resultMap = HashMap<Long, TransactionDailySummary>()
            val cal = Calendar.getInstance()

            for (txn in transactions) {
                cal.timeInMillis = txn.transactionDate
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val dayStart = cal.timeInMillis

                val current = resultMap[dayStart] ?: TransactionDailySummary()
                val typeInt = converter.convertToDatabaseValue(txn.type)

                val updated = when (typeInt) {
                    incomeTypeInt -> current.copy(
                        incomeCentis = current.incomeCentis + txn.totalAmount,
                        count = current.count + 1
                    )

                    expenseTypeInt -> current.copy(
                        expenseCentis = current.expenseCentis + txn.totalAmount,
                        count = current.count + 1
                    )

                    else -> current.copy(count = current.count + 1)
                }
                resultMap[dayStart] = updated
            }
            resultMap
        }
    }

    suspend fun createTransaction(
        type: TransactionType,
        totalAmount: Long,
        transactionDate: Long,
        categoryId: Long,
        categoryName: String,
        fromAccountId: Long?,
        toAccountId: Long?,
        description: String?,
        currencyCode: String
    ): TransactionEntity = withContext(Dispatchers.IO) {
        val localId = localIdGenerator.nextLocalId()

        box.store.callInTx {
            val entity = TransactionEntity(
                id = localId,
                type = type,
                totalAmount = totalAmount,
                searchableText = buildSearchableText(description, categoryName),
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
                currencyCode = currencyCode,
                syncStatus = Status.PENDING
            )

            box.put(entity)

            val request = toCreateRequest(entity)
            pendingOperationBox.put(
                PendingOperationEntity(
                    entityType = entityType,
                    operationType = OperationType.CREATE,
                    localEntityId = localId,
                    payload = json.encodeToString(request),
                    status = Status.PENDING,
                    createdAt = System.currentTimeMillis()
                )
            )

            // Usage tracking — atomic with the entity write in the same transaction
            if (categoryId > 0) categoryLocalRepository.incrementUsage(categoryId)
            if (fromAccountId != null && fromAccountId > 0) accountLocalRepository.incrementUsage(fromAccountId)
            if (toAccountId != null && toAccountId > 0) accountLocalRepository.incrementUsage(toAccountId)

            entity
        }
    }

    fun updateTransaction(
        id: Long,
        type: TransactionType? = null,
        totalAmount: Long? = null,
        transactionDate: Long? = null,
        categoryId: Long? = null,
        categoryName: String? = null,
        fromAccountId: Long? = null,
        toAccountId: Long? = null,
        description: String? = null,
        currencyCode: String? = null
    ): TransactionEntity? {
        val entity = box.get(id) ?: return null

        val oldCategoryId = entity.categoryId
        val oldFromAccountId = entity.fromAccountId
        val oldToAccountId = entity.toAccountId

        return box.store.callInTx {
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
            currencyCode?.let { entity.currencyCode = it }

            if (description != null || categoryName != null) {
                entity.searchableText = buildSearchableText(entity.description, entity.categoryName)
            }

            // Shared entity write — single path for both branches
            entity.syncStatus = Status.PENDING
            box.put(entity)

            if (entity.id <= 0) {
                val createOpType = OperationTypeConverter()
                    .convertToDatabaseValue(OperationType.CREATE)!!.toLong()
                pendingOperationBox.query()
                    .equal(PendingOperationEntity_.operationType, createOpType)
                    .equal(PendingOperationEntity_.localEntityId, entity.id)
                    .build()
                    .findFirst()?.let { pendingOp ->
                        pendingOp.payload = json.encodeToString(toCreateRequest(entity))
                        pendingOperationBox.put(pendingOp)
                    }
            } else {
                val request = TransactionUpdateRequest(
                    type = type?.name,
                    totalAmount = totalAmount?.toAmountString(),
                    transactionDate = transactionDate,
                    categoryId = categoryId,
                    description = description,
                    currencyCode = currencyCode,
                    fromAccountId = fromAccountId,
                    toAccountId = toAccountId
                )
                pendingOperationBox.put(
                    PendingOperationEntity(
                        entityType = entityType,
                        operationType = OperationType.UPDATE,
                        entityId = entity.id,
                        payload = json.encodeToString(request),
                        status = Status.PENDING,
                        createdAt = System.currentTimeMillis()
                    )
                )
            }

            // Usage tracking — atomic with the entity write in the same transaction
            if (categoryId != null && categoryId > 0 && categoryId != oldCategoryId) categoryLocalRepository.incrementUsage(categoryId)
            if (fromAccountId != null && fromAccountId > 0 && fromAccountId != oldFromAccountId) accountLocalRepository.incrementUsage(fromAccountId)
            if (toAccountId != null && toAccountId > 0 && toAccountId != oldToAccountId) accountLocalRepository.incrementUsage(toAccountId)

            entity
        }
    }

    fun deleteTransaction(id: Long): Boolean = queueDeleteEntity(id)

    /**
     * Unified query method for all search/filter/sort operations with true DB-level pagination.
     * Aggregates are computed via separate optimized query to avoid loading all results.
     */
    fun queryTransactions(
        searchQuery: String = "",
        sortOption: SortOption = SortOption.NEWEST_FIRST,
        accountIds: Set<Long> = emptySet(),
        dateRangeStart: Long? = null,
        dateRangeEnd: Long? = null,
        amountMin: Long? = null,
        amountMax: Long? = null,
        transactionTypes: Set<TransactionType> = emptySet(),
        offset: Int = 0,
        limit: Int = 50
    ): PaginatedResult {
        Logger.Database.d("queryTransactions: query='$searchQuery', sort=$sortOption, accounts=${accountIds.size}, offset=$offset, limit=$limit")

        val transactions = queryPaginated(
            searchQuery = searchQuery,
            sortOption = sortOption,
            accountIds = accountIds,
            dateRangeStart = dateRangeStart,
            dateRangeEnd = dateRangeEnd,
            amountMin = amountMin,
            amountMax = amountMax,
            transactionTypes = transactionTypes,
            offset = offset,
            limit = limit
        )

        /** Compute total count and aggregates only on first page (offset == 0)
         * For subsequent pages, return placeholder (caller should cache first page aggregates) */
        val (totalCount, summary) = if (offset == 0) {
            computeAggregates(
                searchQuery = searchQuery,
                accountIds = accountIds,
                dateRangeStart = dateRangeStart,
                dateRangeEnd = dateRangeEnd,
                amountMin = amountMin,
                amountMax = amountMax,
                transactionTypes = transactionTypes
            )
        } else {
            0 to FilteredSummary(0, 0L, 0L)
        }

        Logger.Database.d("queryTransactions: total=$totalCount, returned=${transactions.size}")

        return PaginatedResult(
            transactions = transactions,
            totalCount = totalCount,
            summary = summary,
            hasMore = transactions.size == limit
        )
    }

    /**
     * Query with true DB-level pagination using offset/limit.
     * All filters are applied at the DB level — no in-memory post-filtering.
     */
    private fun queryPaginated(
        searchQuery: String,
        sortOption: SortOption,
        accountIds: Set<Long>,
        dateRangeStart: Long?,
        dateRangeEnd: Long?,
        amountMin: Long?,
        amountMax: Long?,
        transactionTypes: Set<TransactionType>,
        offset: Int,
        limit: Int
    ): List<TransactionEntity> {
        val queryBuilder = box.query()

        applyDateFilter(queryBuilder, dateRangeStart, dateRangeEnd)
        applyTypeFilter(queryBuilder, transactionTypes)
        applyAmountFilter(queryBuilder, amountMin, amountMax)
        applySearchFilter(queryBuilder, searchQuery)
        applyAccountFilter(queryBuilder, accountIds)
        applySorting(queryBuilder, sortOption)

        return queryBuilder.build().use { query ->
            query.find(offset.toLong(), limit.toLong())
        }
    }

    /** Compute aggregates fully at DB level for all filter combinations. */
    private fun computeAggregates(
        searchQuery: String,
        accountIds: Set<Long>,
        dateRangeStart: Long?,
        dateRangeEnd: Long?,
        amountMin: Long?,
        amountMax: Long?,
        transactionTypes: Set<TransactionType>
    ): Pair<Int, FilteredSummary> {
        val converter = TransactionTypeConverter()
        val incomeTypeInt = converter.convertToDatabaseValue(TransactionType.INCOME) ?: -1
        val expenseTypeInt = converter.convertToDatabaseValue(TransactionType.EXPENSE) ?: -1

        fun baseBuilder(): QueryBuilder<TransactionEntity> {
            val qb = box.query()
            applyDateFilter(qb, dateRangeStart, dateRangeEnd)
            applyAmountFilter(qb, amountMin, amountMax)
            applyTypeFilter(qb, transactionTypes)
            applySearchFilter(qb, searchQuery)
            applyAccountFilter(qb, accountIds)
            return qb
        }

        val count = baseBuilder().build().use { it.count().toInt() }

        val includeIncome = transactionTypes.isEmpty() || TransactionType.INCOME in transactionTypes
        val totalIncomeCentis = if (includeIncome) {
            baseBuilder()
                .apply(TransactionEntity_.type.equal(incomeTypeInt)) // Fluent and array-free
                .build()
                .use { q -> q.property(TransactionEntity_.totalAmount).sum() }
        } else 0L

        val includeExpense = transactionTypes.isEmpty() || TransactionType.EXPENSE in transactionTypes
        val totalExpenseCentis = if (includeExpense) {
            baseBuilder()
                .apply(TransactionEntity_.type.equal(expenseTypeInt)) // Fluent and array-free
                .build()
                .use { q -> q.property(TransactionEntity_.totalAmount).sum() }
        } else 0L

        return count to FilteredSummary(count, totalIncomeCentis, totalExpenseCentis)
    }

    /** Apply DB-level text search on pre-computed searchableText field. */
    private fun applySearchFilter(
        queryBuilder: QueryBuilder<TransactionEntity>,
        searchQuery: String
    ) {
        val trimmed = searchQuery.trim().lowercase(Locale.ROOT)
        if (trimmed.isBlank()) return

        val amountCentis = trimmed.asCentisAmount().takeIf { it > 0L }

        val searchCondition = if (amountCentis != null)
            TransactionEntity_.searchableText.contains(trimmed)
                .or(TransactionEntity_.totalAmount.equal(amountCentis))
        else
            TransactionEntity_.searchableText.contains(trimmed)

        queryBuilder.apply(searchCondition)
    }

    /** Apply DB-level account filter as OR across fromAccountId and toAccountId. */
    private fun applyAccountFilter(
        queryBuilder: QueryBuilder<TransactionEntity>,
        accountIds: Set<Long>
    ) {
        if (accountIds.isEmpty()) return

        val ids = accountIds.toLongArray()
        val accountCondition = TransactionEntity_.fromAccountId.oneOf(ids).or(TransactionEntity_.toAccountId.oneOf(ids))
        queryBuilder.apply(accountCondition)
    }

    /** Apply DB-level date range filter. Both bounds are inclusive. */
    private fun applyDateFilter(
        queryBuilder: QueryBuilder<TransactionEntity>,
        dateRangeStart: Long?,
        dateRangeEnd: Long?
    ) {
        when {
            dateRangeStart != null && dateRangeEnd != null ->
                queryBuilder.between(TransactionEntity_.transactionDate, dateRangeStart, dateRangeEnd)

            dateRangeStart != null ->
                queryBuilder.greaterOrEqual(TransactionEntity_.transactionDate, dateRangeStart)

            dateRangeEnd != null ->
                queryBuilder.lessOrEqual(TransactionEntity_.transactionDate, dateRangeEnd)
        }
    }

    /** Apply DB-level transaction type filter. */
    private fun applyTypeFilter(
        queryBuilder: QueryBuilder<TransactionEntity>,
        transactionTypes: Set<TransactionType>
    ) {
        if (transactionTypes.isNotEmpty()) {
            val converter = TransactionTypeConverter()
            val typeInts = transactionTypes.mapNotNull { converter.convertToDatabaseValue(it) }.toIntArray()
            if (typeInts.isNotEmpty()) {
                queryBuilder.apply(TransactionEntity_.type.oneOf(typeInts))
            }
        }
    }

    /** Apply DB-level amount range filter. */
    private fun applyAmountFilter(
        queryBuilder: QueryBuilder<TransactionEntity>,
        amountMin: Long?,
        amountMax: Long?
    ) {
        when {
            amountMin != null && amountMax != null ->
                queryBuilder.between(TransactionEntity_.totalAmount, amountMin, amountMax)

            amountMin != null ->
                queryBuilder.greaterOrEqual(TransactionEntity_.totalAmount, amountMin)

            amountMax != null ->
                queryBuilder.lessOrEqual(TransactionEntity_.totalAmount, amountMax)
        }
    }

    /** Apply DB-level on-demand sorting */
    private fun applySorting(
        queryBuilder: QueryBuilder<TransactionEntity>,
        sortOption: SortOption
    ) {
        when (sortOption) {
            SortOption.NEWEST_FIRST -> queryBuilder.orderDesc(TransactionEntity_.transactionDate)
            SortOption.OLDEST_FIRST -> queryBuilder.order(TransactionEntity_.transactionDate)
            SortOption.AMOUNT_HIGH_TO_LOW -> queryBuilder.orderDesc(TransactionEntity_.totalAmount)
            SortOption.AMOUNT_LOW_TO_HIGH -> queryBuilder.order(TransactionEntity_.totalAmount)
        }
    }
}

/** Result container for paginated query. */
data class PaginatedResult(
    val transactions: List<TransactionEntity>,
    val totalCount: Int,
    val summary: FilteredSummary,
    val hasMore: Boolean
)

/** Summary for filtered transactions, amounts in centis (×100). */
data class FilteredSummary(
    val totalCount: Int,
    val totalIncomeCentis: Long,
    val totalExpenseCentis: Long
) {
    val netCentis: Long get() = totalIncomeCentis - totalExpenseCentis
}