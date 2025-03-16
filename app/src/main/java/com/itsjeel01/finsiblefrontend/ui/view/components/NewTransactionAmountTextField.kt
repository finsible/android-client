package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.itsjeel01.finsiblefrontend.utils.AppConstants

@Composable
fun NewTransactionAmountTextField(
    modifier: Modifier = Modifier,
    initialAmount: Double?,
    onAmountChange: (Double?) -> Unit,
    accentColor: Color,
) {
    val focusManager = LocalFocusManager.current
    var rawInput by remember { mutableStateOf(initialAmount?.toString() ?: "") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Currency format regex (up to 2 decimal places)
    val validAmountPattern = Regex("^([1-9]\\d*(\\.\\d{0,2})?|0(\\.\\d{0,2})?)$")

    fun validateAndProcessInput(input: String) {
        when {
            input.isEmpty() -> {
                rawInput = ""
                showError = false
                onAmountChange(null)
            }

            input == "0" -> {
                rawInput = input
                showError = false
                onAmountChange(0.0)
            }

            input.matches(validAmountPattern) -> {
                try {
                    val amount = input.toDouble()
                    if (amount > AppConstants.MAX_TRANSACTION_AMOUNT) {
                        showError = true
                        errorMessage = "The amount is too large."
                    } else {
                        rawInput = input
                        onAmountChange(amount)
                        showError = false
                    }
                } catch (e: NumberFormatException) {
                    showError = true
                    errorMessage = e.message.toString()
                }
            }
        }
    }

    FinsibleTextField(
        modifier = modifier,
        value = rawInput,
        onValueChange = { validateAndProcessInput(it) },
        label = "Amount",
        placeholder = "0.00",
        isError = showError,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        accentColor = accentColor,
        size = TextFieldSize.SMALL
    )
}