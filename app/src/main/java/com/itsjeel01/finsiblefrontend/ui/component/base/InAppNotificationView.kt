package com.itsjeel01.finsiblefrontend.ui.component.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import com.itsjeel01.finsiblefrontend.ui.util.Animations
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotification
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotificationType
import kotlinx.coroutines.delay

@Composable
fun InAppNotificationView(
    notification: InAppNotification,
    isVisible: Boolean,
    onDismiss: () -> Unit
) {

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
                delay(8L)
            }
        }
    }

    // --- Notification Card UI ---

    AnimatedVisibility(
        visible = isVisible,
        enter = Animations.enterNotification(notification.position),
        exit = Animations.exitNotification(notification.position)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // --- Timer Progress Bar (Conditional) ---

                if (notification.autoDismiss) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
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
        modifier = modifier.size(20.dp)
    )

}

@Composable
private fun NotificationContent(
    notification: InAppNotification,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
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
                    RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
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