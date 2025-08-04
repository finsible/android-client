package com.itsjeel01.finsiblefrontend.ui.component.base

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.height
import com.itsjeel01.finsiblefrontend.ui.theme.dime.size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.spacing
import com.itsjeel01.finsiblefrontend.ui.util.Animation

@Composable
fun BaseCarouselIndicators(currentItem: Int, totalItems: Int) {
    val dims = appDimensions()

    Row(
        horizontalArrangement = Arrangement.spacedBy(dims.spacing(Size.S8)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalItems) { index ->
            val isCurrent = index == currentItem

            val width by animateDpAsState(
                targetValue =
                    if (isCurrent) dims.size(Size.S48)
                    else dims.size(Size.S16),
                animationSpec = Animation.easeOut()
            )
            val color by animateColorAsState(
                targetValue =
                    if (isCurrent) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                animationSpec = Animation.easeOut()
            )

            Box(
                modifier = Modifier
                    .height(Size.S4)
                    .width(width)
                    .background(
                        color = color,
                        shape = CircleShape
                    )
                    .shadow(
                        elevation =
                            if (isCurrent) dims.size(Size.S4)
                            else dims.size(Size.ZERO),
                        ambientColor = MaterialTheme.colorScheme.onBackground,
                        spotColor = MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}
