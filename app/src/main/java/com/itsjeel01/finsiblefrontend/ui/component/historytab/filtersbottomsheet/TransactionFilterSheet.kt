package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.ButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.model.state.TransactionsFilterState
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionsFilterViewModel
import java.text.DecimalFormatSymbols
import java.util.Calendar

/** Bottom sheet for transaction filters and sort options. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFilterSheet(
    isVisible: Boolean,
    appliedFilters: TransactionsFilterState,
    onDismiss: () -> Unit,
    onApply: (TransactionsFilterState) -> Unit,
    modifier: Modifier = Modifier,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    viewModel: TransactionsFilterViewModel = hiltViewModel(),
) {
    if (!isVisible) return

    val sheetState by viewModel.filterState.collectAsStateWithLifecycle()

    val calendar = remember { Calendar.getInstance() }
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    // Seed ViewModel from the currently applied filters whenever the sheet becomes visible.
    LaunchedEffect(isVisible) {
        viewModel.initFromFilterState(appliedFilters, calendar)
    }

    if (sheetState.showDateRangePicker) {
        DateRangePickerDialog(
            initialStartMs = sheetState.dateRangeStart,
            initialEndMs = sheetState.dateRangeEnd,
            onConfirm = { start, end -> viewModel.confirmCustomDateRange(start, end) },
            onDismiss = { viewModel.setShowDateRangePicker(false) }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = FinsibleTheme.colors.primaryBackground,
        contentColor = FinsibleTheme.colors.primaryContent,
        dragHandle = { SheetDragHandle() },
        sheetGesturesEnabled = true,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FinsibleTheme.dimes.d20)
                .padding(bottom = FinsibleTheme.dimes.d32)
        ) {
            SheetHeader(
                hasAnyActive = sheetState.hasAnySheetActive,
                onClearAll = {
                    viewModel.reset(calendar)
                    onApply(TransactionsFilterState.DEFAULT)
                }
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d20))
            TransactionTypeFilter(
                selectedTypes = sheetState.transactionTypes,
                onTypeToggle = { viewModel.toggleType(it) }
            )
            SheetDivider()
            SortOptions(
                selectedOption = sheetState.sortOption,
                onOptionSelected = { viewModel.updateSortOption(it) }
            )
            SheetDivider()
            TimeBasedFilter(
                timeMode = sheetState.timeFilterMode,
                onTimeModeChange = { viewModel.updateTimeMode(it) },
                selectedMonth = sheetState.selectedMonth,
                selectedYear = sheetState.selectedYear,
                currentMonth = currentMonth,
                currentYear = currentYear,
                onMonthChange = { viewModel.updateSelectedMonth(it) },
                onYearChange = { viewModel.updateSelectedYear(it) },
                customRangeStart = sheetState.dateRangeStart,
                customRangeEnd = sheetState.dateRangeEnd,
                onCustomRangeClick = { viewModel.setShowDateRangePicker(true) }
            )
            SheetDivider()
            AmountFilter(
                minAmount = sheetState.amountMinText,
                maxAmount = sheetState.amountMaxText,
                onMinChange = { viewModel.updateAmountMin(sanitizeDecimalInput(it)) },
                onMaxChange = { viewModel.updateAmountMax(sanitizeDecimalInput(it)) },
                isError = sheetState.amountRangeError
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d20))
            FinsibleButton(
                text = stringResource(R.string.apply_filters),
                onClick = {
                    val applied = viewModel.buildAppliedState(appliedFilters.searchQuery)
                    if (applied.amountMin != null && applied.amountMax != null && applied.amountMin > applied.amountMax) {
                        viewModel.markAmountRangeError()
                        return@FinsibleButton
                    }
                    onApply(applied)
                    onDismiss()
                },
                config = ButtonConfig(type = ComponentType.Primary, size = ComponentSize.Medium, fullWidth = true)
            )
        }
    }
}

/** Strip non-decimal characters, prevent multiple decimal points, and enforce 14.2 digit limits. */
fun sanitizeDecimalInput(input: String): String {
    val decimalSeparator = DecimalFormatSymbols.getInstance().decimalSeparator
    var hasDecimal = false
    val filtered = input.filter { c ->
        when {
            c.isDigit() -> true
            (c == '.' || c == decimalSeparator) && !hasDecimal -> {
                hasDecimal = true; true
            }

            else -> false
        }
    }.replace(decimalSeparator, '.')

    val parts = filtered.split('.')
    val integerPart = parts[0].take(14)
    val decimalPart = parts.getOrNull(1)?.take(2) ?: ""
    return if (decimalPart.isEmpty() && !filtered.contains('.')) integerPart
    else "$integerPart.$decimalPart"
}