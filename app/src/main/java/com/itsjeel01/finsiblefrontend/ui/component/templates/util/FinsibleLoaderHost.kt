package com.itsjeel01.finsiblefrontend.ui.component.templates.util

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleLoader
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleLoaderSpeed
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleOverlayOpacity
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun FinsibleLoaderHost(
    modifier: Modifier = Modifier,
    finsibleLoaderManager: FinsibleLoaderManager,
    content: @Composable () -> Unit
) {
    val isActive by finsibleLoaderManager.isActive.collectAsStateWithLifecycle()
    val message by finsibleLoaderManager.message.collectAsStateWithLifecycle()
    val opacity by finsibleLoaderManager.opacity.collectAsStateWithLifecycle()

    BackHandler(enabled = isActive) { } // Block hardware back button

    Box(modifier = modifier.fillMaxSize()) {
        content()

        if (isActive) {
            FullScreenLoaderOverlay(
                message = message,
                opacity = opacity,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(Float.MAX_VALUE)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent().changes.forEach { it.consume() }
                            }
                        }
                    }
            )
        }
    }
}

@Composable
private fun FullScreenLoaderOverlay(
    modifier: Modifier = Modifier,
    message: String?,
    opacity: FinsibleOverlayOpacity
) {
    val scrimColor = FinsibleTheme.colors.primaryBackground.copy(alpha = opacity.alpha)

    Box(
        modifier = modifier.background(scrimColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d20)
        ) {
            FinsibleLoader(
                size = FinsibleSize.Large,
                speed = FinsibleLoaderSpeed.Normal
            )

            message?.let { msg ->
                Text(
                    text = msg,
                    style = FinsibleTheme.typography.t20,
                    color = FinsibleTheme.colors.secondaryContent,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}