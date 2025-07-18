package com.itsjeel01.finsiblefrontend.ui.screen

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
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.TransactionAmountTextField
import com.itsjeel01.finsiblefrontend.ui.component.TransactionCategoryDropdown
import com.itsjeel01.finsiblefrontend.ui.component.TransactionDatePicker
import com.itsjeel01.finsiblefrontend.ui.component.TransactionTypeSegmentedControl
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormScreen() {

    // --- Screen dimensions and padding calculations ---

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val contentPadding = (0.02 * screenHeight).dp
    val formPadding = (0.025 * screenWidth).dp
    val fieldSpacing = (screenHeight * 0.01).dp
    val fieldRowSpacing = (screenWidth * 0.05).dp

    // --- ViewModel and state management ---

    val viewModel: TransactionFormViewModel = hiltViewModel()
    val transactionType = viewModel.transactionType.collectAsState().value

    // --- UI Composition ---

    Scaffold {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(all = contentPadding)
        ) {
            TransactionTypeSegmentedControl(screenWidth = screenWidth)

            Column(
                modifier = Modifier.padding(
                    horizontal = formPadding,
                    vertical = (0.001 * screenHeight).dp
                ),
                verticalArrangement = Arrangement.spacedBy(fieldSpacing)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(fieldRowSpacing)) {
                    val equalWeightModifier = Modifier.weight(1f)
                    TransactionAmountTextField(equalWeightModifier)
                    TransactionDatePicker(equalWeightModifier)
                }

                if (transactionType != TransactionType.TRANSFER) TransactionCategoryDropdown()
            }
        }
    }
}
