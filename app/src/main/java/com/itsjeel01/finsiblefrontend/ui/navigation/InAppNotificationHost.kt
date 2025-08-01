package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.itsjeel01.finsiblefrontend.ui.component.base.InAppNotificationView
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotificationManager
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotificationPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InAppNotificationHost(
    modifier: Modifier = Modifier,
    inAppNotificationManager: InAppNotificationManager,
    content: @Composable () -> Unit
) {
    val currentNotification by inAppNotificationManager.currentNotification.collectAsState()
    var isVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentNotification) {
        if (currentNotification != null) {
            isVisible = true
        }
    }

    val handleDismiss = {
        isVisible = false
        coroutineScope.launch {
            delay(300)
            inAppNotificationManager.dismiss()
        }
        Unit
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .zIndex(Float.MAX_VALUE),
        contentWindowInsets = WindowInsets.systemBars
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            content()

            currentNotification?.let { notification ->
                val isTop = notification.position == InAppNotificationPosition.TOP
                val alignment = if (isTop) Alignment.TopCenter else Alignment.BottomCenter
                val topPadding =
                    if (isTop) WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
                    else 0.dp
                val bottomPadding =
                    if (!isTop) WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
                    else 0.dp

                Box(
                    modifier = Modifier
                        .align(alignment)
                        .fillMaxWidth()
                        .padding(
                            top = topPadding + 16.dp,
                            bottom = bottomPadding + 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    InAppNotificationView(
                        notification = notification,
                        isVisible = isVisible,
                        onDismiss = handleDismiss
                    )
                }
            } ?: run {
                isVisible = false
            }
        }
    }
}