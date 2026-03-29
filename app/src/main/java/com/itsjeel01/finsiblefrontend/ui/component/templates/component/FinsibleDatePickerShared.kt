package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDateRange
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.text.TextStyle as ComposeTextStyle
import com.composables.icons.lucide.R as LucideR
import java.time.format.TextStyle as DateTextStyle

@Composable
internal fun BaseCalendarContainer(
    modifier: Modifier,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.shadow(elevation = FinsibleTheme.dimes.d8, shape = RoundedCornerShape(sizes.containerCornerRadius)),
        shape = RoundedCornerShape(sizes.containerCornerRadius),
        color = colors.containerColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(sizes.contentPadding),
            verticalArrangement = Arrangement.spacedBy(sizes.contentVerticalSpacing),
            content = content,
        )
    }
}

@Composable
internal fun FinsibleCalendarNavigationHeader(
    selectedDateText: String,
    visibleMonth: YearMonth,
    startMonth: YearMonth,
    endMonth: YearMonth,
    locale: Locale,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthYearClick: () -> Unit,
    onSelectedDateClick: () -> Unit,
) {
    val canGoPreviousMonth = !visibleMonth.minusMonths(1).isBefore(startMonth)
    val canGoNextMonth = !visibleMonth.plusMonths(1).isAfter(endMonth)
    val monthYearLabel = visibleMonth.format(DateTimeFormatter.ofPattern("MMM yyyy", locale))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.monthHeaderColor,
                shape = RoundedCornerShape(sizes.dayCellCornerRadius),
            )
            .padding(
                horizontal = sizes.monthYearPickerSpacing,
                vertical = sizes.monthHeaderVerticalPadding,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FinsibleButton(
            onClick = onSelectedDateClick,
            variant = FinsibleButtonVariant.Text,
            size = FinsibleSize.Small,
        ) {
            Text(
                text = selectedDateText,
                style = FinsibleTheme.typography.t14.bold(),
                color = colors.monthHeaderContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            FinsibleButton(
                onClick = onPreviousMonth,
                variant = FinsibleButtonVariant.Text,
                shapeVariant = FinsibleShape.Circle,
                iconOnly = true,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_left),
                        contentDescription = stringResource(R.string.finsible_date_picker_previous_month_label),
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoPreviousMonth,
            ) {}

            FinsibleButton(
                onClick = onMonthYearClick,
                variant = FinsibleButtonVariant.Text,
                size = FinsibleSize.Small,
            ) {
                Text(
                    text = monthYearLabel,
                    style = FinsibleTheme.typography.t14,
                    color = colors.monthHeaderContentColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            FinsibleButton(
                onClick = onNextMonth,
                variant = FinsibleButtonVariant.Text,
                iconOnly = true,
                shapeVariant = FinsibleShape.Circle,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_right),
                        contentDescription = stringResource(R.string.finsible_date_picker_next_month_label),
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoNextMonth,
            ) {}
        }
    }
}

@Composable
internal fun FinsibleCalendarWeekHeader(
    firstDayOfWeek: DayOfWeek,
    locale: Locale,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
) {
    val orderedDays = remember(firstDayOfWeek) { orderedDaysOfWeek(firstDayOfWeek) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = sizes.weekdayHeaderBottomPadding),
    ) {
        orderedDays.forEach { dayOfWeek ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(sizes.dayCellSize),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = dayOfWeek.getDisplayName(DateTextStyle.NARROW, locale),
                    style = FinsibleTheme.typography.t12,
                    color = colors.weekdayContentColor,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
internal fun FinsibleCalendarDay(
    date: LocalDate,
    state: FinsibleCalendarDayState,
    onClick: (LocalDate) -> Unit,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
) {
    val isInteractive = state.isInCurrentMonth && state.isEnabled
    val background by animateColorAsState(
        targetValue = when {
            state.isSelected -> colors.selectedDayContainerColor
            state.isInRange -> colors.inRangeDayContainerColor
            else -> FinsibleTheme.colors.transparent
        },
        animationSpec = tween(Duration.MS_200.toInt()),
        label = "finsibleDayBackground",
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !state.isEnabled -> colors.disabledDayContentColor
            state.isSelected -> colors.selectedDayContentColor
            state.isInRange -> colors.inRangeDayContentColor
            state.isInCurrentMonth -> colors.dayContentColor
            else -> colors.outOfMonthDayContentColor
        },
        animationSpec = tween(Duration.MS_200.toInt()),
        label = "finsibleDayContent",
    )

    val dayShape = CircleShape
    val dayChipSize = sizes.dayCellSize - FinsibleTheme.dimes.d4
    val borderModifier = if (state.isToday && !state.isSelected) {
        Modifier.border(
            width = FinsibleTheme.dimes.d1,
            color = colors.todayBorderColor,
            shape = dayShape,
        )
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(sizes.dayCellSize),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(dayChipSize)
                .then(borderModifier)
                .clip(dayShape)
                .background(background, dayShape)
                .clickable(enabled = isInteractive, role = Role.Button) { onClick(date) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = if (state.isSelected) FinsibleTheme.typography.t14.bold() else FinsibleTheme.typography.t14,
                color = contentColor,
                maxLines = 1,
            )
        }
    }
}

