package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleFilterChipColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleFilterChipSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for `FinsibleFilterChip`. */
object FinsibleFilterChipDefaults {

    @Composable
    fun colors(
        selectedContainerColor: Color = Color.Unspecified,
        selectedLabelColor: Color = Color.Unspecified,
        selectedIconTint: Color = Color.Unspecified,
        selectedBorderColor: Color = Color.Unspecified,
        unselectedContainerColor: Color = Color.Unspecified,
        unselectedLabelColor: Color = Color.Unspecified,
        unselectedIconTint: Color = Color.Unspecified,
        unselectedBorderColor: Color = Color.Unspecified,
        disabledSelectedContainerColor: Color = Color.Unspecified,
        disabledSelectedLabelColor: Color = Color.Unspecified,
        disabledSelectedIconTint: Color = Color.Unspecified,
        disabledSelectedBorderColor: Color = Color.Unspecified,
        disabledUnselectedContainerColor: Color = Color.Unspecified,
        disabledUnselectedLabelColor: Color = Color.Unspecified,
        disabledUnselectedIconTint: Color = Color.Unspecified,
        disabledUnselectedBorderColor: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified,
        variant: FinsibleFilterChipVariant = FinsibleFilterChipVariant.Tonal
    ): FinsibleFilterChipColors {
        val s = FinsibleTheme.colors

        val baseSelectedContainer = when (variant) {
            FinsibleFilterChipVariant.Filled -> s.brandInteractive
            FinsibleFilterChipVariant.Tonal -> s.surfaceBrandTint
            FinsibleFilterChipVariant.Outlined -> Color.Transparent
            FinsibleFilterChipVariant.OutlinedTonal -> s.surfaceBrandTint
        }
        val baseSelectedLabel = s.contentPrimary
        val baseSelectedIcon = s.contentPrimary
        val baseSelectedBorder = when (variant) {
            FinsibleFilterChipVariant.Outlined,
            FinsibleFilterChipVariant.OutlinedTonal -> s.brandInteractive
            FinsibleFilterChipVariant.Filled,
            FinsibleFilterChipVariant.Tonal -> Color.Transparent
        }

        val baseUnselectedContainer = Color.Transparent
        val baseUnselectedLabel = s.contentPrimary
        val baseUnselectedIcon = s.contentSecondary
        val baseUnselectedBorder = s.borderDefault

        val baseDisabledSelectedContainer = s.surfaceSunken
        val baseDisabledSelectedLabel = s.contentDisabled
        val baseDisabledSelectedIcon = s.contentDisabled
        val baseDisabledSelectedBorder = when (variant) {
            FinsibleFilterChipVariant.Outlined,
            FinsibleFilterChipVariant.OutlinedTonal -> s.surfaceSunken
            FinsibleFilterChipVariant.Filled,
            FinsibleFilterChipVariant.Tonal -> Color.Transparent
        }

        val baseDisabledUnselectedContainer = Color.Transparent
        val baseDisabledUnselectedLabel = s.contentDisabled
        val baseDisabledUnselectedIcon = s.contentDisabled
        val baseDisabledUnselectedBorder = s.surfaceSunken

        return FinsibleFilterChipColors(
            selectedContainerColor = if (selectedContainerColor != Color.Unspecified) selectedContainerColor else baseSelectedContainer,
            selectedLabelColor = if (selectedLabelColor != Color.Unspecified) selectedLabelColor else baseSelectedLabel,
            selectedIconTint = if (selectedIconTint != Color.Unspecified) selectedIconTint else baseSelectedIcon,
            selectedBorderColor = if (selectedBorderColor != Color.Unspecified) selectedBorderColor else baseSelectedBorder,
            unselectedContainerColor = if (unselectedContainerColor != Color.Unspecified) unselectedContainerColor else baseUnselectedContainer,
            unselectedLabelColor = if (unselectedLabelColor != Color.Unspecified) unselectedLabelColor else baseUnselectedLabel,
            unselectedIconTint = if (unselectedIconTint != Color.Unspecified) unselectedIconTint else baseUnselectedIcon,
            unselectedBorderColor = if (unselectedBorderColor != Color.Unspecified) unselectedBorderColor else baseUnselectedBorder,
            disabledSelectedContainerColor = if (disabledSelectedContainerColor != Color.Unspecified) disabledSelectedContainerColor else baseDisabledSelectedContainer,
            disabledSelectedLabelColor = if (disabledSelectedLabelColor != Color.Unspecified) disabledSelectedLabelColor else baseDisabledSelectedLabel,
            disabledSelectedIconTint = if (disabledSelectedIconTint != Color.Unspecified) disabledSelectedIconTint else baseDisabledSelectedIcon,
            disabledSelectedBorderColor = if (disabledSelectedBorderColor != Color.Unspecified) disabledSelectedBorderColor else baseDisabledSelectedBorder,
            disabledUnselectedContainerColor = if (disabledUnselectedContainerColor != Color.Unspecified) disabledUnselectedContainerColor else baseDisabledUnselectedContainer,
            disabledUnselectedLabelColor = if (disabledUnselectedLabelColor != Color.Unspecified) disabledUnselectedLabelColor else baseDisabledUnselectedLabel,
            disabledUnselectedIconTint = if (disabledUnselectedIconTint != Color.Unspecified) disabledUnselectedIconTint else baseDisabledUnselectedIcon,
            disabledUnselectedBorderColor = if (disabledUnselectedBorderColor != Color.Unspecified) disabledUnselectedBorderColor else baseDisabledUnselectedBorder,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else s.contentPrimary.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun selectedTintContentColor(inverted: Boolean): Color {
        val s = FinsibleTheme.colors
        return if (inverted) s.surfaceBase else s.contentPrimary
    }

    fun applySelectedTint(
        colors: FinsibleFilterChipColors,
        selectedTint: Color,
        variant: FinsibleFilterChipVariant,
        selectedContentColor: Color
    ): FinsibleFilterChipColors {
        if (selectedTint == Color.Unspecified) return colors

        val selectedContainer = FinsibleSelectableTintDefaults.selectedContainerColor(
            selectedTint = selectedTint,
            variant = variant.toSelectableTintVariant()
        )

        return colors.copy(
            selectedContainerColor = selectedContainer,
            selectedLabelColor = selectedContentColor,
            selectedIconTint = selectedContentColor,
            selectedBorderColor = selectedTint
        )
    }

    private fun FinsibleFilterChipVariant.toSelectableTintVariant(): FinsibleSelectableTintVariant {
        return when (this) {
            FinsibleFilterChipVariant.Filled -> FinsibleSelectableTintVariant.Filled
            FinsibleFilterChipVariant.Tonal -> FinsibleSelectableTintVariant.Tonal
            FinsibleFilterChipVariant.Outlined -> FinsibleSelectableTintVariant.Outlined
            FinsibleFilterChipVariant.OutlinedTonal -> FinsibleSelectableTintVariant.OutlinedTonal
        }
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleFilterChipSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.Small -> FinsibleFilterChipSizes(
                horizontalPadding = sp.gapSm + sp.insetXs / 2,
                verticalPadding = sp.insetSm - sp.insetXs / 2,
                textStyle = t.bodyMd.medium(),
                iconSize = FinsibleTheme.sizes.icon.sm,
                iconSpacing = sp.insetSm - sp.insetXs / 2,
                borderWidth = FinsibleTheme.stroke.thin,
                cornerRadius = FinsibleTheme.radius.sm
            )

            FinsibleSize.Medium -> FinsibleFilterChipSizes(
                horizontalPadding = sp.gapMd,
                verticalPadding = sp.inlineMd,
                textStyle = t.bodyLg.medium(),
                iconSize = FinsibleTheme.sizes.icon.md,
                iconSpacing = sp.inlineMd,
                borderWidth = FinsibleTheme.stroke.thin,
                cornerRadius = FinsibleTheme.radius.md
            )

            FinsibleSize.Large -> FinsibleFilterChipSizes(
                horizontalPadding = sp.insetLg,
                verticalPadding = sp.gapSm + sp.insetXs / 2,
                textStyle = t.bodyLg.medium(),
                iconSize = FinsibleTheme.sizes.icon.md,
                iconSpacing = sp.inlineMd,
                borderWidth = FinsibleTheme.stroke.semiBold,
                cornerRadius = FinsibleRadius.md
            )

            FinsibleSize.ExtraSmall,
            FinsibleSize.ExtraLarge -> error("FinsibleFilterChip supports only Small, Medium, and Large sizes.")
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, chipSizes: FinsibleFilterChipSizes): Dp {
        return when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.radius.none
            FinsibleShape.Rounded -> chipSizes.cornerRadius
            FinsibleShape.Pill -> (chipSizes.iconSize / 2) + chipSizes.verticalPadding
            FinsibleShape.Circle -> error("FinsibleFilterChip does not support Circle shape.")
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, size: FinsibleSize): Dp {
        return cornerRadius(shapeVariant = shapeVariant, chipSizes = sizes(size))
    }

    @Composable
    fun shape(shapeVariant: FinsibleShape, chipSizes: FinsibleFilterChipSizes): Shape {
        return when (shapeVariant) {
            FinsibleShape.Pill -> CircleShape
            FinsibleShape.Rounded -> RoundedCornerShape(cornerRadius(shapeVariant, chipSizes))
            FinsibleShape.Sharp -> RectangleShape
            FinsibleShape.Circle -> error("FinsibleFilterChip does not support Circle shape.")
        }
    }

    @Composable
    fun shape(shapeVariant: FinsibleShape, size: FinsibleSize): Shape {
        return shape(shapeVariant = shapeVariant, chipSizes = sizes(size))
    }
}
