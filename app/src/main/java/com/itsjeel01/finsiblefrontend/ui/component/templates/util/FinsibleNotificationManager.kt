package com.itsjeel01.finsiblefrontend.ui.component.templates.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.staticCompositionLocalOf
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleNotificationPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleNotificationVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

val LocalFinsibleNotification = staticCompositionLocalOf<FinsibleNotificationManager> {
    error("No FinsibleNotificationManager provided, make sure to wrap your content in a FinsibleNotificationHost")
}

data class FinsibleNotificationStateConfig(
    val title: String,
    val variant: FinsibleNotificationVariant,
    val subtitle: String? = null,
    val position: FinsibleNotificationPosition = FinsibleNotificationPosition.Top,
    @DrawableRes val customIcon: Int? = null,
    val autoDismiss: Boolean = false,
    val autoDismissDelay: Long = 5000L,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null
)

@Singleton
class FinsibleNotificationManager @Inject constructor() {

    private val _currentNotification = MutableStateFlow<FinsibleNotificationStateConfig?>(null)
    val currentNotification: StateFlow<FinsibleNotificationStateConfig?> = _currentNotification.asStateFlow()

    fun show(config: FinsibleNotificationStateConfig) {
        _currentNotification.value = config
    }

    fun dismiss() {
        _currentNotification.value = null
    }

    fun isNotificationActive(): Boolean = _currentNotification.value != null

    fun showSuccess(
        title: String,
        subtitle: String? = null,
        position: FinsibleNotificationPosition = FinsibleNotificationPosition.Top,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            FinsibleNotificationStateConfig(
                title = title,
                subtitle = subtitle,
                position = position,
                variant = FinsibleNotificationVariant.Success,
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
        position: FinsibleNotificationPosition = FinsibleNotificationPosition.Top,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            FinsibleNotificationStateConfig(
                title = title,
                subtitle = subtitle,
                position = position,
                variant = FinsibleNotificationVariant.Error,
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
        position: FinsibleNotificationPosition = FinsibleNotificationPosition.Top,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            FinsibleNotificationStateConfig(
                title = title,
                subtitle = subtitle,
                position = position,
                variant = FinsibleNotificationVariant.Warning,
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
        position: FinsibleNotificationPosition = FinsibleNotificationPosition.Top,
        @DrawableRes customIcon: Int? = null,
        autoDismiss: Boolean = false,
        actionLabel: String? = null,
        onAction: (() -> Unit)? = null,
        autoDismissDelay: Long = 5000L
    ) {
        show(
            FinsibleNotificationStateConfig(
                title = title,
                subtitle = subtitle,
                position = position,
                variant = FinsibleNotificationVariant.Info,
                customIcon = customIcon,
                autoDismiss = autoDismiss,
                actionLabel = actionLabel,
                onAction = onAction,
                autoDismissDelay = autoDismissDelay
            )
        )
    }
}