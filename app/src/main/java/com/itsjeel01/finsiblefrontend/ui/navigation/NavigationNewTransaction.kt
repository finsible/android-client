package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.screen.NewTransactionScreen
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel

@Composable
fun NavigationNewTransaction(
    viewModel: NewTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect the new mapped/Top-K flows
    val categoriesMap by viewModel.categoriesMap.collectAsStateWithLifecycle()
    val topKCategories by viewModel.topKCategories.collectAsStateWithLifecycle()
    val topKFromAccounts by viewModel.topKFromAccounts.collectAsStateWithLifecycle()
    val topKToAccounts by viewModel.topKToAccounts.collectAsStateWithLifecycle()
    val availableCurrencies by viewModel.availableCurrencies.collectAsStateWithLifecycle()

    NewTransactionScreen(
        state = state,
        categoriesMap = categoriesMap, // Passed for the Bottom Sheet
        categories = topKCategories,   // Passed for the inline row
        fromAccounts = topKFromAccounts,
        toAccounts = topKToAccounts,
        currencyFormatter = viewModel.currencyFormatter,
        currencyRepository = viewModel.currencyRepository,
        availableCurrencies = availableCurrencies,
        onEvent = { event: NewTransactionUiEvent -> viewModel.onEvent(event) },
        onSave = {
            viewModel.submit(
                addMore = false,
                onSuccess = onNavigateBack,
                onError = { /* TODO: Show error notification */ }
            )
        },
        onSaveAndAddMore = {
            viewModel.submit(
                addMore = true,
                onSuccess = {},
                onError = { /* TODO: Show error notification */ }
            )
        },
        onClose = onNavigateBack
    )
}