package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleRadioButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleRadioButtonSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Defaults for the templatised radio button component. */
object FinsibleRadioButtonDefaults {

    @Composable
    fun colors(
        ringColor: Color = Color.Unspecified,
        dotColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        disabledRingColor: Color = Color.Unspecified,
        disabledDotColor: Color = Color.Unspecified,
        disabledLabelColor: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified
    ): FinsibleRadioButtonColors {
        val theme = FinsibleTheme.colors

        return FinsibleRadioButtonColors(
            ringColor = if (ringColor != Color.Unspecified) ringColor else theme.primaryContent,
            dotColor = if (dotColor != Color.Unspecified) dotColor else theme.primaryContent,
            labelColor = if (labelColor != Color.Unspecified) labelColor else theme.primaryContent,
            disabledRingColor = if (disabledRingColor != Color.Unspecified) disabledRingColor else theme.disabledContent,
            disabledDotColor = if (disabledDotColor != Color.Unspecified) disabledDotColor else theme.disabledContent,
            disabledLabelColor = if (disabledLabelColor != Color.Unspecified) disabledLabelColor else theme.disabledContent,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else theme.primaryContent.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleRadioButtonSizes {
        val d = FinsibleTheme.dimes
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.Small -> FinsibleRadioButtonSizes(
                outerDiameter = d.d18,
                ringWidth = d.d2,
                dotDiameter = d.d8,
                iconSize = d.d14,
                spacing = d.d8,
                labelStyle = t.t14
            )

            FinsibleSize.Medium -> FinsibleRadioButtonSizes(
                outerDiameter = d.d20,
                ringWidth = d.d2,
                dotDiameter = d.d10,
                iconSize = d.d16,
                spacing = d.d8,
                labelStyle = t.t16.medium()
            )

            else -> error("Invalid radio button size: $size. Supported sizes are: Small and Medium.")
        }
    }
}


