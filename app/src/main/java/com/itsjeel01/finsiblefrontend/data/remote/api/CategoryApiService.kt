package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.model.Category
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryAddRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryRenameRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryUpdateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApiService {
    @GET("categories")
    suspend fun getCategories(
        @Query("type") type: String,
    ): BaseResponse<CategoriesData>

    @GET("categories/{id}")
    suspend fun getCategory(@Path("id") id: Long): BaseResponse<Category>

    @POST("categories/")
    suspend fun createCategory(
        @Body request: CategoryCreateRequest
    ): BaseResponse<Category>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Long,
        @Body request: CategoryUpdateRequest
    ): BaseResponse<Category>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Long): BaseResponse<Unit>

    // Legacy endpoints - kept for backward compatibility
    @POST("categories/add")
    suspend fun addCategory(
        @Query("type") type: String,
        @Body request: CategoryAddRequest,
    ): BaseResponse<Unit>

    @POST("categories/remove")
    suspend fun removeCategory(
        @Query("type") type: String,
        @Body id: String,
    ): BaseResponse<Unit>

    @PUT("categories/rename")
    suspend fun renameCategory(
        @Query("type") type: String,
        @Body request: CategoryRenameRequest,
    ): BaseResponse<Unit>
}
