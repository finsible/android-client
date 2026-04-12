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
        val theme = FinsibleTheme.colors

        val baseSelectedContainer = when (variant) {
            FinsibleSegmentedButtonVariant.Filled -> theme.primaryContent
            FinsibleSegmentedButtonVariant.Tonal -> theme.selection
        }
        val baseSelectedContent = when (variant) {
            FinsibleSegmentedButtonVariant.Filled -> theme.primaryBackground
            FinsibleSegmentedButtonVariant.Tonal -> theme.primaryContent
        }
        val baseUnselectedContainer = theme.transparent

        return FinsibleSegmentedButtonColors(
            selectedContainerColor = if (selectedContainerColor != Color.Unspecified) selectedContainerColor else baseSelectedContainer,
            selectedContentColor = if (selectedContentColor != Color.Unspecified) selectedContentColor else baseSelectedContent,
            unselectedContainerColor = if (unselectedContainerColor != Color.Unspecified) unselectedContainerColor else baseUnselectedContainer,
            unselectedContentColor = if (unselectedContentColor != Color.Unspecified) unselectedContentColor else theme.primaryContent,
            disabledContainerColor = if (disabledContainerColor != Color.Unspecified) disabledContainerColor else theme.disabled,
            disabledContentColor = if (disabledContentColor != Color.Unspecified) disabledContentColor else theme.disabledContent,
            borderColor = if (borderColor != Color.Unspecified) borderColor else theme.border,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else theme.ripple
        )
    }

    @Composable
    fun selectedTintContentColor(inverted: Boolean): Color {
        val theme = FinsibleTheme.colors
        return if (inverted) theme.primaryBackground else theme.primaryContent
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
        val d = FinsibleTheme.dimes
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleSegmentedButtonSizes(
                textStyle = t.t12.medium(),
                iconSize = d.d12,
                iconSpacing = d.d4,
                horizontalPadding = d.d10,
                verticalPadding = d.d6
            )

            FinsibleSize.Small -> FinsibleSegmentedButtonSizes(
                textStyle = t.t14.medium(),
                iconSize = d.d14,
                iconSpacing = d.d6,
                horizontalPadding = d.d12,
                verticalPadding = d.d8
            )

            FinsibleSize.Medium -> FinsibleSegmentedButtonSizes(
                textStyle = t.t16.medium(),
                iconSize = d.d18,
                iconSpacing = d.d8,
                horizontalPadding = d.d14,
                verticalPadding = d.d10
            )

            FinsibleSize.Large -> FinsibleSegmentedButtonSizes(
                textStyle = t.t18.medium(),
                iconSize = d.d20,
                iconSpacing = d.d10,
                horizontalPadding = d.d16,
                verticalPadding = d.d12
            )

            FinsibleSize.ExtraLarge -> FinsibleSegmentedButtonSizes(
                textStyle = t.t20.medium(),
                iconSize = d.d24,
                iconSpacing = d.d12,
                horizontalPadding = d.d18,
                verticalPadding = d.d14
            )
        }
    }
}

