package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.ui.theme.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.view.InputCommonProps
import com.itsjeel01.finsiblefrontend.ui.view.InputFieldSize
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel
import com.itsjeel01.finsiblefrontend.utils.AppConstants

@Composable
fun NewTransactionAmountTextField(modifier: Modifier) {
    val errorMaxAmount = "Max amount: ${AppConstants.MAX_TRANSACTION_AMOUNT}"
    val viewModel: NewTransactionFormViewModel = hiltViewModel()

    // Collect state values
    val transactionType = viewModel.transactionTypeState.collectAsState().value
    val transactionAmount = viewModel.transactionAmountState.collectAsState().value

    // Local UI state
    val focusManager = LocalFocusManager.current
    var inputText by remember { mutableStateOf(transactionAmount?.toString() ?: "") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Amount validation pattern - allows currency format with up to 2 decimal places
    val currencyPattern = Regex("^([1-9]\\d*(\\.\\d{0,2})?|0(\\.\\d{0,2})?)$")

    fun validateAmount(input: String) {
        when {
            // Empty input case
            input.isEmpty() -> {
                inputText = ""
                showError = false
                viewModel.setTransactionAmount(null)
            }

            // Zero case
            input == "0" -> {
                inputText = input
                showError = false
                viewModel.setTransactionAmount(0.0)
            }

            // Valid number format case
            input.matches(currencyPattern) -> {
                try {
                    val amount = input.toDouble()
                    if (amount > AppConstants.MAX_TRANSACTION_AMOUNT) {
                        showError = true
                        errorMessage = errorMaxAmount
                    } else {
                        inputText = input
                        viewModel.setTransactionAmount(amount)
                        showError = false
                    }
                } catch (e: NumberFormatException) {
                    showError = true
                    errorMessage = e.message.toString()
                }
            }
        }
    }

    // Configure input field properties
    val inputProps = InputCommonProps(
        modifier = modifier,
        placeholder = "Amount",
        errorText = errorMessage,
        isError = showError,
        enabled = true,
        accentColor = getTransactionColor(transactionType),
        size = InputFieldSize.Large,
    )

    // Render the text field
    FinsibleTextInput(
        value = inputText,
        onValueChange = { validateAmount(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        commonProps = inputProps,
    )
}