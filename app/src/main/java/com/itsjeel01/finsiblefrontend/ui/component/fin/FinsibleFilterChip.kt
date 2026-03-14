package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Configuration for [FinsibleFilterChip]. */
data class FilterChipConfig(
    val enabled: Boolean = true,
    val selectedContainerColor: Color = Color.Unspecified,
    val selectedLabelColor: Color = Color.Unspecified,
    val containerColor: Color = Color.Unspecified,
    val labelColor: Color = Color.Unspecified,
    val selectedBorderColor: Color = Color.Unspecified,
    val borderColor: Color = Color.Unspecified
)

/** Themed filter chip with Finsible brand colors by default. */
@Composable
fun FinsibleFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    config: FilterChipConfig = FilterChipConfig(),
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
        enabled = config.enabled,
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(FinsibleTheme.dimes.d20),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = config.selectedContainerColor.takeOrDefault(FinsibleTheme.colors.inverse),
            selectedLabelColor = config.selectedLabelColor.takeOrDefault(FinsibleTheme.colors.same),
            containerColor = config.containerColor.takeOrDefault(FinsibleTheme.colors.surfaceContainer),
            labelColor = config.labelColor.takeOrDefault(FinsibleTheme.colors.primaryContent)
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = config.borderColor.takeOrDefault(FinsibleTheme.colors.border),
            selectedBorderColor = config.selectedBorderColor.takeOrDefault(FinsibleTheme.colors.brandAccent),
            enabled = config.enabled,
            selected = selected
        )
    )
}
