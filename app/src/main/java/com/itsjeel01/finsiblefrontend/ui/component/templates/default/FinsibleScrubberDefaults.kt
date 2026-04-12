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
        val theme = FinsibleTheme.colors

        return FinsibleScrubberColors(
            currentColor = if (currentColor != Color.Unspecified) currentColor else theme.brandAccent,
            restColor = if (restColor != Color.Unspecified) restColor else theme.primaryContent40,
            disabledCurrentColor = if (disabledCurrentColor != Color.Unspecified) {
                disabledCurrentColor
            } else {
                theme.disabledContent
            },
            disabledRestColor = if (disabledRestColor != Color.Unspecified) {
                disabledRestColor
            } else {
                theme.disabled
            }
        )
    }

    @Composable
    fun sizes() = FinsibleScrubberSizes(
        activeBarWidth = FinsibleTheme.dimes.d28,
        inactiveBarWidth = FinsibleTheme.dimes.d12,
        barHeight = FinsibleTheme.dimes.d4,
        barSpacing = FinsibleTheme.dimes.d8,
        cornerRadius = FinsibleTheme.dimes.d2,
        minTouchTargetHeight = FinsibleTheme.dimes.d24
    )
}

