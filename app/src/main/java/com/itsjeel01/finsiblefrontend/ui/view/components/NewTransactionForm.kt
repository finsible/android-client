@file:OptIn(ExperimentalMaterial3Api::class)

package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.data.TransactionType
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel
import java.util.Locale

@Composable
fun NewTransactionForm() {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(paddingValues = padding)) {
            TransactionSegmentedControl()
        }
    }
}

@Composable
@Preview
fun NewTransactionFormPreview() {
    NewTransactionForm()
}