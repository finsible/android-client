package com.itsjeel01.finsiblefrontend.data.local.repository

import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.data.remote.api.CategoryApiService
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
    }

    fun removeCategory(category: CategoryEntity) {
        box.remove(category)
        // TODO
    }

    fun updateCategory(category: CategoryEntity) {
        box.put(category)
        // TODO
    }
}
