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

private const val DEFAULT_BACKGROUND_ALPHA = 0.1f

/** Configuration for [FinsibleIconBadge]. */
data class IconBadgeConfig(
    val badgeSize: Dp = Dp.Unspecified,
    val iconSize: Dp = Dp.Unspecified,
    val shape: Shape = CircleShape,
    val backgroundAlpha: Float = DEFAULT_BACKGROUND_ALPHA
)

/** Icon rendered inside a tinted background shape. */
@Composable
fun FinsibleIconBadge(
    icon: Int,
    tint: Color,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    config: IconBadgeConfig = IconBadgeConfig()
) {
    val badgeSize = if (config.badgeSize == Dp.Unspecified) FinsibleTheme.dimes.d40 else config.badgeSize
    val iconSize = if (config.iconSize == Dp.Unspecified) FinsibleTheme.dimes.d20 else config.iconSize

    Box(
        modifier = modifier
            .size(badgeSize)
            .clip(config.shape)
            .background(tint.copy(alpha = config.backgroundAlpha)),
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
