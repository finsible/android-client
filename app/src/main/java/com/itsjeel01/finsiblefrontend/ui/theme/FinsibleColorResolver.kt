package com.itsjeel01.finsiblefrontend.ui.theme

import android.util.Log
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

class FinsibleColorResolver(private val colors: FinsibleColors) {

    private val colorCache = mutableMapOf<String, Color>()

    fun resolve(colorReference: String, fallbackColor: Color? = null): Color {
        return colorCache.getOrPut(colorReference) {
            resolveColorToken(colorReference)
                ?: parseHexColor(colorReference)
                ?: fallbackColor
                ?: Color.Companion.Gray
        }
    }

    fun lightColors(): ColorScheme = lightColorScheme(
        primary = colors.primaryButton,
        onPrimary = colors.primaryContent,
        primaryContainer = colors.card,
        onPrimaryContainer = colors.primaryContent,

        secondary = colors.secondaryButton,
        onSecondary = colors.secondaryContent,
        secondaryContainer = colors.card,
        onSecondaryContainer = colors.secondaryContent,

        error = colors.error,
        onError = colors.primaryContent,
        errorContainer = colors.card,
        onErrorContainer = colors.error,

        background = colors.primaryBackground,
        onBackground = colors.primaryContent,
        surface = colors.secondaryBackground,
        onSurface = colors.primaryContent,
        surfaceVariant = colors.card,
        onSurfaceVariant = colors.secondaryContent,

        outline = colors.border,
        outlineVariant = colors.overlay,
        scrim = colors.overlay,

        inverseSurface = colors.primaryContent,
        inverseOnSurface = colors.secondaryBackground,
        inversePrimary = colors.brandAccent,

        surfaceTint = colors.brandAccent
    )

    fun darkColors(): ColorScheme = darkColorScheme(
        primary = colors.primaryButton,
        onPrimary = colors.primaryContent,
        primaryContainer = colors.card,
        onPrimaryContainer = colors.primaryContent,

        secondary = colors.secondaryButton,
        onSecondary = colors.secondaryContent,
        secondaryContainer = colors.card,
        onSecondaryContainer = colors.secondaryContent,

        error = colors.error,
        onError = colors.primaryContent,
        errorContainer = colors.card,
        onErrorContainer = colors.error,

        background = colors.primaryBackground,
        onBackground = colors.primaryContent,
        surface = colors.secondaryBackground,
        onSurface = colors.primaryContent,
        surfaceVariant = colors.card,
        onSurfaceVariant = colors.secondaryContent,

        outline = colors.border,
        outlineVariant = colors.overlay,
        scrim = colors.overlay,

        inverseSurface = colors.secondaryBackground,
        inverseOnSurface = colors.primaryBackground,
        inversePrimary = colors.brandAccent,

        surfaceTint = colors.brandAccent
    )

    private fun resolveColorToken(token: String): Color? {
        return when (token.lowercase()) {
            "primaryBackground".lowercase() -> colors.primaryBackground
            "secondaryBackground".lowercase() -> colors.secondaryBackground
            "primaryContent".lowercase() -> colors.primaryContent
            "secondaryContent".lowercase() -> colors.secondaryContent
            "hover".lowercase() -> colors.hover
            "disabled".lowercase() -> colors.disabled
            "border".lowercase() -> colors.border
            "card".lowercase() -> colors.card
            "input".lowercase() -> colors.input
            "primaryButton".lowercase() -> colors.primaryButton
            "secondaryButton".lowercase() -> colors.secondaryButton
            "link".lowercase() -> colors.link
            "selection".lowercase() -> colors.selection
            "shadow".lowercase() -> colors.shadow
            "overlay".lowercase() -> colors.overlay
            "error".lowercase() -> colors.error
            "success".lowercase() -> colors.success
            "warning".lowercase() -> colors.warning
            "brandAccent".lowercase() -> colors.brandAccent
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
            Log.e(TAG, "Invalid color format: $colorString", e)
            null
        }
    }

    companion object {
        const val TAG = "FinsibleColorResolver"
    }
}