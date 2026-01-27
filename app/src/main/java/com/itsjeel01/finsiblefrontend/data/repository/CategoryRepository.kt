package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.model.Category
import com.itsjeel01.finsiblefrontend.data.remote.api.CategoryApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryUpdateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(private val apiService: CategoryApiService) {
    suspend fun getCategories(type: String): BaseResponse<CategoriesData> {
        return apiService.getCategories(type)
    }

    suspend fun getCategory(id: Long): BaseResponse<Category> {
        return apiService.getCategory(id)
    }

    suspend fun createCategory(request: CategoryCreateRequest): BaseResponse<Category> {
        return apiService.createCategory(request)
    }

    suspend fun updateCategory(id: Long, request: CategoryUpdateRequest): BaseResponse<Category> {
        return apiService.updateCategory(id, request)
    }

    suspend fun deleteCategory(id: Long): BaseResponse<Unit> {
        return apiService.deleteCategory(id)
    }
}
