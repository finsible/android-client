package com.itsjeel01.finsiblefrontend.ui.view.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.view.components.NewTransactionAmountTextField
import com.itsjeel01.finsiblefrontend.ui.view.components.NewTransactionDatePicker
import com.itsjeel01.finsiblefrontend.ui.view.components.TransactionSegmentedControl

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewTransactionForm() {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val outerPadding = (0.02 * screenHeight).dp
    val horizontalInnerPadding = (0.025 * screenWidth).dp
    val verticalInnerTopPadding = (0.001 * screenHeight).dp
    val inputFieldSpacing = (screenWidth * 0.05).dp

    Scaffold { _ ->
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(all = outerPadding)
        ) {
            // Transaction type selector
            TransactionSegmentedControl(screenWidth = screenWidth)

            // Form fields container
            Column(
                modifier = Modifier.padding(
                    horizontal = horizontalInnerPadding,
                    vertical = verticalInnerTopPadding
                )
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(inputFieldSpacing)) {
                    NewTransactionAmountTextField(modifier = Modifier.weight(1f))
                    NewTransactionDatePicker(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
