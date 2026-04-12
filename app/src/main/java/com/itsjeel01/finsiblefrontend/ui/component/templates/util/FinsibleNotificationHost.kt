package com.itsjeel01.finsiblefrontend.ui.component.templates.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleNotification
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleNotificationPosition
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FinsibleNotificationHost(
    modifier: Modifier = Modifier,
    notificationManager: FinsibleNotificationManager,
    content: @Composable () -> Unit
) {
    val currentNotification by notificationManager.currentNotification.collectAsStateWithLifecycle()
    var isVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentNotification) {
        isVisible = currentNotification != null
    }

    val handleDismiss = {
        isVisible = false
        coroutineScope.launch {
            delay(Duration.MS_300)
            notificationManager.dismiss()
        }
        Unit
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        currentNotification?.let { config ->
            val overlayAlignment = if (config.position == FinsibleNotificationPosition.Top) {
                Alignment.TopCenter
            } else {
                Alignment.BottomCenter
            }

            Box(
                modifier = Modifier
                    .align(overlayAlignment)
                    .fillMaxWidth()
                    .zIndex(Float.MAX_VALUE)
                    .systemBarsPadding()
                    .padding(FinsibleTheme.dimes.d16),
                contentAlignment = Alignment.Center
            ) {
                FinsibleNotification(
                    title = config.title,
                    subtitle = config.subtitle,
                    position = config.position,
                    variant = config.variant,
                    customIcon = config.customIcon,
                    autoDismiss = config.autoDismiss,
                    autoDismissDelay = config.autoDismissDelay,
                    actionLabel = config.actionLabel,
                    onAction = config.onAction,
                    isVisible = isVisible,
                    onDismiss = handleDismiss
                )
            }
        }
    }
}