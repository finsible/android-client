package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleFilterChipColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleFilterChipSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for `FinsibleFilterChip`. */
object FinsibleFilterChipDefaults {

    private const val SELECTED_TINT_TONAL_CONTAINER_ALPHA = 0.16f
    private const val SELECTED_TINT_OUTLINED_TONAL_CONTAINER_ALPHA = 0.1f
    private const val SELECTED_TINT_RIPPLE_ALPHA = 0.12f

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
        val theme = FinsibleTheme.colors

        val baseSelectedContainer = when (variant) {
            FinsibleFilterChipVariant.Filled -> theme.brandAccent
            FinsibleFilterChipVariant.Tonal -> theme.selection
            FinsibleFilterChipVariant.Outlined -> theme.transparent
            FinsibleFilterChipVariant.OutlinedTonal -> theme.selection
        }
        val baseSelectedLabel = theme.primaryContent
        val baseSelectedIcon = theme.primaryContent
        val baseSelectedBorder = theme.brandAccent

        val baseUnselectedContainer = theme.transparent
        val baseUnselectedLabel = theme.primaryContent
        val baseUnselectedIcon = theme.secondaryContent
        val baseUnselectedBorder = theme.border

        val baseDisabledSelectedContainer = theme.disabled
        val baseDisabledSelectedLabel = theme.disabledContent
        val baseDisabledSelectedIcon = theme.disabledContent
        val baseDisabledSelectedBorder = theme.disabled

        val baseDisabledUnselectedContainer = theme.transparent
        val baseDisabledUnselectedLabel = theme.disabledContent
        val baseDisabledUnselectedIcon = theme.disabledContent
        val baseDisabledUnselectedBorder = theme.disabled

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
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else baseSelectedBorder.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun selectedTintContentColor(inverted: Boolean): Color {
        val theme = FinsibleTheme.colors
        return if (inverted) theme.primaryBackground else theme.primaryContent
    }

    fun applySelectedTint(
        colors: FinsibleFilterChipColors,
        selectedTint: Color,
        variant: FinsibleFilterChipVariant,
        selectedContentColor: Color
    ): FinsibleFilterChipColors {
        if (selectedTint == Color.Unspecified) return colors

        val selectedContainer = when (variant) {
            FinsibleFilterChipVariant.Filled -> selectedTint
            FinsibleFilterChipVariant.Tonal -> selectedTint.copy(alpha = SELECTED_TINT_TONAL_CONTAINER_ALPHA)
            FinsibleFilterChipVariant.Outlined -> Color.Transparent
            FinsibleFilterChipVariant.OutlinedTonal ->
                selectedTint.copy(alpha = SELECTED_TINT_OUTLINED_TONAL_CONTAINER_ALPHA)
        }

        return colors.copy(
            selectedContainerColor = selectedContainer,
            selectedLabelColor = selectedContentColor,
            selectedIconTint = selectedContentColor,
            selectedBorderColor = selectedTint,
            rippleColor = selectedTint.copy(alpha = SELECTED_TINT_RIPPLE_ALPHA)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleFilterChipSizes {
        val d = FinsibleTheme.dimes
        val type = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.Small -> FinsibleFilterChipSizes(
                height = d.d28,
                contentPadding = PaddingValues(horizontal = d.d10, vertical = d.d6),
                textStyle = type.t14.copy(fontWeight = FontWeight.Medium),
                iconSize = d.d14,
                iconSpacing = d.d6,
                borderWidth = d.d1,
                cornerRadius = d.d8
            )

            FinsibleSize.Medium -> FinsibleFilterChipSizes(
                height = d.d36,
                contentPadding = PaddingValues(horizontal = d.d12, vertical = d.d8),
                textStyle = type.t16.copy(fontWeight = FontWeight.Medium),
                iconSize = d.d16,
                iconSpacing = d.d8,
                borderWidth = d.d1,
                cornerRadius = d.d10
            )

            FinsibleSize.Large -> FinsibleFilterChipSizes(
                height = d.d44,
                contentPadding = PaddingValues(horizontal = d.d16, vertical = d.d10),
                textStyle = type.t18.copy(fontWeight = FontWeight.Medium),
                iconSize = d.d18,
                iconSpacing = d.d8,
                borderWidth = d.d1dot5,
                cornerRadius = d.d12
            )

            FinsibleSize.ExtraSmall,
            FinsibleSize.ExtraLarge -> error("FinsibleFilterChip supports only Small, Medium, and Large sizes.")
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, chipSizes: FinsibleFilterChipSizes): Dp {
        val d = FinsibleTheme.dimes

        return when (shapeVariant) {
            FinsibleShape.Sharp -> d.d0
            FinsibleShape.Rounded -> chipSizes.cornerRadius
            FinsibleShape.Pill -> chipSizes.height / 2
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
            FinsibleShape.Pill -> RoundedCornerShape(cornerRadius(shapeVariant, chipSizes))
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

