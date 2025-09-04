package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun FinsiblePageIndicators(modifier: Modifier, currentItem: Int, totalItems: Int) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
        verticalAlignment = Alignment.CenterVertically
    ) {

        fun <T> linearAnimationSpec() = tween<T>(durationMillis = Duration.MS_300.toInt())

        repeat(totalItems) { index ->
            val isCurrent = index == currentItem

            val width by animateDpAsState(
                targetValue = if (isCurrent) FinsibleTheme.dimes.d48 else FinsibleTheme.dimes.d12,
                animationSpec = linearAnimationSpec(),
                label = "indicator_width"
            )

            val color by animateColorAsState(
                targetValue = if (isCurrent)
                    FinsibleTheme.colors.brandAccent
                else
                    FinsibleTheme.colors.primaryContent40,
                animationSpec = linearAnimationSpec(),
                label = "indicator_color"
            )

            Box(
                modifier = Modifier
                    .width(width)
                    .height(FinsibleTheme.dimes.d4)
                    .clip(RoundedCornerShape(FinsibleTheme.dimes.d2))
                    .background(color)
            )
        }
    }
}