package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.di.hiltUserLocale
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDatePickerDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerShapes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerTypography
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDateRange
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.calculateNextDateRange
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.normalized
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.YearMonth

/**
 * A highly configurable semantic date picker component that adheres to Finsible Design System.
 * @param onRangeSelected Callback when a date range is selected.
 * @param modifier Composable modifier.
 * @param selectedRange The currently selected date range.
 * @param constraints Shared superset config consumed by all temporal pickers.
 * @param colors The resolved color styles for the date picker.
 * @param sizes The resolved size styles for the date picker.
 * @param typography The resolved typography styles for the date picker.
 * @param shapes The resolved shape styles for the date picker.
 */
@Composable
fun FinsibleDateRangePicker(
    onRangeSelected: (FinsibleDateRange) -> Unit,
    modifier: Modifier = Modifier,
    selectedRange: FinsibleDateRange = FinsibleDateRange.Empty,
    constraints: CalendarConstraints = CalendarConstraints(),
    colors: FinsibleDatePickerColors = FinsibleDatePickerDefaults.colors(),
    sizes: FinsibleDatePickerSizes = FinsibleDatePickerDefaults.sizes(),
    typography: FinsibleDatePickerTypography = FinsibleDatePickerDefaults.typography(),
    shapes: FinsibleDatePickerShapes = FinsibleDatePickerDefaults.shapes(),
) {
    val resolvedFirstVisibleMonth = constraints.resolveFirstVisibleMonth(
        selectedDate = selectedRange.startDate,
        fallbackMonth = selectedRange.startDate?.let(YearMonth::from),
    )

    val locale = hiltUserLocale()
    val resolvedFirstDayOfWeek = constraints.resolveFirstDayOfWeek(firstDayOfWeekFromLocale(locale))
    val normalizedRange = remember(selectedRange) { selectedRange.normalized() }
    val selectedDateHeaderText = rangeHeaderText(
        selectedRange = normalizedRange,
        locale = locale,
        fallback = stringResource(R.string.finsible_date_picker_no_date_selected),
    )
    FinsibleCalendarPickerCore(
        modifier = modifier,
        selectedDateText = selectedDateHeaderText,
        startMonth = constraints.startMonth,
        endMonth = constraints.endMonth,
        firstVisibleMonth = resolvedFirstVisibleMonth,
        firstDayOfWeek = resolvedFirstDayOfWeek,
        locale = locale,
        colors = colors,
        sizes = sizes,
        typography = typography,
        shapes = shapes,
        onSelectedDateClickTargetMonth = { YearMonth.from(normalizedRange.startDate ?: constraints.today) },
        dayContent = { calendarDay, onOutOfMonthDateClick ->
            val dayState = resolveDayRangeState(
                calendarDay = calendarDay,
                today = constraints.today,
                selectedRange = normalizedRange,
                startMonth = constraints.startMonth,
                endMonth = constraints.endMonth,
            )

            FinsibleCalendarDay(
                date = calendarDay.date,
                state = dayState,
                onClick = { clickedDate ->
                    onRangeSelected(
                        calculateNextDateRange(
                            current = normalizedRange,
                            clickedDate = clickedDate,
                        ),
                    )
                    if (!dayState.isInCurrentMonth) {
                        onOutOfMonthDateClick(clickedDate)
                    }
                },
                colors = colors,
                sizes = sizes,
                typography = typography,
                shapes = shapes,
            )
        },
    )
}