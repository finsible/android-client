package com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.di.hiltCurrencyFormatter
import com.itsjeel01.finsiblefrontend.data.di.hiltCurrencyRepository
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.ExchangeRateFreshness
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionSheetMode
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionValidationError
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleShadow
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

@Composable
fun NewTransactionAmountCard(
    state: NewTransactionFormState,
    onEvent: (NewTransactionUiEvent) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    val hasError = state.validationErrors.contains(NewTransactionValidationError.AMOUNT)
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val amountFocusRequester = remember { FocusRequester() }
    val currencyFormatter = hiltCurrencyFormatter()
    val currencyRepository = hiltCurrencyRepository()

    val groupedAmountTransformation = remember(currencyFormatter, state.currencyCode) {
        AmountGroupingVisualTransformation { wholePart ->
            val whole = wholePart.toLongOrNull() ?: return@AmountGroupingVisualTransformation wholePart
            currencyFormatter.format(
                centis = whole * 100L,
                currencyCode = state.currencyCode,
                options = CurrencyFormatter.CurrencyFormatOptions(
                    includeCurrencySymbol = false,
                    includeSign = false,
                )
            )
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    stiffness = Spring.StiffnessMediumLow,
                    dampingRatio = Spring.DampingRatioNoBouncy
                )
            )
            .finsibleShadow(FinsibleTheme.elevation.raisedShadow, shape = RoundedCornerShape(FinsibleTheme.radius.component.cardFinance))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        FinsibleTheme.colors.surfaceSunken,
                        FinsibleTheme.colors.surfaceSunken,
                        accentColor.copy(alpha = 0.25f),
                    )
                ),
                shape = RoundedCornerShape(FinsibleTheme.radius.component.cardFinance)
            )
            .padding(vertical = FinsibleTheme.spacing.inlineMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackXs)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FinsibleTheme.spacing.insetLg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(size = FinsibleTheme.radius.component.badge))
                    .clickable(
                        onClick = { onEvent(NewTransactionUiEvent.SheetModeChanged(NewTransactionSheetMode.CURRENCY)) },
                        role = Role.Button
                    )
                    .background(Color.Transparent, RoundedCornerShape(size = FinsibleTheme.spacing.inlineMd))
                    .border(
                        FinsibleTheme.stroke.thin,
                        FinsibleTheme.colors.borderDefault,
                        RoundedCornerShape(size = FinsibleTheme.radius.component.badge)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = FinsibleTheme.spacing.inlineMd, vertical = FinsibleTheme.spacing.inlineMd),
                    horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapXs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FinsibleText(
                        text = "${currencyRepository.getFlagEmojiByIsoCode(state.currencyCode)}",
                        textStyle = FinsibleTheme.typography.bodySm
                    )
                    FinsibleText(
                        state.currencyCode,
                        textStyle = FinsibleTheme.typography.bodySm
                    )
                    Icon(
                        modifier = Modifier.size(FinsibleTheme.spacing.gapMd),
                        painter = painterResource(com.composables.icons.tabler.filled.R.drawable.tabler_ic_caret_down_filled),
                        contentDescription = stringResource(R.string.cd_currency_dropdown_icon)
                    )
                }
            }

            FinsibleText(
                text = "Amount",
                textStyle = FinsibleTheme.typography.bodySm.medium(),
                uppercase = true,
                colorVariant = FinsibleTextColorVariant.Secondary
            )
        }

        FinsibleTextField(
            value = state.amountString,
            onValueChange = { onEvent(NewTransactionUiEvent.AmountChanged(it)) },
            placeholder = stringResource(R.string.amount_placeholder),
            size = FinsibleSize.Large,
            isError = hasError,
            supportingText = if (hasError) "Enter a valid amount greater than 0" else null,
            inputConfig = FinsibleTextFieldDefaults.inputConfig(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            visualTransformation = groupedAmountTransformation,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                }
            ),
            textAlign = TextAlign.Start,
            focusRequester = amountFocusRequester,
            sizes = FinsibleTextFieldDefaults.sizes(
                size = FinsibleSize.Large,
                shapeVariant = FinsibleShape.Sharp
            ).copy(
                textStyle = FinsibleTheme.typography.displayLg.semiBold(),
                placeholderStyle = FinsibleTheme.typography.displayLg.medium().copy(color = FinsibleTheme.colors.contentSecondary),
                verticalPadding = FinsibleTheme.spacing.insetNone
            ),
            colors = FinsibleTextFieldDefaults.colors().copy(
                containerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                borderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                rippleColor = Color.Transparent
            )
        )

        ConversionIndicatorRow(
            convertedAmountDisplay = state.convertedAmountDisplay,
            freshness = state.exchangeRateFreshness,
            isOnline = state.isOnline,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = FinsibleTheme.spacing.insetLg)
        )
    }
}

