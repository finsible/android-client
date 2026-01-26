package com.itsjeel01.finsiblefrontend.data.repository

import com.itsjeel01.finsiblefrontend.common.logging.Logger
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

    suspend fun fetchAllTransactions(
        startDate: Long? = null,
        endDate: Long? = null,
        accountId: Long? = null,
        categoryId: Long? = null,
        type: String? = null,
        pageSize: Int = 100
    ): BaseResponse<TransactionsData> {
        val allTransactions = mutableListOf<Transaction>()
        var currentPage = 0
        var isLastPage = false
        var lastResponse: BaseResponse<TransactionsData>? = null

        Logger.Sync.d("Starting to fetch all transactions with pageSize=$pageSize")

        while (!isLastPage) {
            try {
                val response = getTransactions(
                    startDate = startDate,
                    endDate = endDate,
                    accountId = accountId,
                    categoryId = categoryId,
                    type = type,
                    page = currentPage,
                    size = pageSize
                )

                lastResponse = response

                if (!response.success) {
                    Logger.Sync.e("Failed to fetch transactions page $currentPage: ${response.message}")
                    return response
                }

                val data = response.data
                allTransactions.addAll(data.transactions)
                isLastPage = data.last
                Logger.Sync.d(
                    "Fetched page $currentPage: ${data.transactions.size} transactions, " +
                            "total so far: ${allTransactions.size}, last=$isLastPage"
                )
                currentPage++
            } catch (e: Exception) {
                Logger.Sync.e("Error fetching transactions page $currentPage: ${e.message}", e)
                throw e
            }
        }

        Logger.Sync.i("Completed fetching all transactions: ${allTransactions.size} total")

        return BaseResponse(
            success = true,
            message = "All transactions fetched successfully",
            data = TransactionsData(
                transactions = allTransactions,
                totalElements = allTransactions.size,
                totalPages = currentPage,
                last = true,
                first = true,
                numberOfElements = allTransactions.size,
                empty = allTransactions.isEmpty(),
                number = 0,
                size = allTransactions.size
            ),
            cache = lastResponse?.cache ?: false
        )
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