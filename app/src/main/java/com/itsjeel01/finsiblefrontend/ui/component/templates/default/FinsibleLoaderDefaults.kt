package com.itsjeel01.finsiblefrontend.ui.component.templates.default

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleLoaderColors
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

object FinsibleLoaderDefaults {

    @Composable
    fun colors(
        ballColor: Color = Color.Unspecified,
        barColor: Color = Color.Unspecified
    ): FinsibleLoaderColors {
        return FinsibleLoaderColors(
            ballColor = if (ballColor != Color.Unspecified) ballColor else FinsibleTheme.colors.brandAccent,
            barColor = if (barColor != Color.Unspecified) barColor else FinsibleTheme.colors.primaryContent80
        )
    }

    @Composable
    fun size(size: FinsibleSize): Dp {
        val d = FinsibleTheme.dimes
        return when (size) {
            FinsibleSize.ExtraSmall -> d.d24
            FinsibleSize.Small -> d.d32
            FinsibleSize.Medium -> d.d48
            FinsibleSize.Large -> d.d64
            FinsibleSize.ExtraLarge -> d.d80
        }
    }
}