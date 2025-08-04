package com.itsjeel01.finsiblefrontend.ui.util

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

/** Utility class for common Compose animations. */
class Animation {
    companion object {
        val fadeInDelayed = fadeIn(tween(durationMillis = 300, delayMillis = 300))

        val scaleInDelayed =
            scaleIn(tween(durationMillis = 200, delayMillis = 600), initialScale = 0.9f)

        val fadeOutQuickly = fadeOut(tween(600))

        fun enterNotification(position: InAppNotificationPosition) = slideInVertically(
            animationSpec = springSpec(),
            initialOffsetY = { height ->
                if (position == InAppNotificationPosition.TOP) -height else height
            }
        ) + fadeIn()

        fun exitNotification(position: InAppNotificationPosition) = slideOutVertically(
            animationSpec = springSpec(),
            targetOffsetY = { height ->
                if (position == InAppNotificationPosition.TOP) -height else height
            }
        ) + fadeOut()

        fun <T> fastOutSlowEasingSpec() = tween<T>(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )

        fun <T> easeOut() = tween<T>(
            durationMillis = 600,
            easing = EaseOut
        )

        fun <T> springSpec() = spring<T>(
            dampingRatio = 0.5f,
            stiffness = 400f
        )

        fun <T> restartingInfiniteTween(duration: Int) = infiniteRepeatable<T>(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )

        fun <T> reversingInfiniteTween(duration: Int) = infiniteRepeatable<T>(
            animation = tween(duration / 2, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    }
}