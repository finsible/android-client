package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.repository.TransactionRepository
import com.itsjeel01.finsiblefrontend.data.sync.DataFetcher
import com.itsjeel01.finsiblefrontend.data.sync.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ViewModel for displaying list of transactions with infinite scroll and pull-to-refresh. */
@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionLocalRepo: TransactionLocalRepository,
    private val transactionRepository: TransactionRepository,
    private val syncManager: SyncManager,
    private val dataFetcher: DataFetcher
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 50L
        private const val REFRESH_PAGE_SIZE = 100
    }

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()

    private var currentOffset = 0L

    val syncState = syncManager.syncState
    val pendingCount = syncManager.pendingCount

    init {
        loadInitialTransactions()

        // Auto-fetch if never synced (centralized logic)
        viewModelScope.launch {
            dataFetcher.ensureDataFetched(
                localRepo = transactionLocalRepo,
                scopeKey = null,
                fetcher = { transactionRepository.getTransactions(page = 0, size = REFRESH_PAGE_SIZE) }
            )
        }
    }

    /** Load initial transactions (first page) from local DB. */
    private fun loadInitialTransactions() {
        viewModelScope.launch {
            try {
                currentOffset = 0
                val entities = transactionLocalRepo.getRecentTransactions(
                    offset = currentOffset,
                    limit = PAGE_SIZE
                )
                _transactions.value = entities
                currentOffset = PAGE_SIZE

                // Check if there are more transactions
                val totalCount = transactionLocalRepo.getTotalTransactionCount()
                _hasMore.value = currentOffset < totalCount

                Logger.UI.d("Loaded initial ${entities.size} transactions (total: $totalCount)")
            } catch (e: Exception) {
                Logger.UI.e("Failed to load transactions: ${e.message}")
                _transactions.value = emptyList()
            }
        }
    }

    /** Load more transactions (pagination) when user scrolls to bottom. */
    fun loadMoreTransactions() {
        if (_isLoadingMore.value || !_hasMore.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            try {
                val moreEntities = transactionLocalRepo.getRecentTransactions(
                    offset = currentOffset,
                    limit = PAGE_SIZE
                )

                if (moreEntities.isEmpty()) {
                    _hasMore.value = false
                    Logger.UI.d("No more transactions to load")
                } else {
                    _transactions.value = _transactions.value + moreEntities
                    currentOffset += moreEntities.size

                    // Check if there are still more
                    val totalCount = transactionLocalRepo.getTotalTransactionCount()
                    _hasMore.value = currentOffset < totalCount

                    Logger.UI.d("Loaded ${moreEntities.size} more transactions (offset now: $currentOffset)")
                }
            } catch (e: Exception) {
                Logger.UI.e("Failed to load more transactions: ${e.message}")
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    /** Refresh transactions from server (fetches recent data and caches via ResponseHandler). */
    fun refreshTransactions() {
        if (_isRefreshing.value) return

        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                val success = dataFetcher.refreshData(
                    localRepo = transactionLocalRepo,
                    scopeKey = null,
                    fetcher = { transactionRepository.getTransactions(page = 0, size = REFRESH_PAGE_SIZE) }
                )

                if (success) {
                    // Reset pagination and reload from local
                    loadInitialTransactions()
                } else {
                    Logger.UI.w("Refresh failed")
                }
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    /** Retry failed sync operations. */
    fun retryFailedSync() {
        syncManager.retryFailed()
    }
}
