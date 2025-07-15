package com.itsjeel01.finsiblefrontend.ui.component.base

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class InputCommonProps(
    val modifier: Modifier = Modifier,
    val label: String? = null,
    val placeholder: String? = null,
    val supportingText: String? = null,
    val errorText: String? = null,
    val isError: Boolean = false,
    val enabled: Boolean = true,
    val leadingIcon: (@Composable () -> Unit)? = null,
    val trailingIcon: (@Composable () -> Unit)? = null,
    val accentColor: Color = Color.Unspecified,
    val size: InputFieldSize = InputFieldSize.Large,
) {
    @SuppressLint("ModifierFactoryExtensionFunction")
    fun fieldModifier() = modifier.heightIn(
        min = if (size == InputFieldSize.Small) 48.dp else 56.dp
    )

    @Composable
    fun primaryTextStyle() = when (size) {
        InputFieldSize.Small -> MaterialTheme.typography.bodyMedium
        InputFieldSize.Large -> MaterialTheme.typography.bodyLarge
    }

    @Composable
    fun secondaryTextStyle() = when (size) {
        InputFieldSize.Small -> MaterialTheme.typography.bodySmall
        InputFieldSize.Large -> MaterialTheme.typography.bodyMedium
    }

    fun labelComposable(): (@Composable () -> Unit)? = when {
        label?.isNotEmpty() == true -> {
            { Text(text = label, style = secondaryTextStyle()) }
        }

        else -> null
    }

    fun placeholderComposable(): (@Composable () -> Unit)? = when {
        placeholder?.isNotEmpty() == true -> {
            { Text(text = placeholder, style = primaryTextStyle()) }
        }

        else -> null
    }

    fun supportingTextComposable(): (@Composable () -> Unit)? = when {
        isError && errorText?.isNotEmpty() == true -> {
            { Text(text = errorText, style = secondaryTextStyle()) }
        }

        enabled && supportingText?.isNotEmpty() == true -> {
            { Text(text = supportingText, style = secondaryTextStyle()) }
        }

        else -> null
    }

    fun leadingIconComposable(): (@Composable () -> Unit)? = leadingIcon?.let { { it() } }

    fun trailingIconComposable(): (@Composable () -> Unit)? = trailingIcon?.let { { it() } }
}

enum class InputFieldSize {
    Small,
    Large
}
