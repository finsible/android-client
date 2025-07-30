package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.model.Category
import io.objectbox.Box
import javax.inject.Inject

class CategoryLocalRepository @Inject constructor(private val box: Box<CategoryEntity>) :
    BaseLocalRepository<Category, CategoryEntity> {

    override fun add(entity: CategoryEntity) {
        box.put(entity)
    }

    override fun remove(entity: CategoryEntity) {
        box.remove(entity)
    }

    override fun addAll(models: List<Category>, additionalInfo: Any?, ttlMinutes: Long?) {
        val type = additionalInfo as TransactionType
        models.forEach { model ->
            this.add(model.toEntity(type))
        }
    }

    override fun getAll(): List<CategoryEntity> {
        return box.all
    }

    override fun isStale(): Boolean {
        return box.all.isEmpty() || box.all.any { it.isStale() }
    }

    override fun syncToServer(entity: CategoryEntity) {
        TODO("Not yet implemented")
    }

    private fun Category.toEntity(type: TransactionType): CategoryEntity {
        return CategoryEntity(id, type, name, color)
    }

    private fun CategoryEntity.toModel(): Category {
        return Category(id, name, color)
    }
}
