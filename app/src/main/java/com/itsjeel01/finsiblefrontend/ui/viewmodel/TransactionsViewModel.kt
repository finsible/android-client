package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.ui.mapper.toUiModel
import com.itsjeel01.finsiblefrontend.ui.model.DateAggregates
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.TransactionListState
import com.itsjeel01.finsiblefrontend.ui.model.TransactionUiModel
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionLocalRepository: TransactionLocalRepository,
    private val currencyFormatter: CurrencyFormatter
) : ViewModel() {

    private val daysPerPage = 5
    private var allUniqueDates: List<Long> = emptyList()

    private val initialState: TransactionListState by lazy {
        computeInitialState()
    }

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<TransactionListState> = _uiState.asStateFlow()

    private fun computeInitialState(): TransactionListState {
        return try {
            allUniqueDates = transactionLocalRepository.getAllUniqueDates()
            val aggregates = computeAllDateAggregates()

            val firstDates = allUniqueDates.take(daysPerPage)
            val entities = transactionLocalRepository.getTransactionsForDates(firstDates)

            val uiModels = entities.map { it.toUiModel(currencyFormatter) }
            val grouped = groupAndMapTransactions(entities)

            TransactionListState(
                transactions = uiModels.toPersistentList(),
                isLoading = false,
                hasMoreData = allUniqueDates.size > firstDates.size,
                dateAggregates = aggregates.toPersistentMap(),
                groupedTransactions = grouped,
                loadedDatesCount = firstDates.size
            )
        } catch (e: Exception) {
            Logger.UI.e("Failed to compute initial state: ${e.message}", e)
            TransactionListState(error = "Failed to load transactions")
        }
    }

    fun loadMoreTransactions() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMoreData) return

        viewModelScope.launch {
            if (_uiState.value.isLoadingMore || !_uiState.value.hasMoreData) return@launch

            _uiState.update { it.copy(isLoadingMore = true) }

            try {
                val currentLoadedCount = _uiState.value.loadedDatesCount
                val currentUiModels = _uiState.value.transactions
                val currentGrouped = _uiState.value.groupedTransactions

                withContext(Dispatchers.IO) {
                    val nextDates = allUniqueDates.drop(currentLoadedCount).take(daysPerPage)

                    if (nextDates.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            _uiState.update { it.copy(isLoadingMore = false, hasMoreData = false) }
                        }
                        return@withContext
                    }

                    val newEntities = transactionLocalRepository.getTransactionsForDates(nextDates)
                    val newLoadedCount = currentLoadedCount + nextDates.size

                    val newUiModels = newEntities.map { it.toUiModel(currencyFormatter) }

                    val totalUiModels = (currentUiModels + newUiModels).toPersistentList()

                    val newGrouped = groupAndMapTransactions(newEntities)
                    val totalGrouped = (currentGrouped + newGrouped).toPersistentMap()

                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(
                                transactions = totalUiModels,
                                isLoadingMore = false,
                                hasMoreData = newLoadedCount < allUniqueDates.size,
                                groupedTransactions = totalGrouped,
                                loadedDatesCount = newLoadedCount
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.UI.e("Failed to load more: ${e.message}", e)
                _uiState.update { it.copy(isLoadingMore = false, error = "Failed to load more") }
            }
        }
    }

    fun toggleDateFilter(dateHeader: String) {
        _uiState.update { currentState ->
            val currentMode = currentState.dateFilterModes[dateHeader] ?: DateFilterMode.NET
            val nextMode = when (currentMode) {
                DateFilterMode.NET -> DateFilterMode.INCOME
                DateFilterMode.INCOME -> DateFilterMode.EXPENSE
                DateFilterMode.EXPENSE -> DateFilterMode.NET
            }

            val updatedModes = (currentState.dateFilterModes + (dateHeader to nextMode)).toPersistentMap()
            currentState.copy(dateFilterModes = updatedModes)
        }
    }

    private fun computeAllDateAggregates(): Map<String, DateAggregates> {
        val rawAggregates = transactionLocalRepository.getAllDateAggregates()

        return rawAggregates.map { entry ->
            val timestampMs = entry.key
            val summary = entry.value

            val dateHeader = formatDateHeader(timestampMs)
            val (startMs, endMs) = getDateBounds(timestampMs)

            dateHeader to DateAggregates(
                dateHeader = dateHeader,
                startOfDayMs = startMs,
                endOfDayMs = endMs,
                incomeSum = summary.income,
                expenseSum = summary.expense,
                netSum = summary.net,
                transactionCount = summary.count
            )
        }.toMap()
    }

    private fun getDateBounds(timestamp: Long): Pair<Long, Long> {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = cal.timeInMillis - 1
        return startOfDay to endOfDay
    }

    private fun groupAndMapTransactions(
        transactions: List<TransactionEntity>
    ): ImmutableMap<String, ImmutableList<TransactionUiModel>> {
        return transactions
            .groupBy { transaction -> formatDateHeader(transaction.transactionDate) }
            .mapValues { (_, list) ->
                list.map { it.toUiModel(currencyFormatter) }.toPersistentList()
            }
            .toPersistentMap()
    }

    private fun formatDateHeader(timestamp: Long): String {
        return DateUtils.formatDateHeader(timestamp)
    }
}