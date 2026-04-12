package com.itsjeel01.finsiblefrontend.ui.component.templates.util

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import kotlinx.coroutines.launch

/**
 * A highly optimized modifier that applies a tactile "shrink and bounce" physical effect
 * to components when pressed or tapped, safely ignoring programmatic state changes.
 */
fun Modifier.finsibleBounceIndication(
    interactionSource: InteractionSource,
    downScale: Float = 0.95f
): Modifier = composed {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(interactionSource) {
        // Listen to the raw interactions directly
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    // User touches down
                    launch {
                        scale.animateTo(downScale, tween(Duration.MS_50.toInt()))
                    }
                }

                is PressInteraction.Release, is PressInteraction.Cancel -> {
                    // User releases (or tap is instant)
                    launch {
                        // By animating to downScale first, we guarantee a visual pulse
                        // even if the user tapped faster than the 50ms down-animation.
                        // If they held it, it's already at downScale, so this takes 0ms.
                        scale.animateTo(downScale, tween(Duration.MS_50.toInt()))
                        scale.animateTo(1f, spring(dampingRatio = 0.6f, stiffness = 400f))
                    }
                }
            }
        }
    }

    this.graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
    }
}