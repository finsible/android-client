package com.itsjeel01.finsiblefrontend.data.network.apis

import com.itsjeel01.finsiblefrontend.data.network.requests.CategoryAddRequest
import com.itsjeel01.finsiblefrontend.data.network.requests.CategoryRenameRequest
import com.itsjeel01.finsiblefrontend.data.network.responses.BaseResponse
import com.itsjeel01.finsiblefrontend.data.network.responses.CategoryData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface CategoryApiService {
    // Fetch all categories based on the type (income/expense)
    @GET("categories")
    suspend fun getCategories(
        @Query("type") type: String,
    ): BaseResponse<CategoryData>

    // Add a new category based on the type (income/expense)
    @POST("categories/add")
    suspend fun addCategory(
        @Query("type") type: String,
        @Body request: CategoryAddRequest,
    ): BaseResponse<Unit>

    // Remove a category by ID based on the type (income/expense)
    @POST("categories/remove")
    suspend fun removeCategory(
        @Query("type") type: String,
        @Body id: String,
    ): BaseResponse<Unit>

    // Rename a category by ID based on the type (income/expense)
    @PUT("categories/rename")
    suspend fun renameCategory(
        @Query("type") type: String,
        @Body request: CategoryRenameRequest,
    ): BaseResponse<Unit>
}