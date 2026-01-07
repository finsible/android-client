package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionUpdateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionsData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionApiService {

    @GET("transaction/all")
    suspend fun getTransactions(
        @Query("startDate") startDate: Long? = null,
        @Query("endDate") endDate: Long? = null,
        @Query("accountId") accountId: Long? = null,
        @Query("categoryId") categoryId: Long? = null,
        @Query("type") type: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 50
    ): BaseResponse<TransactionsData>

    @GET("transaction/{id}")
    suspend fun getTransaction(@Path("id") id: Long): BaseResponse<Transaction>


    @POST("transaction/")
    suspend fun createTransaction(
        @Body request: TransactionCreateRequest
    ): BaseResponse<Transaction>

    @PUT("transaction/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Long,
        @Body request: TransactionUpdateRequest
    ): BaseResponse<Transaction>

    @DELETE("transaction/{id}")
    suspend fun deleteTransaction(@Path("id") id: Long): BaseResponse<Unit>
}