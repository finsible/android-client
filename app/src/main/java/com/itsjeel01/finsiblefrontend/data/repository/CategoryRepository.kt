package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.core.network.model.base.BaseResponse
import com.itsjeel01.finsiblefrontend.core.network.model.request.CategoryAddRequest
import com.itsjeel01.finsiblefrontend.core.network.model.request.CategoryRenameRequest
import com.itsjeel01.finsiblefrontend.core.network.model.response.CategoryData
import com.itsjeel01.finsiblefrontend.core.network.service.CategoryApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(private val categoryApiService: CategoryApiService) {
    suspend fun getCategories(type: String): BaseResponse<CategoryData> {
        return categoryApiService.getCategories(type)
    }

    suspend fun addCategory(type: String, name: String, color: String): BaseResponse<Unit> {
        return categoryApiService.addCategory(type, CategoryAddRequest(name, color))
    }

    suspend fun removeCategory(type: String, categoryId: String): BaseResponse<Unit> {
        return categoryApiService.removeCategory(type, categoryId)
    }

    suspend fun renameCategory(
        type: String,
        categoryId: String,
        newName: String,
    ): BaseResponse<Unit> {
        return categoryApiService.renameCategory(type, CategoryRenameRequest(categoryId, newName))
    }
}