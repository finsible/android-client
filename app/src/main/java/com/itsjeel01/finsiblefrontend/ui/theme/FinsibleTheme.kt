package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

private val LocalFinsibleColors = compositionLocalOf<FinsibleColors> {
    error("No FinsibleColors provided")
}

private val LocalFinsibleColorResolver = compositionLocalOf<FinsibleColorResolver> {
    error("No ColorResolver provided")
}

private val LocalDeviceInfo = compositionLocalOf<DeviceInfo> {
    error("No ScreenContext provided")
}

private val LocalFinsibleTypes = compositionLocalOf<FinsibleTypes> {
    error("No FinsibleTypes provided")
}

private val LocalFinsibleDimes = compositionLocalOf<FinsibleDimes> {
    error("No FinsibleDimes provided")
}

@Composable
fun FinsibleTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val deviceInfo = rememberDeviceInfo()
    val scaler = remember(deviceInfo.width) { FinsibleUiScaler(deviceInfo) }
    val colors = if (isDarkTheme) FinsibleColors.dark else FinsibleColors.light
    val colorResolver = remember(isDarkTheme) { FinsibleColorResolver(colors) }
    val finsibleTypes = scaler.scaleTypes(FinsibleTypes.types)
    val scaledDimes = scaler.scaleDimes(FinsibleDimes.values)

    CompositionLocalProvider(
        LocalFinsibleColors provides colors,
        LocalFinsibleColorResolver provides colorResolver,
        LocalDeviceInfo provides deviceInfo,
        LocalFinsibleTypes provides finsibleTypes,
        LocalFinsibleDimes provides scaledDimes
    ) {
        MaterialTheme(
            colorScheme =
                if (isDarkTheme) colorResolver.darkColors()
                else colorResolver.lightColors(),
            typography = FinsibleTypes.materialTypography
        ) {
            content()
        }
    }
}

object FinsibleTheme {
    val screenHeight: Dp
        @Composable
        @ReadOnlyComposable
        get() = deviceInfo.height

    val screenWidth: Dp
        @Composable
        @ReadOnlyComposable
        get() = deviceInfo.width

    val colors: FinsibleColors
        @Composable
        @ReadOnlyComposable
        get() = LocalFinsibleColors.current

    val typography: FinsibleTypes
        @Composable
        @ReadOnlyComposable
        get() = LocalFinsibleTypes.current

    val materialTypography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val deviceInfo: DeviceInfo
        @Composable
        @ReadOnlyComposable
        get() = LocalDeviceInfo.current

    val dimes: FinsibleDimes
        @Composable
        @ReadOnlyComposable
        get() = LocalFinsibleDimes.current

    @Composable
    fun isDarkTheme(): Boolean = isSystemInDarkTheme()

    @Composable
    fun resolveColor(token: String, fallback: Color? = null): Color =
        LocalFinsibleColorResolver.current.resolve(token, fallback)

    @Composable
    fun Dp.adaptedDp(): Dp =
        FinsibleUiScaler(LocalDeviceInfo.current).scaleCustomDime(this)
}