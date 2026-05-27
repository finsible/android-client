package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.local.entity.TransactionEntity
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.repository.CurrencyRepository
import com.itsjeel01.finsiblefrontend.ui.mapper.toUiModel
import com.itsjeel01.finsiblefrontend.ui.model.DateAggregates
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.FilteredTransactionSummary
import com.itsjeel01.finsiblefrontend.ui.model.state.TransactionListState
import com.itsjeel01.finsiblefrontend.ui.model.state.TransactionsFilterState
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.TransactionUIModel
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 50
private const val SEARCH_DEBOUNCE_MS = 300L

@HiltViewModel
class HistoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val transactionLocalRepository: TransactionLocalRepository,
    val currencyFormatter: CurrencyFormatter,
    val currencyRepository: CurrencyRepository,
    preferenceManager: PreferenceManager
) : ViewModel() {

    private val preferenceManager = preferenceManager

    private val _filterState = MutableStateFlow(TransactionsFilterState.DEFAULT)
    val filterState: StateFlow<TransactionsFilterState> = _filterState.asStateFlow()

    private val _uiState = MutableStateFlow(TransactionListState())
    val uiState: StateFlow<TransactionListState> = _uiState.asStateFlow()

    private val _isSearchExpanded = MutableStateFlow(false)
    val isSearchExpanded: StateFlow<Boolean> = _isSearchExpanded.asStateFlow()

    private val _isFilterSheetVisible = MutableStateFlow(false)
    val isFilterSheetVisible: StateFlow<Boolean> = _isFilterSheetVisible.asStateFlow()

    private val _dateFilterModes = MutableStateFlow(persistentMapOf<Long, DateFilterMode>())
    val dateFilterModes: StateFlow<ImmutableMap<Long, DateFilterMode>> = _dateFilterModes.asStateFlow()

    val defaultCurrencyCode: StateFlow<String> = preferenceManager.defaultCurrencyCodeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private var currentOffset: Int = 0
    private var searchDebounceJob: Job? = null
    private var loadMoreJob: Job? = null

    init {
        executeQuery()
    }

    /** Load more data using unified pagination. */
    fun loadMore() {
        if (loadMoreJob?.isActive == true || !_uiState.value.hasMoreData) return

        loadMoreJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val filters = _filterState.value
                val offset = currentOffset

                withContext(Dispatchers.IO) {
                    val result = transactionLocalRepository.queryTransactions(
                        searchQuery = filters.searchQuery,
                        sortOption = filters.sortOption,
                        accountIds = filters.accountIds,
                        dateRangeStart = filters.dateRangeStart,
                        dateRangeEnd = filters.dateRangeEnd,
                        amountMin = filters.amountMin,
                        amountMax = filters.amountMax,
                        transactionTypes = filters.transactionTypes,
                        offset = offset,
                        limit = ITEMS_PER_PAGE
                    )

                    val shouldGroup = filters.shouldShowDateGrouping
                    val loadedTransactions = _uiState.value.transactions
                    val loadedTransactionsGrouped = _uiState.value.groupedTransactions

                    val incomingTransactions = if (!shouldGroup) {
                        result.transactions.map { it.toUiModel(currencyFormatter, currencyRepository, context) }.toPersistentList()
                    } else {
                        loadedTransactions
                    }

                    val incomingTransactionsGrouped = if (shouldGroup) {
                        val mapped = result.transactions.map { it.toUiModel(currencyFormatter, currencyRepository, context) }
                        val groupedPage = mapped.groupBy { DateUtils.getStartOfDayMs(it.transactionDate) }
                            .mapValues { (_, list) -> list.toPersistentList() }
                        mergeGroupedTransactions(loadedTransactionsGrouped, groupedPage.toPersistentMap())
                    } else {
                        loadedTransactionsGrouped
                    }

                    withContext(Dispatchers.Main) {
                        currentOffset = offset + result.transactions.size
                        _uiState.update {
                            it.copy(
                                transactions = if (!shouldGroup) {
                                    (loadedTransactions + incomingTransactions).toPersistentList()
                                } else loadedTransactions,
                                groupedTransactions = incomingTransactionsGrouped,
                                isLoading = false,
                                hasMoreData = result.hasMore
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.UI.e("Failed to load more: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(isLoading = false, error = context.getString(R.string.error_failed_to_load_more)) }
                }
            }
        }
    }

    /** Update search query with debouncing. */
    fun updateSearchQuery(query: String) {
        _filterState.update { it.copy(searchQuery = query) }
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            executeQuery()
        }
    }

    /** Toggle search UI expansion. */
    fun toggleSearchExpanded() {
        _isSearchExpanded.update { !it }
    }

    fun toggleFilterSheet() {
        _isFilterSheetVisible.update { !it }
    }

    fun hideFilterSheet() {
        _isFilterSheetVisible.value = false
    }

    /** Clear all filters and return to idle mode. */
    fun clearAllFilters() {
        _filterState.value = TransactionsFilterState.DEFAULT
        _isSearchExpanded.value = false
        _isFilterSheetVisible.value = false
        executeQuery()
    }

    /** Apply complete filter criteria (from filter sheet). */
    fun applyFilterCriteria(criteria: TransactionsFilterState) {
        _filterState.value = criteria.copy(searchQuery = _filterState.value.searchQuery)
        _isFilterSheetVisible.value = false
        executeQuery()
    }

    /** Collapse search and clear query. */
    fun collapseSearch() {
        _filterState.update { it.copy(searchQuery = "") }
        _isSearchExpanded.value = false
        executeQuery()
    }

    fun toggleDateFilter(startOfDayMs: Long) {
        _dateFilterModes.update { current ->
            val next = when (current[startOfDayMs] ?: DateFilterMode.NET) {
                DateFilterMode.NET -> DateFilterMode.INCOME
                DateFilterMode.INCOME -> DateFilterMode.EXPENSE
                DateFilterMode.EXPENSE -> DateFilterMode.NET
            }
            (current + (startOfDayMs to next)).toPersistentMap()
        }
    }

    /** Single entry point for all data loads. Resets pagination when resetOffset is true. */
    private fun executeQuery() {
        loadMoreJob?.cancel()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            currentOffset = 0

            try {
                val criteria = _filterState.value

                withContext(Dispatchers.IO) {
                    val result = transactionLocalRepository.queryTransactions(
                        searchQuery = criteria.searchQuery,
                        sortOption = criteria.sortOption,
                        accountIds = criteria.accountIds,
                        dateRangeStart = criteria.dateRangeStart,
                        dateRangeEnd = criteria.dateRangeEnd,
                        amountMin = criteria.amountMin,
                        amountMax = criteria.amountMax,
                        transactionTypes = criteria.transactionTypes,
                        offset = 0,
                        limit = ITEMS_PER_PAGE
                    )

                    val shouldGroup = criteria.shouldShowDateGrouping
                    val (uiModels, grouped) = mapTransactions(result.transactions, shouldGroup)

                    val filteredSummary = if (criteria.hasActiveFiltersOrSearch) {
                        FilteredTransactionSummary(
                            totalCount = result.summary.totalCount,
                            totalIncomeCentis = result.summary.totalIncomeCentis,
                            totalExpenseCentis = result.summary.totalExpenseCentis
                        )
                    } else null

                    val aggregates = if (!criteria.hasActiveFiltersOrSearch) computeAllDateAggregates()
                    else _uiState.value.dateAggregates

                    withContext(Dispatchers.Main) {
                        currentOffset = result.transactions.size
                        _uiState.update {
                            it.copy(
                                transactions = uiModels,
                                groupedTransactions = grouped,
                                dateAggregates = aggregates.toPersistentMap(),
                                isLoading = false,
                                hasMoreData = result.hasMore,
                                filteredSummary = filteredSummary
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.UI.e("Failed to execute query: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _uiState.update { it.copy(isLoading = false, error = context.getString(R.string.error_failed_to_load_transactions)) }
                }
            }
        }
    }

    /** Map entities to UI models and optionally group by date. */
    private fun mapTransactions(
        entities: List<TransactionEntity>,
        shouldGroup: Boolean = true
    ): Pair<ImmutableList<TransactionUIModel>, ImmutableMap<Long, ImmutableList<TransactionUIModel>>> {
        val uiModels = entities.map { it.toUiModel(currencyFormatter, currencyRepository, context) }

        val grouped = if (shouldGroup) {
            uiModels.groupBy { DateUtils.getStartOfDayMs(it.transactionDate) }
                .mapValues { (_, list) -> list.toPersistentList() }
                .toPersistentMap()
        } else {
            persistentMapOf()
        }

        return uiModels.toPersistentList() to grouped
    }

    /** Merge two grouped transaction maps preserving order. */
    private fun mergeGroupedTransactions(
        current: ImmutableMap<Long, ImmutableList<TransactionUIModel>>,
        new: ImmutableMap<Long, ImmutableList<TransactionUIModel>>
    ): ImmutableMap<Long, ImmutableList<TransactionUIModel>> {
        val merged = current.toMutableMap()
        new.forEach { (key, newList) ->
            merged[key] = merged[key]?.let { (it + newList).toPersistentList() } ?: newList
        }
        return merged.toPersistentMap()
    }

    /** Compute date aggregates for all dates in the database. */
    private fun computeAllDateAggregates(): Map<Long, DateAggregates> {
        return transactionLocalRepository.getAllDateAggregates().map { (timestampMs, summary) ->
            val startOfDayMs = DateUtils.getStartOfDayMs(timestampMs)
            val endOfDayMs = startOfDayMs + (24 * 60 * 60 * 1000) - 1 // Simplified for Logic layer

            startOfDayMs to DateAggregates(
                startOfDayMs = startOfDayMs,
                endOfDayMs = endOfDayMs,
                incomeSumCentis = summary.incomeCentis,
                expenseSumCentis = summary.expenseCentis,
                netSumCentis = summary.netCentis,
                transactionCount = summary.count
            )
        }.toMap()
    }
}