package com.itsjeel01.finsiblefrontend.ui.util

import androidx.annotation.DrawableRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InAppNotificationManager @Inject constructor() {

    private val _currentNotification = MutableStateFlow<InAppNotification?>(null)
    val currentNotification: StateFlow<InAppNotification?> = _currentNotification.asStateFlow()

    fun show(notification: InAppNotification) {
        _currentNotification.value = notification
    }

    fun dismiss() {
        _currentNotification.value = null
    }

    fun hasActiveNotification(): Boolean = _currentNotification.value != null

    /** Shows a success notification with sensible defaults. */
    fun showSuccess(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        position: InAppNotificationPosition = InAppNotificationPosition.TOP,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            InAppNotification(
                title = title,
                subtitle = subtitle,
                type = InAppNotificationType.SUCCESS,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                position = position,
                autoDismissDelay = autoDismissDelay
            )
        )
    }

    /** Shows an error notification with sensible defaults. */
    fun showError(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        position: InAppNotificationPosition = InAppNotificationPosition.TOP,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            InAppNotification(
                title = title,
                subtitle = subtitle,
                type = InAppNotificationType.ERROR,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                position = position,
                autoDismissDelay = autoDismissDelay
            )
        )
    }

    /** Shows a warning notification with sensible defaults. */
    fun showWarning(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        position: InAppNotificationPosition = InAppNotificationPosition.TOP,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            InAppNotification(
                title = title,
                subtitle = subtitle,
                type = InAppNotificationType.WARNING,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                position = position,
                autoDismissDelay = autoDismissDelay
            )
        )
    }

    /** Shows an info notification with sensible defaults. */
    fun showInfo(
        title: String,
        subtitle: String? = null,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        position: InAppNotificationPosition = InAppNotificationPosition.TOP,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            InAppNotification(
                title = title,
                subtitle = subtitle,
                type = InAppNotificationType.INFO,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                position = position,
                autoDismissDelay = autoDismissDelay
            )
        )
    }
}