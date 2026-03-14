package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Color configuration for [FinsibleFilterChip]. */
@Immutable
data class FinsibleFilterChipColors(
    val selectedContainerColor: Color,
    val selectedLabelColor: Color,
    val containerColor: Color,
    val labelColor: Color,
    val selectedBorderColor: Color,
    val borderColor: Color
)

/** Defaults factory for [FinsibleFilterChip] colors. */
object FinsibleFilterChipDefaults {

    /** Brand-themed filter chip colors. */
    @Composable
    fun colors(
        selectedContainerColor: Color = FinsibleTheme.colors.inverse,
        selectedLabelColor: Color = FinsibleTheme.colors.same,
        containerColor: Color = FinsibleTheme.colors.surfaceContainer,
        labelColor: Color = FinsibleTheme.colors.primaryContent,
        selectedBorderColor: Color = FinsibleTheme.colors.brandAccent,
        borderColor: Color = FinsibleTheme.colors.border
    ) = FinsibleFilterChipColors(
        selectedContainerColor, selectedLabelColor,
        containerColor, labelColor, selectedBorderColor, borderColor
    )
}

/** Themed filter chip with Finsible brand colors by default. */
@Composable
fun FinsibleFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: FinsibleFilterChipColors = FinsibleFilterChipDefaults.colors(),
    leadingIcon: @Composable (() -> Unit)? = null
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = FinsibleTheme.typography.t14,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
        },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(FinsibleTheme.dimes.d20),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colors.selectedContainerColor,
            selectedLabelColor = colors.selectedLabelColor,
            containerColor = colors.containerColor,
            labelColor = colors.labelColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = colors.borderColor,
            selectedBorderColor = colors.selectedBorderColor,
            enabled = enabled,
            selected = selected
        )
    )
}
