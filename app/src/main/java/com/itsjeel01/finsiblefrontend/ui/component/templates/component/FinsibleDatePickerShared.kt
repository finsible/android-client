package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerShapes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerTypography
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDateRange
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
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
        modifier = modifier,
        shape = RoundedCornerShape(sizes.containerCornerRadius),
        shadowElevation = sizes.containerElevation,
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
internal fun FinsibleCalendarPickerCore(
    selectedDateText: String,
    startMonth: YearMonth,
    endMonth: YearMonth,
    firstVisibleMonth: YearMonth,
    firstDayOfWeek: DayOfWeek,
    locale: Locale,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
    typography: FinsibleDatePickerTypography,
    shapes: FinsibleDatePickerShapes,
    onSelectedDateClickTargetMonth: () -> YearMonth,
    dayContent: @Composable BoxScope.(CalendarDay, (LocalDate) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var showMonthYearPicker by remember { mutableStateOf(false) }
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = firstVisibleMonth,
        firstDayOfWeek = firstDayOfWeek,
        outDateStyle = OutDateStyle.EndOfGrid,
    )
    val visibleMonth by remember(calendarState) {
        derivedStateOf { calendarState.firstVisibleMonth.yearMonth }
    }
    var displayYear by remember { mutableIntStateOf(visibleMonth.year) }

    LaunchedEffect(visibleMonth, showMonthYearPicker) {
        if (!showMonthYearPicker) displayYear = visibleMonth.year
    }

    val calendarBodyMinHeight = remember(sizes) { calculateCalendarBodyMinHeight(sizes) }

    BaseCalendarContainer(modifier = modifier, colors = colors, sizes = sizes) {
        FinsibleCalendarNavigationHeader(
            selectedDateText = selectedDateText,
            visibleMonth = visibleMonth,
            startMonth = startMonth,
            endMonth = endMonth,
            locale = locale,
            colors = colors,
            sizes = sizes,
            shapes = shapes,
            onPreviousMonth = {
                coroutineScope.launch { calendarState.scrollToMonth(visibleMonth.minusMonths(1)) }
            },
            onNextMonth = {
                coroutineScope.launch { calendarState.scrollToMonth(visibleMonth.plusMonths(1)) }
            },
            onMonthYearClick = {
                showMonthYearPicker = !showMonthYearPicker
                displayYear = visibleMonth.year
            },
            onSelectedDateClick = {
                coroutineScope.launch {
                    val targetMonth = onSelectedDateClickTargetMonth().coerceIn(startMonth, endMonth)
                    calendarState.scrollToMonth(targetMonth)
                }
            },
        )

        Column(
            modifier = Modifier.heightIn(min = calendarBodyMinHeight),
        ) {
            if (showMonthYearPicker) {
                FinsibleMonthYearGridPicker(
                    onMonthYearSelected = { monthYear ->
                        val resolvedMonth = monthYear.toYearMonthOrNull() ?: return@FinsibleMonthYearGridPicker
                        val targetMonth = resolvedMonth.coerceIn(startMonth, endMonth)
                        coroutineScope.launch { calendarState.scrollToMonth(targetMonth) }
                        showMonthYearPicker = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    selectedMonthYear = FinsibleMonthYear.from(visibleMonth),
                    displayYear = displayYear,
                    onDisplayYearChange = { requestedYear ->
                        displayYear = requestedYear.coerceIn(startMonth.year, endMonth.year)
                    },
                    yearRange = startMonth.year .. endMonth.year,
                    startMonth = startMonth,
                    endMonth = endMonth,
                    locale = locale,
                    colors = colors,
                    sizes = sizes,
                    typography = typography,
                    shapes = shapes,
                )
            } else {
                FinsibleCalendarWeekHeader(
                    firstDayOfWeek = firstDayOfWeek,
                    locale = locale,
                    colors = colors,
                    sizes = sizes,
                    typography = typography,
                )

                HorizontalCalendar(
                    modifier = Modifier.clipToBounds(),
                    state = calendarState,
                    dayContent = { calendarDay ->
                        dayContent(calendarDay) { clickedDate ->
                            coroutineScope.launch {
                                calendarState.scrollToMonth(YearMonth.from(clickedDate).coerceIn(startMonth, endMonth))
                            }
                        }
                    },
                )
            }
        }
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
    shapes: FinsibleDatePickerShapes,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthYearClick: () -> Unit,
    onSelectedDateClick: () -> Unit,
) {
    val canGoPreviousMonth = !visibleMonth.minusMonths(1).isBefore(startMonth)
    val canGoNextMonth = !visibleMonth.plusMonths(1).isAfter(endMonth)
    val formatter = remember(locale) { DateTimeFormatter.ofPattern("MMM yyyy", locale) }
    val monthYearLabel = visibleMonth.format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.monthHeaderColor,
                shape = RoundedCornerShape(sizes.monthHeaderCornerRadius),
            )
            .padding(
                horizontal = sizes.monthYearPickerSpacing,
                vertical = sizes.monthHeaderVerticalPadding,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val buttonColors = FinsibleButtonDefaults.colors(
            variant = FinsibleButtonVariant.Text,
            contentColor = colors.monthHeaderContentColor,
        )

        FinsibleButton(
            onClick = onSelectedDateClick,
            variant = FinsibleButtonVariant.Text,
            colors = buttonColors,
            size = FinsibleSize.Small,
            text = selectedDateText,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            FinsibleButton(
                onClick = onPreviousMonth,
                variant = FinsibleButtonVariant.Text,
                colors = buttonColors,
                shapeVariant = shapes.navigationIconButtonShape,
                iconOnly = true,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_left),
                        contentDescription = stringResource(R.string.finsible_date_picker_previous_month_label),
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoPreviousMonth,
            )

            FinsibleButton(
                onClick = onMonthYearClick,
                variant = FinsibleButtonVariant.Text,
                colors = buttonColors,
                size = FinsibleSize.Small,
                text = monthYearLabel,
            )

            FinsibleButton(
                onClick = onNextMonth,
                variant = FinsibleButtonVariant.Text,
                colors = buttonColors,
                iconOnly = true,
                shapeVariant = shapes.navigationIconButtonShape,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_right),
                        contentDescription = stringResource(R.string.finsible_date_picker_next_month_label),
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoNextMonth,
            )
        }
    }
}

