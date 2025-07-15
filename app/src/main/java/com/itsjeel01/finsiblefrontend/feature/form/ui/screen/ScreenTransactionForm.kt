package com.itsjeel01.finsiblefrontend.feature.form.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.data.model.TransactionType
import com.itsjeel01.finsiblefrontend.feature.form.ui.component.NewTransactionAmountTextField
import com.itsjeel01.finsiblefrontend.feature.form.ui.component.NewTransactionDatePicker
import com.itsjeel01.finsiblefrontend.feature.form.ui.component.TransactionCategoryDropdown
import com.itsjeel01.finsiblefrontend.feature.form.ui.component.TransactionSegmentedControl
import com.itsjeel01.finsiblefrontend.feature.form.viewmodel.TransactionFormViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewTransactionForm() {
    // Get screen dimensions
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    // Calculate paddings based on screen dimensions
    val contentPadding = (0.02 * screenHeight).dp
    val formPadding = (0.025 * screenWidth).dp
    val fieldSpacing = (screenHeight * 0.01).dp
    val fieldRowSpacing = (screenWidth * 0.05).dp

    val viewModel: TransactionFormViewModel = hiltViewModel()
    val transactionType = viewModel.transactionTypeState.collectAsState().value

    Scaffold {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(all = contentPadding)
        ) {
            // Transaction type selector
            TransactionSegmentedControl(screenWidth = screenWidth)

            // Form fields container
            Column(
                modifier = Modifier.padding(
                    horizontal = formPadding,
                    vertical = (0.001 * screenHeight).dp
                ),
                verticalArrangement = Arrangement.spacedBy(fieldSpacing)
            ) {
                // Amount and date fields in a row
                Row(horizontalArrangement = Arrangement.spacedBy(fieldRowSpacing)) {
                    val equalWeightModifier = Modifier.weight(1f)
                    NewTransactionAmountTextField(equalWeightModifier)
                    NewTransactionDatePicker(equalWeightModifier)
                }

                // Category dropdown
                if (transactionType != TransactionType.TRANSFER) TransactionCategoryDropdown()
            }
        }
    }
}