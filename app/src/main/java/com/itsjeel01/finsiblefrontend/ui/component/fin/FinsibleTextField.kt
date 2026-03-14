package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Size configuration for [FinsibleTextField]. */
@Immutable
data class FinsibleTextFieldSizes(
    val cornerRadius: Dp,
    val horizontalPadding: Dp,
    val textStyle: TextStyle
)

/** Defaults factory for [FinsibleTextField] sizes. */
object FinsibleTextFieldDefaults {

    /** Small text field sizes. */
    @Composable
    fun smallSizes() = FinsibleTextFieldSizes(
        cornerRadius = FinsibleTheme.dimes.d8,
        horizontalPadding = FinsibleTheme.dimes.d12,
        textStyle = FinsibleTheme.typography.t14
    )

    /** Medium text field sizes. */
    @Composable
    fun mediumSizes() = FinsibleTextFieldSizes(
        cornerRadius = FinsibleTheme.dimes.d12,
        horizontalPadding = FinsibleTheme.dimes.d16,
        textStyle = FinsibleTheme.typography.t20
    )

    /** Large text field sizes. */
    @Composable
    fun largeSizes() = FinsibleTextFieldSizes(
        cornerRadius = FinsibleTheme.dimes.d12,
        horizontalPadding = FinsibleTheme.dimes.d20,
        textStyle = FinsibleTheme.typography.t24
    )
}

@Composable
fun FinsibleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    sizes: FinsibleTextFieldSizes = FinsibleTextFieldDefaults.mediumSizes(),
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val effectiveError = isError && errorText != null

    val borderColor = when {
        effectiveError -> FinsibleTheme.colors.error
        isFocused -> FinsibleTheme.colors.primaryContent
        !enabled -> FinsibleTheme.colors.disabled
        else -> FinsibleTheme.colors.border
    }

    val backgroundColor = when {
        !enabled -> FinsibleTheme.colors.disabled.copy(alpha = 0.1f)
        else -> FinsibleTheme.colors.input
    }

    val textColor = when {
        !enabled -> FinsibleTheme.colors.disabledContent
        else -> FinsibleTheme.colors.primaryContent
    }

    val textStyle = sizes.textStyle.copy(color = textColor)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
        label?.let {
            Text(
                text = it,
                style = FinsibleTheme.typography.t16.medium(),
                color = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.secondaryContent
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(FinsibleTheme.colors.primaryContent80),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .let { mod ->
                            if (singleLine) mod.padding(vertical = FinsibleTheme.dimes.d12)
                            else mod
                        }
                        .background(backgroundColor, RoundedCornerShape(sizes.cornerRadius))
                        .border(
                            width = FinsibleTheme.dimes.d1,
                            color = borderColor,
                            shape = RoundedCornerShape(sizes.cornerRadius)
                        )
                        .padding(horizontal = sizes.horizontalPadding, vertical = FinsibleTheme.dimes.d12)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
                    ) {
                        leadingIcon?.let {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                                tint = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.onSurfaceVariant,
                                modifier = Modifier.padding(end = FinsibleTheme.dimes.d4)
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = textStyle.copy(color = FinsibleTheme.colors.primaryContent40),
                                    maxLines = if (singleLine) 1 else Int.MAX_VALUE
                                )
                            }
                            innerTextField()
                        }

                        trailingIcon?.let {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                                tint = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.onSurfaceVariant,
                                modifier = Modifier
                                    .padding(start = FinsibleTheme.dimes.d4)
                                    .then(
                                        if (onTrailingIconClick != null) Modifier.clickable(onClick = onTrailingIconClick)
                                        else Modifier
                                    )
                            )
                        }
                    }
                }
            }
        )

        AnimatedVisibility(
            visible = (helperText != null && !effectiveError) || effectiveError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = if (effectiveError) errorText else helperText ?: "",
                style = FinsibleTheme.typography.t14,
                color = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.secondaryContent,
                modifier = Modifier.padding(start = sizes.horizontalPadding)
            )
        }
    }
}

@Composable
fun FinsibleTextFieldWithCounter(
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    sizes: FinsibleTextFieldSizes = FinsibleTextFieldDefaults.mediumSizes(),
    label: String? = null,
    placeholder: String? = null,
    showCounter: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val counterText = "${value.length}/$maxLength"
    val isNearLimit = value.length.toFloat() / maxLength > 0.9f
    val counterColor = when {
        value.length >= maxLength -> FinsibleTheme.colors.error
        isNearLimit -> FinsibleTheme.colors.warning
        else -> FinsibleTheme.colors.secondaryContent
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
        if (label != null || showCounter) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        style = FinsibleTheme.typography.t16.medium(),
                        color = FinsibleTheme.colors.secondaryContent
                    )
                }
                if (showCounter) {
                    Text(
                        text = counterText,
                        style = FinsibleTheme.typography.t14,
                        color = counterColor,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        FinsibleTextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            isError = value.length >= maxLength,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            sizes = sizes,
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource
        )
    }
}

