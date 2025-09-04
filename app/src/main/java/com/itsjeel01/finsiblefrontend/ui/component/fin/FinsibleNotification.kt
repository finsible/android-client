package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.NotificationType.Companion.getColor
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

private const val COMPLETE_FRACTION = 1f
private const val EMPTY_FRACTION = 0f
private const val ALPHA_REDUCTION_FACTOR = 0.5f
private const val VERTICAL_THRESHOLD_FACTOR = 0.15f

@Composable
fun FinsibleNotification(
    config: NotificationConfig,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val showActionButton = config.actionLabel != null && config.onAction != null
    val screenHeight = FinsibleTheme.screenHeight.value
    var currentOffsetY by remember { mutableFloatStateOf(0f) }
    var isAutoDismissing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // --- Auto Dismiss Logic with optimized updates ---

    var timeLeft by remember(config) { mutableFloatStateOf(COMPLETE_FRACTION) }

    LaunchedEffect(config.hashCode(), isVisible) {
        if (config.autoDismiss && isVisible && config.showTimerProgressBar) {
            val startTime = System.currentTimeMillis()
            val duration = config.autoDismissDelay
            val updateInterval = Duration.MS_16

            while (timeLeft > EMPTY_FRACTION) {
                val elapsed = System.currentTimeMillis() - startTime
                timeLeft = (COMPLETE_FRACTION - (elapsed.toFloat() / duration.toFloat()))
                    .coerceAtLeast(EMPTY_FRACTION)

                if (elapsed >= duration) {
                    isAutoDismissing = true
                    onDismiss()
                    break
                }
                delay(updateInterval)
            }
        }
    }

    // --- Swipe Up to Dismiss Logic ---

    val verticalDismissThreshold = screenHeight * VERTICAL_THRESHOLD_FACTOR

    val animatedOffsetY by animateFloatAsState(
        targetValue = currentOffsetY,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "notification_offset_y"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible && !isAutoDismissing) {
            val fractionDismissed =
                (abs(currentOffsetY) / verticalDismissThreshold).coerceAtMost(COMPLETE_FRACTION)
            COMPLETE_FRACTION - (fractionDismissed * ALPHA_REDUCTION_FACTOR)
        } else COMPLETE_FRACTION,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "notification_alpha"
    )

    // --- Notification Card UI ---

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            initialOffsetY = { height -> -height }
        ),
        exit = fadeOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .let { modifier ->
                    if (isAutoDismissing) {
                        modifier
                    } else {
                        modifier.graphicsLayer {
                            translationY = animatedOffsetY
                            alpha = animatedAlpha
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            val verticalDismiss =
                                abs(currentOffsetY) >= verticalDismissThreshold && currentOffsetY < 0

                            if (verticalDismiss) {
                                currentOffsetY = -screenHeight
                                coroutineScope.launch {
                                    delay(300)
                                    onDismiss()
                                }
                            } else {
                                currentOffsetY = 0f
                            }
                        }
                    ) { _, dragAmount ->
                        if (isVisible && !isAutoDismissing) {
                            val newOffset = currentOffsetY + dragAmount
                            if (newOffset <= 0) {
                                currentOffsetY = newOffset
                            }
                        }
                    }
                },
            shape = RoundedCornerShape(FinsibleTheme.dimes.d16),
            colors = CardDefaults.cardColors(
                containerColor = FinsibleTheme.colors.same
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = FinsibleTheme.dimes.d8)
        ) {

            Column(
                modifier = Modifier
            ) {
                // First Row: Icon + Content + Dismiss Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = FinsibleTheme.dimes.d12,
                            start = FinsibleTheme.dimes.d12,
                            bottom = if (showActionButton) FinsibleTheme.dimes.d0 else FinsibleTheme.dimes.d12,
                            end = FinsibleTheme.dimes.d12
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
                ) {
                    NotificationIcon(
                        notification = config,
                        modifier = Modifier
                    )

                    NotificationContent(
                        notification = config,
                        modifier = Modifier.weight(1f)
                    )

                    if (config.showDismissButton) {
                        FinsibleIconButton(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            icon = R.drawable.ic_close,
                            contentDescription = "Dismiss notification",
                            onClick = onDismiss,
                            config = IconButtonConfig(
                                type = ComponentType.Tertiary,
                                size = ComponentSize.Medium,
                                shape = IconButtonShape.Circle,
                                tintIcon = true
                            )
                        )
                    }
                }

                // Second Row: Action Button (if any)
                if (showActionButton) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(FinsibleTheme.dimes.d12),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(FinsibleTheme.dimes.d48)
                                .background(
                                    color = FinsibleTheme.colors.transparent,
                                    shape = RoundedCornerShape(FinsibleTheme.dimes.d12)
                                ),
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            ActionButton(
                                label = config.actionLabel,
                                action = {
                                    config.onAction.invoke()
                                    onDismiss()
                                }
                            )
                        }
                    }
                }

                // Progress Bar: Only if autoDismiss & showTimerProgressBar are true
                if (config.autoDismiss && config.showTimerProgressBar) {
                    LinearProgressIndicator(
                        progress = { timeLeft },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(FinsibleTheme.dimes.d2)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = FinsibleTheme.dimes.d16,
                                    bottomEnd = FinsibleTheme.dimes.d16
                                )
                            ),
                        color = config.type.getColor().copy(alpha = 0.8f),
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationIcon(
    notification: NotificationConfig,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(FinsibleTheme.dimes.d48)
            .background(
                color = notification.type.getColor().copy(alpha = 0.2f),
                shape = RoundedCornerShape(FinsibleTheme.dimes.d12)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                id = notification.customIcon ?: notification.type.icon
            ),
            contentDescription = null,
            tint = notification.type.getColor(),
            modifier = Modifier.size(FinsibleTheme.dimes.d20)
        )
    }
}

@Composable
private fun NotificationContent(
    notification: NotificationConfig,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)
    ) {
        Text(
            text = notification.title,
            style = FinsibleTheme.typography.uiSmall,
            color = FinsibleTheme.colors.primaryContent
        )

        notification.subtitle?.let { subtitle ->
            Text(
                text = subtitle,
                style = FinsibleTheme.typography.uiSmall,
                color = FinsibleTheme.colors.secondaryContent,
                softWrap = true
            )
        }
    }
}

@Composable
private fun ActionButton(label: String?, action: () -> Unit) {
    label?.let {
        FinsibleButton(
            label,
            action,
            config = ButtonConfig(
                type = ComponentType.Secondary,
                customCornerRadius = FinsibleTheme.dimes.d48,
                size = ComponentSize.Small
            )
        )
    }
}

enum class NotificationType(val icon: Int) {
    SUCCESS(R.drawable.ic_success),
    ERROR(R.drawable.ic_error),
    WARNING(R.drawable.ic_warning),
    INFO(R.drawable.ic_info);

    companion object {
        @Composable
        fun NotificationType.getColor(): Color {
            return when (this) {
                SUCCESS -> FinsibleTheme.colors.success
                ERROR -> FinsibleTheme.colors.error
                WARNING -> FinsibleTheme.colors.warning
                INFO -> FinsibleTheme.colors.primaryContent60
            }
        }
    }
}

@Stable
data class NotificationConfig(
    val type: NotificationType,
    val title: String,
    val subtitle: String? = null,
    @DrawableRes val customIcon: Int? = null,
    val autoDismiss: Boolean = false,
    val autoDismissDelay: Long = Duration.MS_5000,
    val showTimerProgressBar: Boolean = autoDismiss,
    val showDismissButton: Boolean = false,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
)