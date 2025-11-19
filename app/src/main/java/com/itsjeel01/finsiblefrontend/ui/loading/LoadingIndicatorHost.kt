package com.itsjeel01.finsiblefrontend.ui.loading

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleLoadingIndicator
import com.itsjeel01.finsiblefrontend.ui.component.fin.LoadingIndicatorConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.LoadingSpeed
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

private const val OVERLAY_ALPHA = 0.5f

@Composable
fun LoadingIndicatorHost(
    modifier: Modifier = Modifier,
    loadingIndicatorManager: LoadingIndicatorManager,
    content: @Composable () -> Unit
) {
    val isActive by loadingIndicatorManager.isActive.collectAsStateWithLifecycle()
    val message by loadingIndicatorManager.message.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        content()

        if (isActive) {
            FullScreenLoadingOverlay(
                message = message,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(Float.MAX_VALUE)
            )
        }
    }
}

@Composable
private fun FullScreenLoadingOverlay(
    modifier: Modifier = Modifier,
    message: String?
) {
    Box(
        modifier = modifier.background(
            FinsibleTheme.colors.primaryBackground.copy(alpha = OVERLAY_ALPHA)
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d20)
        ) {
            FinsibleLoadingIndicator(
                config = LoadingIndicatorConfig(
                    size = ComponentSize.Large,
                    speed = LoadingSpeed.NORMAL
                )
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