package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NewTransactionForm() {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(paddingValues = padding)) {
            TransactionSegmentedControl()
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                NewTransactionDatePicker()
            }
        }
    }
}

@Composable
@Preview
fun NewTransactionFormPreview() {
    NewTransactionForm()
}