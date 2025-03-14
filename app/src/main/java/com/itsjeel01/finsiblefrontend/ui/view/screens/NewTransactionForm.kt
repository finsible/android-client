package com.itsjeel01.finsiblefrontend.ui.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.data.TransactionType
import com.itsjeel01.finsiblefrontend.data.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.view.components.NewTransactionAmountTextField
import com.itsjeel01.finsiblefrontend.ui.view.components.NewTransactionDatePicker
import com.itsjeel01.finsiblefrontend.ui.view.components.TransactionCategoryDropdown
import com.itsjeel01.finsiblefrontend.ui.view.components.TransactionSegmentedControl
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel

@Composable
fun NewTransactionForm() {
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionType = newTransactionFormViewModel.transactionTypeState.collectAsState().value
    val transactionAmount =
        newTransactionFormViewModel.transactionAmountState.collectAsState().value

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = padding)
        ) {
            TransactionSegmentedControl()
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                NewTransactionAmountTextField(
                    initialAmount = transactionAmount,
                    onAmountChange = { amount ->
                        newTransactionFormViewModel.setTransactionAmount(amount)
                    },
                    accentColor = getTransactionColor(type = transactionType)
                )
                NewTransactionDatePicker()
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (transactionType != TransactionType.TRANSFER)
                        TransactionCategoryDropdown(modifier = Modifier.weight(1F))
                }
            }
        }
    }
}

@Composable
@Preview
fun NewTransactionFormPreview() {
    NewTransactionForm()
}