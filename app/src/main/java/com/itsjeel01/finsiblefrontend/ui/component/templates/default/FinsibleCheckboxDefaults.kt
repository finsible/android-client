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
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleCheckboxColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleCheckboxSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleCheckboxVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for `FinsibleCheckbox`. */
object FinsibleCheckboxDefaults {

    @Composable
    fun colors(
        variant: FinsibleCheckboxVariant,
        checkedContainerColor: Color = Color.Unspecified,
        checkedBorderColor: Color = Color.Unspecified,
        checkedIconColor: Color = Color.Unspecified,
        uncheckedContainerColor: Color = Color.Unspecified,
        uncheckedBorderColor: Color = Color.Unspecified,
        uncheckedIconColor: Color = Color.Unspecified,
        disabledCheckedContainerColor: Color = Color.Unspecified,
        disabledCheckedBorderColor: Color = Color.Unspecified,
        disabledCheckedIconColor: Color = Color.Unspecified,
        disabledUncheckedContainerColor: Color = Color.Unspecified,
        disabledUncheckedBorderColor: Color = Color.Unspecified,
        disabledUncheckedIconColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        disabledLabelColor: Color = Color.Unspecified,
    ): FinsibleCheckboxColors {
        val s = FinsibleTheme.colors

        val baseCheckedContainer = when (variant) {
            FinsibleCheckboxVariant.Monochrome -> s.contentPrimary
            FinsibleCheckboxVariant.Colorful -> s.brandInteractive
        }
        val baseCheckedBorder = baseCheckedContainer
        val baseCheckedIcon = s.surfaceBase
        val baseUncheckedContainer = Color.Transparent
        val baseUncheckedBorder = s.borderDefault
        val baseUncheckedIcon = Color.Transparent
        val baseDisabledCheckedContainer = s.surfaceSunken
        val baseDisabledCheckedBorder = s.surfaceSunken
        val baseDisabledCheckedIcon = s.contentDisabled
        val baseDisabledUncheckedContainer = Color.Transparent
        val baseDisabledUncheckedBorder = s.surfaceSunken
        val baseDisabledUncheckedIcon = Color.Transparent
        val baseLabelColor = s.contentPrimary
        val baseDisabledLabelColor = s.contentDisabled

        return FinsibleCheckboxColors(
            checkedContainerColor = if (checkedContainerColor != Color.Unspecified) checkedContainerColor else baseCheckedContainer,
            checkedBorderColor = if (checkedBorderColor != Color.Unspecified) checkedBorderColor else baseCheckedBorder,
            checkedIconColor = if (checkedIconColor != Color.Unspecified) checkedIconColor else baseCheckedIcon,
            uncheckedContainerColor = if (uncheckedContainerColor != Color.Unspecified) uncheckedContainerColor else baseUncheckedContainer,
            uncheckedBorderColor = if (uncheckedBorderColor != Color.Unspecified) uncheckedBorderColor else baseUncheckedBorder,
            uncheckedIconColor = if (uncheckedIconColor != Color.Unspecified) uncheckedIconColor else baseUncheckedIcon,
            disabledCheckedContainerColor = if (disabledCheckedContainerColor != Color.Unspecified) disabledCheckedContainerColor else baseDisabledCheckedContainer,
            disabledCheckedBorderColor = if (disabledCheckedBorderColor != Color.Unspecified) disabledCheckedBorderColor else baseDisabledCheckedBorder,
            disabledCheckedIconColor = if (disabledCheckedIconColor != Color.Unspecified) disabledCheckedIconColor else baseDisabledCheckedIcon,
            disabledUncheckedContainerColor = if (disabledUncheckedContainerColor != Color.Unspecified) disabledUncheckedContainerColor else baseDisabledUncheckedContainer,
            disabledUncheckedBorderColor = if (disabledUncheckedBorderColor != Color.Unspecified) disabledUncheckedBorderColor else baseDisabledUncheckedBorder,
            disabledUncheckedIconColor = if (disabledUncheckedIconColor != Color.Unspecified) disabledUncheckedIconColor else baseDisabledUncheckedIcon,
            labelColor = if (labelColor != Color.Unspecified) labelColor else baseLabelColor,
            disabledLabelColor = if (disabledLabelColor != Color.Unspecified) disabledLabelColor else baseDisabledLabelColor,
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleCheckboxSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.Small -> FinsibleCheckboxSizes(
                boxSize = FinsibleTheme.sizes.icon.md,
            cornerRadius = FinsibleTheme.radius.xs,
                borderWidth = FinsibleTheme.stroke.thin,
                checkStrokeWidth = FinsibleTheme.stroke.bold,
                labelSpacing = sp.inlineMd,
                labelTextStyle = t.bodyMd
            )

            FinsibleSize.Medium -> FinsibleCheckboxSizes(
                boxSize = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                cornerRadius = FinsibleRadius.xs,
                borderWidth = FinsibleTheme.stroke.semiBold,
                checkStrokeWidth = FinsibleTheme.stroke.bold,
                labelSpacing = sp.gapSm + sp.insetXs / 2,
                labelTextStyle = t.bodyLg
            )

            FinsibleSize.Large -> FinsibleCheckboxSizes(
                boxSize = FinsibleTheme.sizes.icon.lg,
                cornerRadius = FinsibleRadius.xs,
                borderWidth = FinsibleTheme.stroke.bold,
                checkStrokeWidth = FinsibleTheme.stroke.heavy,
                labelSpacing = sp.gapMd,
                labelTextStyle = t.headingSm
            )

            else -> error("Invalid checkbox size: $size. Supported sizes are: Small, Medium, and Large.")
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, size: FinsibleSize): Dp {
        val resolvedSizes = sizes(size)

        return when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.radius.none
            FinsibleShape.Rounded -> resolvedSizes.cornerRadius
            else -> error("Invalid shape variant: $shapeVariant. Supported variants are: Rounded and Sharp.")
        }
    }

    @Composable
    fun shape(shapeVariant: FinsibleShape, size: FinsibleSize): Shape {
        return when (shapeVariant) {
            FinsibleShape.Circle,
            FinsibleShape.Pill -> CircleShape
            FinsibleShape.Rounded -> RoundedCornerShape(cornerRadius(shapeVariant, size))
            FinsibleShape.Sharp -> RectangleShape
        }
    }
}
