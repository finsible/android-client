package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.FinsibleConstants
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.toReadableDate
import com.itsjeel01.finsiblefrontend.ui.component.fin.ButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.fin.TextFieldConfig
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.model.TimeFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.TransactionsFilterState
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.common.FinsibleConstants.ALL_MONTHS
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
            TypeSection(
                selectedTypes = sheetState.transactionTypes,
                onTypeToggle = { viewModel.toggleType(it) }
            )
            SheetDivider()
            SortSection(
                selectedOption = sheetState.sortOption,
                onOptionSelected = { viewModel.updateSortOption(it) }
            )
            SheetDivider()
            TimeSection(
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
            AmountSection(
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

/** Composable function to draw a drag handle for the filters bottom sheet. */
@Composable
private fun SheetDragHandle() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(FinsibleTheme.dimes.d12))
        Box(
            Modifier
                .width(FinsibleTheme.dimes.d36)
                .height(FinsibleTheme.dimes.d4)
                .clip(RoundedCornerShape(FinsibleTheme.dimes.d2))
                .background(FinsibleTheme.colors.outlineVariant)
        )
        Spacer(Modifier.height(FinsibleTheme.dimes.d12))
    }
}

// ─── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun SheetHeader(hasAnyActive: Boolean, onClearAll: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.filters_and_sort), style = FinsibleTheme.typography.t18.bold())

        FinsibleButton(
            text = stringResource(R.string.clear_all),
            onClick = onClearAll,
            config = ButtonConfig(
                type = ComponentType.Tertiary,
                size = ComponentSize.Small,
                enabled = hasAnyActive
            )
        )
    }
}

/** Divider between sections. */
@Composable
private fun SheetDivider() =
    HorizontalDivider(
        modifier = Modifier.padding(vertical = FinsibleTheme.dimes.d14),
        color = FinsibleTheme.colors.divider,
        thickness = FinsibleTheme.dimes.d0dot5
    )

/** Label composable for sections. */
@Composable
private fun SectionLabel(text: String) = Text(
    text = text,
    style = FinsibleTheme.typography.t10.semiBold(),
    color = FinsibleTheme.colors.tertiaryContent
)

/** Small dot shown before the currently selected item in dropdowns. */
@Composable
private fun SelectionDot(visible: Boolean) {
    if (visible) {
        Box(
            Modifier
                .size(FinsibleTheme.dimes.d4)
                .clip(CircleShape)
                .background(FinsibleTheme.colors.primaryContent)
        )
    }
}

