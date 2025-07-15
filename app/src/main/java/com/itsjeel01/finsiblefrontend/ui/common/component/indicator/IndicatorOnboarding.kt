package com.itsjeel01.finsiblefrontend.ui.common.component.indicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.core.constants.AppConstants

@Composable
fun OnboardingIndicators(currentSlide: Int, totalSlides: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSlides) { index ->
            val isCurrent = index == currentSlide
            val width by animateDpAsState(
                targetValue = if (isCurrent) 48.dp else 16.dp,
                animationSpec = tween(
                    durationMillis = AppConstants.ANIMATION_DURATION_SHORT, easing = EaseOut
                )
            )
            val color by animateColorAsState(
                targetValue = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                animationSpec = tween(durationMillis = AppConstants.ANIMATION_DURATION_SHORT, easing = EaseOut)
            )
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(width)
                    .background(
                        color = color,
                        shape = CircleShape
                    )
                    .shadow(
                        elevation = if (isCurrent) 4.dp else 0.dp,
                        ambientColor = MaterialTheme.colorScheme.onBackground,
                        spotColor = MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}