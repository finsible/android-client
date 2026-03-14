package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Color configuration for [FinsibleSwitch]. */
@Immutable
data class FinsibleSwitchColors(
    val checkedThumbColor: Color,
    val checkedTrackColor: Color,
    val uncheckedThumbColor: Color,
    val uncheckedTrackColor: Color,
    val uncheckedBorderColor: Color
)

/** Defaults factory for [FinsibleSwitch] colors. */
object FinsibleSwitchDefaults {

    /** Brand-themed switch colors. */
    @Composable
    fun colors(
        checkedThumbColor: Color = FinsibleTheme.colors.white,
        checkedTrackColor: Color = FinsibleTheme.colors.brandAccent,
        uncheckedThumbColor: Color = FinsibleTheme.colors.white,
        uncheckedTrackColor: Color = FinsibleTheme.colors.border,
        uncheckedBorderColor: Color = FinsibleTheme.colors.border
    ) = FinsibleSwitchColors(
        checkedThumbColor, checkedTrackColor,
        uncheckedThumbColor, uncheckedTrackColor, uncheckedBorderColor
    )
}

/** Themed switch that applies Finsible brand colors by default. */
@Composable
fun FinsibleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: FinsibleSwitchColors = FinsibleSwitchDefaults.colors()
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.checkedThumbColor,
            checkedTrackColor = colors.checkedTrackColor,
            uncheckedThumbColor = colors.uncheckedThumbColor,
            uncheckedTrackColor = colors.uncheckedTrackColor,
            uncheckedBorderColor = colors.uncheckedBorderColor
        )
    )
}
