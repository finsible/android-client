package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleScrubberColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleScrubberSizes
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for `FinsibleScrubber`. */
object FinsibleScrubberDefaults {

    @Composable
    fun colors(
        currentColor: Color = Color.Unspecified,
        restColor: Color = Color.Unspecified,
        disabledCurrentColor: Color = Color.Unspecified,
        disabledRestColor: Color = Color.Unspecified
    ): FinsibleScrubberColors {
        val s = FinsibleTheme.colors

        return FinsibleScrubberColors(
            currentColor = if (currentColor != Color.Unspecified) currentColor else s.brandInteractive,
            restColor = if (restColor != Color.Unspecified) restColor else s.contentPrimary.copy(alpha = 0.4f),
            disabledCurrentColor = if (disabledCurrentColor != Color.Unspecified) disabledCurrentColor else s.contentDisabled,
            disabledRestColor = if (disabledRestColor != Color.Unspecified) disabledRestColor else s.surfaceSunken,
        )
    }

    @Composable
    fun sizes() = FinsibleScrubberSizes(
        activeBarWidth = FinsibleTheme.spacing.inset2xl,
        inactiveBarWidth = FinsibleTheme.spacing.insetMd,
        barHeight = FinsibleTheme.spacing.insetXs,
        barSpacing = FinsibleTheme.spacing.inlineMd,
        cornerRadius = FinsibleTheme.radius.xs,
        minTouchTargetHeight = FinsibleTheme.spacing.inset2xl,
    )
}
