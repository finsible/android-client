package com.itsjeel01.finsiblefrontend.ui.viewmodel

import com.google.common.truth.Truth.assertThat
import com.itsjeel01.finsiblefrontend.MainDispatcherRule
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.model.TimeFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.state.TransactionsFilterState
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class TransactionsFilterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val currencyFormatter: CurrencyFormatter = mockk {
        every { format(any<Long>(), any(), any()) } returns "1,000"
    }

    private lateinit var viewModel: TransactionsFilterViewModel

    @Before
    fun setUp() {
        viewModel = TransactionsFilterViewModel(currencyFormatter)
    }

    @Test
    fun `initial state is DEFAULT`() {
        assertThat(viewModel.filterState.value).isEqualTo(TransactionsFilterState.DEFAULT)
    }

    @Test
    fun `initFromFilterState seeds sheet state from source`() {
        val source = TransactionsFilterState(
            sortOption = SortOption.AMOUNT_HIGH_TO_LOW,
            selectedYear = 2025,
        )
        val calendar = Calendar.getInstance().apply { set(2025, Calendar.MARCH, 15) }

        viewModel.initFromFilterState(source, calendar, "INR")

        assertThat(viewModel.filterState.value.sortOption).isEqualTo(SortOption.AMOUNT_HIGH_TO_LOW)
        assertThat(viewModel.filterState.value.selectedYear).isEqualTo(2025)
        assertThat(viewModel.filterState.value.showDateRangePicker).isFalse()
        assertThat(viewModel.filterState.value.amountRangeError).isFalse()
    }

    @Test
    fun `reset sets default state with current month and year`() {
        val calendar = Calendar.getInstance().apply { set(2024, Calendar.DECEMBER, 31) }

        viewModel.updateSortOption(SortOption.AMOUNT_HIGH_TO_LOW)
        viewModel.reset(calendar)

        assertThat(viewModel.filterState.value).isEqualTo(
            TransactionsFilterState.DEFAULT.copy(selectedMonth = Calendar.DECEMBER, selectedYear = 2024)
        )
    }

    @Test
    fun `updateSortOption updates sort option`() {
        viewModel.updateSortOption(SortOption.OLDEST_FIRST)

        assertThat(viewModel.filterState.value.sortOption).isEqualTo(SortOption.OLDEST_FIRST)
    }

    @Test
    fun `toggleType adds and removes transaction types`() {
        viewModel.toggleType(TransactionType.EXPENSE)
        assertThat(viewModel.filterState.value.transactionTypes).contains(TransactionType.EXPENSE)

        viewModel.toggleType(TransactionType.EXPENSE)
        assertThat(viewModel.filterState.value.transactionTypes).doesNotContain(TransactionType.EXPENSE)
    }

    @Test
    fun `updateTimeMode updates time filter`() {
        viewModel.updateTimeMode(TimeFilterMode.MONTH_YEAR)
        assertThat(viewModel.filterState.value.timeFilterMode).isEqualTo(TimeFilterMode.MONTH_YEAR)

        viewModel.updateTimeMode(TimeFilterMode.CUSTOM)
        assertThat(viewModel.filterState.value.timeFilterMode).isEqualTo(TimeFilterMode.CUSTOM)
    }

    @Test
    fun `updateSelectedMonth and updateSelectedYear update month and year`() {
        viewModel.updateSelectedMonth(Calendar.JUNE)
        viewModel.updateSelectedYear(2026)

        assertThat(viewModel.filterState.value.selectedMonth).isEqualTo(Calendar.JUNE)
        assertThat(viewModel.filterState.value.selectedYear).isEqualTo(2026)
    }

    @Test
    fun `updateAmountMin updates min amount text and clears error`() {
        viewModel.markAmountRangeError()
        assertThat(viewModel.filterState.value.amountRangeError).isTrue()

        viewModel.updateAmountMin("500")

        assertThat(viewModel.filterState.value.amountMinText).isEqualTo("500")
        assertThat(viewModel.filterState.value.amountRangeError).isFalse()
    }

    @Test
    fun `updateAmountMax updates max amount text and clears error`() {
        viewModel.markAmountRangeError()
        assertThat(viewModel.filterState.value.amountRangeError).isTrue()

        viewModel.updateAmountMax("1000")

        assertThat(viewModel.filterState.value.amountMaxText).isEqualTo("1000")
        assertThat(viewModel.filterState.value.amountRangeError).isFalse()
    }

    @Test
    fun `setShowDateRangePicker shows and hides date picker`() {
        viewModel.setShowDateRangePicker(true)
        assertThat(viewModel.filterState.value.showDateRangePicker).isTrue()

        viewModel.setShowDateRangePicker(false)
        assertThat(viewModel.filterState.value.showDateRangePicker).isFalse()
    }

    @Test
    fun `confirmCustomDateRange sets range and hides picker`() {
        viewModel.setShowDateRangePicker(true)

        viewModel.confirmCustomDateRange(1_000L, 2_000L)

        assertThat(viewModel.filterState.value.dateRangeStart).isEqualTo(1_000L)
        assertThat(viewModel.filterState.value.dateRangeEnd).isEqualTo(2_000L)
        assertThat(viewModel.filterState.value.showDateRangePicker).isFalse()
    }

    @Test
    fun `markAmountRangeError sets error state`() {
        viewModel.markAmountRangeError()
        assertThat(viewModel.filterState.value.amountRangeError).isTrue()
    }

    @Test
    fun `buildAppliedState preserves search query and resolves filters`() {
        viewModel.updateSortOption(SortOption.AMOUNT_HIGH_TO_LOW)
        viewModel.toggleType(TransactionType.EXPENSE)

        val builtState = viewModel.buildAppliedState("test query")

        assertThat(builtState.searchQuery).isEqualTo("test query")
        assertThat(builtState.sortOption).isEqualTo(SortOption.AMOUNT_HIGH_TO_LOW)
        assertThat(builtState.transactionTypes).contains(TransactionType.EXPENSE)
        // Orphan fields zeroed out
        assertThat(builtState.amountMinText).isEmpty()
        assertThat(builtState.amountMaxText).isEmpty()
    }

    @Test
    fun `buildAppliedState with MONTH_YEAR mode resolves date range`() {
        viewModel.updateTimeMode(TimeFilterMode.MONTH_YEAR)
        viewModel.updateSelectedMonth(Calendar.JANUARY)
        viewModel.updateSelectedYear(2025)

        val builtState = viewModel.buildAppliedState("")

        assertThat(builtState.dateRangeStart).isNotNull()
        assertThat(builtState.dateRangeEnd).isNotNull()
        assertThat(builtState.dateRangeEnd!!).isGreaterThan(builtState.dateRangeStart!!)
    }
}