@Composable
internal fun FinsibleCalendarWeekHeader(
    firstDayOfWeek: DayOfWeek,
    locale: Locale,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
    typography: FinsibleDatePickerTypography,
) {
    val orderedDaysAndLabels = remember(firstDayOfWeek, locale) {
        val days = orderedDaysOfWeek(firstDayOfWeek)
        days.map { day ->
            day to (
                    day.getDisplayName(DateTextStyle.NARROW, locale) to
                            day.getDisplayName(DateTextStyle.FULL, locale)
                    )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = sizes.weekdayHeaderBottomPadding),
    ) {
        orderedDaysAndLabels.forEach { (_, labels) ->
            val (narrowName, fullName) = labels
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(sizes.dayCellSize),
                contentAlignment = Alignment.Center,
            ) {
                FinsibleText(
                    text = narrowName,
                    variant = typography.weekdayTextVariant,
                    color = colors.weekdayContentColor,
                    maxLines = 1,
                    modifier = Modifier.semantics { contentDescription = fullName },
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
    typography: FinsibleDatePickerTypography,
    shapes: FinsibleDatePickerShapes,
) {
    val selectedState = stringResource(R.string.finsible_date_picker_state_selected)
    val availableState = stringResource(R.string.finsible_date_picker_state_available)
    val unavailableState = stringResource(R.string.finsible_date_picker_state_unavailable)

    val isInteractive = state.isEnabled
    val background = when {
        state.isSelected -> colors.selectedDayContainerColor
        state.isInRange -> colors.inRangeDayContainerColor
        else -> colors.dayContainerColor
    }

    val contentColor = when {
        !state.isEnabled -> colors.disabledDayContentColor
        state.isSelected -> colors.selectedDayContentColor
        state.isInRange -> colors.inRangeDayContentColor
        state.isInCurrentMonth -> colors.dayContentColor
        else -> colors.outOfMonthDayContentColor
    }

    val dayShape = remember(shapes.dayShape, sizes.dayCellCornerRadius) {
        resolveDayShape(shapes.dayShape, sizes.dayCellCornerRadius)
    }
    val dayChipSize = sizes.dayCellSize - sizes.dayCellInnerSpacing
    val borderModifier = if (state.isToday && !state.isSelected) {
        Modifier.border(
            width = sizes.todayBorderWidth,
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
                .semantics {
                    stateDescription = when {
                        !state.isEnabled -> unavailableState
                        state.isSelected -> selectedState
                        else -> availableState
                    }
                }
                .clickable(enabled = isInteractive, role = Role.Button) { onClick(date) },
            contentAlignment = Alignment.Center,
        ) {
            val dayVariant = if (state.isSelected) {
                typography.daySelectedTextVariant
            } else {
                typography.dayDefaultTextVariant
            }
            FinsibleText(
                text = date.dayOfMonth.toString(),
                variant = dayVariant,
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
    yearRange: IntRange =
        (displayYear - CalendarConstraints.DEFAULT_YEAR_WINDOW) ..
                (displayYear + CalendarConstraints.DEFAULT_YEAR_WINDOW),
    startMonth: YearMonth,
    endMonth: YearMonth,
    locale: Locale,
    colors: FinsibleDatePickerColors,
    sizes: FinsibleDatePickerSizes,
    typography: FinsibleDatePickerTypography,
    shapes: FinsibleDatePickerShapes,
) {
    val orderedMonths = Month.entries
    val monthShape = remember(shapes.monthGridItemShape, sizes.monthGridItemCornerRadius) {
        resolveDayShape(shapes.monthGridItemShape, sizes.monthGridItemCornerRadius)
    }
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
            val buttonColors = FinsibleButtonDefaults.colors(
                variant = FinsibleButtonVariant.Text,
                contentColor = colors.monthHeaderContentColor,
            )

            FinsibleButton(
                onClick = { onDisplayYearChange(displayYear - 1) },
                variant = FinsibleButtonVariant.Text,
                colors = buttonColors,
                shapeVariant = shapes.navigationIconButtonShape,
                iconOnly = true,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_left),
                        contentDescription = stringResource(R.string.finsible_date_picker_previous_year),
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoPreviousYear,
            )

            FinsibleText(
                text = displayYear.toString(),
                variant = typography.monthGridYearTextVariant,
                color = colors.monthHeaderContentColor,
            )

            FinsibleButton(
                onClick = { onDisplayYearChange(displayYear + 1) },
                variant = FinsibleButtonVariant.Text,
                colors = buttonColors,
                shapeVariant = shapes.navigationIconButtonShape,
                iconOnly = true,
                icon = {
                    Icon(
                        painter = painterResource(id = LucideR.drawable.lucide_ic_chevron_right),
                        contentDescription = stringResource(R.string.finsible_date_picker_next_year),
                    )
                },
                size = FinsibleSize.Small,
                enabled = canGoNextYear,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(sizes.daysVerticalSpacing)) {
            orderedMonths.chunked(MONTHS_PER_ROW).forEach { monthRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(sizes.monthGridHorizontalSpacing),
                ) {
                    monthRow.forEach { month ->
                        val monthLabel = month.getDisplayName(DateTextStyle.SHORT, locale)
                        val isSelected = selectedMonthYear.year == displayYear && selectedMonthYear.month == month
                        val isEnabled = isMonthSelectable(
                            displayYear = displayYear,
                            month = month,
                            startMonth = startMonth,
                            endMonth = endMonth,
                        )

                        val background = if (isSelected) colors.selectedDayContainerColor else colors.dayContainerColor
                        val contentColor = when {
                            !isEnabled -> colors.disabledDayContentColor
                            isSelected -> colors.selectedDayContentColor
                            else -> colors.dayContentColor
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(sizes.dayCellSize)
                                .alpha(if (isEnabled) 1f else colors.disabledMonthOpacity)
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
                            FinsibleText(
                                text = monthLabel,
                                variant = typography.monthGridItemTextVariant,
                                color = contentColor,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun orderedDaysOfWeek(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
    val allDays = DayOfWeek.entries
    return allDays.drop(firstDayOfWeek.ordinal) + allDays.take(firstDayOfWeek.ordinal)
}

private val headerDateLabelFormatters = ConcurrentHashMap<Locale, DateTimeFormatter>()

internal fun LocalDate.toHeaderDateLabel(locale: Locale): String {
    val formatter = headerDateLabelFormatters.getOrPut(locale) {
        DateTimeFormatter.ofPattern("dd MMM ''yy", locale)
    }
    return this.format(formatter)
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

private const val MONTHS_PER_ROW = 3

private fun isMonthSelectable(
    displayYear: Int,
    month: Month,
    startMonth: YearMonth,
    endMonth: YearMonth,
): Boolean {
    val candidate = YearMonth.of(displayYear, month)
    return !candidate.isBefore(startMonth) && !candidate.isAfter(endMonth)
}

private fun calculateCalendarBodyMinHeight(sizes: FinsibleDatePickerSizes): Dp {
    val maxWeeks = 6
    val gridRows = 4
    val weekHeaderHeight = sizes.dayCellSize + sizes.weekdayHeaderBottomPadding
    val maxMonthHeight = sizes.dayCellSize * maxWeeks
    val maxCalendarHeight = weekHeaderHeight + maxMonthHeight

    val yearHeaderHeight = sizes.dayCellSize + sizes.monthHeaderVerticalPadding
    val monthGridHeight = (sizes.dayCellSize * gridRows) + (sizes.daysVerticalSpacing * (gridRows - 1))
    val monthYearGridHeight = yearHeaderHeight + monthGridHeight

    return maxOf(maxCalendarHeight, monthYearGridHeight)
}

private fun resolveDayShape(shape: FinsibleShape, cornerRadius: Dp): Shape {
    return when (shape) {
        FinsibleShape.Circle,
        FinsibleShape.Pill -> CircleShape

        FinsibleShape.Rounded -> RoundedCornerShape(cornerRadius)
        FinsibleShape.Sharp -> RoundedCornerShape(0.dp)
    }
}

internal fun resolveDayState(
    calendarDay: CalendarDay,
    today: LocalDate,
    selectedDate: LocalDate?,
    startMonth: YearMonth,
    endMonth: YearMonth,
): FinsibleCalendarDayState {
    val date = calendarDay.date
    val isInCurrentMonth = calendarDay.position == DayPosition.MonthDate
    val isEnabled = isDateWithinBounds(date = date, startMonth = startMonth, endMonth = endMonth)
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
    startMonth: YearMonth,
    endMonth: YearMonth,
): FinsibleCalendarDayState {
    val date = calendarDay.date
    val isInCurrentMonth = calendarDay.position == DayPosition.MonthDate
    val isEnabled = isDateWithinBounds(date = date, startMonth = startMonth, endMonth = endMonth)
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

private fun isDateWithinBounds(
    date: LocalDate,
    startMonth: YearMonth,
    endMonth: YearMonth,
): Boolean {
    val firstAllowedDate = startMonth.atDay(1)
    val lastAllowedDate = endMonth.atEndOfMonth()
    return !date.isBefore(firstAllowedDate) && !date.isAfter(lastAllowedDate)
}

@Immutable
internal data class FinsibleCalendarDayState(
    val isInCurrentMonth: Boolean,
    val isEnabled: Boolean,
    val isToday: Boolean,
    val isSelected: Boolean,
    val isInRange: Boolean,
)