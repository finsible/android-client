package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonBadgeSpec
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for Button Component. */
object FinsibleButtonDefaults {

    @Composable
    fun colors(
        variant: FinsibleButtonVariant,
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified,
        badgeContainerColor: Color = Color.Unspecified,
        badgeContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        disabledBorderColor: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified
    ): FinsibleButtonColors {
        val theme = FinsibleTheme.colors

        val (baseContainer, baseContent, baseBorder) = when (variant) {
            FinsibleButtonVariant.Filled -> Triple(
                theme.primaryContent,
                theme.primaryBackground,
                null
            )

            FinsibleButtonVariant.FilledTonal -> Triple(
                theme.secondaryBackground,
                theme.primaryContent,
                theme.primaryContent
            )

            FinsibleButtonVariant.Outlined -> Triple(
                Color.Transparent,
                theme.primaryContent,
                theme.border
            )

            FinsibleButtonVariant.Text -> Triple(
                Color.Transparent,
                theme.primaryContent,
                null
            )

            FinsibleButtonVariant.Link -> Triple(
                Color.Transparent,
                theme.link,
                null
            )
        }

        val (baseDisabledContainer, baseDisabledContent, baseDisabledBorder) = when (variant) {
            FinsibleButtonVariant.Filled -> Triple(
                theme.disabled,
                theme.disabledContent,
                null
            )

            FinsibleButtonVariant.FilledTonal -> Triple(
                theme.disabled,
                theme.disabledContent,
                theme.disabledContent
            )

            FinsibleButtonVariant.Outlined -> Triple(
                Color.Transparent,
                theme.disabledContent,
                theme.disabledContent
            )

            FinsibleButtonVariant.Text -> Triple(
                Color.Transparent,
                theme.disabledContent,
                null
            )

            FinsibleButtonVariant.Link -> Triple(
                Color.Transparent,
                theme.disabledContent,
                null
            )
        }

        val baseBadgeContainer = theme.brandAccent
        val baseBadgeContent = theme.primaryContent

        return FinsibleButtonColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else baseContainer,
            contentColor = if (contentColor != Color.Unspecified) contentColor else baseContent,
            borderColor = if (borderColor != Color.Unspecified) borderColor else baseBorder,
            badgeContainerColor = if (badgeContainerColor != Color.Unspecified) badgeContainerColor else baseBadgeContainer,
            badgeContentColor = if (badgeContentColor != Color.Unspecified) badgeContentColor else baseBadgeContent,
            disabledContainerColor = if (disabledContainerColor != Color.Unspecified) disabledContainerColor else baseDisabledContainer,
            disabledContentColor = if (disabledContentColor != Color.Unspecified) disabledContentColor else baseDisabledContent,
            disabledBorderColor = if (disabledBorderColor != Color.Unspecified) disabledBorderColor else baseDisabledBorder,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else baseContent.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleButtonSizes {
        val dimes = FinsibleTheme.dimes
        val type = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleButtonSizes(
                height = dimes.d24,
                contentPadding = PaddingValues(horizontal = dimes.d8),
                textStyle = type.t12.copy(fontWeight = FontWeight.Medium),
                iconSize = dimes.d12,
                iconSpacing = dimes.d4,
            )

            FinsibleSize.Small -> FinsibleButtonSizes(
                height = dimes.d32,
                contentPadding = PaddingValues(horizontal = dimes.d12),
                textStyle = type.t16.copy(fontWeight = FontWeight.Medium),
                iconSize = dimes.d16,
                iconSpacing = dimes.d6
            )

            FinsibleSize.Medium -> FinsibleButtonSizes(
                height = dimes.d48,
                contentPadding = PaddingValues(horizontal = dimes.d20),
                textStyle = type.t18.copy(fontWeight = FontWeight.Medium),
                iconSize = dimes.d20,
                iconSpacing = dimes.d8
            )

            FinsibleSize.Large -> FinsibleButtonSizes(
                height = dimes.d56,
                contentPadding = PaddingValues(horizontal = dimes.d24),
                textStyle = type.t20.copy(fontWeight = FontWeight.Medium),
                iconSize = dimes.d24,
                iconSpacing = dimes.d8
            )

            FinsibleSize.ExtraLarge -> FinsibleButtonSizes(
                height = dimes.d64,
                contentPadding = PaddingValues(horizontal = dimes.d32),
                textStyle = type.t24.copy(fontWeight = FontWeight.Medium),
                iconSize = dimes.d28,
                iconSpacing = dimes.d12
            )
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, size: FinsibleSize): Dp {
        val dimes = FinsibleTheme.dimes

        return when (shapeVariant) {
            FinsibleShape.Sharp -> dimes.d0
            FinsibleShape.Rounded -> when (size) {
                FinsibleSize.ExtraSmall -> dimes.d6
                FinsibleSize.Small -> dimes.d8
                FinsibleSize.Medium -> dimes.d12
                FinsibleSize.Large -> dimes.d14
                FinsibleSize.ExtraLarge -> dimes.d16
            }

            FinsibleShape.Circle,
            FinsibleShape.Pill -> sizes(size).height / 2
        }
    }

    @Composable
    fun shape(shapeVariant: FinsibleShape, size: FinsibleSize): Shape {
        return when (shapeVariant) {
            FinsibleShape.Circle -> CircleShape
            FinsibleShape.Pill -> CircleShape
            FinsibleShape.Rounded -> RoundedCornerShape(cornerRadius(shapeVariant, size))
            FinsibleShape.Sharp -> RectangleShape
        }
    }

    @Composable
    fun badgeSpec(size: FinsibleSize): FinsibleButtonBadgeSpec {
        val d = FinsibleTheme.dimes
        val type = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleButtonBadgeSpec(
                diameter = d.d10,
                textStyle = type.t8.copy(fontWeight = FontWeight.Bold)
            )

            FinsibleSize.Small -> FinsibleButtonBadgeSpec(
                diameter = d.d12,
                textStyle = type.t8.copy(fontWeight = FontWeight.Bold)
            )

            FinsibleSize.Medium -> FinsibleButtonBadgeSpec(
                diameter = d.d16,
                textStyle = type.t10.copy(fontWeight = FontWeight.Bold)
            )

            FinsibleSize.Large -> FinsibleButtonBadgeSpec(
                diameter = d.d18,
                textStyle = type.t10.copy(fontWeight = FontWeight.Bold)
            )

            FinsibleSize.ExtraLarge -> FinsibleButtonBadgeSpec(
                diameter = d.d20,
                textStyle = type.t10.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}