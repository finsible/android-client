package com.itsjeel01.finsiblefrontend.ui.inappnotification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
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
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleNotification
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NotificationHost(
    modifier: Modifier = Modifier,
    notificationManager: NotificationManager,
    content: @Composable () -> Unit
) {
    val currentNotification by notificationManager.currentNotification.collectAsStateWithLifecycle()
    var isVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentNotification) {
        if (currentNotification != null) isVisible = true
    }

    val handleDismiss = {
        isVisible = false
        coroutineScope.launch {
            delay(300)
            notificationManager.dismiss()
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
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .systemBarsPadding()
                        .padding(FinsibleTheme.dimes.d16),
                    contentAlignment = Alignment.Center
                ) {
                    FinsibleNotification(
                        config = notification,
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