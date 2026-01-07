package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.data.model.Transaction
import com.itsjeel01.finsiblefrontend.data.remote.api.TransactionApiService
import com.itsjeel01.finsiblefrontend.data.remote.model.BaseResponse
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionCreateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionUpdateRequest
import com.itsjeel01.finsiblefrontend.data.remote.model.TransactionsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val apiService: TransactionApiService
) {
    suspend fun getTransactions(
        startDate: Long? = null,
        endDate: Long? = null,
        accountId: Long? = null,
        categoryId: Long? = null,
        type: String? = null,
        page: Int = 0,
        size: Int = 50
    ): BaseResponse<TransactionsData> {
        return apiService.getTransactions(startDate, endDate, accountId, categoryId, type, page, size)
    }

    suspend fun getTransaction(id: Long): BaseResponse<Transaction> {
        return apiService.getTransaction(id)
    }


    suspend fun createTransaction(request: TransactionCreateRequest): BaseResponse<Transaction> {
        return apiService.createTransaction(request)
    }

    suspend fun updateTransaction(id: Long, request: TransactionUpdateRequest): BaseResponse<Transaction> {
        return apiService.updateTransaction(id, request)
    }

    suspend fun deleteTransaction(id: Long): BaseResponse<Unit> {
        return apiService.deleteTransaction(id)
    }
}