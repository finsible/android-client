package com.itsjeel01.finsiblefrontend.ui.view.components

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
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerDialogColors
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.theme.sansSerifFont
import com.itsjeel01.finsiblefrontend.ui.view.InputCommonProps
import com.itsjeel01.finsiblefrontend.utils.FSUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FinsibleDateInput(
    date: Long,
    onValueChange: (Long) -> Unit,
    clearFocus: () -> Unit,
    commonProps: InputCommonProps = InputCommonProps(),
) {
    val accentColor = commonProps.accentColor
    var showModal by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(convertMillisToDate(date)) }

    TextField(
        modifier = commonProps
            .fieldModifier()
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    showModal = true
                }
            },
        label = commonProps.labelComposable(),
        value = value,
        onValueChange = {},
        readOnly = true,
        isError = commonProps.isError,
        textStyle = commonProps.primaryTextStyle(),
        placeholder = commonProps.placeholderComposable(),
        leadingIcon = commonProps.leadingIconComposable(),
        trailingIcon = commonProps.trailingIconComposable(),
        supportingText = commonProps.supportingTextComposable(),
        colors = finsibleTextFieldColors(accentColor),
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                it?.let { date ->
                    onValueChange(date)
                    value = convertMillisToDate(date)
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

fun convertMillisToDate(millis: Long): String {
    val date = Date(millis)
    val now = Date()
    val diffInMillis = now.time - millis
    val diffInHours = diffInMillis / (1000 * 60 * 60)
    val diffInDays = diffInHours / 24

    return when {
        diffInDays == 0L && diffInHours < 24 -> "Today"
        diffInDays == 1L -> "Yesterday"
        diffInDays in 2..7 -> SimpleDateFormat("dd/MM (EEE)", Locale.getDefault()).format(date)
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    date: Long,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    accentColor: Color,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = FSUtils.convertToUTCMillis(date))
    val confirmButtonComposable: @Composable (() -> Unit) = {
        TextButton(
            onClick = {
                onDateSelected(FSUtils.convertToLocalMillis(datePickerState.selectedDateMillis!!))
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
    val dismissButtonComposable: @Composable (() -> Unit) = {
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

    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(fontFamily = sansSerifFont)) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            colors = finsibleDatePickerDialogColors(),
            confirmButton = confirmButtonComposable,
            dismissButton = dismissButtonComposable
        ) {
            DatePicker(
                state = datePickerState,
                colors = finsibleDatePickerColors(accentColor = accentColor),
                showModeToggle = true,
            )
        }
    }
}