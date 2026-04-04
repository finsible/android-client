package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleAccordionColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleAccordionSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for accordion component. */
object FinsibleAccordionDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        titleColor: Color = Color.Unspecified,
        subtitleColor: Color = Color.Unspecified,
        iconTint: Color = Color.Unspecified,
        disabledIconTint: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified
    ): FinsibleAccordionColors {
        val colors = FinsibleTheme.colors

        return FinsibleAccordionColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else colors.secondaryBackground,
            titleColor = if (titleColor != Color.Unspecified) titleColor else colors.primaryContent,
            subtitleColor = if (subtitleColor != Color.Unspecified) subtitleColor else colors.secondaryContent,
            iconTint = if (iconTint != Color.Unspecified) iconTint else colors.secondaryContent,
            disabledIconTint = if (disabledIconTint != Color.Unspecified) disabledIconTint else colors.disabledContent,
            borderColor = if (borderColor != Color.Unspecified) borderColor else colors.border,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else colors.primaryContent.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize, shapeVariant: FinsibleShape = FinsibleShape.Rounded): FinsibleAccordionSizes {
        val d = FinsibleTheme.dimes
        val t = FinsibleTheme.typography

        val base = when (size) {
            FinsibleSize.Small -> FinsibleAccordionSizes(
                cornerRadius = d.d10,
                padding = d.d12,
                titleStyle = t.t16.medium(),
                subtitleStyle = t.t12,
                iconSize = d.d18,
                spacing = d.d8
            )

            FinsibleSize.Medium -> FinsibleAccordionSizes(
                cornerRadius = d.d12,
                padding = d.d14,
                titleStyle = t.t18.medium(),
                subtitleStyle = t.t12,
                iconSize = d.d20,
                spacing = d.d10
            )

            FinsibleSize.Large -> FinsibleAccordionSizes(
                cornerRadius = d.d14,
                padding = d.d16,
                titleStyle = t.t20.medium(),
                subtitleStyle = t.t14,
                iconSize = d.d24,
                spacing = d.d12
            )

            else -> error("Unsupported size: $size. Accordion only supports Small, Medium, and Large sizes.")
        }

        val corner = when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.dimes.d0
            FinsibleShape.Rounded -> base.cornerRadius
            else -> error("Unsupported shape variant: $shapeVariant. Accordion only supports Rounded and Sharp shape variants.")
        }

        return base.copy(cornerRadius = corner)
    }
}

