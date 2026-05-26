package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for templatised toggle/switch component. */
object FinsibleToggleDefaults {

    @Composable
    fun colors(
        trackOnColor: Color = Color.Unspecified,
        thumbOnColor: Color = Color.Unspecified,
        trackOffColor: Color = Color.Unspecified,
        trackOffBorderColor: Color = Color.Unspecified,
        thumbOffColor: Color = Color.Unspecified,
        disabledTrackColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        disabledLabelColor: Color = Color.Unspecified,
        hintColor: Color = Color.Unspecified,
        disabledHintColor: Color = Color.Unspecified,
    ): FinsibleToggleColors {
        val s = FinsibleTheme.colors

        return FinsibleToggleColors(
            trackOnColor = if (trackOnColor != Color.Unspecified) trackOnColor else s.brandInteractive,
            thumbOnColor = if (thumbOnColor != Color.Unspecified) thumbOnColor else s.surfaceBase,
            trackOffColor = if (trackOffColor != Color.Unspecified) trackOffColor else s.surfaceRaised,
            trackOffBorderColor = if (trackOffBorderColor != Color.Unspecified) trackOffBorderColor else s.borderDefault,
            thumbOffColor = if (thumbOffColor != Color.Unspecified) thumbOffColor else s.contentPrimary,
            disabledTrackColor = if (disabledTrackColor != Color.Unspecified) disabledTrackColor else s.surfaceSunken,
            disabledThumbColor = if (disabledThumbColor != Color.Unspecified) disabledThumbColor else s.contentDisabled,
            labelColor = if (labelColor != Color.Unspecified) labelColor else s.contentPrimary,
            disabledLabelColor = if (disabledLabelColor != Color.Unspecified) disabledLabelColor else s.contentDisabled,
            hintColor = if (hintColor != Color.Unspecified) hintColor else s.contentTertiary,
            disabledHintColor = if (disabledHintColor != Color.Unspecified) disabledHintColor else s.contentDisabled,
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleToggleSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleToggleSizes(
                width = FinsibleTheme.sizes.touch.xs + sp.insetXs,
                height = FinsibleTheme.sizes.icon.md + sp.insetXs / 2,  // ~18dp
                thumbDiameter = FinsibleTheme.sizes.icon.xs,
                padding = sp.insetXs / 2,
                labelStyle = t.bodySm,
                hintStyle = t.labelSm,
                iconSize = FinsibleTheme.sizes.icon.sm,
                spacing = sp.inlineMd
            )

            FinsibleSize.Small -> FinsibleToggleSizes(
                width = FinsibleTheme.sizes.touch.sm,
                height = FinsibleTheme.sizes.icon.lg - sp.insetXs / 2,  // ~20dp
                thumbDiameter = FinsibleTheme.sizes.icon.sm,
                padding = sp.insetXs / 2,
                labelStyle = t.bodyMd,
                hintStyle = t.labelSm,
                iconSize = FinsibleTheme.sizes.icon.md,
                spacing = sp.inlineMd
            )

            FinsibleSize.Medium -> FinsibleToggleSizes(
                width = FinsibleTheme.sizes.touch.md,
                height = FinsibleTheme.sizes.touch.xs,
                thumbDiameter = FinsibleTheme.sizes.icon.md,
                padding = sp.insetXs,
                labelStyle = t.labelMd,
                hintStyle = t.labelSm,
                iconSize = FinsibleTheme.sizes.icon.md,
                spacing = sp.gapMd
            )

            FinsibleSize.Large -> FinsibleToggleSizes(
                width = FinsibleTheme.sizes.touch.lg,
                height = FinsibleTheme.sizes.touch.sm - sp.insetSm,
                thumbDiameter = FinsibleTheme.sizes.icon.lg,
                padding = sp.insetXs,
                labelStyle = t.labelLg,
                hintStyle = t.labelMd,
                iconSize = FinsibleTheme.sizes.icon.lg,
                spacing = sp.gapMd
            )

            FinsibleSize.ExtraLarge -> FinsibleToggleSizes(
                width = FinsibleTheme.sizes.touch.xl,
                height = FinsibleTheme.sizes.touch.md - sp.insetMd,
                thumbDiameter = FinsibleTheme.sizes.icon.xl - sp.insetSm,
                padding = sp.insetSm,
                labelStyle = t.labelLg,
                hintStyle = t.labelMd,
                iconSize = FinsibleTheme.sizes.icon.lg,
                spacing = sp.gapMd
            )
        }
    }
}
