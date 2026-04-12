package com.itsjeel01.finsiblefrontend.ui.component.templates.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class FinsibleNotificationColors(
    val containerColor: Color,
    val titleColor: Color,
    val subtitleColor: Color,
    val iconContainerColor: Color,
    val iconTintColor: Color,
    val progressTrackColor: Color,
    val progressIndicatorColor: Color
)

enum class FinsibleNotificationPosition {
    Top, Bottom
}