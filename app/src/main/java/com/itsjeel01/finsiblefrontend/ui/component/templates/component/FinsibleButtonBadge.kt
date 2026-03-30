package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleButtonBadgeSpec

@Composable
internal fun FinsibleButtonBadge(
    badgeType: FinsibleBadgeType,
    badgeCount: Int,
    badgeMetrics: FinsibleButtonBadgeSpec,
    badgeDiameter: Dp,
    containerColor: Color,
    contentColor: Color,
    badgeDescription: String?
) {
    when (badgeType) {
        FinsibleBadgeType.None -> Unit
        FinsibleBadgeType.Dot -> {
            Box(
                modifier = Modifier
                    .size(badgeDiameter)
                    .semantics {
                        badgeDescription?.let { contentDescription = it }
                    }
                    .background(containerColor, CircleShape)
            )
        }

        FinsibleBadgeType.Count -> {
            // Strictly bound to 99 to fit perfectly inside the circular bounds
            val displayText = badgeCount.coerceIn(0, 99).toString()

            Box(
                modifier = Modifier
                    .size(badgeDiameter)
                    .semantics {
                        badgeDescription?.let { contentDescription = it }
                    }
                    .background(containerColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    color = contentColor,
                    style = badgeMetrics.textStyle,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}