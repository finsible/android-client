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
        selectedRingColor: Color = Color.Unspecified,
        unselectedRingColor: Color = Color.Unspecified,
        selectedDotColor: Color = Color.Unspecified,
        selectedLabelColor: Color = Color.Unspecified,
        unselectedLabelColor: Color = Color.Unspecified,
        disabledSelectedRingColor: Color = Color.Unspecified,
        disabledUnselectedRingColor: Color = Color.Unspecified,
        disabledSelectedDotColor: Color = Color.Unspecified,
        disabledSelectedLabelColor: Color = Color.Unspecified,
        disabledUnselectedLabelColor: Color = Color.Unspecified,
        rippleColor: Color = Color.Unspecified
    ): FinsibleRadioButtonColors {
        val theme = FinsibleTheme.colors

        return FinsibleRadioButtonColors(
            selectedRingColor = if (selectedRingColor != Color.Unspecified) selectedRingColor else theme.primaryContent,
            unselectedRingColor = if (unselectedRingColor != Color.Unspecified) unselectedRingColor else theme.outlineVariant,
            selectedDotColor = if (selectedDotColor != Color.Unspecified) selectedDotColor else theme.primaryContent,
            selectedLabelColor = if (selectedLabelColor != Color.Unspecified) selectedLabelColor else theme.primaryContent,
            unselectedLabelColor = if (unselectedLabelColor != Color.Unspecified) unselectedLabelColor else theme.secondaryContent,
            disabledSelectedRingColor = if (disabledSelectedRingColor != Color.Unspecified) disabledSelectedRingColor else theme.disabledContent,
            disabledUnselectedRingColor = if (disabledUnselectedRingColor != Color.Unspecified) disabledUnselectedRingColor else theme.disabledContent,
            disabledSelectedDotColor = if (disabledSelectedDotColor != Color.Unspecified) disabledSelectedDotColor else theme.disabledContent,
            disabledSelectedLabelColor = if (disabledSelectedLabelColor != Color.Unspecified) disabledSelectedLabelColor else theme.disabledContent,
            disabledUnselectedLabelColor = if (disabledUnselectedLabelColor != Color.Unspecified) disabledUnselectedLabelColor else theme.disabledContent,
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


