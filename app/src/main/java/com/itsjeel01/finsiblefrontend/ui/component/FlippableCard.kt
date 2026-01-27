package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.model.FlippableCardData
import com.itsjeel01.finsiblefrontend.ui.model.StatisticsModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@Composable
fun FlippableCard(
    items: ImmutableList<FlippableCardData>,
    gradients: List<Brush>,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return

    var displayedIndex by remember { mutableIntStateOf(0) }
    var isFlipping by remember { mutableStateOf(false) }

    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val elasticEasing = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f)

    val frontIndex = displayedIndex
    val backIndex = (displayedIndex + 1) % items.size

    val frontItem = items[frontIndex]
    val backItem = items[backIndex]

    val frontGradient = gradients.getOrElse(frontIndex) { gradients.first() }
    val backGradient = gradients.getOrElse(backIndex) { gradients.first() }

    val currentRotation = rotation.value % 360f
    val showFront = currentRotation !in 90f ..< 270f

    fun onRotateClick() {
        if (isFlipping) return

        isFlipping = true
        scope.launch {
            rotation.animateTo(
                targetValue = 180f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = elasticEasing
                )
            )
            displayedIndex = backIndex
            rotation.snapTo(0f)
            isFlipping = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(FinsibleTheme.dimes.d8)
            .graphicsLayer {
                cameraDistance = 12f * density
                rotationY = rotation.value
                transformOrigin = TransformOrigin.Center
            }
    ) {
        // Back face (pre-rotated 180 degrees so it appears correctly when flipped)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    rotationY = 180f
                    alpha = if (!showFront) 1f else 0f
                },
            shape = RoundedCornerShape(FinsibleTheme.dimes.d16),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = FinsibleTheme.dimes.d4)
        ) {
            CardFace(
                title = backItem.title,
                largeText = backItem.largeText,
                statistics = backItem.statistics,
                gradientBrush = backGradient,
                onRotateClick = ::onRotateClick
            )
        }

        // Front face
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = if (showFront) 1f else 0f
                },
            shape = RoundedCornerShape(FinsibleTheme.dimes.d16),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = FinsibleTheme.dimes.d4)
        ) {
            CardFace(
                title = frontItem.title,
                largeText = frontItem.largeText,
                statistics = frontItem.statistics,
                gradientBrush = frontGradient,
                onRotateClick = ::onRotateClick
            )
        }
    }
}

/** Renders a single face of the card. */
@Composable
private fun CardFace(
    title: String,
    largeText: String,
    statistics: ImmutableList<StatisticsModel>,
    gradientBrush: Brush,
    onRotateClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = gradientBrush)
            .padding(FinsibleTheme.dimes.d20)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = FinsibleTheme.typography.t16.semiBold(),
                    color = FinsibleTheme.colors.white.copy(alpha = 0.9f)
                )

                IconButton(
                    onClick = onRotateClick,
                    modifier = Modifier.size(FinsibleTheme.dimes.d32)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rotate),
                        contentDescription = "Rotate card",
                        tint = FinsibleTheme.colors.white.copy(alpha = 0.95f),
                        modifier = Modifier.size(FinsibleTheme.dimes.d20)
                    )
                }
            }

            // Large text
            Text(
                text = largeText,
                style = FinsibleTheme.typography.t40.extraBold(),
                color = FinsibleTheme.colors.white
            )

            Spacer(Modifier.height(FinsibleTheme.dimes.d16))

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = FinsibleTheme.dimes.d1,
                color = FinsibleTheme.colors.white.copy(alpha = 0.25f)
            )

            Spacer(Modifier.height(FinsibleTheme.dimes.d16))

            // Statistics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                statistics.forEachIndexed { index, stat ->
                    StatisticItem(
                        title = stat.title,
                        value = stat.value,
                        modifier = Modifier.weight(1f)
                    )
                    if (index < statistics.size - 1) {
                        Spacer(Modifier.width(FinsibleTheme.dimes.d8))
                    }
                }
            }
        }
    }
}

/** Displays a single statistic item. */
@Composable
private fun StatisticItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = FinsibleTheme.typography.t12.medium(),
            color = FinsibleTheme.colors.white.copy(alpha = 0.75f)
        )
        Spacer(Modifier.height(FinsibleTheme.dimes.d4))
        Text(
            text = value,
            style = FinsibleTheme.typography.t16.bold(),
            color = FinsibleTheme.colors.white
        )
    }
}
