package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

enum class FinsibleOverlayOpacity(val alpha: Float) {
    Low(0.3f),
    Regular(0.6f),
    High(0.85f),
    Solid(1.0f)
}

enum class FinsibleLoaderSpeed(val durationMs: Int) {
    Slow(3200),
    Normal(2000),
    Fast(1200)
}

@Immutable
data class FinsibleLoaderColors(
    val ballColor: Color,
    val barColor: Color
)