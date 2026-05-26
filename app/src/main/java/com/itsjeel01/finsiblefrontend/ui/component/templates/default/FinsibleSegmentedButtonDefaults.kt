package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for segmented button rows. */
object FinsibleSegmentedButtonDefaults {

    @Composable
    fun colors(
        selectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContainerColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified,
        variant: FinsibleSegmentedButtonVariant = FinsibleSegmentedButtonVariant.Filled
    ): FinsibleSegmentedButtonColors {
        val s = FinsibleTheme.colors

        val baseSelectedContainer = when (variant) {
            FinsibleSegmentedButtonVariant.Filled -> s.contentPrimary
            FinsibleSegmentedButtonVariant.Tonal -> s.surfaceBrandTint
        }
        val baseSelectedContent = when (variant) {
            FinsibleSegmentedButtonVariant.Filled -> s.surfaceBase
            FinsibleSegmentedButtonVariant.Tonal -> s.contentPrimary
        }
        val baseUnselectedContainer = Color.Transparent

        return FinsibleSegmentedButtonColors(
            selectedContainerColor = if (selectedContainerColor != Color.Unspecified) selectedContainerColor else baseSelectedContainer,
            selectedContentColor = if (selectedContentColor != Color.Unspecified) selectedContentColor else baseSelectedContent,
            unselectedContainerColor = if (unselectedContainerColor != Color.Unspecified) unselectedContainerColor else baseUnselectedContainer,
            unselectedContentColor = if (unselectedContentColor != Color.Unspecified) unselectedContentColor else s.contentPrimary,
            disabledContainerColor = if (disabledContainerColor != Color.Unspecified) disabledContainerColor else s.surfaceSunken,
            disabledContentColor = if (disabledContentColor != Color.Unspecified) disabledContentColor else s.contentDisabled,
            borderColor = if (borderColor != Color.Unspecified) borderColor else s.borderDefault,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else s.contentPrimary.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun selectedTintContentColor(inverted: Boolean): Color {
        val s = FinsibleTheme.colors
        return if (inverted) s.surfaceBase else s.contentPrimary
    }

    fun applySelectedTint(
        colors: FinsibleSegmentedButtonColors,
        selectedTint: Color,
        variant: FinsibleSegmentedButtonVariant,
        selectedContentColor: Color
    ): FinsibleSegmentedButtonColors {
        if (selectedTint == Color.Unspecified) return colors

        val selectedContainer = FinsibleSelectableTintDefaults.selectedContainerColor(
            selectedTint = selectedTint,
            variant = variant.toSelectableTintVariant()
        )

        return colors.copy(
            selectedContainerColor = selectedContainer,
            selectedContentColor = selectedContentColor
        )
    }

    private fun FinsibleSegmentedButtonVariant.toSelectableTintVariant(): FinsibleSelectableTintVariant {
        return when (this) {
            FinsibleSegmentedButtonVariant.Filled -> FinsibleSelectableTintVariant.Filled
            FinsibleSegmentedButtonVariant.Tonal -> FinsibleSelectableTintVariant.Tonal
        }
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleSegmentedButtonSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleSegmentedButtonSizes(
                textStyle = t.bodySm.medium(),
                iconSize = FinsibleTheme.sizes.icon.xs,
                iconSpacing = sp.insetXs,
                horizontalPadding = sp.gapSm + sp.insetXs / 2,
                verticalPadding = sp.insetSm - sp.insetXs / 2
            )

            FinsibleSize.Small -> FinsibleSegmentedButtonSizes(
                textStyle = t.bodyMd.medium(),
                iconSize = FinsibleTheme.sizes.icon.sm,
                iconSpacing = sp.insetSm - sp.insetXs / 2,
                horizontalPadding = sp.gapMd,
                verticalPadding = sp.inlineMd
            )

            FinsibleSize.Medium -> FinsibleSegmentedButtonSizes(
                textStyle = t.bodyLg.medium(),
                iconSize = FinsibleTheme.sizes.icon.md,
                iconSpacing = sp.inlineMd,
                horizontalPadding = sp.insetLg - sp.insetXs / 2,
                verticalPadding = sp.gapSm + sp.insetXs / 2
            )

            FinsibleSize.Large -> FinsibleSegmentedButtonSizes(
                textStyle = t.bodyLg.medium(),
                iconSize = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                iconSpacing = sp.gapSm + sp.insetXs / 2,
                horizontalPadding = sp.insetLg,
                verticalPadding = sp.gapMd
            )

            FinsibleSize.ExtraLarge -> FinsibleSegmentedButtonSizes(
                textStyle = t.headingSm,
                iconSize = FinsibleTheme.sizes.icon.lg,
                iconSpacing = sp.gapMd,
                horizontalPadding = sp.insetLg + sp.insetXs / 2,
                verticalPadding = sp.insetLg - sp.insetXs / 2
            )
        }
    }
}