/** Sorting section of the filter sheet. */
@Composable
private fun SortSection(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)) {
        SectionLabel(stringResource(R.string.sort_by))

        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
            SortOption.entries.chunked(2).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { option ->
                        RadioOption(
                            label = stringResource(option.displayText),
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/** Transaction types section of the filter sheet. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TypeSection(
    selectedTypes: Set<TransactionType>,
    onTypeToggle: (TransactionType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10)) {
        SectionLabel(stringResource(R.string.transaction_type))
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
        ) {
            TransactionType.entries.forEach { type ->
                FilterChip(
                    modifier = Modifier.weight(1f),
                    type = type,
                    isSelected = type in selectedTypes,
                    onClick = { onTypeToggle(type) },
                    leadingIcon = type.icon
                )
            }
        }
    }
}

/** Time based filtering section of the filter sheet. */
@Composable
private fun TimeSection(
    timeMode: TimeFilterMode,
    onTimeModeChange: (TimeFilterMode) -> Unit,
    selectedMonth: Int,
    selectedYear: Int,
    currentMonth: Int,
    currentYear: Int,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    customRangeStart: Long?,
    customRangeEnd: Long?,
    onCustomRangeClick: () -> Unit
) {
    Column {
        SectionLabel(stringResource(R.string.time_period))

        Spacer(Modifier.height(FinsibleTheme.dimes.d8))

        // Radio row
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeFilterMode.entries.forEach { mode ->
                RadioOption(
                    label = stringResource(mode.displayText),
                    selected = mode == timeMode,
                    onClick = { onTimeModeChange(mode) }
                )
            }
        }

        // Inline picker — collapses entirely for ALL, swaps for others
        AnimatedContent(
            targetState = timeMode,
            transitionSpec = { fadeIn(tween(Duration.MS_150.toInt())) togetherWith fadeOut(tween(Duration.MS_100.toInt())) },
            label = "time_picker_swap"
        ) { mode ->

            Column {
                if (mode != TimeFilterMode.ALL) Spacer(Modifier.height(FinsibleTheme.dimes.d8))

                when (mode) {
                    TimeFilterMode.ALL -> Unit  // no picker — null range means all transactions
                    TimeFilterMode.MONTH_YEAR -> MonthYearRow(
                        selectedMonth = selectedMonth,
                        selectedYear = selectedYear,
                        currentMonth = currentMonth,
                        currentYear = currentYear,
                        onMonthChange = onMonthChange,
                        onYearChange = onYearChange
                    )

                    TimeFilterMode.CUSTOM -> CustomRangeField(
                        rangeStart = customRangeStart,
                        rangeEnd = customRangeEnd,
                        onClick = onCustomRangeClick
                    )
                }
            }
        }
    }
}

/** A single labelled radio option. */
@Composable
private fun RadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dotColor by animateColorAsState(
        targetValue = if (selected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.transparent,
        animationSpec = tween(150), label = "radio_dot"
    )
    val ringColor by animateColorAsState(
        targetValue = if (selected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.outlineVariant,
        animationSpec = tween(150), label = "radio_ring"
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.secondaryContent,
        animationSpec = tween(150), label = "radio_label"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
        modifier = modifier
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d6))
            .clickable(onClick = onClick)
            .padding(vertical = FinsibleTheme.dimes.d4)
    ) {
        // Ring
        Box(
            Modifier
                .size(FinsibleTheme.dimes.d18)
                .clip(CircleShape)
                .border(FinsibleTheme.dimes.d1dot5, ringColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Fill dot
            Box(
                Modifier
                    .size(FinsibleTheme.dimes.d9)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
        Text(label, style = FinsibleTheme.typography.t14.medium(), color = labelColor)
    }
}

/** Dropdown that shows month name + year dropdowns side-by-side. */
@Composable
private fun MonthYearRow(
    selectedMonth: Int,
    selectedYear: Int,
    currentMonth: Int,
    currentYear: Int,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Month dropdown
        val allMonthsLabel = stringResource(R.string.all_months)
        val monthLabel = if (selectedMonth == ALL_MONTHS) allMonthsLabel else FinsibleConstants.MONTHS[selectedMonth]
        InlineDropdown(value = monthLabel, modifier = Modifier.weight(1.6f)) { closeMenu ->
            // "All months" entry — year-only filtering
            DropdownMenuItem(
                text = {
                    Row {
                        Text(
                            text = allMonthsLabel,
                            style = FinsibleTheme.typography.t14.medium(),
                            color = if (selectedMonth == ALL_MONTHS) FinsibleTheme.colors.link
                            else FinsibleTheme.colors.secondaryContent
                        )
                    }
                },
                onClick = { onMonthChange(ALL_MONTHS); closeMenu() },
                colors = MenuDefaults.itemColors(textColor = FinsibleTheme.colors.primaryContent)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = FinsibleTheme.dimes.d4),
                color = FinsibleTheme.colors.divider,
                thickness = FinsibleTheme.dimes.d0dot5
            )
            FinsibleConstants.MONTHS.forEachIndexed { index, name ->
                val isDisabled = selectedYear == currentYear && index > currentMonth
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
                            SelectionDot(selectedMonth == index)
                            Text(
                                text = name,
                                style = FinsibleTheme.typography.t14.medium(),
                                color = when {
                                    isDisabled -> FinsibleTheme.colors.disabledContent
                                    index == selectedMonth -> FinsibleTheme.colors.primaryContent
                                    else -> FinsibleTheme.colors.secondaryContent
                                }
                            )
                        }
                    },
                    onClick = { if (!isDisabled) onMonthChange(index); closeMenu() },
                    enabled = !isDisabled,
                    colors = MenuDefaults.itemColors(
                        textColor = FinsibleTheme.colors.primaryContent,
                        disabledTextColor = FinsibleTheme.colors.disabledContent
                    )
                )
            }
        }

        // Year dropdown
        val years = remember(currentYear) { (currentYear downTo currentYear - 10).toList() }
        InlineDropdown(value = selectedYear.toString(), modifier = Modifier.weight(1f)) { closeMenu ->
            years.forEach { year ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
                            SelectionDot(selectedYear == year)
                            Text(
                                text = year.toString(),
                                style = FinsibleTheme.typography.t14.medium(),
                                color = if (year == selectedYear) FinsibleTheme.colors.primaryContent
                                else FinsibleTheme.colors.secondaryContent
                            )
                        }
                    },
                    onClick = { onYearChange(year); closeMenu() },
                    colors = MenuDefaults.itemColors(textColor = FinsibleTheme.colors.primaryContent)
                )
            }
        }
    }
}

