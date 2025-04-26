package com.itsjeel01.finsiblefrontend.core.storage.objectbox.repository

import com.itsjeel01.finsiblefrontend.core.network.service.CategoryApiService
import com.itsjeel01.finsiblefrontend.core.storage.objectbox.entity.CategoryEntity
import io.objectbox.Box
import javax.inject.Inject

class CategoryLocalRepository @Inject constructor(
    private val box: Box<CategoryEntity>,
    private val categoryApiService: CategoryApiService,
) {

    fun getAllCategories(): List<CategoryEntity> {
        return box.all.map { it }
    }

    fun addCategory(category: CategoryEntity) {
        box.put(category)
        // TODO
        // apiService.addCategory(category)
    }

    fun removeCategory(category: CategoryEntity) {
        box.remove(category)
        // TODO
        // apiService.deleteCategory(category.id)
    }

    fun updateCategory(category: CategoryEntity) {
        box.put(category)
        // TODO
        // apiService.updateCategory(category)
    }
}