package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity_
import com.itsjeel01.finsiblefrontend.data.model.Category
import com.itsjeel01.finsiblefrontend.data.model.toEntity
import io.objectbox.Box
import javax.inject.Inject

class CategoryLocalRepository @Inject constructor(override val box: Box<CategoryEntity>) :
    BaseLocalRepository<Category, CategoryEntity> {

    override fun addAll(data: List<Category>, additionalInfo: Any?, ttlMinutes: Long?) {
        val type = additionalInfo as TransactionType

        val parentCategories = data.filter { it.parentCategory == null }
        val subCategories = data.filter { it.parentCategory != null }.groupBy { it.parentCategory }

        parentCategories.forEach { parentCategory ->
            val parentEntity = parentCategory.toEntity(type).apply {
                updateCacheTime(ttlMinutes)
            }
            this.add(parentEntity)
        }

        parentCategories.forEach { parentCategory ->
            val childCategories = subCategories[parentCategory.id] ?: emptyList()

            childCategories.forEach { childCat ->
                val childEntity = childCat.toEntity(type).apply {
                    parentCategoryId = parentCategory.id
                    updateCacheTime(ttlMinutes)
                }
                this.add(childEntity)

                childEntity.parentCategory.target = box.get(parentCategory.id)
                box.put(childEntity)
            }
        }
    }

    override fun syncToServer(entity: CategoryEntity) {
        TODO("Not yet implemented")
    }

    fun getCategories(type: TransactionType): HashMap<CategoryEntity, List<CategoryEntity>> {
        val categories = HashMap<CategoryEntity, List<CategoryEntity>>()

        val parentCategories = box.query()
            .equal(CategoryEntity_.type, type.ordinal.toLong())
            .equal(CategoryEntity_.parentCategoryId, 0L)
            .build()
            .find()

        parentCategories.forEach { parent ->
            categories[parent] = parent.subCategories.toList()
        }

        return categories
    }

    fun getParentCategories(type: TransactionType): List<CategoryEntity> {
        return box.query()
            .equal(CategoryEntity_.type, type.ordinal.toLong())
            .equal(CategoryEntity_.parentCategoryId, 0L)
            .build()
            .find()
    }

    fun getSubCategories(parentId: Long): List<CategoryEntity> {
        return box.query()
            .equal(CategoryEntity_.parentCategoryId, parentId)
            .build()
            .find()
    }
}
