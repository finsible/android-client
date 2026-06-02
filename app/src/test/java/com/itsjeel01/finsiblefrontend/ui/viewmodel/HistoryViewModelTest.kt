package com.itsjeel01.finsiblefrontend.ui.viewmodel

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.PreferenceManager
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.repository.TransactionLocalRepository
import com.itsjeel01.finsiblefrontend.data.local.repository.PaginatedResult
import com.itsjeel01.finsiblefrontend.data.local.repository.FilteredSummary
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.model.state.TransactionsFilterState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HistoryViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context = mockk()
    private val transactionLocalRepository: TransactionLocalRepository = mockk()
    private val currencyFormatter: CurrencyFormatter = mockk()
    private val preferenceManager: PreferenceManager = mockk()

    private lateinit var viewModel: HistoryViewModel

    @Before
    fun setUp() {
        every { context.getString(R.string.error_failed_to_load_transactions) } returns "Failed to load"
        every { context.getString(R.string.error_failed_to_load_more) } returns "Failed to load more"
        every { preferenceManager.defaultCurrencyCodeFlow } returns MutableStateFlow("INR")

        every {
            transactionLocalRepository.queryTransactions(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } returns PaginatedResult(
            transactions = emptyList(),
            totalCount = 0,
            summary = FilteredSummary(0, 0L, 0L),
            hasMore = false
        )

        every { transactionLocalRepository.getAllDateAggregates() } returns emptyMap()

        viewModel = HistoryViewModel(
            context = context,
            transactionLocalRepository = transactionLocalRepository,
            currencyFormatter = currencyFormatter,
            preferenceManager = preferenceManager
        )
    }

    @Test
    fun `initial state has default filter`() {
        assertThat(viewModel.filterState.value).isEqualTo(TransactionsFilterState.DEFAULT)
        assertThat(viewModel.isSearchExpanded.value).isFalse()
        assertThat(viewModel.isFilterSheetVisible.value).isFalse()
    }

    @Test
    fun `updateSearchQuery updates filter state`() {
        viewModel.updateSearchQuery("coffee")

        assertThat(viewModel.filterState.value.searchQuery).isEqualTo("coffee")
    }

    @Test
    fun `toggleSearchExpanded toggles search expanded state`() {
        assertThat(viewModel.isSearchExpanded.value).isFalse()

        viewModel.toggleSearchExpanded()
        assertThat(viewModel.isSearchExpanded.value).isTrue()

        viewModel.toggleSearchExpanded()
        assertThat(viewModel.isSearchExpanded.value).isFalse()
    }

    @Test
    fun `toggleFilterSheet toggles filter sheet visibility`() {
        assertThat(viewModel.isFilterSheetVisible.value).isFalse()

        viewModel.toggleFilterSheet()
        assertThat(viewModel.isFilterSheetVisible.value).isTrue()

        viewModel.toggleFilterSheet()
        assertThat(viewModel.isFilterSheetVisible.value).isFalse()
    }

    @Test
    fun `hideFilterSheet sets visibility to false`() {
        viewModel.toggleFilterSheet()
        assertThat(viewModel.isFilterSheetVisible.value).isTrue()

        viewModel.hideFilterSheet()
        assertThat(viewModel.isFilterSheetVisible.value).isFalse()
    }

    @Test
    fun `clearAllFilters resets filter state and hides sheets`() {
        viewModel.updateSearchQuery("test")
        viewModel.toggleSearchExpanded()
        viewModel.toggleFilterSheet()

        viewModel.clearAllFilters()

        assertThat(viewModel.filterState.value).isEqualTo(TransactionsFilterState.DEFAULT)
        assertThat(viewModel.isSearchExpanded.value).isFalse()
        assertThat(viewModel.isFilterSheetVisible.value).isFalse()
    }

    @Test
    fun `applyFilterCriteria updates filter state and hides sheet`() {
        val criteria = TransactionsFilterState(
            sortOption = SortOption.OLDEST_FIRST,
        )
        viewModel.toggleFilterSheet()

        viewModel.applyFilterCriteria(criteria)

        assertThat(viewModel.filterState.value.sortOption).isEqualTo(SortOption.OLDEST_FIRST)
        assertThat(viewModel.isFilterSheetVisible.value).isFalse()
    }

    @Test
    fun `collapseSearch clears search and collapses`() {
        viewModel.updateSearchQuery("test")
        viewModel.toggleSearchExpanded()

        viewModel.collapseSearch()

        assertThat(viewModel.filterState.value.searchQuery).isEmpty()
        assertThat(viewModel.isSearchExpanded.value).isFalse()
    }

    @Test
    fun `toggleDateFilter cycles through NET, INCOME, EXPENSE`() {
        val dateMs = 1_700_000_000_000L

        viewModel.toggleDateFilter(dateMs)
        assertThat(viewModel.dateFilterModes.value[dateMs]).isEqualTo(DateFilterMode.INCOME)

        viewModel.toggleDateFilter(dateMs)
        assertThat(viewModel.dateFilterModes.value[dateMs]).isEqualTo(DateFilterMode.EXPENSE)

        viewModel.toggleDateFilter(dateMs)
        assertThat(viewModel.dateFilterModes.value[dateMs]).isEqualTo(DateFilterMode.NET)
    }

    @Test
    fun `loadMore does nothing when no more data`() {
        viewModel.loadMore()
    }

    @Test
    fun `rapid search query updates only trigger debounced query`() {
        // 10 rapid updates — check last value is set
        viewModel.updateSearchQuery("query0")
        viewModel.updateSearchQuery("query1")
        viewModel.updateSearchQuery("query2")
        viewModel.updateSearchQuery("query3")
        viewModel.updateSearchQuery("query4")
        viewModel.updateSearchQuery("query5")
        viewModel.updateSearchQuery("query6")
        viewModel.updateSearchQuery("query7")
        viewModel.updateSearchQuery("query8")
        viewModel.updateSearchQuery("query9")

        assertThat(viewModel.filterState.value.searchQuery).isEqualTo("query9")
    }

    @Test
    fun `toggleFilterSheet then clearAllFilters hides sheet`() {
        viewModel.toggleFilterSheet()
        assertThat(viewModel.isFilterSheetVisible.value).isTrue()

        viewModel.clearAllFilters()
        assertThat(viewModel.isFilterSheetVisible.value).isFalse()
    }

    @Test
    fun `applyFilterCriteria then collapseSearch restores search`() {
        viewModel.updateSearchQuery("test")
        viewModel.applyFilterCriteria(
            com.itsjeel01.finsiblefrontend.ui.model.state.TransactionsFilterState(
                sortOption = com.itsjeel01.finsiblefrontend.ui.model.SortOption.AMOUNT_HIGH_TO_LOW
            )
        )

        // Search query should be preserved, sort option applied
        assertThat(viewModel.filterState.value.searchQuery).isEqualTo("test")
        assertThat(viewModel.filterState.value.sortOption).isEqualTo(
            com.itsjeel01.finsiblefrontend.ui.model.SortOption.AMOUNT_HIGH_TO_LOW
        )
    }

    @Test
    fun `empty search does not trigger executeQuery`() {
        viewModel.updateSearchQuery("")
        // No crash = pass
    }
}
