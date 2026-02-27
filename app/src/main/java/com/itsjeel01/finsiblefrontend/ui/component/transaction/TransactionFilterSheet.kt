package com.itsjeel01.finsiblefrontend.ui.component.transaction

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.itsjeel01.finsiblefrontend.ui.model.TimeFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.TransactionSortOption
import com.itsjeel01.finsiblefrontend.ui.model.TransactionViewOptions
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import java.text.DecimalFormatSymbols
import java.util.Calendar

private const val ALL_MONTHS = -1

/** Bottom sheet for transaction filters and sort options. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFilterSheet(
    isVisible: Boolean,
    viewOptions: TransactionViewOptions,
    onDismiss: () -> Unit,
    onApplyFilters: (TransactionViewOptions) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    if (!isVisible) return

    val calendar = remember { Calendar.getInstance() }
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    var sortOption by remember(viewOptions) { mutableStateOf(viewOptions.sortOption) }
    var selectedTypes by remember(viewOptions) { mutableStateOf(viewOptions.transactionTypes) }
    var timeMode by remember(viewOptions) { mutableStateOf(viewOptions.timeFilterMode) }
    var selectedMonth by remember(viewOptions) {
        mutableIntStateOf(if (viewOptions.selectedMonth == -1) currentMonth else viewOptions.selectedMonth)
    }
    var selectedYear by remember(viewOptions) {
        mutableIntStateOf(if (viewOptions.selectedYear == -1) currentYear else viewOptions.selectedYear)
    }
    var amountMinText by remember(viewOptions) {
        mutableStateOf(viewOptions.amountMin?.toPlainString() ?: "")
    }
    var amountMaxText by remember(viewOptions) {
        mutableStateOf(viewOptions.amountMax?.toPlainString() ?: "")
    }
    var amountRangeError by remember { mutableStateOf(false) }
    var showDateRangePicker by remember { mutableStateOf(false) }
    var customRangeStart by remember(viewOptions) { mutableStateOf(viewOptions.dateRangeStart) }
    var customRangeEnd by remember(viewOptions) { mutableStateOf(viewOptions.dateRangeEnd) }

    val updatedRange by remember {
        derivedStateOf {
            resolveRange(timeMode, selectedMonth, selectedYear, customRangeStart, customRangeEnd)
        }
    }

    val amountMin = amountMinText.toBigDecimalOrNull()
    val amountMax = amountMaxText.toBigDecimalOrNull()

    val hasAnyActive by remember {
        derivedStateOf {
            sortOption != TransactionSortOption.NEWEST_FIRST ||
                    selectedTypes.isNotEmpty() ||
                    updatedRange.first != null ||
                    updatedRange.second != null ||
                    amountMin != null ||
                    amountMax != null
        }
    }

    fun clearAllFilters() {
        sortOption = TransactionSortOption.NEWEST_FIRST
        selectedTypes = emptySet()
        amountMinText = ""
        amountMaxText = ""
        timeMode = TimeFilterMode.ALL
        selectedMonth = currentMonth
        selectedYear = currentYear
        customRangeStart = null
        customRangeEnd = null
        onApplyFilters(TransactionViewOptions.DEFAULT)
        onDismiss()
    }

    // ── Date range picker dialog ──
    if (showDateRangePicker) {
        DateRangePickerDialog(
            initialStartMs = customRangeStart,
            initialEndMs = customRangeEnd,
            onConfirm = { start, end ->
                customRangeStart = start
                customRangeEnd = end
                showDateRangePicker = false
            },
            onDismiss = { showDateRangePicker = false }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
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
                hasAnyActive = hasAnyActive,
                onClearAll = { clearAllFilters() }
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d20))
            TypeSection(
                selectedTypes = selectedTypes,
                onTypeToggle = { type ->
                    selectedTypes = if (type in selectedTypes) selectedTypes - type
                    else selectedTypes + type
                }
            )
            SheetDivider()
            SortSection(selectedOption = sortOption, onOptionSelected = { sortOption = it })
            SheetDivider()
            TimeSection(
                timeMode = timeMode,
                onTimeModeChange = { timeMode = it },
                selectedMonth = selectedMonth,
                selectedYear = selectedYear,
                currentMonth = currentMonth,
                currentYear = currentYear,
                onMonthChange = { selectedMonth = it },
                onYearChange = { selectedYear = it },
                customRangeStart = customRangeStart,
                customRangeEnd = customRangeEnd,
                onCustomRangeClick = { showDateRangePicker = true }
            )
            SheetDivider()
            AmountSection(
                minAmount = amountMinText,
                maxAmount = amountMaxText,
                onMinChange = { amountMinText = it; amountRangeError = false },
                onMaxChange = { amountMaxText = it; amountRangeError = false },
                isError = amountRangeError
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d20))
            FinsibleButton(
                text = "Apply Filters",
                onClick = {
                    val minVal = amountMinText.toBigDecimalOrNull()
                    val maxVal = amountMaxText.toBigDecimalOrNull()
                    if (minVal != null && maxVal != null && minVal > maxVal) {
                        amountRangeError = true
                        return@FinsibleButton
                    }

                    onApplyFilters(
                        viewOptions.copy(
                            sortOption = sortOption,
                            transactionTypes = selectedTypes,
                            timeFilterMode = timeMode,
                            selectedMonth = selectedMonth,
                            selectedYear = selectedYear,
                            dateRangeStart = updatedRange.first,
                            dateRangeEnd = updatedRange.second,
                            amountMin = minVal,
                            amountMax = maxVal
                        )
                    )
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
        Text("Filters & Sort", style = FinsibleTheme.typography.t18.bold())

        FinsibleButton(
            text = "Clear All",
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
    selectedOption: TransactionSortOption,
    onOptionSelected: (TransactionSortOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)) {
        SectionLabel("SORT BY")

        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
            TransactionSortOption.entries.chunked(2).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { option ->
                        RadioOption(
                            label = option.displayText,
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
        SectionLabel("TRANSACTION TYPE")
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
        SectionLabel("TIME PERIOD")

        Spacer(Modifier.height(FinsibleTheme.dimes.d8))

        // Radio row
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeFilterMode.entries.forEach { mode ->
                RadioOption(
                    label = mode.displayText,
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
        val monthLabel = if (selectedMonth == ALL_MONTHS) "All months" else FinsibleConstants.MONTHS[selectedMonth]
        InlineDropdown(value = monthLabel, modifier = Modifier.weight(1.6f)) { closeMenu ->
            // "All months" entry — year-only filtering
            DropdownMenuItem(
                text = {
                    Row {
                        Text(
                            text = "All months",
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
            "${rangeStart.toReadableDate()} – ${rangeEnd.toReadableDate()}"

        rangeStart != null -> "From ${rangeStart.toReadableDate()}"
        else -> "Select date range"
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
        SectionLabel("AMOUNT RANGE")
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FinsibleTextField(
                value = minAmount,
                onValueChange = { onMinChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = "Min",
                config = TextFieldConfig(size = ComponentSize.Small),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
            )
            Text("–", style = FinsibleTheme.typography.t16.medium(), color = FinsibleTheme.colors.tertiaryContent)
            FinsibleTextField(
                value = maxAmount,
                onValueChange = { onMaxChange(sanitizeDecimalInput(it)) },
                modifier = Modifier.weight(1f),
                placeholder = "Max",
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
                text = "Min amount cannot exceed max amount",
                style = FinsibleTheme.typography.t12.medium(),
                color = FinsibleTheme.colors.error
            )
        }
    }
}

/** Strip non-decimal characters and prevent multiple decimal points. Supports locale-aware decimal separators. */
private fun sanitizeDecimalInput(input: String): String {
    val decimalSeparator = DecimalFormatSymbols.getInstance().decimalSeparator
    var hasDecimal = false
    return input.filter { c ->
        when {
            c.isDigit() -> true
            (c == '.' || c == decimalSeparator) && !hasDecimal -> {
                hasDecimal = true; true
            }

            else -> false
        }
    }.replace(decimalSeparator, '.')
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
            text = type.displayText,
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
                Text("OK", color = FinsibleTheme.colors.primaryContent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = FinsibleTheme.colors.tertiaryContent)
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

/** Resolve the final [dateRangeStart, dateRangeEnd] pair from the active [TimeFilterMode]. */
internal fun resolveRange(
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

