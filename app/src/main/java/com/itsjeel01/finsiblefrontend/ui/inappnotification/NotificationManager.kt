package com.itsjeel01.finsiblefrontend.ui.inappnotification

import androidx.annotation.DrawableRes
import com.itsjeel01.finsiblefrontend.ui.component.fin.NotificationConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.NotificationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManager @Inject constructor() {

    private val _currentNotification = MutableStateFlow<NotificationConfig?>(null)
    val currentNotification: StateFlow<NotificationConfig?> =
        _currentNotification.asStateFlow()

    fun show(notification: NotificationConfig) {
        _currentNotification.value = notification
    }

    fun dismiss() {
        _currentNotification.value = null
    }

    fun isNotificationActive(): Boolean = _currentNotification.value != null

    fun showSuccess(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            NotificationConfig(
                title = title,
                subtitle = subtitle,
                type = NotificationType.SUCCESS,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                autoDismissDelay = autoDismissDelay
            )
        )
    }

    fun showError(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            NotificationConfig(
                title = title,
                subtitle = subtitle,
                type = NotificationType.ERROR,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                autoDismissDelay = autoDismissDelay
            )
        )
    }

    fun showWarning(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            NotificationConfig(
                title = title,
                subtitle = subtitle,
                type = NotificationType.WARNING,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                autoDismissDelay = autoDismissDelay
            )
        )
    }

    fun showInfo(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            NotificationConfig(
                title = title,
                subtitle = subtitle,
                type = NotificationType.INFO,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                autoDismissDelay = autoDismissDelay
            )
        )
    }
}