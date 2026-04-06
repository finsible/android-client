package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldInputConfig
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextFieldSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for templatised text field. */
object FinsibleTextFieldDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        placeholderColor: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified,
        focusedBorderColor: Color = Color.Unspecified,
        errorBorderColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        supportingTextColor: Color = Color.Unspecified,
        errorTextColor: Color = Color.Unspecified,
        iconTint: Color = Color.Unspecified,
        disabledIconTint: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified
    ): FinsibleTextFieldColors {
        val colors = FinsibleTheme.colors

        return FinsibleTextFieldColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else colors.input,
            contentColor = if (contentColor != Color.Unspecified) contentColor else colors.primaryContent,
            placeholderColor = if (placeholderColor != Color.Unspecified) placeholderColor else colors.secondaryContent,
            borderColor = if (borderColor != Color.Unspecified) borderColor else colors.border,
            focusedBorderColor = if (focusedBorderColor != Color.Unspecified) focusedBorderColor else colors.brandAccent,
            errorBorderColor = if (errorBorderColor != Color.Unspecified) errorBorderColor else colors.error,
            disabledContainerColor = if (disabledContainerColor != Color.Unspecified) disabledContainerColor else colors.disabled,
            disabledContentColor = if (disabledContentColor != Color.Unspecified) disabledContentColor else colors.disabledContent,
            supportingTextColor = if (supportingTextColor != Color.Unspecified) supportingTextColor else colors.secondaryContent,
            errorTextColor = if (errorTextColor != Color.Unspecified) errorTextColor else colors.error,
            iconTint = if (iconTint != Color.Unspecified) iconTint else colors.primaryContent,
            disabledIconTint = if (disabledIconTint != Color.Unspecified) disabledIconTint else colors.disabledContent,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else colors.primaryContent.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize, shapeVariant: FinsibleShape = FinsibleShape.Rounded): FinsibleTextFieldSizes {
        val d = FinsibleTheme.dimes
        val t = FinsibleTheme.typography

        val spec = when (size) {
            FinsibleSize.ExtraSmall -> FinsibleTextFieldSizes(
                height = d.d36,
                contentPadding = d.d10,
                textStyle = t.t14,
                placeholderStyle = t.t14,
                supportingTextStyle = t.t12,
                iconSize = d.d16,
                cornerRadius = d.d8
            )

            FinsibleSize.Small -> FinsibleTextFieldSizes(
                height = d.d44,
                contentPadding = d.d12,
                textStyle = t.t16,
                placeholderStyle = t.t16,
                supportingTextStyle = t.t12,
                iconSize = d.d18,
                cornerRadius = d.d10
            )

            FinsibleSize.Medium -> FinsibleTextFieldSizes(
                height = d.d52,
                contentPadding = d.d14,
                textStyle = t.t18,
                placeholderStyle = t.t18,
                supportingTextStyle = t.t12,
                iconSize = d.d20,
                cornerRadius = d.d12
            )

            FinsibleSize.Large -> FinsibleTextFieldSizes(
                height = d.d60,
                contentPadding = d.d16,
                textStyle = t.t20.medium(),
                placeholderStyle = t.t20,
                supportingTextStyle = t.t14,
                iconSize = d.d24,
                cornerRadius = d.d14
            )

            FinsibleSize.ExtraLarge -> FinsibleTextFieldSizes(
                height = d.d68,
                contentPadding = d.d16,
                textStyle = t.t24.medium(),
                placeholderStyle = t.t24,
                supportingTextStyle = t.t16,
                iconSize = d.d28,
                cornerRadius = d.d16
            )
        }

        val corner = when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.dimes.d0
            FinsibleShape.Rounded -> spec.cornerRadius
            FinsibleShape.Pill -> spec.height / 2
            FinsibleShape.Circle -> spec.height / 2
        }

        return spec.copy(cornerRadius = corner)
    }

    fun inputConfig(
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Done,
        maxLength: Int? = null
    ): FinsibleTextFieldInputConfig {
        return FinsibleTextFieldInputConfig(
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            maxLength = maxLength
        )
    }
}


