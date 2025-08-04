package com.itsjeel01.finsiblefrontend.ui.component.base

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.itsjeel01.finsiblefrontend.common.Utils
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerDialogColors
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.theme.sansSerifFont

/** BaseDateInput composable for displaying a read-only date field with a modal date picker. */
@Composable
fun BaseDateInput(
    date: Long,
    onValueChange: (Long) -> Unit,
    clearFocus: () -> Unit,
    commonProps: CommonProps = CommonProps(),
) {

    // --- State and Properties ---

    val accentColor = commonProps.accentColor
    var showModal by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(Utils.convertMillisToDate(date)) }

    // --- DateInput UI ---

    TextField(
        modifier = commonProps
            .commonModifier()
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    showModal = true
                }
            },
        label = commonProps.label(),
        value = value,
        onValueChange = {},
        readOnly = true,
        isError = commonProps.isError,
        textStyle = commonProps.primaryTextStyle(),
        placeholder = commonProps.placeholder(),
        leadingIcon = commonProps.leadingIconComposable(),
        trailingIcon = commonProps.trailingIconComposable(),
        supportingText = commonProps.supportingText(),
        colors = finsibleTextFieldColors(accentColor),
    )

    // --- Date Picker Modal ---

    if (showModal) {
        BaseDatePickerModal(
            onDateSelected = {
                it?.let { date ->
                    onValueChange(date)
                    value = Utils.convertMillisToDate(date)
                    clearFocus()
                }
            },
            onDismiss = {
                showModal = false
                clearFocus()
            },
            date = date,
            accentColor = accentColor,
        )
    }
}

/** Modal dialog composable for picking a date. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseDatePickerModal(
    date: Long,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    accentColor: Color,
) {

    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = Utils.convertToUTCMillis(date))

    // --- Date Picker Buttons UI ---

    val confirmButton: @Composable (() -> Unit) = {
        TextButton(
            onClick = {
                onDateSelected(Utils.convertToLocalMillis(datePickerState.selectedDateMillis!!))
                onDismiss()
            },
            colors = ButtonDefaults.textButtonColors().copy(
                containerColor = accentColor.copy(alpha = 0.5F),
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                "OK",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = sansSerifFont)
            )
        }
    }

    val dismissButton: @Composable (() -> Unit) = {
        TextButton(
            onClick = onDismiss,
            colors = ButtonDefaults.textButtonColors().copy(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                "Cancel",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = sansSerifFont)
            )
        }
    }

    // --- Date Picker Dialog ---

    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
            fontFamily = sansSerifFont
        )
    ) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            colors = finsibleDatePickerDialogColors(),
            confirmButton = confirmButton,
            dismissButton = dismissButton
        ) {
            DatePicker(
                state = datePickerState,
                colors = finsibleDatePickerColors(accentColor = accentColor),
                showModeToggle = true,
            )
        }
    }
}
