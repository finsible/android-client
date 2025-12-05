package com.itsjeel01.finsiblefrontend.ui.screen.newtransaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.common.toReadableCurrency
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.displayFont
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun Step1Amount(
    amount: String,
    onAmountChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(focusRequester) { focusRequester.requestFocus() }

    // Count only digits for sizing decisions.
    val digitCount = remember(amount) { amount.count { it.isDigit() } }

    // Adaptive text size based on amount digits
    val textSize = when {
        digitCount <= 7 -> FinsibleTheme.typography.t64
        digitCount <= 12 -> FinsibleTheme.typography.t56
        else -> FinsibleTheme.typography.t40
    }

    val textStyle = textSize.displayFont().bold().copy(
        color = FinsibleTheme.colors.primaryContent,
        textAlign = TextAlign.Center
    )

    // Animated vertical padding based on content
    val topPadding by animateDpAsState(
        targetValue = if (amount.isNotEmpty()) FinsibleTheme.dimes.d24 else FinsibleTheme.dimes.d48,
        animationSpec = tween(300),
        label = "topPadding"
    )

    val formattedAmount by remember(amount) {
        derivedStateOf { amount.toReadableCurrency() }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = FinsibleTheme.dimes.d16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Main amount input area with background card.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    FinsibleTheme.colors.surfaceContainerLow,
                    RoundedCornerShape(FinsibleTheme.dimes.d16)
                )
                .padding(
                    vertical = FinsibleTheme.dimes.d24,
                    horizontal = FinsibleTheme.dimes.d16
                ),
            contentAlignment = Alignment.Center
        ) {

            // Amount input with zero-width space for layout stability
            BasicTextField(
                value = amount.ifEmpty { "\u200B" },
                onValueChange = { newValue ->
                    val cleanedValue = newValue.replace("\u200B", "")
                    onAmountChange(cleanedValue)
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .widthIn(min = FinsibleTheme.dimes.d52),
                textStyle = textStyle,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                cursorBrush = SolidColor(FinsibleTheme.colors.brandAccent),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        // Placeholder when empty.
                        if (amount.isEmpty()) {
                            Text(
                                text = "0",
                                style = textStyle.copy(color = FinsibleTheme.colors.primaryContent40)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }

        Spacer(Modifier.height(FinsibleTheme.dimes.d16))

        // Formatted amount display with Indian number system.
        AnimatedVisibility(
            visible = amount.isNotEmpty() && amount.toDoubleOrNull() != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)
            ) {
                Text(
                    text = formattedAmount,
                    style = FinsibleTheme.typography.t18.medium(),
                    color = FinsibleTheme.colors.primaryContent80,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Helper text when empty.
        AnimatedVisibility(
            visible = amount.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Enter transaction amount",
                style = FinsibleTheme.typography.t16,
                color = FinsibleTheme.colors.secondaryContent,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = FinsibleTheme.dimes.d8)
            )
        }
    }
}

@Composable
fun Step1Amount(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    viewModel: NewTransactionViewModel,
) {
    val amount by viewModel.transactionAmountString.collectAsStateWithLifecycle()
    Step1Amount(
        amount = amount,
        onAmountChange = { input ->
            val validated = viewModel.validateAmount(input)
            viewModel.setTransactionAmountString(validated)
        },
        focusRequester = focusRequester,
        modifier = modifier,
    )
}

/** Cached Indian number formatter (lakhs and crores). */
private val indianFormatter: NumberFormat by lazy {
    NumberFormat.getInstance(Locale.forLanguageTag("en-IN")).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 4
    }
}

/** Format amount with Indian number system commas (lakhs and crores). */
private fun formatAmountWithCommas(amount: String, currencySymbol: String): String {
    if (amount.isBlank()) return ""
    try {
        BigDecimal(amount)
    } catch (_: Exception) {
        return amount
    }
    return try {
        amount.toReadableCurrency()
    } catch (_: Exception) {
        "$currencySymbol $amount"
    }
}