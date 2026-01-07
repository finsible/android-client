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

    override fun addAll(data: List<Category>, additionalInfo: Any?) {
        super.addAll(data, additionalInfo)

        val type = additionalInfo as TransactionType

        val parentCategories = data.filter { it.parentCategory == null }
        val subCategories = data.filter { it.parentCategory != null }.groupBy { it.parentCategory }

        parentCategories.forEach { parentCategory ->
            val parentEntity = parentCategory.toEntity(type).apply {
                syncStatus = Status.COMPLETED
            }
            this.add(parentEntity)
        }

        parentCategories.forEach { parentCategory ->
            val childCategories = subCategories[parentCategory.id] ?: emptyList()

            childCategories.forEach { childCat ->
                val childEntity = childCat.toEntity(type).apply {
                    parentCategoryId = parentCategory.id
                    syncStatus = Status.COMPLETED
                }
                this.add(childEntity)

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

    fun getParentCategories(type: TransactionType): List<CategoryEntity> {
        val parents = box.query()
            .equal(CategoryEntity_.type, TransactionTypeConverter().convertToDatabaseValue(type)!!)
            .equal(CategoryEntity_.parentCategoryId, 0L)
            .build()
            .find()

        Logger.Database.d("Fetched ${parents.size} parent categories of type $type")
        return parents
    }

    fun getSubCategories(parentId: Long): List<CategoryEntity> {
        val subs = box.query()
            .equal(CategoryEntity_.parentCategoryId, parentId)
            .build()
            .find()

        Logger.Database.d("Fetched ${subs.size} subcategories for parent id=$parentId")
        return subs
    }

    /** Create category locally and queue for sync. Returns immediately with local entity. */
    fun createCategory(
        type: TransactionType,
        name: String,
        icon: String,
        parentCategoryId: Long? = null
    ): CategoryEntity {
        val localId = localIdGenerator.nextLocalId()

        val entity = CategoryEntity(
            id = localId,
            type = type,
            name = name,
            icon = icon,
            readOnly = false,
            parentCategoryId = parentCategoryId ?: 0L,
            syncStatus = Status.PENDING
        )

        parentCategoryId?.let {
            entity.parentCategory.targetId = it
        }

        box.put(entity)

        queueCreate(
            localEntityId = localId,
            request = CategoryCreateRequest(
                type = type.name,
                name = name,
                icon = icon,
                parentCategoryId = parentCategoryId
            )
        )

        Logger.Database.i("Created local category: id=$localId, name=$name, type=$type")
        return entity
    }

    /** Update category locally and queue for sync (only for server-synced entities). */
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

        entity.syncStatus = Status.PENDING
        box.put(entity)

        // Only queue if server-synced (positive ID)
        if (id > 0) {
            queueUpdate(
                entityId = id,
                request = CategoryUpdateRequest(
                    name = name,
                    icon = icon
                )
            )
        }

        Logger.Database.i("Updated category: id=$id")
        return entity
    }

    /** Delete category locally and queue for sync (server-synced) or remove immediately (local-only). */
    fun deleteCategory(id: Long): Boolean {
        val entity = box.get(id) ?: return false

        // Don't allow deleting read-only categories
        if (entity.readOnly) {
            Logger.Database.w("Cannot delete read-only category: id=$id")
            return false
        }

        return deleteSyncAware(id)
    }
}
