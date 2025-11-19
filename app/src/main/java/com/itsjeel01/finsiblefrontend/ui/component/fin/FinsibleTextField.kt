package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

data class TextFieldConfig(
    val size: ComponentSize = ComponentSize.Medium,
    val type: ComponentType = ComponentType.Secondary,
    val enabled: Boolean = true,
    val readOnly: Boolean = false,
    val isError: Boolean = false,
    val singleLine: Boolean = true,
    val maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    val minLines: Int = 1
)

@Composable
fun FinsibleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    config: TextFieldConfig = TextFieldConfig(),
    label: String? = null,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    leadingIcon: Int? = null,
    trailingIcon: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val effectiveError = config.isError && errorText != null

    val borderColor = when {
        effectiveError -> FinsibleTheme.colors.error
        isFocused -> FinsibleTheme.colors.brandAccent
        !config.enabled -> FinsibleTheme.colors.disabled
        else -> FinsibleTheme.colors.border
    }

    val backgroundColor = when {
        !config.enabled -> FinsibleTheme.colors.disabled.copy(alpha = 0.1f)
        else -> FinsibleTheme.colors.input
    }

    val textColor = when {
        !config.enabled -> FinsibleTheme.colors.disabled
        else -> FinsibleTheme.colors.primaryContent
    }

    val cornerRadius = config.size.cornerRadius
    val horizontalPadding = config.size.horizontalPadding
    val textStyle = config.size.typography().copy(color = textColor)

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
        // Label
        label?.let {
            Text(
                text = it,
                style = FinsibleTheme.typography.t16.medium(),
                color = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.secondaryContent
            )
        }

        // Text Field
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = config.enabled,
            readOnly = config.readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = config.singleLine,
            maxLines = config.maxLines,
            minLines = config.minLines,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(FinsibleTheme.colors.brandAccent),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .let { mod ->
                            if (config.singleLine) mod.padding(vertical = FinsibleTheme.dimes.d12)
                            else mod
                        }
                        .background(backgroundColor, RoundedCornerShape(cornerRadius))
                        .border(
                            width = FinsibleTheme.dimes.d1,
                            color = borderColor,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .padding(horizontal = horizontalPadding, vertical = FinsibleTheme.dimes.d12)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
                    ) {
                        // Leading icon
                        leadingIcon?.let {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                                tint = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.primaryContent60,
                                modifier = Modifier.padding(end = FinsibleTheme.dimes.d4)
                            )
                        }

                        // Text field content
                        Box(modifier = Modifier.weight(1f)) {
                            // Placeholder
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = textStyle.copy(color = FinsibleTheme.colors.primaryContent40),
                                    maxLines = if (config.singleLine) 1 else Int.MAX_VALUE
                                )
                            }
                            innerTextField()
                        }

                        // Trailing icon
                        trailingIcon?.let {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                                tint = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.primaryContent60,
                                modifier = Modifier.padding(start = FinsibleTheme.dimes.d4)
                            )
                        }
                    }
                }
            }
        )

        // Helper or Error text
        AnimatedVisibility(
            visible = (helperText != null && !effectiveError) || effectiveError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = if (effectiveError) errorText else helperText ?: "",
                style = FinsibleTheme.typography.t14,
                color = if (effectiveError) FinsibleTheme.colors.error else FinsibleTheme.colors.secondaryContent,
                modifier = Modifier.padding(start = horizontalPadding)
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
    config: TextFieldConfig = TextFieldConfig(),
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
        // Label with counter
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
            config = config.copy(isError = value.length >= maxLength),
            placeholder = placeholder,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource
        )
    }
}

