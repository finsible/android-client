package com.itsjeel01.finsiblefrontend.ui.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable

@Stable
data class InAppNotification(
    val type: InAppNotificationType,
    val title: String,
    val subtitle: String? = null,
    @DrawableRes val customIcon: Int? = null,
    val autoDismiss: Boolean = false,
    val autoDismissDelay: Long = 5000L,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val position: InAppNotificationPosition = InAppNotificationPosition.TOP,
)

enum class InAppNotificationType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

enum class InAppNotificationPosition {
    TOP,
    BOTTOM
}