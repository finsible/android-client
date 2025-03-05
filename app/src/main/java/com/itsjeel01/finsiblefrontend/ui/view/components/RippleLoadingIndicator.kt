package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.utils.AppConstants

@Composable
fun RippleLoadingIndicator(
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary,
    rippleCount: Int = 3,
    size: Dp = 80.dp,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ripple_transition")

    // Calculate proportional sizes
    val rippleSize = size
    val centerSize = size * 0.375f

    // List to store animation values for multiple ripples
    val animations = List(rippleCount) { index ->
        // Stagger the start of each ripple
        val delay = (AppConstants.ANIMATION_DURATION_MEDIUM / rippleCount) * index

        // Scale animation
        val scale = infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = AppConstants.ANIMATION_DURATION_VERY_LONG,
                    delayMillis = delay,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "scale_$index"
        )

        // Alpha animation
        val alpha = infiniteTransition.animateFloat(
            initialValue = 0.7f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = AppConstants.ANIMATION_DURATION_VERY_LONG,
                    delayMillis = delay,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "alpha_$index"
        )

        // Rotation for the inner shape
        val rotation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation_$index"
        )

        Triple(scale, alpha, rotation)
    }

    Box(contentAlignment = Alignment.Center) {
        // Draw ripple circles
        animations.forEachIndexed { index, (scale, alpha, _) ->
            val color = if (index % 2 == 0) primaryColor else secondaryColor

            Box(
                modifier = Modifier
                    .size(rippleSize)
                    .scale(scale.value)
                    .alpha(alpha.value)
                    .background(
                        color = color,
                        shape = CircleShape
                    )
            )
        }

        // Center shape that rotates
        Box(
            modifier = Modifier
                .size(centerSize)
                .graphicsLayer {
                    rotationZ = animations[0].third.value
                }
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(primaryColor, secondaryColor),
                        start = Offset(0f, 0f),
                        end = Offset(100f, 100f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}