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
            ballColor = if (ballColor != Color.Unspecified) ballColor else FinsibleTheme.colors.brandInteractive,
            barColor = if (barColor != Color.Unspecified) barColor else FinsibleTheme.colors.contentPrimary.copy(alpha = 0.8f)
        )
    }

    @Composable
    fun size(size: FinsibleSize): Dp {
        return when (size) {
            FinsibleSize.ExtraSmall -> FinsibleTheme.sizes.touch.xs
            FinsibleSize.Small      -> FinsibleTheme.sizes.touch.sm
            FinsibleSize.Medium     -> FinsibleTheme.sizes.touch.md
            FinsibleSize.Large      -> FinsibleTheme.sizes.touch.lg
            FinsibleSize.ExtraLarge -> FinsibleTheme.sizes.touch.xl
        }
    }
}
