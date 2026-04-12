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
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleIconBadgeColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleIconBadgeSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for `FinsibleIconBadge`. */
object FinsibleIconBadgeDefaults {

    const val DEFAULT_BACKGROUND_ALPHA: Float = 0.14f

    @Composable
    fun colors(
        iconTint: Color = Color.Unspecified,
        backgroundTint: Color = Color.Unspecified
    ): FinsibleIconBadgeColors {
        val theme = FinsibleTheme.colors
        val baseIconTint = theme.primaryContent
        val baseBackgroundTint = theme.primaryContent

        return FinsibleIconBadgeColors(
            iconTint = if (iconTint != Color.Unspecified) iconTint else baseIconTint,
            backgroundColor = if (backgroundTint != Color.Unspecified) backgroundTint else baseBackgroundTint
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleIconBadgeSizes {
        val d = FinsibleTheme.dimes

        fun containerSizeFor(iconSize: Dp): Dp {
            return iconSize.times(2.5f)
        }

        return when (size) {
            FinsibleSize.Small -> FinsibleIconBadgeSizes(
                containerSize = containerSizeFor(d.d12),
                iconSize = d.d12,
                roundedCornerRadius = d.d6
            )

            FinsibleSize.Medium -> FinsibleIconBadgeSizes(
                containerSize = containerSizeFor(d.d16),
                iconSize = d.d16,
                roundedCornerRadius = d.d8
            )

            FinsibleSize.Large -> FinsibleIconBadgeSizes(
                containerSize = containerSizeFor(d.d20),
                iconSize = d.d20,
                roundedCornerRadius = d.d10
            )

            else -> error("Invalid icon badge size: $size. Supported sizes are: Small, Medium, and Large.")
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, sizes: FinsibleIconBadgeSizes): Dp {
        val d = FinsibleTheme.dimes

        return when (shapeVariant) {
            FinsibleShape.Circle -> sizes.containerSize / 2
            FinsibleShape.Rounded -> sizes.roundedCornerRadius
            FinsibleShape.Sharp -> d.d0
            else -> error("Invalid shape variant: $shapeVariant. Supported variants are: Circle, Rounded, and Sharp.")
        }
    }

    @Composable
    fun cornerRadius(shapeVariant: FinsibleShape, size: FinsibleSize): Dp {
        return cornerRadius(shapeVariant = shapeVariant, sizes = sizes(size))
    }

    @Composable
    fun shape(shapeVariant: FinsibleShape, sizes: FinsibleIconBadgeSizes): Shape {
        return when (shapeVariant) {
            FinsibleShape.Circle -> CircleShape
            FinsibleShape.Rounded -> RoundedCornerShape(cornerRadius(shapeVariant, sizes))
            FinsibleShape.Sharp -> RectangleShape
            else -> error("Invalid shape variant: $shapeVariant. Supported variants are: Circle, Rounded, and Sharp.")
        }
    }

    @Composable
    fun shape(shapeVariant: FinsibleShape, size: FinsibleSize): Shape {
        return shape(shapeVariant = shapeVariant, sizes = sizes(size))
    }
}




