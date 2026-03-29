package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

@Immutable
data class FinsibleDateRange(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
) {
    companion object {
        val Empty = FinsibleDateRange()
    }
}

@Immutable
data class FinsibleMonthYear(
    val year: Int,
    val month: Month?,
) {
    fun toYearMonthOrNull(): YearMonth? = month?.let { YearMonth.of(year, it) }

    fun toYearMonth(): YearMonth {
        val resolvedMonth = requireNotNull(month) {
            "Month is required to convert FinsibleMonthYear to YearMonth."
        }
        return YearMonth.of(year, resolvedMonth)
    }

    companion object {
        fun from(yearMonth: YearMonth): FinsibleMonthYear {
            return FinsibleMonthYear(year = yearMonth.year, month = yearMonth.month)
        }
    }
}

@Immutable
data class FinsibleDatePickerColors(
    val containerColor: Color,
    val monthHeaderColor: Color,
    val monthHeaderContentColor: Color,
    val weekdayContentColor: Color,
    val dayContentColor: Color,
    val outOfMonthDayContentColor: Color,
    val disabledDayContentColor: Color,
    val selectedDayContainerColor: Color,
    val selectedDayContentColor: Color,
    val inRangeDayContainerColor: Color,
    val inRangeDayContentColor: Color,
    val todayBorderColor: Color,
)

@Immutable
data class FinsibleDatePickerSizes(
    val containerCornerRadius: Dp,
    val contentPadding: Dp,
    val monthHeaderVerticalPadding: Dp,
    val weekdayHeaderBottomPadding: Dp,
    val contentVerticalSpacing: Dp,
    val monthGridHorizontalSpacing: Dp,
    val daysVerticalSpacing: Dp,
    val dayCellSize: Dp,
    val dayCellCornerRadius: Dp,
    val monthYearPickerSpacing: Dp,
)

fun FinsibleDateRange.normalized(): FinsibleDateRange {
    if (startDate == null || endDate == null) {
        return this
    }
    return if (startDate <= endDate) this else copy(startDate = endDate, endDate = startDate)
}

fun calculateNextDateRange(
    current: FinsibleDateRange,
    clickedDate: LocalDate,
    enabledDatePredicate: (LocalDate) -> Boolean = { true },
): FinsibleDateRange {
    if (!enabledDatePredicate(clickedDate)) {
        return current
    }

    val startDate = current.startDate
    val endDate = current.endDate

    if (startDate == null || endDate != null) {
        return FinsibleDateRange(startDate = clickedDate, endDate = null)
    }

    if (clickedDate < startDate) {
        return FinsibleDateRange(startDate = clickedDate, endDate = startDate)
    }

    if (clickedDate == startDate) {
        return FinsibleDateRange(startDate = clickedDate, endDate = clickedDate)
    }

    return FinsibleDateRange(startDate = startDate, endDate = clickedDate)
}