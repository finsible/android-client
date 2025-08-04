package com.itsjeel01.finsiblefrontend.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.TransactionAmountTextField
import com.itsjeel01.finsiblefrontend.ui.component.TransactionCategoryDropdown
import com.itsjeel01.finsiblefrontend.ui.component.TransactionDatePicker
import com.itsjeel01.finsiblefrontend.ui.component.TransactionTypeSegmentedControl
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingAll
import com.itsjeel01.finsiblefrontend.ui.theme.dime.screenPadding
import com.itsjeel01.finsiblefrontend.ui.theme.dime.size
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormScreen() {
    val dims = appDimensions()

    // --- ViewModel and state management ---

    val viewModel: TransactionFormViewModel = hiltViewModel()
    val transactionType = viewModel.transactionType.collectAsState().value

    // --- UI Composition ---

    Scaffold {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .screenPadding()
        ) {
            TransactionTypeSegmentedControl()

            Column(
                modifier = Modifier.paddingAll(Size.S12),
                verticalArrangement = Arrangement.spacedBy(dims.size(Size.S8))
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(dims.size(Size.S8))) {
                    val equalWeightModifier = Modifier.weight(1f)
                    TransactionAmountTextField(equalWeightModifier)
                    TransactionDatePicker(equalWeightModifier)
                }

                if (transactionType != TransactionType.TRANSFER) TransactionCategoryDropdown()
            }
        }
    }
}
