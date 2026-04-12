package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDateRangePicker
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleDatePickerDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDateRange
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.normalized
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun DateRangePickerDialog(
    initialStartMs: Long?,
    initialEndMs: Long?,
    onConfirm: (start: Long?, end: Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedRangeState = remember(initialStartMs, initialEndMs) {
        mutableStateOf(initialSelectedRange(initialStartMs, initialEndMs))
    }

    val currentSelection = selectedRangeState.value

    DatePickerDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        currentSelection.startDate?.toStartOfDayMillis(),
                        currentSelection.endDate?.toEndOfDayMillis()
                    )
                },
                colors = ButtonDefaults.textButtonColors().copy(containerColor = FinsibleTheme.colors.surfaceBright)
            ) {
                FinsibleText(
                    text = stringResource(R.string.date_range_dialog_ok),
                    variant = FinsibleTextVariant.SmallBodyMedium,
                    color = FinsibleTheme.colors.primaryContent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                FinsibleText(
                    text = stringResource(R.string.date_range_dialog_cancel),
                    variant = FinsibleTextVariant.SmallBodyMedium,
                    color = FinsibleTheme.colors.tertiaryContent
                )
            }
        }
    )
    {
        FinsibleDateRangePicker(
            selectedRange = currentSelection,
            onRangeSelected = { selectedRangeState.value = it },
            colors = FinsibleDatePickerDefaults.colors(
                containerColor = FinsibleTheme.colors.surface,
                monthHeaderColor = FinsibleTheme.colors.surface,
            ),
            sizes = FinsibleDatePickerDefaults.sizes().copy(
                containerElevation = FinsibleTheme.dimes.d0,
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

private fun initialSelectedRange(startMs: Long?, endMs: Long?): FinsibleDateRange {
    return FinsibleDateRange(
        startDate = startMs?.toLocalDate(),
        endDate = endMs?.toLocalDate(),
    ).normalized()
}

private fun Long.toLocalDate(): LocalDate = Instant.ofEpochMilli(this).atOffset(ZoneOffset.UTC).toLocalDate()

private fun LocalDate.toStartOfDayMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun LocalDate.toEndOfDayMillis(): Long =
    plusDays(1).atStartOfDay(ZoneOffset.UTC).minus(Duration.ofMillis(1)).toInstant().toEpochMilli()


