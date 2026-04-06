package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** A single labelled radio option. */
@Composable
fun RadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dotColor by animateColorAsState(
        targetValue = if (selected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.transparent,
        animationSpec = tween(150), label = "radio_dot"
    )
    val ringColor by animateColorAsState(
        targetValue = if (selected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.outlineVariant,
        animationSpec = tween(150), label = "radio_ring"
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.secondaryContent,
        animationSpec = tween(150), label = "radio_label"
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
        modifier = modifier
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d6))
            .clickable(onClick = onClick)
            .padding(vertical = FinsibleTheme.dimes.d4)
    ) {
        // Ring
        Box(
            Modifier
                .size(FinsibleTheme.dimes.d18)
                .clip(CircleShape)
                .border(FinsibleTheme.dimes.d1dot5, ringColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Fill dot
            Box(
                Modifier
                    .size(FinsibleTheme.dimes.d9)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
        FinsibleText(text = label, variant = FinsibleTextVariant.SmallBodyMedium, color = labelColor)
    }
}