/**
 * A tap-to-open field showing the resolved range label.
 * Opens the M3 DateRangePicker dialog via [onClick].
 */
@Composable
private fun CustomRangeField(
    rangeStart: Long?,
    rangeEnd: Long?,
    onClick: () -> Unit
) {
    val label = when {
        rangeStart != null && rangeEnd != null ->
            stringResource(R.string.date_range_full, rangeStart.toReadableDate(), rangeEnd.toReadableDate())

        rangeStart != null -> stringResource(R.string.date_range_from, rangeStart.toReadableDate())
        else -> stringResource(R.string.select_date_range)
    }
    val hasValue = rangeStart != null || rangeEnd != null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d10))
            .background(FinsibleTheme.colors.surfaceContainerLow)
            .border(
                width = FinsibleTheme.dimes.d0dot5,
                color = if (hasValue) FinsibleTheme.colors.outline
                else FinsibleTheme.colors.outlineVariant,
                shape = RoundedCornerShape(FinsibleTheme.dimes.d10)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = FinsibleTheme.dimes.d14, vertical = FinsibleTheme.dimes.d12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = FinsibleTheme.typography.t14.medium(),
            color = if (hasValue) FinsibleTheme.colors.primaryContent
            else FinsibleTheme.colors.placeholder,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(FinsibleTheme.dimes.d8))
        Icon(
            painter = painterResource(R.drawable.ic_calendar),
            contentDescription = null,
            tint = if (hasValue) FinsibleTheme.colors.primaryContent
            else FinsibleTheme.colors.tertiaryContent,
            modifier = Modifier.size(FinsibleTheme.dimes.d18)
        )
    }
}

/**
 * Reusable inline dropdown trigger + menu.
 * [content] receives a callback to close the menu.
 */
@Composable
private fun InlineDropdown(
    value: String,
    modifier: Modifier = Modifier,
    content: @Composable (closeMenu: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(200), label = "chevron_rot"
    )

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(FinsibleTheme.dimes.d10))
                .background(FinsibleTheme.colors.surfaceContainerLow)
                .border(
                    width = FinsibleTheme.dimes.d0dot5,
                    color = if (expanded) FinsibleTheme.colors.outline
                    else FinsibleTheme.colors.outlineVariant,
                    shape = RoundedCornerShape(FinsibleTheme.dimes.d10)
                )
                .clickable { expanded = true }
                .padding(horizontal = FinsibleTheme.dimes.d14, vertical = FinsibleTheme.dimes.d12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value,
                style = FinsibleTheme.typography.t14.medium(),
                color = FinsibleTheme.colors.primaryContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(FinsibleTheme.dimes.d6))
            Icon(
                painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_chevron_down),
                contentDescription = null,
                tint = FinsibleTheme.colors.secondaryContent,
                modifier = Modifier
                    .size(FinsibleTheme.dimes.d16)
                    .rotate(chevronRotation)
            )
        }

        if (expanded) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(FinsibleTheme.colors.surfaceContainer)
                    .wrapContentHeight()
            ) {
                content { expanded = false }
            }
        }
    }
}

