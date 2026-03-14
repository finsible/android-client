package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

/** Color configuration for [FinsibleRadioButton]. */
@Immutable
data class FinsibleRadioButtonColors(
    val selectedDotColor: Color,
    val unselectedDotColor: Color,
    val selectedRingColor: Color,
    val unselectedRingColor: Color,
    val selectedLabelColor: Color,
    val unselectedLabelColor: Color
)

/** Defaults factory for [FinsibleRadioButton] colors. */
object FinsibleRadioButtonDefaults {

    /** Brand-themed radio button colors. */
    @Composable
    fun colors(
        selectedDotColor: Color = FinsibleTheme.colors.primaryContent,
        unselectedDotColor: Color = FinsibleTheme.colors.transparent,
        selectedRingColor: Color = FinsibleTheme.colors.primaryContent,
        unselectedRingColor: Color = FinsibleTheme.colors.outlineVariant,
        selectedLabelColor: Color = FinsibleTheme.colors.primaryContent,
        unselectedLabelColor: Color = FinsibleTheme.colors.secondaryContent
    ) = FinsibleRadioButtonColors(
        selectedDotColor, unselectedDotColor,
        selectedRingColor, unselectedRingColor,
        selectedLabelColor, unselectedLabelColor
    )
}

/** Themed radio button with animated dot, ring, and label. */
@Composable
fun FinsibleRadioButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: FinsibleRadioButtonColors = FinsibleRadioButtonDefaults.colors()
) {
    val dotColor by animateColorAsState(
        targetValue = if (selected) colors.selectedDotColor else colors.unselectedDotColor,
        animationSpec = tween(Duration.MS_150.toInt()), label = "radio_dot"
    )
    val ringColor by animateColorAsState(
        targetValue = if (selected) colors.selectedRingColor else colors.unselectedRingColor,
        animationSpec = tween(Duration.MS_150.toInt()), label = "radio_ring"
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) colors.selectedLabelColor else colors.unselectedLabelColor,
        animationSpec = tween(Duration.MS_150.toInt()), label = "radio_label"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
        modifier = modifier
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d6))
            .clickable(onClick = onClick)
            .padding(vertical = FinsibleTheme.dimes.d4)
    ) {
        Box(
            Modifier
                .size(FinsibleTheme.dimes.d18)
                .clip(CircleShape)
                .border(FinsibleTheme.dimes.d1dot5, ringColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(FinsibleTheme.dimes.d9)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
        Text(label, style = FinsibleTheme.typography.t14.medium(), color = labelColor)
    }
}
