package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Themed date range picker dialog with Finsible brand colors and standard confirm/dismiss buttons. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinsibleDateRangePickerDialog(
    pickerState: DateRangePickerState,
    onConfirm: (start: Long?, end: Long?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.fillMaxWidth(),
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        pickerState.selectedStartDateMillis,
                        pickerState.selectedEndDateMillis
                    )
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = FinsibleTheme.colors.surfaceBright
                )
            ) {
                Text(
                    text = stringResource(R.string.date_range_dialog_ok),
                    color = FinsibleTheme.colors.primaryContent
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.date_range_dialog_cancel),
                    color = FinsibleTheme.colors.tertiaryContent
                )
            }
        }
    ) {
        DateRangePicker(
            state = pickerState,
            showModeToggle = false,
            colors = DatePickerDefaults.colors().copy(
                containerColor = FinsibleTheme.colors.surface,
                selectedDayContainerColor = FinsibleTheme.colors.inverse,
                todayContentColor = FinsibleTheme.colors.primaryContent,
                todayDateBorderColor = FinsibleTheme.colors.primaryContent,
                dayContentColor = FinsibleTheme.colors.primaryContent,
                weekdayContentColor = FinsibleTheme.colors.secondaryContent,
                yearContentColor = FinsibleTheme.colors.primaryContent
            ),
            title = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = FinsibleTheme.dimes.d12)
        )
    }
}
