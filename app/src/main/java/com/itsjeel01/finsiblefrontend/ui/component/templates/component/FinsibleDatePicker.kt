package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.UserLocaleRegistry
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDatePickerDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerShapes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerTypography
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth

/**
 * A highly configurable semantic date picker component that adheres to Finsible Design System.
 *
 * @param onDateSelected Callback when a date is selected.
 * @param modifier Composable modifier.
 * @param selectedDate The currently selected date.
 * @param constraints Shared superset config consumed by all temporal pickers.
 * @param colors The resolved color styles for the date picker.
 * @param sizes The resolved size styles for the date picker.
 * @param typography The resolved typography styles for the date picker.
 * @param shapes The resolved shape styles for the date picker.
 */
@Composable
fun FinsibleDatePicker(
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    constraints: CalendarConstraints = CalendarConstraints(),
    colors: FinsibleDatePickerColors = FinsibleDatePickerDefaults.colors(),
    sizes: FinsibleDatePickerSizes = FinsibleDatePickerDefaults.sizes(),
    typography: FinsibleDatePickerTypography = FinsibleDatePickerDefaults.typography(),
    shapes: FinsibleDatePickerShapes = FinsibleDatePickerDefaults.shapes(),
) {
    val resolvedFirstVisibleMonth = constraints.resolveFirstVisibleMonth(selectedDate)

    val locale = UserLocaleRegistry.currentLocale()
    val resolvedFirstDayOfWeek = constraints.resolveFirstDayOfWeek(firstDayOfWeekFromLocale(locale))
    val selectedDateHeaderText = selectedDate?.toHeaderDateLabel(locale)
        ?: stringResource(R.string.finsible_date_picker_no_date_selected)
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
        onSelectedDateClickTargetMonth = { YearMonth.from(constraints.today) },
        dayContent = { calendarDay, onOutOfMonthDateClick ->
            val dayState = resolveDayState(
                calendarDay = calendarDay,
                today = constraints.today,
                selectedDate = selectedDate,
                startMonth = constraints.startMonth,
                endMonth = constraints.endMonth,
            )
            FinsibleCalendarDay(
                date = calendarDay.date,
                state = dayState,
                onClick = { clickedDate ->
                    onDateSelected(clickedDate)
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