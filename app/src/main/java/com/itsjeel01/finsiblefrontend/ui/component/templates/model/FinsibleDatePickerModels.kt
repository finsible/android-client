package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.Locale

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
    val dayContainerColor: Color,
    val dayContentColor: Color,
    val outOfMonthDayContentColor: Color,
    val disabledDayContentColor: Color,
    val selectedDayContainerColor: Color,
    val selectedDayContentColor: Color,
    val inRangeDayContainerColor: Color,
    val inRangeDayContentColor: Color,
    val todayBorderColor: Color,
    val disabledMonthOpacity: Float,
)

@Immutable
data class FinsibleDatePickerSizes(
    val containerElevation: Dp,
    val containerCornerRadius: Dp,
    val contentPadding: Dp,
    val monthHeaderVerticalPadding: Dp,
    val monthHeaderCornerRadius: Dp,
    val weekdayHeaderBottomPadding: Dp,
    val contentVerticalSpacing: Dp,
    val monthGridHorizontalSpacing: Dp,
    val daysVerticalSpacing: Dp,
    val dayCellSize: Dp,
    val dayCellInnerSpacing: Dp,
    val dayCellCornerRadius: Dp,
    val monthGridItemCornerRadius: Dp,
    val monthYearPickerSpacing: Dp,
    val todayBorderWidth: Dp,
)

@Immutable
data class FinsibleDatePickerTypography(
    val weekdayTextVariant: FinsibleTextVariant,
    val daySelectedTextVariant: FinsibleTextVariant,
    val dayDefaultTextVariant: FinsibleTextVariant,
    val monthGridYearTextVariant: FinsibleTextVariant,
    val monthGridItemTextVariant: FinsibleTextVariant,
)

@Immutable
data class FinsibleDatePickerShapes(
    val navigationIconButtonShape: FinsibleShape,
    val dayShape: FinsibleShape,
    val monthGridItemShape: FinsibleShape,
)

@Immutable
data class CalendarConstraints(
    val startMonth: YearMonth = YearMonth.now().minusYears(10),
    val endMonth: YearMonth = YearMonth.now().plusYears(10),
    val firstVisibleMonth: YearMonth? = null,
    val today: LocalDate = LocalDate.now(),
    val firstDayOfWeek: DayOfWeek? = null,
    val displayYear: Int = YearMonth.now().year,
    val yearRange: IntRange =
        (displayYear - DEFAULT_YEAR_WINDOW) ..
                (displayYear + DEFAULT_YEAR_WINDOW),
    val availableMonths: List<Month> = Month.entries,
    val includeAllMonthsOption: Boolean = true,
    val locale: Locale? = null,
) {
    init {
        require(!endMonth.isBefore(startMonth)) { "endMonth must be on or after startMonth." }
        require(!yearRange.isEmpty()) { "yearRange must not be empty." }
        require(displayYear in yearRange) { "displayYear must be inside yearRange." }
        require(availableMonths.isNotEmpty()) { "availableMonths must not be empty." }
        require(availableMonths.distinct().size == availableMonths.size) {
            "availableMonths must not contain duplicate months."
        }
    }

    fun resolveFirstVisibleMonth(selectedDate: LocalDate? = null, fallbackMonth: YearMonth? = null): YearMonth {
        val fallback = fallbackMonth ?: selectedDate?.let(YearMonth::from) ?: YearMonth.from(today)
        return (firstVisibleMonth ?: fallback).coerceIn(startMonth, endMonth)
    }

    fun resolveFirstDayOfWeek(defaultFirstDayOfWeek: DayOfWeek): DayOfWeek {
        return firstDayOfWeek ?: defaultFirstDayOfWeek
    }

    fun resolveLocale(fallbackLocale: Locale): Locale = locale ?: fallbackLocale

    fun validateSelection(selectedMonthYear: FinsibleMonthYear) {
        require(selectedMonthYear.year in yearRange) {
            "selectedMonthYear.year must be inside yearRange."
        }
        require(selectedMonthYear.month == null || selectedMonthYear.month in availableMonths) {
            "selectedMonthYear.month must be part of availableMonths when provided."
        }
        require(includeAllMonthsOption || selectedMonthYear.month != null) {
            "selectedMonthYear.month cannot be null when includeAllMonthsOption is false."
        }
    }

    companion object {
        const val DEFAULT_YEAR_WINDOW: Int = 20
        const val ID_ALL_MONTHS: String = "ALL_MONTHS"
    }
}

fun FinsibleDateRange.normalized(): FinsibleDateRange {
    if (startDate == null || endDate == null) return this
    return if (startDate > endDate) copy(startDate = endDate, endDate = startDate) else this
}

fun calculateNextDateRange(
    current: FinsibleDateRange,
    clickedDate: LocalDate,
): FinsibleDateRange {

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