package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Predefined gradient types for financial cards. */
enum class CardGradientType {
    NET_WORTH,
    ASSETS,
    LIABILITIES,
    BRAND
}

/** Helper object to access gradient colors for financial cards. */
object FinsibleGradients {

    /** Get gradient color pair for a specific card type (darker shade first for left-to-right gradients). */
    @Composable
    fun getGradientColors(type: CardGradientType): List<Color> {
        val colors = FinsibleTheme.colors
        return when (type) {
            CardGradientType.NET_WORTH -> listOf(colors.netWorthGradientStart, colors.netWorthGradientEnd)
            CardGradientType.ASSETS -> listOf(colors.assetsGradientStart, colors.assetsGradientEnd)
            CardGradientType.LIABILITIES -> listOf(colors.liabilitiesGradientStart, colors.liabilitiesGradientEnd)
            CardGradientType.BRAND -> listOf(colors.brandAccent, colors.brandAccent50)
        }
    }

    /** Get linear gradient brush for a specific card type (left-to-right, darker on left). */
    @Composable
    fun getLinearGradient(type: CardGradientType): Brush {
        return Brush.horizontalGradient(colors = getGradientColors(type))
    }

    /** Get vertical gradient brush for a specific card type. */
    @Composable
    fun getVerticalGradient(type: CardGradientType): Brush {
        return Brush.verticalGradient(colors = getGradientColors(type))
    }

    /** Get horizontal gradient brush for a specific card type. */
    @Composable
    fun getHorizontalGradient(type: CardGradientType): Brush {
        return Brush.horizontalGradient(colors = getGradientColors(type))
    }
}

