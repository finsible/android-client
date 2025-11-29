package com.itsjeel01.finsiblefrontend.ui.theme

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.common.logging.Logger

@Immutable
data class DeviceInfo(
    val width: Dp,
    val height: Dp,
    val fontScale: Float,
    val fontWeightAdjustment: Int
) {
    val uiScaleFactor = when {
        width < 360.dp -> 0.85f   // Compact phones: maximize content
        width < 480.dp -> 1.0f    // Standard phones: baseline
        width < 720.dp -> 1.15f   // Large phones/small tablets: more breathing room
        else -> 1.3f              // Tablets: generous spacing
    }

    val textScaleFactor = when {
        width < 360.dp -> 0.95f    // Compact phones: slightly smaller text
        width >= 600.dp -> 1.08f   // Large screens: slightly larger text
        else -> 1.0f               // Standard phones: baseline
    } * fontScale

    fun adjustFontWeight(originalWeight: FontWeight): FontWeight {
        val adjustedWeight = originalWeight.weight + fontWeightAdjustment
        return FontWeight(adjustedWeight.coerceIn(100, 900))
    }
}

@Composable
fun rememberDeviceInfo(): DeviceInfo {
    val configuration = LocalConfiguration.current
    val fontWeightAdjustment =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) configuration.fontWeightAdjustment
        else 0

    val width = LocalWindowInfo.current.containerSize.width
    val height = LocalWindowInfo.current.containerSize.height

    return remember(
        width,
        height,
        configuration.fontScale,
        fontWeightAdjustment
    ) {
        Logger.UI.i(
            "Device info: Width=${width}dp, " +
                    "Height=${height}dp, " +
                    "FontScale=${configuration.fontScale}, " +
                    "FontWeightAdjustment=$fontWeightAdjustment"
        )
        DeviceInfo(
            width = width.dp,
            height = height.dp,
            fontScale = configuration.fontScale,
            fontWeightAdjustment = fontWeightAdjustment
        )
    }
}