package com.itsjeel01.finsiblefrontend.ui.component.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.dime.IconSize
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Radius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.height
import com.itsjeel01.finsiblefrontend.ui.theme.dime.iconSize
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingAll
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingHorizontal
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingVertical
import com.itsjeel01.finsiblefrontend.ui.theme.dime.roundedCornerShape
import com.itsjeel01.finsiblefrontend.ui.theme.dime.size
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import com.itsjeel01.finsiblefrontend.ui.util.Animation
import com.itsjeel01.finsiblefrontend.ui.util.Duration
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotification
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotificationType
import kotlinx.coroutines.delay

@Composable
fun InAppNotificationView(
    notification: InAppNotification,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    val dims = appDimensions()

    // --- Auto Dismiss Logic ---

    var progress by remember(notification) { mutableFloatStateOf(1f) }

    LaunchedEffect(notification.hashCode(), isVisible) {
        if (notification.autoDismiss && isVisible) {
            val startTime = System.currentTimeMillis()
            val duration = notification.autoDismissDelay

            while (progress > 0f) {
                val elapsed = System.currentTimeMillis() - startTime
                progress = (1f - (elapsed.toFloat() / duration.toFloat())).coerceAtLeast(0f)

                if (elapsed >= duration) {
                    onDismiss()
                    break
                }
                delay(Duration.MSEC_8)
            }
        }
    }

    // --- Notification Card UI ---

    AnimatedVisibility(
        visible = isVisible,
        enter = Animation.enterNotification(notification.position),
        exit = Animation.exitNotification(notification.position)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = dims.roundedCornerShape(Radius.LG),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = dims.size(Size.S6))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingHorizontal(Size.S16)
                        .paddingVertical(Size.S8),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dims.size(Size.S8))
                ) {
                    // --- Icon (Custom or based on type) ---

                    NotificationIcon(
                        notification = notification,
                        modifier = Modifier
                    )

                    // --- Title (Always rendered) & Subtitle (Conditional) ---

                    NotificationContent(
                        notification = notification,
                        modifier = Modifier.weight(1f)
                    )

                    // --- Action Button (Conditional) ---

                    ActionButton(notification.actionLabel) {
                        notification.onAction?.invoke()
                        onDismiss()
                    }

                    // --- Dismiss Button (Always rendered) ---

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(Size.S24)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(Size.S16)
                        )
                    }
                }

                // --- Timer Progress Bar (Conditional) ---

                if (notification.autoDismiss) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Size.S2)
                            .clip(
                                dims.roundedCornerShape(
                                    bottomStart = Radius.LG,
                                    bottomEnd = Radius.LG
                                )
                            ),
                        color = getContentColor(notification.type).copy(alpha = 0.8f),
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationIcon(
    notification: InAppNotification,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(
            id = notification.customIcon ?: getTypeBasedIcon(notification.type)
        ),
        contentDescription = null,
        tint = getContentColor(notification.type),
        modifier = modifier.iconSize(IconSize.MD)
    )

}

@Composable
private fun NotificationContent(
    notification: InAppNotification,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.paddingHorizontal(Size.S8),
    ) {
        // Title - Always rendered (mandatory)
        Text(
            text = notification.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        // Subtitle - Only rendered if provided
        notification.subtitle?.let { subtitle ->
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.outline
                ),
                softWrap = true
            )
        }
    }
}

@Composable
private fun ActionButton(label: String?, action: () -> Unit) {
    label?.let {
        Box(
            modifier = Modifier
                .clickable(
                    onClick = action
                )
                .background(
                    MaterialTheme.colorScheme.onBackground,
                    appDimensions().roundedCornerShape(Radius.MD)
                )
        ) {
            Text(
                modifier = Modifier.paddingAll(Size.S8),
                text = label,
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
            )
        }
    }
}

@Composable
private fun getTypeBasedIcon(type: InAppNotificationType): Int {
    return when (type) {
        InAppNotificationType.SUCCESS -> R.drawable.ic_success
        InAppNotificationType.ERROR -> R.drawable.ic_error
        InAppNotificationType.WARNING -> R.drawable.ic_warning
        InAppNotificationType.INFO -> R.drawable.ic_info
    }
}

@Composable
private fun getContentColor(type: InAppNotificationType): Color {
    return when (type) {
        InAppNotificationType.SUCCESS -> getCustomColor(ColorKey.GREEN)
        InAppNotificationType.ERROR -> MaterialTheme.colorScheme.error
        InAppNotificationType.WARNING -> getCustomColor(ColorKey.YELLOW)
        InAppNotificationType.INFO -> getCustomColor(ColorKey.BLUE)
    }
}