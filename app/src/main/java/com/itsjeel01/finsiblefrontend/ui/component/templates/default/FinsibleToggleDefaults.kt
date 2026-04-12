package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for templatised toggle/switch component. */
object FinsibleToggleDefaults {

    @Composable
    fun colors(
        trackOnColor: Color = Color.Unspecified,
        thumbOnColor: Color = Color.Unspecified,
        trackOffColor: Color = Color.Unspecified,
        thumbOffColor: Color = Color.Unspecified,
        disabledTrackColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        disabledLabelColor: Color = Color.Unspecified,
    ): FinsibleToggleColors {
        val colors = FinsibleTheme.colors

        return FinsibleToggleColors(
            trackOnColor = if (trackOnColor != Color.Unspecified) trackOnColor else colors.brandAccent,
            thumbOnColor = if (thumbOnColor != Color.Unspecified) thumbOnColor else colors.primaryBackground,
            trackOffColor = if (trackOffColor != Color.Unspecified) trackOffColor else colors.surfaceContainerHigh,
            thumbOffColor = if (thumbOffColor != Color.Unspecified) thumbOffColor else colors.primaryContent,
            disabledTrackColor = if (disabledTrackColor != Color.Unspecified) disabledTrackColor else colors.disabled,
            disabledThumbColor = if (disabledThumbColor != Color.Unspecified) disabledThumbColor else colors.disabledContent,
            labelColor = if (labelColor != Color.Unspecified) labelColor else colors.primaryContent,
            disabledLabelColor = if (disabledLabelColor != Color.Unspecified) disabledLabelColor else colors.disabledContent,
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleToggleSizes {
        val d = FinsibleTheme.dimes
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleToggleSizes(
                width = d.d32,
                height = d.d18,
                thumbDiameter = d.d12,
                padding = d.d2,
                labelStyle = t.t12,
                iconSize = d.d14,
                spacing = d.d8
            )

            FinsibleSize.Small -> FinsibleToggleSizes(
                width = d.d36,
                height = d.d20,
                thumbDiameter = d.d14,
                padding = d.d2,
                labelStyle = t.t14,
                iconSize = d.d16,
                spacing = d.d10
            )

            FinsibleSize.Medium -> FinsibleToggleSizes(
                width = d.d44,
                height = d.d24,
                thumbDiameter = d.d16,
                padding = d.d4,
                labelStyle = t.t16.medium(),
                iconSize = d.d18,
                spacing = d.d12
            )

            FinsibleSize.Large -> FinsibleToggleSizes(
                width = d.d52,
                height = d.d28,
                thumbDiameter = d.d18,
                padding = d.d4,
                labelStyle = t.t18.medium(),
                iconSize = d.d20,
                spacing = d.d12
            )

            FinsibleSize.ExtraLarge -> FinsibleToggleSizes(
                width = d.d60,
                height = d.d32,
                thumbDiameter = d.d22,
                padding = d.d6,
                labelStyle = t.t20.medium(),
                iconSize = d.d24,
                spacing = d.d12
            )
        }
    }
}

