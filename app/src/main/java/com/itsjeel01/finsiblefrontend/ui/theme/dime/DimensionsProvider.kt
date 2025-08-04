package com.itsjeel01.finsiblefrontend.ui.theme.dime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

val LocalAppDimensions = compositionLocalOf<AppDimensions> {
    error("AppDimensions not provided. Wrap your app with DimensionsProvider.")
}

@Composable
fun DimensionsProvider(
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val dimensions = remember(configuration.screenWidthDp, configuration.screenHeightDp) {
        DimensionCalculator.calculate(
            screenWidth = configuration.screenWidthDp.dp,
            screenHeight = configuration.screenHeightDp.dp,
            density = density
        )
    }

    CompositionLocalProvider(LocalAppDimensions provides dimensions) {
        content()
    }
}

@Composable
fun appDimensions(): AppDimensions = LocalAppDimensions.current