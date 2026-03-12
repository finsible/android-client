package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Configuration for [FinsibleSwitch]. */
data class SwitchConfig(
    val enabled: Boolean = true,
    val checkedThumbColor: Color = Color.Unspecified,
    val checkedTrackColor: Color = Color.Unspecified,
    val uncheckedThumbColor: Color = Color.Unspecified,
    val uncheckedTrackColor: Color = Color.Unspecified,
    val uncheckedBorderColor: Color = Color.Unspecified
)

/** Themed switch that applies Finsible brand colors by default. */
@Composable
fun FinsibleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    config: SwitchConfig = SwitchConfig()
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = config.enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = config.checkedThumbColor.takeOrDefault(FinsibleTheme.colors.white),
            checkedTrackColor = config.checkedTrackColor.takeOrDefault(FinsibleTheme.colors.brandAccent),
            uncheckedThumbColor = config.uncheckedThumbColor.takeOrDefault(FinsibleTheme.colors.white),
            uncheckedTrackColor = config.uncheckedTrackColor.takeOrDefault(FinsibleTheme.colors.border),
            uncheckedBorderColor = config.uncheckedBorderColor.takeOrDefault(FinsibleTheme.colors.border)
        )
    )
}
