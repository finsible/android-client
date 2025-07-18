package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoriesData
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryAddRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.CategoryRenameRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

/** Service interface for managing categories in the application. Provides methods to get, add, remove, and rename categories. */
interface CategoryApiService {
    @GET("categories")
    suspend fun getCategories(
        @Query("type") type: String,
    ): BaseResponse<CategoriesData>

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
