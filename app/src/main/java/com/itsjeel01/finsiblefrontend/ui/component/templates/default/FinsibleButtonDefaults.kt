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
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.medium

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
        val s = FinsibleTheme.colors

        val (baseContainer, baseContent, baseBorder) = when (variant) {
            FinsibleButtonVariant.Filled -> Triple(
                s.contentPrimary,
                s.surfaceBase,
                null
            )

            FinsibleButtonVariant.FilledTonal -> Triple(
                s.surfaceDefault,
                s.contentPrimary,
                s.contentPrimary
            )

            FinsibleButtonVariant.Outlined -> Triple(
                Color.Transparent,
                s.contentPrimary,
                s.borderDefault
            )

            FinsibleButtonVariant.Text -> Triple(
                Color.Transparent,
                s.contentPrimary,
                null
            )

            FinsibleButtonVariant.Link -> Triple(
                Color.Transparent,
                s.contentLink,
                null
            )
        }

        val (baseDisabledContainer, baseDisabledContent, baseDisabledBorder) = when (variant) {
            FinsibleButtonVariant.Filled -> Triple(
                s.surfaceSunken,
                s.contentDisabled,
                null
            )

            FinsibleButtonVariant.FilledTonal -> Triple(
                s.surfaceSunken,
                s.contentDisabled,
                s.contentDisabled
            )

            FinsibleButtonVariant.Outlined -> Triple(
                Color.Transparent,
                s.contentDisabled,
                s.contentDisabled
            )

            FinsibleButtonVariant.Text -> Triple(
                Color.Transparent,
                s.contentDisabled,
                null
            )

            FinsibleButtonVariant.Link -> Triple(
                Color.Transparent,
                s.contentDisabled,
                null
            )
        }

        val baseBadgeContainer = s.brandInteractive
        val baseBadgeContent = s.contentPrimary

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
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleButtonSizes(
                contentPadding = PaddingValues(horizontal = sp.insetSm - sp.insetXs / 2, vertical = sp.insetXs / 2),
                textStyle = t.bodySm.medium(),
                iconSize = FinsibleTheme.sizes.icon.xs,
                iconSpacing = sp.insetXs,
            )

            FinsibleSize.Small -> FinsibleButtonSizes(
                contentPadding = PaddingValues(horizontal = sp.gapSm + sp.insetXs / 2, vertical = sp.insetXs),
                textStyle = t.bodyLg.medium(),
                iconSize = FinsibleTheme.sizes.icon.md,
                iconSpacing = sp.insetXs
            )

            FinsibleSize.Medium -> FinsibleButtonSizes(
                contentPadding = PaddingValues(horizontal = sp.insetLg, vertical = sp.insetSm - sp.insetXs / 2),
                textStyle = t.bodyLg.copy(fontWeight = FontWeight.Medium),
                iconSize = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                iconSpacing = sp.insetSm - sp.insetXs / 2
            )

            FinsibleSize.Large -> FinsibleButtonSizes(
                contentPadding = PaddingValues(horizontal = sp.insetXl, vertical = sp.inlineMd),
                textStyle = t.headingSm.medium(),
                iconSize = FinsibleTheme.sizes.icon.lg,
                iconSpacing = sp.insetSm - sp.insetXs / 2
            )

            FinsibleSize.ExtraLarge -> FinsibleButtonSizes(
                contentPadding = PaddingValues(horizontal = sp.inset2xl + sp.insetXs, vertical = sp.gapSm + sp.insetXs / 2),
                textStyle = t.headingMd.medium(),
                iconSize = FinsibleTheme.sizes.icon.xl - sp.insetXs,
                iconSpacing = sp.gapSm + sp.insetXs / 2
            )
        }
    }

    @Composable
    fun iconOnlySize(size: FinsibleSize): Dp {
        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleTheme.sizes.touch.xs
            FinsibleSize.Small      -> FinsibleTheme.sizes.touch.sm
            FinsibleSize.Medium     -> FinsibleTheme.sizes.touch.md
            FinsibleSize.Large      -> FinsibleTheme.sizes.touch.lg
            FinsibleSize.ExtraLarge -> FinsibleTheme.sizes.touch.xl
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, size: FinsibleSize): Dp {
        return when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.radius.none
            FinsibleShape.Rounded -> when (size) {
                FinsibleSize.ExtraSmall -> FinsibleTheme.radius.xs
                FinsibleSize.Small      -> FinsibleTheme.radius.sm
                FinsibleSize.Medium     -> FinsibleTheme.radius.md
                FinsibleSize.Large      -> FinsibleRadius.md
                FinsibleSize.ExtraLarge -> FinsibleTheme.radius.lg
            }

            FinsibleShape.Circle,
            FinsibleShape.Pill -> when (size) {
                FinsibleSize.ExtraSmall -> FinsibleTheme.sizes.icon.xs
                FinsibleSize.Small      -> FinsibleTheme.sizes.icon.md
                FinsibleSize.Medium     -> FinsibleTheme.sizes.icon.lg
                FinsibleSize.Large      -> FinsibleTheme.sizes.icon.xl - FinsibleTheme.spacing.insetXs
                FinsibleSize.ExtraLarge -> FinsibleTheme.sizes.icon.xl
            }
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
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleButtonBadgeSpec(
                diameter = sp.gapSm + sp.insetXs / 2,
                textStyle = t.labelSm.bold()
            )

            FinsibleSize.Small -> FinsibleButtonBadgeSpec(
                diameter = sp.gapMd,
                textStyle = t.labelSm.copy(fontWeight = FontWeight.Bold)
            )

            FinsibleSize.Medium -> FinsibleButtonBadgeSpec(
                diameter = sp.insetLg,
                textStyle = t.caption.bold()
            )

            FinsibleSize.Large -> FinsibleButtonBadgeSpec(
                diameter = sp.insetLg + sp.insetXs / 2,
                textStyle = t.caption.copy(fontWeight = FontWeight.Bold)
            )

            FinsibleSize.ExtraLarge -> FinsibleButtonBadgeSpec(
                diameter = sp.insetXl,
                textStyle = t.caption.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
