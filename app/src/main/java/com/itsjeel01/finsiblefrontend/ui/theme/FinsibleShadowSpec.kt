package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class FinsibleDropShadow(
    val elevation: Dp,
    val color: Color
) {
    companion object {
        val None = FinsibleDropShadow(0.dp, Color.Transparent)
    }
}

fun Modifier.finsibleShadow(
    shadow: FinsibleDropShadow,
    shape: Shape
): Modifier {
    if (shadow == FinsibleDropShadow.None || shadow.color.alpha == 0f) {
        return this
    }

    return this.shadow(
        elevation = shadow.elevation,
        shape = shape,
        clip = false,
        // We pass the custom color to both ambient (underneath) and spot (directional) lights
        ambientColor = shadow.color,
        spotColor = shadow.color
    )
}