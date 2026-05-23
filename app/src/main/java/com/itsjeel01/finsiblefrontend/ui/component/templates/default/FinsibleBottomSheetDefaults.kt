package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBottomSheetColors
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults for Bottom Sheet Component. */
object FinsibleBottomSheetDefaults {

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        scrimColor: Color = Color.Unspecified,
        titleColor: Color = Color.Unspecified,
        subtitleColor: Color = Color.Unspecified,
        dragHandleColor: Color = Color.Unspecified,
        accentColor: Color = Color.Unspecified
    ): FinsibleBottomSheetColors {
        val s = FinsibleTheme.colors

        return FinsibleBottomSheetColors(
            containerColor = if (containerColor != Color.Unspecified) containerColor else s.surfaceSunken,
            scrimColor = if (scrimColor != Color.Unspecified) scrimColor else FinsibleTheme.colors.scrim,
            titleColor = if (titleColor != Color.Unspecified) titleColor else s.contentPrimary,
            subtitleColor = if (subtitleColor != Color.Unspecified) subtitleColor else s.contentSecondary,
            dragHandleColor = when {
                dragHandleColor != Color.Unspecified -> dragHandleColor
                accentColor != Color.Unspecified -> accentColor
                else -> s.borderDefault
            },
            accentColor = accentColor
        )
    }

    @Composable
    fun shape() = RoundedCornerShape(
        topStart = FinsibleTheme.radius.component.sheet,
        topEnd = FinsibleTheme.radius.component.sheet
    )
}
