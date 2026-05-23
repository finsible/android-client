package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleAccordionColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleAccordionSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleRadius
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
        val s = FinsibleTheme.colors

        return FinsibleAccordionColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else s.surfaceDefault,
            titleColor = if (titleColor != Color.Unspecified) titleColor else s.contentPrimary,
            subtitleColor = if (subtitleColor != Color.Unspecified) subtitleColor else s.contentSecondary,
            iconTint = if (iconTint != Color.Unspecified) iconTint else s.contentSecondary,
            disabledIconTint = if (disabledIconTint != Color.Unspecified) disabledIconTint else s.contentDisabled,
            borderColor = if (borderColor != Color.Unspecified) borderColor else s.borderDefault,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else s.contentPrimary.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize, shapeVariant: FinsibleShape = FinsibleShape.Rounded): FinsibleAccordionSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        val base = when (size) {
            FinsibleSize.Small -> FinsibleAccordionSizes(
                cornerRadius = FinsibleTheme.radius.sm,
                padding = sp.insetMd,
                titleStyle = t.bodyLg.medium(),
                subtitleStyle = t.bodySm,
                iconSize = FinsibleTheme.sizes.icon.md,
                spacing = sp.inlineMd
            )

            FinsibleSize.Medium -> FinsibleAccordionSizes(
                cornerRadius = FinsibleTheme.radius.md,
                padding = sp.insetLg - sp.insetXs / 2,
                titleStyle = t.bodyLg.medium(),
                subtitleStyle = t.bodySm,
                iconSize = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                spacing = sp.inlineMd
            )

            FinsibleSize.Large -> FinsibleAccordionSizes(
                cornerRadius = FinsibleRadius.md,
                padding = sp.insetLg,
                titleStyle = t.headingSm,
                subtitleStyle = t.bodyMd,
                iconSize = FinsibleTheme.sizes.icon.lg,
                spacing = sp.gapMd
            )

            else -> error("Unsupported size: $size. Accordion only supports Small, Medium, and Large sizes.")
        }

        val corner = when (shapeVariant) {
            FinsibleShape.Sharp -> FinsibleTheme.radius.none
            FinsibleShape.Rounded -> base.cornerRadius
            else -> error("Unsupported shape variant: $shapeVariant. Accordion only supports Rounded and Sharp shape variants.")
        }

        return base.copy(cornerRadius = corner)
    }
}
