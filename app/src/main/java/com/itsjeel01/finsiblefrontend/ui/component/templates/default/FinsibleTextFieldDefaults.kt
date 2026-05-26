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
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for templatised text field. */
object FinsibleTextFieldDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        focusedContainerColor: Color = Color.Unspecified,
        errorContainerColor: Color = Color.Unspecified,
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
        val s = FinsibleTheme.colors

        return FinsibleTextFieldColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else s.inputSurface,
            focusedContainerColor = if (focusedContainerColor != Color.Unspecified) focusedContainerColor else s.inputSurface.copy(alpha = 0.7f),
            errorContainerColor = if (errorContainerColor != Color.Unspecified) errorContainerColor else s.feedbackError.copy(alpha = 0.1f),
            contentColor = if (contentColor != Color.Unspecified) contentColor else s.contentPrimary,
            placeholderColor = if (placeholderColor != Color.Unspecified) placeholderColor else s.contentSecondary,
            borderColor = if (borderColor != Color.Unspecified) borderColor else Color.Transparent,
            focusedBorderColor = if (focusedBorderColor != Color.Unspecified) focusedBorderColor else Color.Transparent,
            errorBorderColor = if (errorBorderColor != Color.Unspecified) errorBorderColor else Color.Transparent,
            disabledContainerColor = if (disabledContainerColor != Color.Unspecified) disabledContainerColor else s.surfaceSunken,
            disabledContentColor = if (disabledContentColor != Color.Unspecified) disabledContentColor else s.contentDisabled,
            supportingTextColor = if (supportingTextColor != Color.Unspecified) supportingTextColor else s.contentSecondary,
            errorTextColor = if (errorTextColor != Color.Unspecified) errorTextColor else s.feedbackError,
            iconTint = if (iconTint != Color.Unspecified) iconTint else s.contentPrimary,
            disabledIconTint = if (disabledIconTint != Color.Unspecified) disabledIconTint else s.contentDisabled,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else s.contentPrimary.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize, shapeVariant: FinsibleShape = FinsibleShape.Rounded): FinsibleTextFieldSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        val spec = when (size) {
            FinsibleSize.ExtraSmall -> FinsibleTextFieldSizes(
                horizontalPadding = sp.gapSm + sp.insetXs / 2,
                verticalPadding = sp.inlineMd,
                textStyle = t.bodySm,
                placeholderStyle = t.bodySm,
                supportingTextStyle = t.caption,
                iconSize = FinsibleTheme.sizes.icon.md,
                cornerRadius = FinsibleTheme.radius.sm
            )

            FinsibleSize.Small -> FinsibleTextFieldSizes(
                horizontalPadding = sp.gapMd,
                verticalPadding = sp.gapSm + sp.insetXs / 2,
                textStyle = t.bodyMd,
                placeholderStyle = t.bodyMd,
                supportingTextStyle = t.bodySm,
                iconSize = FinsibleTheme.sizes.icon.md,
                cornerRadius = FinsibleTheme.radius.md
            )

            FinsibleSize.Medium -> FinsibleTextFieldSizes(
                horizontalPadding = sp.insetLg - sp.insetXs / 2,
                verticalPadding = sp.gapMd,
                textStyle = t.bodyLg,
                placeholderStyle = t.bodyLg,
                supportingTextStyle = t.bodySm,
                iconSize = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                cornerRadius = FinsibleRadius.md
            )

            FinsibleSize.Large -> FinsibleTextFieldSizes(
                horizontalPadding = sp.insetLg,
                verticalPadding = sp.insetLg - sp.insetXs / 2,
                textStyle = t.bodyLg.medium(),
                placeholderStyle = t.bodyLg,
                supportingTextStyle = t.bodyMd,
                iconSize = FinsibleTheme.sizes.icon.lg,
                cornerRadius = FinsibleRadius.md
            )

            FinsibleSize.ExtraLarge -> FinsibleTextFieldSizes(
                horizontalPadding = sp.insetLg,
                verticalPadding = sp.insetLg,
                textStyle = t.headingMd.medium(),
                placeholderStyle = t.headingMd,
                supportingTextStyle = t.bodyLg,
                iconSize = FinsibleTheme.sizes.icon.xl - sp.insetXs,
                cornerRadius = FinsibleTheme.radius.lg
            )
        }

        val corner = when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.radius.none
            FinsibleShape.Rounded -> spec.cornerRadius
            FinsibleShape.Pill,
            FinsibleShape.Circle -> FinsibleTheme.radius.pill
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
