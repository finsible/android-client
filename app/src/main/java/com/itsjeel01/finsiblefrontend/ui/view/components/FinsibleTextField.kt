package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

enum class TextFieldSize { SMALL, LARGE }

@Composable
fun FinsibleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorIcon: @Composable (() -> Unit)? = null,
    supportingTextIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    size: TextFieldSize = TextFieldSize.LARGE,
    accentColor: Color = MaterialTheme.colorScheme.primary,
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val (height, textStyle, secondaryTextStyle) = when (size) {
        TextFieldSize.SMALL -> Triple(48.dp, MaterialTheme.typography.bodyMedium, MaterialTheme.typography.bodySmall)
        TextFieldSize.LARGE -> Triple(56.dp, MaterialTheme.typography.bodyLarge, MaterialTheme.typography.bodyMedium)
    }

    val labelPadding = 4.dp
    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> accentColor
        else -> MaterialTheme.colorScheme.outline
    }

    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            style = secondaryTextStyle,
            color = when {
                isError -> MaterialTheme.colorScheme.error
                isFocused -> accentColor
                else -> MaterialTheme.colorScheme.outline
            },
            modifier = Modifier.padding(start = labelPadding, bottom = labelPadding)
        )

        // Input Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .border(1.dp, borderColor, RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
                cursorBrush = SolidColor(accentColor),
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .onFocusChanged { isFocused = it.isFocused },
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        leadingIcon?.let {
                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(24.dp)
                            ) {
                                it
                            }
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && placeholder != null) {
                                Text(
                                    text = placeholder,
                                    style = textStyle.copy(color = Color.Gray),
                                    modifier = Modifier.alpha(0.6f)
                                )
                            }
                            innerTextField()
                        }

                        trailingIcon?.let {
                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(24.dp)
                            ) {
                                it
                            }
                        }
                    }
                }
            )
        }

        // Error message or supporting text
        when {
            isError && errorMessage != null -> {
                HelpRow(
                    text = errorMessage,
                    icon = errorIcon,
                    textColor = MaterialTheme.colorScheme.error,
                    textStyle = secondaryTextStyle
                )
            }

            supportingText != null && !isError -> {
                HelpRow(
                    text = supportingText,
                    icon = supportingTextIcon,
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    textStyle = secondaryTextStyle
                )
            }
        }
    }
}

@Composable
private fun HelpRow(
    text: String,
    icon: @Composable (() -> Unit)?,
    textColor: Color,
    textStyle: TextStyle,
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
    ) {
        icon?.let {
            Box(
                modifier = Modifier
                    .padding(end = 4.dp, top = 2.dp)
                    .size(12.dp)
            ) {
                it()
            }
        }
        Text(
            text = text,
            style = textStyle,
            color = textColor
        )
    }
}