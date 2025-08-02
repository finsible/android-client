package com.itsjeel01.finsiblefrontend.ui.component.base

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class LoadingSpeed(val durationMs: Int) {
    SLOW(2000),        // For heavy operations like data sync
    NORMAL(1200),      // Default for most operations
    FAST(800),         // For quick operations like form validation
    INSTANT(400)       // For immediate feedback
}

@Composable
fun BaseLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    speed: LoadingSpeed = LoadingSpeed.NORMAL,
    primaryColor: Color = getCustomColor(ColorKey.IndicatorPrimary),
    secondaryColor: Color = MaterialTheme.colorScheme.background
) {
    val progress = remember { Animatable(0f) }
    val barAnimations = remember { List(5) { Animatable(0f) } }

    LaunchedEffect(speed) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(speed.durationMs, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    LaunchedEffect(Unit) {
        barAnimations.forEachIndexed { index, animatable ->
            launch {
                delay(index * 100L)
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(speed.durationMs / 2, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }
    }

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val center = this.size.center
            val chartWidth = size.toPx() * 0.7f
            val chartHeight = size.toPx() * 0.5f
            val barWidth = chartWidth / 7
            val barSpacing = barWidth * 0.3f

            val barHeights = listOf(0.3f, 0.7f, 0.5f, 0.9f, 0.4f)

            for (i in barHeights.indices) {
                val x = center.x - chartWidth / 2 + i * (barWidth + barSpacing) + barWidth / 2
                val animatedHeight = chartHeight * barHeights[i] * barAnimations[i].value
                val y = center.y + chartHeight / 2 - animatedHeight

                val barColor = lerp(secondaryColor, primaryColor, barAnimations[i].value)

                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.1f),
                    topLeft = Offset(x - barWidth / 2 + 2, y + 2),
                    size = Size(barWidth, animatedHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                )

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x - barWidth / 2, y),
                    size = Size(barWidth, animatedHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                )

                drawRoundRect(
                    color = barColor.copy(alpha = 0.6f),
                    topLeft = Offset(x - barWidth / 2 + 2, y + 2),
                    size = Size(barWidth - 4, kotlin.math.max(0f, animatedHeight - 4)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx())
                )
            }
        }
    }
}

@Composable
@Preview
fun FullScreenLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    speed: LoadingSpeed = LoadingSpeed.NORMAL,
    primaryColor: Color = getCustomColor(ColorKey.IndicatorPrimary),
    secondaryColor: Color = MaterialTheme.colorScheme.background
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        BaseLoadingIndicator(
            size = size,
            speed = speed,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor
        )
    }
}