private class AmountGroupingVisualTransformation(
    private val formatWholePart: (String) -> String
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        if (raw.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val separatorIndex = raw.indexOf('.')
        val hasDecimal = separatorIndex != -1

        val integerRaw = if (hasDecimal) raw.substring(0, separatorIndex) else raw
        val decimalRaw = if (hasDecimal) raw.substring(separatorIndex) else ""

        val formattedInteger = if (integerRaw.isEmpty()) "" else formatWholePart(integerRaw)
        val transformedString = formattedInteger + decimalRaw

        val integerRawLength = integerRaw.length
        val formattedIntegerLength = formattedInteger.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset >= integerRawLength) {
                    return offset + (formattedIntegerLength - integerRawLength)
                }

                var origCount = 0
                for (i in 0 until formattedIntegerLength) {
                    if (formattedInteger[i].isDigit()) {
                        origCount++
                    }
                    if (origCount == offset) {
                        return i + 1
                    }
                }
                return formattedIntegerLength
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset >= formattedIntegerLength) {
                    return offset - (formattedIntegerLength - integerRawLength)
                }

                var origCount = 0
                for (i in 0 until offset) {
                    if (formattedInteger[i].isDigit()) {
                        origCount++
                    }
                }
                return origCount
            }
        }

        return TransformedText(AnnotatedString(transformedString), offsetMapping)
    }
}

@Composable
private fun ConversionIndicatorRow(
    convertedAmountDisplay: String?,
    freshness: ExchangeRateFreshness,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val isVisible = convertedAmountDisplay != null || freshness != ExchangeRateFreshness.AVAILABLE

    if (isVisible) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val primaryText = resolvePrimaryText(convertedAmountDisplay, freshness)
            if (primaryText.isNotBlank()) {
                FinsibleText(
                    text = primaryText,
                    textStyle = FinsibleTheme.typography.bodySm,
                    colorVariant = FinsibleTextColorVariant.Secondary
                )
            }

            val warningText = resolveWarningText(freshness, isOnline)
            if (warningText != null) {
                FinsibleText(
                    text = warningText,
                    textStyle = FinsibleTheme.typography.bodySm,
                    colorVariant = if (isOnline) FinsibleTextColorVariant.Error else FinsibleTextColorVariant.Secondary
                )
            }
        }
    }
}

@Composable
private fun resolvePrimaryText(amount: String?, freshness: ExchangeRateFreshness): String {
    return amount ?: when (freshness) {
        ExchangeRateFreshness.UNAVAILABLE -> stringResource(id = R.string.conversion_unavailable)
        else -> ""
    }
}

@Composable
private fun resolveWarningText(freshness: ExchangeRateFreshness, isOnline: Boolean): String? {
    return if (freshness == ExchangeRateFreshness.STALE) {
        if (isOnline) {
            stringResource(id = R.string.rates_may_be_stale)
        } else {
            stringResource(id = R.string.offline_using_last_known_rate)
        }
    } else {
        null
    }
}

