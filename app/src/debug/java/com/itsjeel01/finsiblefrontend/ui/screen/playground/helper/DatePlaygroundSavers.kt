package com.itsjeel01.finsiblefrontend.ui.screen.playground.helper

import androidx.compose.runtime.saveable.mapSaver
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDateRange
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import java.time.LocalDate
import java.time.Month

internal val finsibleDateRangeSaver = mapSaver(
    save = { range ->
        mapOf(
            "start" to range.startDate?.toEpochDay(),
            "end" to range.endDate?.toEpochDay()
        )
    },
    restore = { saved ->
        FinsibleDateRange(
            startDate = (saved["start"] as? Number)?.toLong()?.let(LocalDate::ofEpochDay),
            endDate = (saved["end"] as? Number)?.toLong()?.let(LocalDate::ofEpochDay)
        )
    }
)

internal val finsibleMonthYearSaver = mapSaver(
    save = { monthYear ->
        mapOf(
            "year" to monthYear.year,
            "month" to monthYear.month?.value
        )
    },
    restore = { saved ->
        val year = (saved["year"] as? Number)?.toInt() ?: return@mapSaver null
        val monthValue = (saved["month"] as? Number)?.toInt()
        FinsibleMonthYear(year = year, month = monthValue?.let(Month::of))
    }
)
