package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel
import com.itsjeel01.finsiblefrontend.ui.component.base.FinsibleDateInput
import com.itsjeel01.finsiblefrontend.ui.component.base.InputCommonProps
import com.itsjeel01.finsiblefrontend.ui.component.base.InputFieldSize
import com.itsjeel01.finsiblefrontend.ui.theme.getTransactionColor

@Composable
fun NewTransactionDatePicker(modifier: Modifier = Modifier) {
    val transactionFormViewModel: TransactionFormViewModel = hiltViewModel()
    val transactionDate = transactionFormViewModel.transactionDateState.collectAsState().value
        ?: System.currentTimeMillis()
    val transactionType = transactionFormViewModel.transactionTypeState.collectAsState().value

    val focusManager = LocalFocusManager.current

    // Configure input field properties
    val inputProps = InputCommonProps(
        modifier = modifier,
        placeholder = "Date",
        enabled = true,
        accentColor = getTransactionColor(transactionType),
        trailingIcon = {
            Icon(
                painterResource(R.drawable.ic_calendar),
                contentDescription = "Calendar Icon",
                tint = MaterialTheme.colorScheme.outline
            )
        },
        size = InputFieldSize.Large,
    )

    fun onDateSelected(date: Long) {
        transactionFormViewModel.setTransactionDate(date)
    }

    FinsibleDateInput(
        date = transactionDate,
        onValueChange = { onDateSelected(it) },
        commonProps = inputProps,
        clearFocus = { focusManager.clearFocus() },
    )
}
