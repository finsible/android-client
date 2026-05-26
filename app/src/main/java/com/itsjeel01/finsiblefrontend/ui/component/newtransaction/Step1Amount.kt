package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.toCompactCurrency
import com.itsjeel01.finsiblefrontend.ui.di.hiltCurrencyFormatter
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.displayFont
import com.itsjeel01.finsiblefrontend.ui.theme.medium

@Composable
fun Step1Amount(
    amount: String,
    onAmountChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val currencyFormatter = hiltCurrencyFormatter()
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

    val formattedAmount by remember(amount, currencyFormatter) {
        derivedStateOf { amount.toCompactCurrency(currencyFormatter) }
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
                            FinsibleText(
                                text = stringResource(R.string.amount_placeholder),
                                variant = FinsibleTextVariant.XLargeHeadingBold,
                                color = FinsibleTheme.colors.primaryContent40,
                                textAlign = TextAlign.Center,
                                textStyleOverride = textStyle.copy(color = FinsibleTheme.colors.primaryContent40)
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
                    FinsibleText(
                        text = formattedAmount,
                        variant = FinsibleTextVariant.SmallTitleMedium,
                        color = FinsibleTheme.colors.primaryContent80,
                        textAlign = TextAlign.Center,
                        textStyleOverride = FinsibleTheme.typography.t18.medium()
                    )
            }
        }

        // Helper text when empty.
        AnimatedVisibility(
            visible = amount.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FinsibleText(
                text = stringResource(R.string.enter_transaction_amount),
                variant = FinsibleTextVariant.BodyRegular,
                colorVariant = FinsibleTextColorVariant.Secondary,
                textAlign = TextAlign.Center,
                textStyleOverride = FinsibleTheme.typography.t16,
                modifier = Modifier.padding(top = FinsibleTheme.dimes.d8)
            )
        }
    }
}


