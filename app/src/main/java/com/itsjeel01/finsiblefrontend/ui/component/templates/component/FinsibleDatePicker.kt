package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.di.hiltUserLocale
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDatePickerDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDatePickerSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * A highly configurable semantic date picker component that adheres to Finsible Design System.
 *
 * @param onDateSelected Callback when a date is selected.
 * @param modifier Composable modifier.
 * @param selectedDate The currently selected date.
 * @param startMonth The first month that can be selected.
 * @param endMonth The last month that can be selected.
 * @param firstVisibleMonth The first month that is visible.
 * @param today The current date.
 * @param firstDayOfWeek The first day of the week.
 * @param enabledDatePredicate A predicate that determines whether a date is enabled.
 * @param colors The resolved color styles for the date picker.
 * @param sizes The resolved size styles for the date picker.
 */
@Composable
fun FinsibleDatePicker(
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    startMonth: YearMonth = YearMonth.now().minusYears(10),
    endMonth: YearMonth = YearMonth.now().plusYears(10),
    firstVisibleMonth: YearMonth = selectedDate?.let { YearMonth.from(it) } ?: YearMonth.now(),
    today: LocalDate = LocalDate.now(),
    firstDayOfWeek: DayOfWeek? = null,
    enabledDatePredicate: (LocalDate) -> Boolean = { true },
    colors: FinsibleDatePickerColors = FinsibleDatePickerDefaults.colors(),
    sizes: FinsibleDatePickerSizes = FinsibleDatePickerDefaults.sizes(),
) {
    require(!endMonth.isBefore(startMonth)) { "endMonth must be on or after startMonth." }
    require(!firstVisibleMonth.isBefore(startMonth) && !firstVisibleMonth.isAfter(endMonth)) {
        "firstVisibleMonth must be in the [startMonth, endMonth] range."
    }

    val locale = hiltUserLocale()
    val resolvedFirstDayOfWeek = firstDayOfWeek ?: firstDayOfWeekFromLocale(locale)
    val selectedDateHeaderText = selectedDate?.toHeaderDateLabel(locale)
        ?: stringResource(R.string.finsible_date_picker_no_date_selected)
    val coroutineScope = rememberCoroutineScope()
    var showMonthYearPicker by remember { mutableStateOf(false) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = firstVisibleMonth,
        firstDayOfWeek = resolvedFirstDayOfWeek,
    )
    val visibleMonth by remember(calendarState) {
        derivedStateOf { calendarState.firstVisibleMonth.yearMonth }
    }
    var displayYear by remember { mutableIntStateOf(visibleMonth.year) }

    LaunchedEffect(visibleMonth, showMonthYearPicker) {
        if (!showMonthYearPicker) displayYear = visibleMonth.year
    }

    BaseCalendarContainer(modifier = modifier, colors = colors, sizes = sizes) {
        FinsibleCalendarNavigationHeader(
            selectedDateText = selectedDateHeaderText,
            visibleMonth = visibleMonth,
            startMonth = startMonth,
            endMonth = endMonth,
            locale = locale,
            colors = colors,
            sizes = sizes,
            onPreviousMonth = {
                coroutineScope.launch { calendarState.animateScrollToMonth(visibleMonth.minusMonths(1)) }
            },
            onNextMonth = {
                coroutineScope.launch { calendarState.animateScrollToMonth(visibleMonth.plusMonths(1)) }
            },
            onMonthYearClick = {
                showMonthYearPicker = !showMonthYearPicker
                displayYear = visibleMonth.year
            },
            onSelectedDateClick = {
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(
                        clampToRange(YearMonth.from(today), startMonth, endMonth),
                    )
                }
            },
        )

        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = tween(Duration.MS_200.toInt()),
            ),
        ) {
            if (showMonthYearPicker) {
                FinsibleMonthYearGridPicker(
                    onMonthYearSelected = { monthYear ->
                        val resolvedMonth = monthYear.toYearMonthOrNull() ?: return@FinsibleMonthYearGridPicker
                        val targetMonth = clampToRange(resolvedMonth, startMonth, endMonth)
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
                )
            } else {
                FinsibleCalendarWeekHeader(
                    firstDayOfWeek = resolvedFirstDayOfWeek,
                    locale = locale,
                    colors = colors,
                    sizes = sizes,
                )

                HorizontalCalendar(
                    state = calendarState,
                    dayContent = { calendarDay ->
                        val dayState = resolveDayState(
                            calendarDay = calendarDay,
                            today = today,
                            selectedDate = selectedDate,
                            enabledDatePredicate = enabledDatePredicate,
                        )
                        FinsibleCalendarDay(
                            date = calendarDay.date,
                            state = dayState,
                            onClick = onDateSelected,
                            colors = colors,
                            sizes = sizes,
                        )
                    },
                )
            }
        }
    }
}