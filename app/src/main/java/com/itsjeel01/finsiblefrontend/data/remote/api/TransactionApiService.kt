package com.itsjeel01.finsiblefrontend.data.remote.api

import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionUpdateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionsData
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionsDeltaData
import retrofit2.http.*

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

    /** Delta sync - fetch only transactions modified since timestamp. */
    @GET("transaction/modified")
    suspend fun getTransactionsModifiedSince(
        @Query("since") since: Long,
        @Query("includeDeleted") includeDeleted: Boolean = true
    ): BaseResponse<TransactionsDeltaData>

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