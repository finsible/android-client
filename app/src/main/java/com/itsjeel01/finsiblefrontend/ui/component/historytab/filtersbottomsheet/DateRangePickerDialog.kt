package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleDateRangePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    initialStartMs: Long?,
    initialEndMs: Long?,
    onConfirm: (start: Long?, end: Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val pickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartMs,
        initialSelectedEndDateMillis = initialEndMs
    )

    FinsibleDateRangePickerDialog(
        pickerState = pickerState,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}