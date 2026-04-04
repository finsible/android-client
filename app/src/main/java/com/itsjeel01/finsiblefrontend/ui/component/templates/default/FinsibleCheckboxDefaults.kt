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
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleCheckboxVariant
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
        val theme = FinsibleTheme.colors

        val baseCheckedContainer = when (variant) {
            FinsibleCheckboxVariant.Monochrome -> theme.primaryContent
            FinsibleCheckboxVariant.Colorful -> theme.brandAccent
        }
        val baseCheckedBorder = baseCheckedContainer // Checked border intentionally matches filled background.

        val baseCheckedIcon = theme.primaryBackground
        val baseUncheckedContainer = theme.transparent
        val baseUncheckedBorder = theme.border
        val baseUncheckedIcon = theme.transparent

        val baseDisabledCheckedContainer = theme.disabled
        val baseDisabledCheckedBorder = theme.disabled
        val baseDisabledCheckedIcon = theme.disabledContent
        val baseDisabledUncheckedContainer = theme.transparent
        val baseDisabledUncheckedBorder = theme.disabled
        val baseDisabledUncheckedIcon = theme.transparent
        val baseLabelColor = theme.primaryContent
        val baseDisabledLabelColor = theme.disabledContent

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
        val d = FinsibleTheme.dimes
        val type = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.Small -> FinsibleCheckboxSizes(
                boxSize = d.d16,
                cornerRadius = d.d4,
                borderWidth = d.d1,
                checkStrokeWidth = d.d2,
                labelSpacing = d.d8,
                labelTextStyle = type.t14
            )

            FinsibleSize.Medium -> FinsibleCheckboxSizes(
                boxSize = d.d20,
                cornerRadius = d.d5,
                borderWidth = d.d1dot5,
                checkStrokeWidth = d.d2,
                labelSpacing = d.d10,
                labelTextStyle = type.t16
            )

            FinsibleSize.Large -> FinsibleCheckboxSizes(
                boxSize = d.d24,
                cornerRadius = d.d6,
                borderWidth = d.d2,
                checkStrokeWidth = d.d3,
                labelSpacing = d.d12,
                labelTextStyle = type.t20
            )

            else -> error("Invalid checkbox size: $size. Supported sizes are: Small, Medium, and Large.")
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, size: FinsibleSize): Dp {
        val d = FinsibleTheme.dimes
        val resolvedSizes = sizes(size)

        return when (shapeVariant) {
            FinsibleShape.Sharp -> d.d0
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

