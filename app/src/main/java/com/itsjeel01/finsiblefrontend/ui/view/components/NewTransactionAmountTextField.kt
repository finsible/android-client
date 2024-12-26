package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors

@Composable
fun NewTransactionAmountTextField(
    modifier: Modifier = Modifier,
    initialAmount: Double?,
    onAmountChange: (Double?) -> Unit,
    accentColor: Color,
) {
    var rawInput by remember { mutableStateOf(initialAmount?.toString() ?: "") }

    TextField(
        modifier = modifier.fillMaxWidth(),
        value = rawInput,
        onValueChange = { input ->
            if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                rawInput = input
                val amount = input.toDoubleOrNull()
                onAmountChange(amount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = finsibleTextFieldColors(accentColor = accentColor),
        label = { Text(text = "Amount") },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.rupee_icon),
                contentDescription = "Transaction Currency",
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    )
}