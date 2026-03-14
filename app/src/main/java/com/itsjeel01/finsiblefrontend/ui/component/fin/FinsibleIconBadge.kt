package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Defaults factory for [FinsibleIconBadge] sizes. */
object FinsibleIconBadgeDefaults {

    /** Default badge (outer container) size. */
    @Composable
    fun badgeSize(): Dp = FinsibleTheme.dimes.d40

    /** Default icon (inner drawable) size. */
    @Composable
    fun iconSize(): Dp = FinsibleTheme.dimes.d20

    /** Default background alpha applied to [tint]. */
    const val BackgroundAlpha = 0.1f
}

/** Icon rendered inside a tinted background shape. */
@Composable
fun FinsibleIconBadge(
    icon: Int,
    tint: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    badgeSize: Dp = FinsibleIconBadgeDefaults.badgeSize(),
    iconSize: Dp = FinsibleIconBadgeDefaults.iconSize(),
    shape: Shape = CircleShape,
    backgroundAlpha: Float = FinsibleIconBadgeDefaults.BackgroundAlpha
) {
    Box(
        modifier = modifier
            .size(badgeSize)
            .clip(shape)
            .background(tint.copy(alpha = backgroundAlpha)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}
