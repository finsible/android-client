package com.itsjeel01.finsiblefrontend.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.toAmountCentis
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.model.TimeFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.TransactionsFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

const val ALL_MONTHS = -1

/** ViewModel that owns the transient editing state of the transaction filter bottom sheet. Uses [TransactionsFilterState] internally. */
@HiltViewModel
class TransactionsFilterViewModel @Inject constructor(
    private val currencyFormatter: CurrencyFormatter
) : ViewModel() {

    private val _filterState = MutableStateFlow(TransactionsFilterState.DEFAULT)
    val filterState: StateFlow<TransactionsFilterState> = _filterState.asStateFlow()

    /** Seed sheet state from an applied [TransactionsFilterState] and the current calendar. */
    fun initFromFilterState(source: TransactionsFilterState, calendar: Calendar) {
        val currentYear = calendar.get(Calendar.YEAR)

        _filterState.value = source.copy(
            selectedYear = if (source.selectedYear == -1) currentYear else source.selectedYear,
            selectedMonth = source.selectedMonth,
            amountMinText = source.amountMin?.let { currencyFormatter.formatWithoutSign(it) } ?: "",
            amountMaxText = source.amountMax?.let { currencyFormatter.formatWithoutSign(it) } ?: "",
            showDateRangePicker = false,
            amountRangeError = false
        )
    }

    /** Reset sheet to defaults, seeding month/year from the current calendar. */
    fun reset(calendar: Calendar) {
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        _filterState.value = TransactionsFilterState.DEFAULT.copy(selectedMonth = month, selectedYear = year)
    }

    /** Update the selected sort option. */
    fun updateSortOption(option: SortOption) {
        _filterState.update { it.copy(sortOption = option) }
    }

    /** Toggle a transaction type in/out of the active set. */
    fun toggleType(type: TransactionType) {
        _filterState.update { state ->
            val updated = if (type in state.transactionTypes) state.transactionTypes - type
            else state.transactionTypes + type
            state.copy(transactionTypes = updated)
        }
    }

    /** Update the active time filter mode. */
    fun updateTimeMode(mode: TimeFilterMode) {
        _filterState.update { it.copy(timeFilterMode = mode) }
    }

    /** Update the selected month for MONTH_YEAR mode. */
    fun updateSelectedMonth(month: Int) {
        _filterState.update { it.copy(selectedMonth = month) }
    }

    /** Update the selected year for MONTH_YEAR mode. */
    fun updateSelectedYear(year: Int) {
        _filterState.update { it.copy(selectedYear = year) }
    }

    /** Update the minimum amount text field value. */
    fun updateAmountMin(text: String) {
        _filterState.update { it.copy(amountMinText = text, amountRangeError = false) }
    }

    /** Update the maximum amount text field value. */
    fun updateAmountMax(text: String) {
        _filterState.update { it.copy(amountMaxText = text, amountRangeError = false) }
    }

    /** Show or hide the date range picker dialog. */
    fun setShowDateRangePicker(show: Boolean) {
        _filterState.update { it.copy(showDateRangePicker = show) }
    }

    /** Confirm a custom date range selection from the date picker. */
    fun confirmCustomDateRange(start: Long?, end: Long?) {
        _filterState.update { it.copy(dateRangeStart = start, dateRangeEnd = end, showDateRangePicker = false) }
    }

    /** Mark an amount range validation error. */
    fun markAmountRangeError() {
        _filterState.update { it.copy(amountRangeError = true) }
    }

    /** Build the applied [TransactionsFilterState] from the current sheet state, resolving the date range and preserving [existingSearchQuery]. Sheet-exclusive orphan fields are zeroed out before passing to [HistoryViewModel]. */
    fun buildAppliedState(existingSearchQuery: String): TransactionsFilterState {
        val state = _filterState.value
        val (rangeStart, rangeEnd) = resolveRange(
            state.timeFilterMode, state.selectedMonth, state.selectedYear,
            state.dateRangeStart, state.dateRangeEnd
        )
        return state.copy(
            searchQuery = existingSearchQuery,
            dateRangeStart = rangeStart,
            dateRangeEnd = rangeEnd,
            amountMin = state.amountMinText.takeIf { it.isNotBlank() }?.toAmountCentis()?.takeIf { it != 0L },
            amountMax = state.amountMaxText.takeIf { it.isNotBlank() }?.toAmountCentis()?.takeIf { it != 0L },
            // zero out orphan fields so HistoryViewModel never sees them
            amountMinText = "",
            amountMaxText = "",
            showDateRangePicker = false,
            amountRangeError = false
        )
    }
}

/** Resolve the final [dateRangeStart, dateRangeEnd] pair from the active [TimeFilterMode]. */
private fun resolveRange(
    mode: TimeFilterMode,
    month: Int,
    year: Int,
    customStart: Long?,
    customEnd: Long?
): Pair<Long?, Long?> = when (mode) {
    TimeFilterMode.ALL -> null to null

    TimeFilterMode.MONTH_YEAR -> {
        if (month == ALL_MONTHS) {
            val start = Calendar.getInstance().apply {
                set(year, Calendar.JANUARY, 1, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val end = Calendar.getInstance().apply {
                set(year, Calendar.DECEMBER, 31, 23, 59, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis

            start to end
        } else {
            val start = Calendar.getInstance().apply {
                set(year, month, 1, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val end = Calendar.getInstance().apply {
                set(year, month, 1, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
                add(Calendar.MONTH, 1)
                add(Calendar.MILLISECOND, -1)
            }.timeInMillis

            start to end
        }
    }

    TimeFilterMode.CUSTOM -> customStart to customEnd
}
