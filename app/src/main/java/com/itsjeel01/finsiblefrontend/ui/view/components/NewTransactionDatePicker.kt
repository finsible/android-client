package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerColors
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleDatePickerDialogColors
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NewTransactionDatePicker(modifier: Modifier = Modifier) {
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionDate = getTransactionDate()
    var showModal by remember { mutableStateOf(false) }
    val transactionColor = getTransactionColor()

    TextField(
        value = convertMillisToDate(transactionDate),
        onValueChange = { },
        colors = finsibleTextFieldColors(accentColor = transactionColor),
        label = { Text("Date") },
        placeholder = { Text("DD/MM/YYYY") },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.calendar_icon),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                contentDescription = "Select Date"
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(transactionDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { it?.let { date -> newTransactionFormViewModel.setTransactionDate(date) } },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    val transactionDate = getTransactionDate()
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = transactionDate)
    val transactionColor = getTransactionColor()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = finsibleDatePickerDialogColors(),
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                },
                colors = ButtonDefaults.textButtonColors().copy(
                    containerColor = transactionColor.copy(alpha = 0.5F),
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = finsibleDatePickerColors(accentColor = transactionColor)
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun getTransactionColor(): Color {
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionType = newTransactionFormViewModel.transactionTypeState.collectAsState().value
    val transactionColor = getTransactionColor(type = transactionType)

    return transactionColor
}

@Composable
fun getTransactionDate(): Long {
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionDate = newTransactionFormViewModel.transactionDateState.collectAsState().value

    return transactionDate
}