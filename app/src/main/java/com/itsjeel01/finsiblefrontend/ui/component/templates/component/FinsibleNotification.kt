package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleNotificationDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleNotificationColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleNotificationPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleNotificationVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
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
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    variant: FinsibleNotificationVariant = FinsibleNotificationVariant.Info,
    position: FinsibleNotificationPosition = FinsibleNotificationPosition.Top,
    colors: FinsibleNotificationColors = FinsibleNotificationDefaults.colors(variant),
    isVisible: Boolean = true,
    @DrawableRes customIcon: Int? = null,
    autoDismiss: Boolean = false,
    autoDismissDelay: Long = Duration.MS_5000,
    showTimerProgressBar: Boolean = autoDismiss,
    showDismissButton: Boolean = false,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val showActionButton = actionLabel != null && onAction != null
    val screenHeight = FinsibleTheme.screenHeight.value
    var currentOffsetY by remember { mutableFloatStateOf(0f) }
    var isAutoDismissing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var timeLeft by remember { mutableFloatStateOf(COMPLETE_FRACTION) }

    LaunchedEffect(isVisible) {
        if (autoDismiss && isVisible && showTimerProgressBar) {
            val startTime = System.currentTimeMillis()
            val updateInterval = Duration.MS_16

            while (timeLeft > EMPTY_FRACTION) {
                val elapsed = System.currentTimeMillis() - startTime
                timeLeft = (COMPLETE_FRACTION - (elapsed.toFloat() / autoDismissDelay.toFloat()))
                    .coerceAtLeast(EMPTY_FRACTION)

                if (elapsed >= autoDismissDelay) {
                    isAutoDismissing = true
                    onDismiss()
                    break
                }
                delay(updateInterval)
            }
        }
    }

    // Swipe up to dismiss behavior
    val isTop = position == FinsibleNotificationPosition.Top
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

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            initialOffsetY = { height -> if (isTop) -height else height }
        ),
        exit = fadeOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize()
                .let { mod ->
                    if (isAutoDismissing) {
                        mod
                    } else {
                        mod.graphicsLayer {
                            translationY = animatedOffsetY
                            alpha = animatedAlpha
                        }
                    }
                }
                .pointerInput(position) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            val dismissed = if (isTop) {
                                currentOffsetY <= -verticalDismissThreshold
                            } else {
                                currentOffsetY >= verticalDismissThreshold
                            }

                            if (dismissed) {
                                currentOffsetY = if (isTop) -screenHeight else screenHeight
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
                            if (isTop && newOffset <= 0) currentOffsetY = newOffset
                            if (!isTop && newOffset >= 0) currentOffsetY = newOffset
                        }
                    }
                },
            shape = RoundedCornerShape(FinsibleTheme.dimes.d12),
            colors = CardDefaults.cardColors(containerColor = colors.containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = FinsibleTheme.dimes.d8)
        ) {
            Column {
                val isSimpleNotification = subtitle == null && !showActionButton

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FinsibleTheme.dimes.d12, vertical = FinsibleTheme.dimes.d8),
                    verticalAlignment = if (isSimpleNotification) Alignment.CenterVertically else Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
                ) {
                    Icon(
                        modifier = Modifier.size(FinsibleTheme.dimes.d20),
                        painter = painterResource(id = customIcon ?: FinsibleNotificationDefaults.iconFor(variant)),
                        contentDescription = variant.name,
                        tint = colors.iconTintColor,
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d2)
                    ) {
                        FinsibleText(
                            text = title,
                            variant = FinsibleTextVariant.SmallBodyMedium,
                            color = colors.titleColor,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        subtitle?.let { sub ->
                            FinsibleText(
                                text = sub,
                                variant = FinsibleTextVariant.MicroLabelMedium,
                                color = colors.subtitleColor,
                                softWrap = true
                            )
                        }

                        if (showActionButton) {
                            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides FinsibleTheme.dimes.d0) {
                                FinsibleButton(
                                    modifier = Modifier
                                        .padding(top = FinsibleTheme.dimes.d6)
                                        .requiredHeight(FinsibleTheme.dimes.d28),
                                    text = actionLabel,
                                    onClick = {
                                        onAction()
                                        onDismiss()
                                    },
                                    variant = FinsibleButtonVariant.FilledTonal,
                                    size = FinsibleSize.ExtraSmall,
                                    shapeVariant = FinsibleShape.Pill,
                                    sizes = FinsibleButtonDefaults.sizes(FinsibleSize.ExtraSmall).copy(
                                        contentPadding = PaddingValues(horizontal = FinsibleTheme.dimes.d12, vertical = FinsibleTheme.dimes.d0)
                                    )
                                )
                            }
                        }
                    }

                    if (showDismissButton) {
                        FinsibleButton(
                            modifier = Modifier.clearAndSetSemantics { customActions = listOf() },
                            onClick = onDismiss,
                            iconOnly = true,
                            variant = FinsibleButtonVariant.Text,
                            size = FinsibleSize.ExtraSmall,
                            shapeVariant = FinsibleShape.Circle,
                            colors = FinsibleButtonDefaults.colors(
                                variant = FinsibleButtonVariant.Text,
                                contentColor = FinsibleTheme.colors.secondaryContent
                            ),
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = stringResource(R.string.cd_dismiss_notification),
                                    tint = LocalContentColor.current
                                )
                            }
                        )
                    }
                }

                if (autoDismiss && showTimerProgressBar) {
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
                        color = colors.progressIndicatorColor,
                        trackColor = colors.progressTrackColor,
                    )
                }
            }
        }
    }
}