package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.data.model.Currency
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.sheet.CategorySelectionSheetContent
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.sheet.CurrencySheetContent
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.sheet.DatePickerSheetContent
import com.itsjeel01.finsiblefrontend.ui.component.newtransaction.sheet.SelectAccountBottomSheetContent
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleBottomSheet
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleBottomSheetDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionSheetMode
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTransactionBottomSheetHost(
    state: NewTransactionFormState,
    categories: Map<CategoryUIModel, List<CategoryUIModel>>,
    fromAccounts: List<AccountUIModel>,
    toAccounts: List<AccountUIModel>,
    onEvent: (NewTransactionUiEvent) -> Unit,
    accentColor: Color,
    surfaceColor: Color,
    availableCurrencies: List<Currency>,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val mode = state.sheetMode

    LaunchedEffect(mode) {
        if (mode != null) {
            focusManager.clearFocus(force = true)
            keyboardController?.hide()
        }
    }

    val closeSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            onEvent(NewTransactionUiEvent.SheetModeChanged(null))
        }
    }

    if (mode != null) {
        val sheetTitle = when (mode) {
            NewTransactionSheetMode.CURRENCY -> "Select Currency"
            NewTransactionSheetMode.DATE_PICKER -> "Select Date"
            NewTransactionSheetMode.CATEGORY_EXPLORER -> "Select Category"
            NewTransactionSheetMode.ACCOUNT_FROM_SELECTOR -> "Select Source Account"
            NewTransactionSheetMode.ACCOUNT_TO_SELECTOR -> "Select Destination Account"
        }

        val fixedHeight: Float? = when (mode) {
            NewTransactionSheetMode.CURRENCY -> 0.6f // 60% of screen height for currency list
            else -> null
        }

        val closeButton: @Composable () -> Unit = {
            FinsibleButton(
                onClick = closeSheet,
                iconOnly = true,
                icon = {
                    Icon(
                        painter = painterResource(id = com.composables.icons.lucide.R.drawable.lucide_ic_x),
                        contentDescription = "Close",
                        modifier = Modifier.size(FinsibleTheme.spacing.insetXl)
                    )
                },
                variant = FinsibleButtonVariant.Text,
                shapeVariant = FinsibleShape.Circle,
                size = FinsibleSize.Medium
            )
        }
        FinsibleBottomSheet(
            onDismissRequest = { onEvent(NewTransactionUiEvent.SheetModeChanged(null)) },
            modifier = modifier,
            sheetState = sheetState,
            title = sheetTitle,
            trailingIcon = closeButton,
            colors = FinsibleBottomSheetDefaults.colors(accentColor = accentColor),
            fixedHeightPercent = fixedHeight,
            maxHeightPercent = 0.85f,
            scrollContent = false
        ) {
            when (mode) {
                NewTransactionSheetMode.CURRENCY -> {
                    CurrencySheetContent(
                        selectedCurrencyCode = state.currencyCode,
                        currencies = availableCurrencies,
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        onCurrencySelected = { currencyCode ->
                            onEvent(NewTransactionUiEvent.CurrencySelected(currencyCode))
                            closeSheet()
                        }
                    )
                }

                NewTransactionSheetMode.DATE_PICKER -> {
                    DatePickerSheetContent(
                        initialDateMillis = state.dateMillis,
                        accentColor = accentColor,
                        onDateSelected = { millis ->
                            onEvent(NewTransactionUiEvent.CustomDateSelected(millis))
                            closeSheet()
                        },
                        onDismiss = closeSheet
                    )
                }

                NewTransactionSheetMode.CATEGORY_EXPLORER -> {
                    CategorySelectionSheetContent(
                        categories = categories,
                        selectedCategoryId = state.categoryId,
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        onCategorySelected = { categoryId ->
                            onEvent(NewTransactionUiEvent.CategorySelected(categoryId))
                            closeSheet()
                        },
                    )
                }

                NewTransactionSheetMode.ACCOUNT_FROM_SELECTOR -> {
                    SelectAccountBottomSheetContent(
                        accounts = fromAccounts,
                        selectedAccountId = state.fromAccountId,
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        onAccountSelected = { accountId ->
                            onEvent(NewTransactionUiEvent.FromAccountSelected(accountId))
                            closeSheet()
                        }
                    )
                }

                NewTransactionSheetMode.ACCOUNT_TO_SELECTOR -> {
                    SelectAccountBottomSheetContent(
                        accounts = toAccounts,
                        selectedAccountId = state.toAccountId,
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        onAccountSelected = { accountId ->
                            onEvent(NewTransactionUiEvent.ToAccountSelected(accountId))
                            closeSheet()
                        }
                    )
                }
            }
        }
    }
}