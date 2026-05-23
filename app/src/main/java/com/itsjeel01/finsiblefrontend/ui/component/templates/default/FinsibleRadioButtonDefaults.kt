package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleRadioButtonColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleRadioButtonSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

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
        val s = FinsibleTheme.colors

        return FinsibleRadioButtonColors(
            selectedRingColor = if (selectedRingColor != Color.Unspecified) selectedRingColor else s.contentPrimary,
            unselectedRingColor = if (unselectedRingColor != Color.Unspecified) unselectedRingColor else s.borderDefault,
            selectedDotColor = if (selectedDotColor != Color.Unspecified) selectedDotColor else s.contentPrimary,
            selectedLabelColor = if (selectedLabelColor != Color.Unspecified) selectedLabelColor else s.contentPrimary,
            unselectedLabelColor = if (unselectedLabelColor != Color.Unspecified) unselectedLabelColor else s.contentSecondary,
            disabledSelectedRingColor = if (disabledSelectedRingColor != Color.Unspecified) disabledSelectedRingColor else s.contentDisabled,
            disabledUnselectedRingColor = if (disabledUnselectedRingColor != Color.Unspecified) disabledUnselectedRingColor else s.contentDisabled,
            disabledSelectedDotColor = if (disabledSelectedDotColor != Color.Unspecified) disabledSelectedDotColor else s.contentDisabled,
            disabledSelectedLabelColor = if (disabledSelectedLabelColor != Color.Unspecified) disabledSelectedLabelColor else s.contentDisabled,
            disabledUnselectedLabelColor = if (disabledUnselectedLabelColor != Color.Unspecified) disabledUnselectedLabelColor else s.contentDisabled,
            rippleColor = if (rippleColor != Color.Unspecified) rippleColor else s.contentPrimary.copy(alpha = 0.12f)
        )
    }

    @Composable
    fun sizes(size: FinsibleSize): FinsibleRadioButtonSizes {
        val sp = FinsibleTheme.spacing
        val t = FinsibleTheme.typography

        return when (size) {
            FinsibleSize.Small -> FinsibleRadioButtonSizes(
                outerDiameter = FinsibleTheme.sizes.icon.md,
                ringWidth = FinsibleTheme.stroke.bold,
                dotDiameter = sp.inlineMd,
                iconSize = FinsibleTheme.sizes.icon.sm,
                spacing = sp.inlineMd,
                labelStyle = t.bodyMd
            )

            FinsibleSize.Medium -> FinsibleRadioButtonSizes(
                outerDiameter = FinsibleTheme.sizes.icon.lg - sp.insetXs,
                ringWidth = FinsibleTheme.stroke.bold,
                dotDiameter = sp.inlineMd + sp.insetXs / 2,
                iconSize = FinsibleTheme.sizes.icon.md,
                spacing = sp.inlineMd,
                labelStyle = t.labelLg
            )

            else -> error("Invalid radio button size: $size. Supported sizes are: Small and Medium.")
        }
    }
}
