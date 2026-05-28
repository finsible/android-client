package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDatePicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDateRangePicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleMonthYearPicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDateRange
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleMonthYear
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import java.time.LocalDate
import java.time.YearMonth

@Preview(name = "Date Picker Light", showBackground = true, widthDp = 420, heightDp = 3000)
@Preview(name = "Date Picker Dark", showBackground = true, widthDp = 420, heightDp = 3000, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleDatePickerPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedRange by remember { mutableStateOf(FinsibleDateRange.Empty) }
    var selectedMonthYear by remember { mutableStateOf(FinsibleMonthYear.from(YearMonth.now())) }
    var displayYear by remember { mutableIntStateOf(selectedMonthYear.year) }

    FinsibleComponentPreviewScaffold {
        val s = FinsibleTheme.colors

        FinsibleText(
            text = "Finsible Date Picker",
            textStyle = FinsibleTheme.typography.displaySm.bold(),
            color = s.brandInteractive,
        )

        HorizontalDivider(color = s.borderSubtle)

        FinsiblePreviewSection(title = "Single Date") {
            FinsibleDatePicker(
                onDateSelected = { selectedDate = it },
                selectedDate = selectedDate,
            )
        }

        FinsiblePreviewSection(title = "Date Range") {
            FinsibleDateRangePicker(
                onRangeSelected = { selectedRange = it },
                selectedRange = selectedRange,
            )
        }

        FinsiblePreviewSection(title = "Month / Year Coverage Matrix") {
            Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)) {
                listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large).forEach { size ->
                    MonthYearPreviewRow(
                        selectedMonthYear = selectedMonthYear,
                        displayYear = displayYear,
                        onMonthYearSelected = { selectedMonthYear = it },
                        onDisplayYearChange = { displayYear = it },
                        size = size,
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthYearPreviewRow(
    selectedMonthYear: FinsibleMonthYear,
    displayYear: Int,
    onMonthYearSelected: (FinsibleMonthYear) -> Unit,
    onDisplayYearChange: (Int) -> Unit,
    size: FinsibleSize,
) {
    val s = FinsibleTheme.colors

    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackXs)) {

        FinsibleText(
            text = size.name,
            textStyle = FinsibleTheme.typography.bodySm,
            color = s.contentSecondary,
        )

        FinsibleMonthYearPicker(
            onMonthYearSelected = onMonthYearSelected,
            selectedMonthYear = selectedMonthYear,
            constraints = CalendarConstraints(displayYear = displayYear),
            onDisplayYearChange = onDisplayYearChange,
            size = size,
        )
    }
}