@Composable
private fun AmountSection(
    minAmount: String,
    maxAmount: String,
    onMinChange: (String) -> Unit,
    onMaxChange: (String) -> Unit,
    isError: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        SectionLabel(stringResource(R.string.amount_range))
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleTextField(
                value = minAmount,
                onValueChange = { onMinChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.amount_min_placeholder),
                config = TextFieldConfig(size = ComponentSize.Small),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Text(stringResource(R.string.amount_range_dash), style = FinsibleTheme.typography.t16.medium(), color = FinsibleTheme.colors.tertiaryContent)
            FinsibleTextField(
                value = maxAmount,
                onValueChange = { onMaxChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = stringResource(R.string.amount_max_placeholder),
                config = TextFieldConfig(size = ComponentSize.Small),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
        }
        AnimatedVisibility(
            visible = isError,
            enter = fadeIn(tween(Duration.MS_150.toInt())),
            exit = fadeOut(tween(Duration.MS_100.toInt()))
        ) {
            Text(
                text = stringResource(R.string.min_exceeds_max_error),
                style = FinsibleTheme.typography.t12.medium(),
                color = FinsibleTheme.colors.error
            )
        }
    }
}

/** Strip non-decimal characters, prevent multiple decimal points, and enforce 14.2 digit limits. */
private fun sanitizeDecimalInput(input: String): String {
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


/** Filter chips used for transaction types filters in the filter sheet. */
@Composable
private fun FilterChip(
    type: TransactionType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: Int? = null,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) type.getColor().copy(alpha = 0.25f)
        else FinsibleTheme.colors.surfaceContainerLow,
        animationSpec = tween(160), label = "chip_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) FinsibleTheme.colors.outline
        else FinsibleTheme.colors.outlineVariant,
        animationSpec = tween(160), label = "chip_border"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) FinsibleTheme.colors.primaryContent
        else FinsibleTheme.colors.secondaryContent,
        animationSpec = tween(160), label = "chip_content"
    )
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d8))
            .background(bgColor)
            .border(FinsibleTheme.dimes.d0dot5, borderColor, RoundedCornerShape(FinsibleTheme.dimes.d8))
            .clickable(onClick = onClick)
            .padding(horizontal = FinsibleTheme.dimes.d12, vertical = FinsibleTheme.dimes.d9),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            Icon(painterResource(it), null, tint = contentColor, modifier = Modifier.size(FinsibleTheme.dimes.d14))
            Spacer(Modifier.width(FinsibleTheme.dimes.d6))
        }
        Text(
            text = stringResource(type.displayText),
            style = FinsibleTheme.typography.t14.medium(),
            color = contentColor,
            textAlign = if (leadingIcon == null) TextAlign.Center else TextAlign.Start
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerDialog(
    initialStartMs: Long?,
    initialEndMs: Long?,
    onConfirm: (start: Long?, end: Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartMs,
        initialSelectedEndDateMillis = initialEndMs
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        pickerState.selectedStartDateMillis,
                        pickerState.selectedEndDateMillis
                    )
                },
                colors = ButtonDefaults.textButtonColors().copy(containerColor = FinsibleTheme.colors.surfaceBright)
            ) {
                Text(stringResource(R.string.date_range_dialog_ok), color = FinsibleTheme.colors.primaryContent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.date_range_dialog_cancel), color = FinsibleTheme.colors.tertiaryContent)
            }
        }
    ) {
        DateRangePicker(
            state = pickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors().copy(
                containerColor = FinsibleTheme.colors.surface,
                selectedDayContainerColor = FinsibleTheme.colors.inverse,
                todayContentColor = FinsibleTheme.colors.primaryContent,
                todayDateBorderColor = FinsibleTheme.colors.primaryContent,
                dayContentColor = FinsibleTheme.colors.primaryContent,
                weekdayContentColor = FinsibleTheme.colors.secondaryContent,
                yearContentColor = FinsibleTheme.colors.primaryContent,
            ),
            title = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = FinsibleTheme.dimes.d12),
        )
    }
}