package com.itsjeel01.finsiblefrontend.ui.component

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
import com.itsjeel01.finsiblefrontend.common.Constants
import com.itsjeel01.finsiblefrontend.common.InputFieldSize
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.common.Utils
import com.itsjeel01.finsiblefrontend.ui.component.base.BaseTextInput
import com.itsjeel01.finsiblefrontend.ui.component.base.CommonProps
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel

@Composable
fun TransactionAmountTextField(modifier: Modifier) {

    // --- ViewModel and State Initialization ---

    val viewModel: TransactionFormViewModel = hiltViewModel()
    val transactionType = viewModel.transactionType.collectAsState().value
    val amount = viewModel.transactionAmount.collectAsState().value

    val focusManager = LocalFocusManager.current
    var inputText by remember { mutableStateOf(amount?.toString() ?: "") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // --- Validation Logic ---

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
            input.matches(Regex(Strings.VALID_AMOUNT_PATTERN)) -> {
                try {
                    val amount = input.toDouble()
                    if (amount > Constants.MAX_TRANSACTION_AMOUNT) {
                        showError = true
                        errorMessage =
                            "Exceeds ${Utils.formatNumber(Constants.MAX_TRANSACTION_AMOUNT.toDouble())}"
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

    // --- Amount Input Field UI ---

    val inputProps = CommonProps(
        modifier = modifier,
        placeholder = "Amount",
        errorText = errorMessage,
        isError = showError,
        enabled = true,
        accentColor = Utils.getTransactionColor(transactionType),
        size = InputFieldSize.Large,
    )

    BaseTextInput(
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
