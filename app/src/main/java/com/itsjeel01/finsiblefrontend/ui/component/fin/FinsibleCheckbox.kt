package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Configuration for [FinsibleCheckbox]. */
data class CheckboxConfig(
    val enabled: Boolean = true,
    val checkedColor: Color = Color.Unspecified,
    val uncheckedColor: Color = Color.Unspecified,
    val checkmarkColor: Color = Color.Unspecified
)

/** Themed checkbox that applies Finsible brand colors by default. */
@Composable
fun FinsibleCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    config: CheckboxConfig = CheckboxConfig()
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = config.enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = config.checkedColor.takeOrDefault(FinsibleTheme.colors.brandAccent),
            uncheckedColor = config.uncheckedColor.takeOrDefault(FinsibleTheme.colors.border),
            checkmarkColor = config.checkmarkColor.takeOrDefault(FinsibleTheme.colors.white)
        )
    )
}
