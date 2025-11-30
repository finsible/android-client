package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.itsjeel01.finsiblefrontend.common.logging.Logger

class FinsibleColorResolver(private val colors: FinsibleColors) {

    private val colorCache = mutableMapOf<String, Color>()

    fun resolve(colorReference: String, fallbackColor: Color? = null): Color {
        return colorCache.getOrPut(colorReference) {
            resolveColorToken(colorReference)
                ?: parseHexColor(colorReference)
                ?: fallbackColor
                ?: Color.Gray
        }
    }

    fun lightColors(): ColorScheme = lightColorScheme(
        primary = colors.primaryButton,
        onPrimary = colors.primaryContent,
        primaryContainer = colors.surfaceContainerHigh,
        onPrimaryContainer = colors.primaryContent,

        secondary = colors.secondaryButton,
        onSecondary = colors.secondaryContent,
        secondaryContainer = colors.surfaceContainer,
        onSecondaryContainer = colors.secondaryContent,

        error = colors.error,
        onError = colors.primaryContent,
        errorContainer = colors.errorContainer,
        onErrorContainer = colors.error,

        background = colors.primaryBackground,
        onBackground = colors.primaryContent,
        surface = colors.secondaryBackground,
        onSurface = colors.primaryContent,
        surfaceVariant = colors.surfaceContainer,
        onSurfaceVariant = colors.onSurfaceVariant,

        outline = colors.outline,
        outlineVariant = colors.outlineVariant,
        scrim = colors.scrim,

        inverseSurface = colors.primaryContent,
        inverseOnSurface = colors.secondaryBackground,
        inversePrimary = colors.brandAccent,

        surfaceTint = colors.brandAccent
    )

    fun darkColors(): ColorScheme = darkColorScheme(
        primary = colors.primaryButton,
        onPrimary = colors.primaryContent,
        primaryContainer = colors.surfaceContainerHigh,
        onPrimaryContainer = colors.primaryContent,

        secondary = colors.secondaryButton,
        onSecondary = colors.secondaryContent,
        secondaryContainer = colors.surfaceContainer,
        onSecondaryContainer = colors.secondaryContent,

        error = colors.error,
        onError = colors.primaryContent,
        errorContainer = colors.errorContainer,
        onErrorContainer = colors.error,

        background = colors.primaryBackground,
        onBackground = colors.primaryContent,
        surface = colors.secondaryBackground,
        onSurface = colors.primaryContent,
        surfaceVariant = colors.surfaceContainer,
        onSurfaceVariant = colors.onSurfaceVariant,

        outline = colors.outline,
        outlineVariant = colors.outlineVariant,
        scrim = colors.scrim,

        inverseSurface = colors.secondaryBackground,
        inverseOnSurface = colors.primaryBackground,
        inversePrimary = colors.brandAccent,

        surfaceTint = colors.brandAccent
    )

    private fun resolveColorToken(token: String): Color? {
        return when (token.lowercase()) {
            "primarybackground" -> colors.primaryBackground
            "secondarybackground" -> colors.secondaryBackground
            "primarycontent" -> colors.primaryContent
            "secondarycontent" -> colors.secondaryContent
            "hover" -> colors.hover
            "disabled" -> colors.disabled
            "border" -> colors.border
            "card" -> colors.card
            "input" -> colors.input
            "primarybutton" -> colors.primaryButton
            "secondarybutton" -> colors.secondaryButton
            "link" -> colors.link
            "selection" -> colors.selection
            "shadow" -> colors.shadow
            "overlay" -> colors.overlay
            "error" -> colors.error
            "success" -> colors.success
            "warning" -> colors.warning
            "brandaccent" -> colors.brandAccent

            // Semantic + containers
            "info" -> colors.info
            "infocontainer" -> colors.infoContainer
            "successcontainer" -> colors.successContainer
            "warningcontainer" -> colors.warningContainer
            "errorcontainer" -> colors.errorContainer

            // Surfaces
            "surface" -> colors.surface
            "surfacecontainer" -> colors.surfaceContainer
            "surfacecontainerhigh" -> colors.surfaceContainerHigh
            "surfacecontainerlow" -> colors.surfaceContainerLow
            "surfacecontainerdim" -> colors.surfaceContainerDim

            // Outlines
            "outline" -> colors.outline
            "outlinevariant" -> colors.outlineVariant
            "scrim" -> colors.scrim

            // Content hierarchy
            "tertiarycontent" -> colors.tertiaryContent
            "onsurfacevariant" -> colors.onSurfaceVariant
            "placeholder" -> colors.placeholder

            // States
            "pressed" -> colors.pressed
            "focused" -> colors.focused
            "hoverstrong" -> colors.hoverStrong
            "disabledcontent" -> colors.disabledContent
            "divider" -> colors.divider

            // Financial card gradients
            "gradientbrand1" -> colors.gradientBrand1
            "gradientbrand2" -> colors.gradientBrand2
            "gradientsuccess1" -> colors.gradientSuccess1
            "gradientsuccess2" -> colors.gradientSuccess2
            "gradientwarning1" -> colors.gradientWarning1
            "gradientwarning2" -> colors.gradientWarning2
            "gradientincome1" -> colors.gradientIncome1
            "gradientincome2" -> colors.gradientIncome2
            "gradientexpense1" -> colors.gradientExpense1
            "gradientexpense2" -> colors.gradientExpense2
            "gradientsavings1" -> colors.gradientSavings1
            "gradientsavings2" -> colors.gradientSavings2
            "gradientinvestment1" -> colors.gradientInvestment1
            "gradientinvestment2" -> colors.gradientInvestment2
            "gradientbudget1" -> colors.gradientBudget1
            "gradientbudget2" -> colors.gradientBudget2
            "gradientneutral1" -> colors.gradientNeutral1
            "gradientneutral2" -> colors.gradientNeutral2
            "gradientpremium1" -> colors.gradientPremium1
            "gradientpremium2" -> colors.gradientPremium2

            else -> null
        }
    }

    private fun parseHexColor(colorString: String): Color? {
        return try {
            val hex = colorString.removePrefix("#")
            if (hex.length == 6 || hex.length == 8)
                Color("#$hex".toColorInt())
            else null
        } catch (e: IllegalArgumentException) {
            Logger.UI.e("Invalid color format: $colorString", e)
            null
        }
    }
}