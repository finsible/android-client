package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.ui.theme.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel

@Composable
fun NewTransactionDatePicker(modifier: Modifier = Modifier) {
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionDate = newTransactionFormViewModel.transactionDateState.collectAsState().value
    val transactionType = newTransactionFormViewModel.transactionTypeState.collectAsState().value
    val transactionColor = getTransactionColor(type = transactionType)

    FinsibleDatePicker(
        value = transactionDate,
        onDateSelected = { newTransactionFormViewModel.setTransactionDate(it) },
        label = "Date",
        accentColor = transactionColor,
        size = DatePickerSize.SMALL,
        modifier = modifier.fillMaxWidth()
    )
}