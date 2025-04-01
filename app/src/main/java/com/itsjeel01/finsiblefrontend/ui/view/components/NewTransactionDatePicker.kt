package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.view.InputCommonProps
import com.itsjeel01.finsiblefrontend.ui.view.InputFieldSize
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel

@Composable
fun NewTransactionDatePicker(modifier: Modifier = Modifier) {
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionDate = newTransactionFormViewModel.transactionDateState.collectAsState().value
    val transactionType = newTransactionFormViewModel.transactionTypeState.collectAsState().value

    val focusManager = LocalFocusManager.current

    // Configure input field properties
    val inputProps = InputCommonProps(
        modifier = modifier,
        placeholder = "Date",
        enabled = true,
        accentColor = getTransactionColor(transactionType),
        trailingIcon = {
            Icon(
                painterResource(R.drawable.calendar_icon),
                contentDescription = "Calendar Icon",
                tint = MaterialTheme.colorScheme.outline
            )
        },
        size = InputFieldSize.Large,
    )

    fun onDateSelected(date: Long) {
        newTransactionFormViewModel.setTransactionDate(date)
    }

    FinsibleDateInput(
        date = transactionDate,
        onValueChange = { onDateSelected(it) },
        commonProps = inputProps,
        clearFocus = { focusManager.clearFocus() },
    )
}
