package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Predefined gradient types for financial cards. */
enum class GradientType {
    NET_WORTH,
    ASSETS,
    LIABILITIES,
    CARDS,
    BRAND
}

/**
 * Gradient color pairs for financial cards.
 *
 * Gradient pairs are visual treatments, not general-purpose surface tokens,
 * so they live here rather than in the semantic color layer.
 */
object FinsibleGradients {

    // Light gradient pairs
    private val netWorthStartLight  = Color(0xFF174E30)
    private val netWorthEndLight    = Color(0xFF3A8856)
    private val assetsStartLight    = Color(0xFF7A5D26)
    private val assetsEndLight      = Color(0xFFC49640)
    private val liabilitiesStartLight = Color(0xFF3A3A42)
    private val liabilitiesEndLight   = Color(0xFF5C5C66)

    // Dark gradient pairs
    private val netWorthStartDark   = Color(0xFF164228)
    private val netWorthEndDark     = Color(0xFF34A064)
    private val assetsStartDark     = Color(0xFF5C4418)
    private val assetsEndDark       = Color(0xFFA88040)
    private val liabilitiesStartDark = Color(0xFF1C1C20)
    private val liabilitiesEndDark   = Color(0xFF3C3C46)

    @Composable
    fun getGradientColors(type: GradientType): List<Color> {
        val s = FinsibleTheme.colors
        val isDark = FinsibleTheme.isDarkTheme()

        return when (type) {
            GradientType.NET_WORTH -> listOf(
                if (isDark) netWorthStartDark else netWorthStartLight,
                if (isDark) netWorthEndDark else netWorthEndLight,
            )
            GradientType.ASSETS -> listOf(
                if (isDark) assetsStartDark else assetsStartLight,
                if (isDark) assetsEndDark else assetsEndLight,
            )
            GradientType.LIABILITIES -> listOf(
                if (isDark) liabilitiesStartDark else liabilitiesStartLight,
                if (isDark) liabilitiesEndDark else liabilitiesEndLight,
            )
            GradientType.CARDS -> listOf(
                s.surfaceBase,
                s.surfaceDefault.copy(alpha = 0.9f),
            )
            GradientType.BRAND -> listOf(
                s.brandInteractive,
                s.brandAccent,
            )
        }
    }

    @Composable
    fun getLinearGradient(type: GradientType): Brush =
        Brush.horizontalGradient(colors = getGradientColors(type))

    @Composable
    fun getVerticalGradient(type: GradientType): Brush =
        Brush.verticalGradient(colors = getGradientColors(type))

    @Composable
    fun getHorizontalGradient(type: GradientType): Brush =
        Brush.horizontalGradient(colors = getGradientColors(type))
}
