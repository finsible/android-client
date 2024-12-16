@file:OptIn(ExperimentalMaterial3Api::class)

package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.viewmodel.DashboardViewModel

@Composable
fun NewTransactionForm(dashboardViewModel: DashboardViewModel) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            dashboardViewModel.hideModalSheet()
        },
        sheetState = sheetState
    ) {
    }
}