@Composable
internal fun FinsibleMonthYearGridPicker(
    onMonthYearSelected: (FinsibleMonthYear) -> Unit,
    modifier: Modifier = Modifier,
    selectedMonthYear: FinsibleMonthYear = FinsibleMonthYear.from(YearMonth.now()),
    displayYear: Int = selectedMonthYear.year,
    onDisplayYearChange: (Int) -> Unit,
    yearRange: IntRange = (displayYear - DEFAULT_YEAR_WINDOW) .. (displayYear + DEFAULT_YEAR_WINDOW),
    startMonth: YearMonth,
    endMonth: YearMonth,
    locale: Locale,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
) {
    val orderedMonths = Month.entries
    val selectedState = stringResource(R.string.finsible_date_picker_state_selected)
    val availableState = stringResource(R.string.finsible_date_picker_state_available)
    val unavailableState = stringResource(R.string.finsible_date_picker_state_unavailable)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = sizes.monthHeaderVerticalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val canGoPreviousYear = displayYear > yearRange.first
            val canGoNextYear = displayYear < yearRange.last

            FinsibleButton(
                onClick = { onDisplayYearChange(displayYear - 1) },
                variant = FinsibleButtonVariant.Text,
                shapeVariant = FinsibleShape.Circle,
                iconOnly = true,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_left),
                        contentDescription = null,
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoPreviousYear,
            ) {}

            Text(
                text = displayYear.toString(),
                style = FinsibleTheme.typography.t14.bold(),
                color = colors.monthHeaderContentColor,
            )

            FinsibleButton(
                onClick = { onDisplayYearChange(displayYear + 1) },
                variant = FinsibleButtonVariant.Text,
                shapeVariant = FinsibleShape.Circle,
                iconOnly = true,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_right),
                        contentDescription = null,
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoNextYear,
            ) {}
        }

        Column(verticalArrangement = Arrangement.spacedBy(sizes.daysVerticalSpacing)) {
            orderedMonths.chunked(MONTHS_PER_ROW).forEach { monthRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(sizes.monthGridHorizontalSpacing),
                ) {
                    monthRow.forEach { month ->
                        val monthShape = RoundedCornerShape(sizes.dayCellCornerRadius)
                        val monthLabel = month.getDisplayName(DateTextStyle.SHORT, locale)
                        val isSelected = selectedMonthYear.year == displayYear && selectedMonthYear.month == month
                        val isEnabled = isMonthSelectable(
                            displayYear = displayYear,
                            month = month,
                            startMonth = startMonth,
                            endMonth = endMonth,
                        )

                        val background = if (isSelected) colors.selectedDayContainerColor else FinsibleTheme.colors.transparent
                        val contentColor = when {
                            !isEnabled -> colors.disabledDayContentColor
                            isSelected -> colors.selectedDayContentColor
                            else -> colors.dayContentColor
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(sizes.dayCellSize)
                                .alpha(if (isEnabled) 1f else 0.62f)
                                .clip(monthShape)
                                .background(background, monthShape)
                                .semantics {
                                    role = Role.Button
                                    contentDescription = "$monthLabel $displayYear"
                                    stateDescription = when {
                                        !isEnabled -> unavailableState
                                        isSelected -> selectedState
                                        else -> availableState
                                    }
                                }
                                .clickable(enabled = isEnabled, role = Role.Button) {
                                    onMonthYearSelected(FinsibleMonthYear(displayYear, month))
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = monthLabel,
                                style = FinsibleTheme.typography.t14,
                                color = contentColor,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T> FinsibleDropdownPicker(
    modifier: Modifier,
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<T>,
    optionLabel: (T) -> String,
    isOptionSelected: (T) -> Boolean,
    onOptionSelected: (T) -> Unit,
    size: FinsibleSize,
    menuItemTextColor: androidx.compose.ui.graphics.Color,
) {
    val trailingIconRes = if (expanded) {
        LucideR.drawable.lucide_ic_chevron_up
    } else {
        LucideR.drawable.lucide_ic_chevron_down
    }
    val dropdownMetrics = rememberMonthYearDropdownMetrics(size = size)
    val horizontalPadding = dropdownMetrics.horizontalPadding
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val textStyle = dropdownMetrics.textStyle
    val longestLabelPx = remember(options, optionLabel, textStyle, textMeasurer) {
        options.maxOfOrNull { option ->
            textMeasurer.measure(
                text = AnnotatedString(optionLabel(option)),
                style = textStyle,
            ).size.width
        } ?: 0
    }
    val valueLabelPx = remember(value, textStyle, textMeasurer) {
        textMeasurer.measure(
            text = AnnotatedString(value),
            style = textStyle,
        ).size.width
    }
    val contentWidthDp = with(density) { maxOf(longestLabelPx, valueLabelPx).toDp() }
    val controlWidth = contentWidthDp + (horizontalPadding * 2) + dropdownMetrics.iconSize + FinsibleTheme.dimes.d8

    val containerColor = FinsibleTheme.colors.input
    val borderColor by animateColorAsState(
        targetValue = if (expanded) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.border,
        animationSpec = tween(Duration.MS_200.toInt()),
        label = "monthYearDropdownBorder",
    )
    val contentColor by animateColorAsState(
        targetValue = FinsibleTheme.colors.primaryContent,
        animationSpec = tween(Duration.MS_200.toInt()),
        label = "monthYearDropdownContent",
    )

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange,
    ) {
        Surface(
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true,
                )
                .width(controlWidth),
            shape = RoundedCornerShape(dropdownMetrics.cornerRadius),
            color = containerColor,
            tonalElevation = FinsibleTheme.dimes.d0,
            shadowElevation = FinsibleTheme.dimes.d0,
            border = androidx.compose.foundation.BorderStroke(
                width = FinsibleTheme.dimes.d1,
                color = borderColor,
            ),
        ) {
            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = dropdownMetrics.minHeight)
                    .padding(horizontal = horizontalPadding, vertical = FinsibleTheme.dimes.d12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = value,
                    modifier = Modifier.weight(1f),
                    style = textStyle,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.width(FinsibleTheme.dimes.d8))
                Icon(
                    painter = painterResource(id = trailingIconRes),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(dropdownMetrics.iconSize),
                )
            }
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
        ) {
            options.forEach { option ->
                val isSelected = isOptionSelected(option)
                DropdownMenuItem(
                    text = {
                        Text(
                            text = optionLabel(option),
                            style = if (isSelected) textStyle.bold() else textStyle,
                            color = menuItemTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    onClick = { onOptionSelected(option) },
                )
            }
        }
    }
}

private fun orderedDaysOfWeek(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
    val allDays = DayOfWeek.entries
    return allDays.drop(firstDayOfWeek.ordinal) + allDays.take(firstDayOfWeek.ordinal)
}

internal fun LocalDate.toHeaderDateLabel(locale: Locale): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM ''yy", locale))
}

internal fun rangeHeaderText(
    selectedRange: FinsibleDateRange,
    locale: Locale,
    fallback: String,
): String {
    val start = selectedRange.startDate
    val end = selectedRange.endDate
    return when {
        start == null -> fallback
        end == null -> start.toHeaderDateLabel(locale)
        else -> start.toHeaderDateLabel(locale) + " - " + end.toHeaderDateLabel(locale)
    }
}

internal fun clampToRange(value: YearMonth, startMonth: YearMonth, endMonth: YearMonth): YearMonth {
    return when {
        value.isBefore(startMonth) -> startMonth
        value.isAfter(endMonth) -> endMonth
        else -> value
    }
}

internal const val DEFAULT_YEAR_WINDOW = 20
private const val MONTHS_PER_ROW = 3

@Immutable
private data class FinsibleDropdownMetrics(
    val minHeight: androidx.compose.ui.unit.Dp,
    val iconSize: androidx.compose.ui.unit.Dp,
    val horizontalPadding: androidx.compose.ui.unit.Dp,
    val cornerRadius: androidx.compose.ui.unit.Dp,
    val textStyle: ComposeTextStyle,
)

@Composable
private fun rememberMonthYearDropdownMetrics(size: FinsibleSize): FinsibleDropdownMetrics {
    require(size in SUPPORTED_MONTH_YEAR_PICKER_SIZES) {
        "Month/year dropdown supports only Small, Medium, and Large sizes."
    }
    val buttonSizes = FinsibleButtonDefaults.sizes(size)
    val layoutDirection = LocalLayoutDirection.current
    return FinsibleDropdownMetrics(
        minHeight = buttonSizes.height,
        iconSize = buttonSizes.iconSize,
        horizontalPadding = buttonSizes.contentPadding.calculateLeftPadding(layoutDirection),
        cornerRadius = FinsibleButtonDefaults.cornerRadius(FinsibleShape.Rounded, size),
        textStyle = buttonSizes.textStyle,
    )
}

private val SUPPORTED_MONTH_YEAR_PICKER_SIZES = setOf(
    FinsibleSize.Small,
    FinsibleSize.Medium,
    FinsibleSize.Large,
)

private fun isMonthSelectable(
    displayYear: Int,
    month: Month,
    startMonth: YearMonth,
    endMonth: YearMonth,
): Boolean {
    val candidate = YearMonth.of(displayYear, month)
    return !candidate.isBefore(startMonth) && !candidate.isAfter(endMonth)
}

internal fun resolveDayState(
    calendarDay: CalendarDay,
    today: LocalDate,
    selectedDate: LocalDate?,
    enabledDatePredicate: (LocalDate) -> Boolean,
): FinsibleCalendarDayState {
    val date = calendarDay.date
    val isInCurrentMonth = calendarDay.position == DayPosition.MonthDate
    val isEnabled = isInCurrentMonth && enabledDatePredicate(date)
    return FinsibleCalendarDayState(
        isInCurrentMonth = isInCurrentMonth,
        isEnabled = isEnabled,
        isToday = date == today,
        isSelected = date == selectedDate,
        isInRange = false,
    )
}

internal fun resolveDayRangeState(
    calendarDay: CalendarDay,
    today: LocalDate,
    selectedRange: FinsibleDateRange,
    enabledDatePredicate: (LocalDate) -> Boolean,
): FinsibleCalendarDayState {
    val date = calendarDay.date
    val isInCurrentMonth = calendarDay.position == DayPosition.MonthDate
    val isEnabled = isInCurrentMonth && enabledDatePredicate(date)
    val startDate = selectedRange.startDate
    val endDate = selectedRange.endDate
    val isSelected = date == startDate || date == endDate
    val isInRange = if (startDate != null && endDate != null) {
        date.isAfter(startDate) && date.isBefore(endDate)
    } else {
        false
    }

    return FinsibleCalendarDayState(
        isInCurrentMonth = isInCurrentMonth,
        isEnabled = isEnabled,
        isToday = date == today,
        isSelected = isSelected,
        isInRange = isInRange,
    )
}

@Immutable
internal data class FinsibleCalendarDayState(
    val isInCurrentMonth: Boolean,
    val isEnabled: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val isInRange: Boolean,
)
