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

    fun createCategory(
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
}
