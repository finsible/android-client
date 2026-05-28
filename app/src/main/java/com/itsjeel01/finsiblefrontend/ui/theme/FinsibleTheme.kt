package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText

private val LocalFinsibleSemanticColors = compositionLocalOf<FinsibleSemanticColors> {
    error("No FinsibleSemanticColors provided")
}

private val LocalFinsibleSpacing = compositionLocalOf<FinsibleSpacing> {
    error("No FinsibleSpacing provided")
}

private val LocalFinsibleSizes = compositionLocalOf<FinsibleSizeTokens> {
    error("No FinsibleSizes provided")
}

private val LocalFinsibleStroke = compositionLocalOf<FinsibleStroke> {
    error("No FinsibleStroke provided")
}

private val LocalFinsibleElevation = compositionLocalOf<FinsibleElevation> {
    error("No FinsibleElevation provided")
}

private val LocalDeviceInfo = compositionLocalOf<DeviceInfo> {
    error("No ScreenContext provided")
}

private val LocalFinsibleAnimations = compositionLocalOf<FinsibleAnimations> {
    error("No FinsibleAnimations provided")
}

private val LocalFinsibleTypes = compositionLocalOf<FinsibleTypes> {
    error("No FinsibleTypes provided")
}

private val LocalFinsibleRadius = compositionLocalOf<FinsibleRadius> {
    error("No FinsibleRadius provided")
}

@Composable
fun FinsibleTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val deviceInfo = rememberDeviceInfo()
    val scaler = remember(deviceInfo.width) { FinsibleUiScaler(deviceInfo) }

    val colors = if (isDarkTheme) FinsibleSemanticColors.dark else FinsibleSemanticColors.light
    val colorResolver = remember(isDarkTheme) { FinsibleColorResolver(colors) }
    val spacing = remember(scaler) { scaler.scaleSpacing(FinsibleSpacing.values) }
    val sizes = remember(scaler) { scaler.scaleSizes(FinsibleSizes.values) }
    val stroke = remember(scaler) { scaler.scaleStroke(FinsibleStroke.values) }
    val elevation = remember(isDarkTheme, sizes) { if (isDarkTheme) FinsibleElevation.dark(sizes) else FinsibleElevation.light(sizes) }
    val finsibleTypes = remember(scaler) { scaler.scaleTypes(FinsibleTypes.values) }
    val durations = FinsibleDurations.values
    val animationSpecs = remember(durations) { FinsibleAnimationSpecs.create(durations) }
    val animations = remember(durations, animationSpecs) {
        FinsibleAnimations(durations = durations, specs = animationSpecs)
    }
    val radius = remember(scaler) { scaler.scaleRadius(FinsibleRadius.values) }

    CompositionLocalProvider(
        LocalFinsibleSemanticColors provides colors,
        LocalFinsibleSpacing provides spacing,
        LocalFinsibleSizes provides sizes,
        LocalFinsibleStroke provides stroke,
        LocalFinsibleElevation provides elevation,
        LocalDeviceInfo provides deviceInfo,
        LocalFinsibleTypes provides finsibleTypes,
        LocalFinsibleAnimations provides animations,
        LocalFinsibleRadius provides radius,
    ) {
        MaterialTheme(
            colorScheme =
                if (isDarkTheme) colorResolver.darkColors()
                else colorResolver.lightColors(),
            typography = FinsibleTypes.materialTypography
        ) {
            PreloadFonts()
            content()
        }
    }
}

@Composable
fun PreloadFonts() {
    listOf(FinsibleFontFamily.displayFont, FinsibleFontFamily.interfaceFont).forEach { fontFamily ->
        listOf(
            FontWeight.ExtraLight,
            FontWeight.Light,
            FontWeight.Normal,
            FontWeight.Medium,
            FontWeight.SemiBold,
            FontWeight.Bold,
            FontWeight.ExtraBold,
        ).forEach { weight ->
            FinsibleText(
                text = "Preload",
                modifier = Modifier
                    .height(0.dp)
                    .width(0.dp),
                textStyle = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = weight
                ),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Clip
            )
        }
    }
}

/**
 * Groups animation durations and pre-built specs under one accessor.
 * Accessed via [FinsibleTheme.animations].
 */
@Immutable
data class FinsibleAnimations(
    val durations: FinsibleDurations,
    val specs: FinsibleAnimationSpecs,
)

object FinsibleTheme {

    val screenHeight: Dp
        @Composable @ReadOnlyComposable get() = deviceInfo.height

    val screenWidth: Dp
        @Composable @ReadOnlyComposable get() = deviceInfo.width

    @Composable
    fun isDarkTheme(): Boolean = isSystemInDarkTheme()

    val deviceInfo: DeviceInfo
        @Composable @ReadOnlyComposable get() = LocalDeviceInfo.current

    /** Layer 2 — semantic color tokens. */
    val colors: FinsibleSemanticColors
        @Composable @ReadOnlyComposable get() = LocalFinsibleSemanticColors.current

    /** Full typography set (old tXX + new semantic roles). */
    val typography: FinsibleTypes
        @Composable @ReadOnlyComposable get() = LocalFinsibleTypes.current

    /** Direct access to Material3 Typography (for interop). */
    val materialTypography: Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography

    /** Layer 2 — semantic spacing tokens. */
    val spacing: FinsibleSpacing
        @Composable @ReadOnlyComposable get() = LocalFinsibleSpacing.current

    /** Layer 2 — semantic size tokens. */
    val sizes: FinsibleSizeTokens
        @Composable @ReadOnlyComposable get() = LocalFinsibleSizes.current

    /** Layer 2 — stroke / border-width tokens. */
    val stroke: FinsibleStroke
        @Composable @ReadOnlyComposable get() = LocalFinsibleStroke.current

    /** Layer 2 — elevation and shadow tokens. */
    val elevation: FinsibleElevation
        @Composable @ReadOnlyComposable get() = LocalFinsibleElevation.current

    /** Layer 2 — animation durations and specs. */
    val animations: FinsibleAnimations
        @Composable @ReadOnlyComposable get() = LocalFinsibleAnimations.current

    /** Layer 2 — radius tokens. */
    val radius: FinsibleRadius
        @Composable @ReadOnlyComposable get() = LocalFinsibleRadius.current

    /** Font family definitions (display + interface). */
    val fontFamily: FinsibleFontFamily
        get() = FinsibleFontFamily

    /** Predefined gradient brushes for financial cards. */
    val gradients: FinsibleGradients
        get() = FinsibleGradients

    @Composable
    fun resolveColor(token: String, fallback: Color? = null): Color =
        FinsibleColorResolver(LocalFinsibleSemanticColors.current).resolve(token, fallback)

    @Composable
    fun Dp.adaptedDp(): Dp =
        FinsibleUiScaler(LocalDeviceInfo.current).scaleCustomDime(this)
}
