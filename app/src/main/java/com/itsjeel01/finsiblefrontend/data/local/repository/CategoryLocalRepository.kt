package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.TransactionTypeConverter
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity_
import com.itsjeel01.finsiblefrontend.data.local.entity.PendingOperationEntity
import com.itsjeel01.finsiblefrontend.data.model.Category
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryUpdateRequest
import com.itsjeel01.finsiblefrontend.data.sync.LocalIdGenerator
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

class CategoryLocalRepository @Inject constructor(
    override val box: Box<CategoryEntity>,
    pendingOperationBox: Box<PendingOperationEntity>,
    localIdGenerator: LocalIdGenerator
) : SyncableLocalRepository<Category, CategoryEntity>(
    box,
    pendingOperationBox,
    localIdGenerator
) {

    override val entityType: EntityType = EntityType.CATEGORY
    override fun idProperty(): Property<CategoryEntity> = CategoryEntity_.id
    override fun syncStatusProperty(): Property<CategoryEntity> = CategoryEntity_.syncStatus

    override fun toCreateRequest(entity: CategoryEntity) = CategoryCreateRequest(
        type = entity.type.name,
        name = entity.name,
        icon = entity.icon,
        parentCategoryId = if (entity.parentCategoryId > 0) entity.parentCategoryId else null
    )

    override fun toUpdateRequest(entity: CategoryEntity) = CategoryUpdateRequest(
        name = entity.name,
        icon = entity.icon
    )

    override fun addAll(data: List<Category>, additionalInfo: Any?) {
        super.addAll(data, additionalInfo)

        val type = additionalInfo as TransactionType

        val parentCategories = data.filter { it.parentCategory == null }
        val subCategories = data.filter { it.parentCategory != null }.groupBy { it.parentCategory }

        parentCategories.forEach { parentCategory ->
            val parentEntity = parentCategory.toEntity(type).apply {
                syncStatus = Status.COMPLETED
            }
            box.put(parentEntity)
        }

        parentCategories.forEach { parentCategory ->
            val childCategories = subCategories[parentCategory.id] ?: emptyList()

            childCategories.forEach { childCat ->
                val childEntity = childCat.toEntity(type).apply {
                    parentCategoryId = parentCategory.id
                    syncStatus = Status.COMPLETED
                }
                box.put(childEntity)

                childEntity.parentCategory.target = box.get(parentCategory.id)
                box.put(childEntity)
            }
        }
    }

    fun getCategories(type: TransactionType): HashMap<CategoryEntity, List<CategoryEntity>> {
        val categories = HashMap<CategoryEntity, List<CategoryEntity>>()

        val parentCategories = box.query()
            .equal(CategoryEntity_.type, TransactionTypeConverter().convertToDatabaseValue(type)!!)
            .equal(CategoryEntity_.parentCategoryId, 0L)
            .build()
            .find()

        Logger.Database.d("Found ${parentCategories.size} parent categories of type $type")

        parentCategories.forEach { parent ->
            categories[parent] = parent.subCategories.toList()
        }

        return categories
    }

    fun getCategories(ids: List<Long>): Map<Long, CategoryEntity> {
        return box.query(CategoryEntity_.id.oneOf(ids.toLongArray()))
            .build()
            .find()
            .associateBy { it.id }
    }

    suspend fun createCategory(
        type: TransactionType,
        name: String,
        icon: String,
        parentCategoryId: Long? = null
    ): CategoryEntity {
        return queueCreateEntity { localId ->
            CategoryEntity(
                id = localId,
                type = type,
                name = name,
                icon = icon,
                readOnly = false,
                parentCategoryId = parentCategoryId ?: 0L,
                syncStatus = Status.PENDING
            ).apply {
                parentCategoryId?.let { this.parentCategory.targetId = it }
            }
        }
    }

    fun updateCategory(
        id: Long,
        name: String? = null,
        icon: String? = null
    ): CategoryEntity? {
        val entity = box.get(id) ?: return null

        // Don't allow updating read-only categories
        if (entity.readOnly) {
            Logger.Database.w("Cannot update read-only category: id=$id")
            return null
        }

        // Apply updates
        name?.let { entity.name = it }
        icon?.let { entity.icon = it }

        return queueUpdateEntity(entity)
    }

    fun deleteCategory(id: Long): Boolean {
        val entity = box.get(id) ?: return false

        // Don't allow deleting read-only categories
        if (entity.readOnly) {
            Logger.Database.w("Cannot delete read-only category: id=$id")
            return false
        }

        return queueDeleteEntity(id)
    }

    /**
     * Reactively emits the top [limit] categories sorted by recency.
     * Categories never used (null lastUsedAt) are excluded — ObjectBox places nulls first on
     * orderDesc, which would incorrectly rank unused categories as "most recent".
     */
    fun getRecentCategoriesFlow(
        type: TransactionType,
        limit: Long
    ): Flow<List<CategoryEntity>> = callbackFlow {
        val typeInt = TransactionTypeConverter().convertToDatabaseValue(type)!!

        val query = box.query()
            .equal(CategoryEntity_.type, typeInt)
            .notNull(CategoryEntity_.lastUsedAt)
            .orderDesc(CategoryEntity_.lastUsedAt)
            .build()

        val subscription = query.subscribe().observer {
            trySend(query.find(0, limit))
        }

        awaitClose {
            subscription.cancel()
            query.close()
        }
    }.conflate()

    /** Reactively emits the top [limit] categories sorted purely by usage frequency. */
    fun getFrequentCategoriesFlow(
        type: TransactionType,
        limit: Long
    ): Flow<List<CategoryEntity>> = callbackFlow {
        val typeInt = TransactionTypeConverter().convertToDatabaseValue(type)!!

        val query = box.query()
            .equal(CategoryEntity_.type, typeInt)
            .orderDesc(CategoryEntity_.usageCount)
            .build()

        val subscription = query.subscribe().observer {
            trySend(query.find(0, limit))
        }

        awaitClose {
            subscription.cancel()
            query.close()
        }
    }.conflate()

    /**
     * Reactive top-K by usage frequency, driven by a Flow<Int> for K so the limit can change reactively.
     * Local ObjectBox only — no remote fetch.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTopK(type: TransactionType, kFlow: Flow<Int>): Flow<List<CategoryEntity>> {
        val typeInt = TransactionTypeConverter().convertToDatabaseValue(type)!!
        val baseFlow = box.query()
            .equal(CategoryEntity_.type, typeInt)
            .orderDesc(CategoryEntity_.usageCount)
            .build()
            .flow()

        return combine(baseFlow, kFlow) { entities, k -> entities.take(k) }
    }

    /**
     * Increments usage count and updates lastUsedAt for the given category.
     *
     * Uses `BoxStore.callInTx` which blocks the calling thread. Callers must ensure this
     * runs on a background dispatcher (e.g. `Dispatchers.IO`) to avoid main-thread jank.
     */
    fun updateCategoryUsage(id: Long): CategoryEntity? {
        return box.store.callInTx {
            val entity = box.get(id) ?: return@callInTx null

            entity.usageCount += 1
            entity.lastUsedAt = System.currentTimeMillis()

            box.put(entity)
            Logger.Database.d("Updated category usage: id=$id, count=${entity.usageCount}")
            entity
        }
    }
}
