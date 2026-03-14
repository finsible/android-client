package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Color configuration for [FinsibleCheckbox]. */
@Immutable
data class FinsibleCheckboxColors(
    val checkedColor: Color,
    val uncheckedColor: Color,
    val checkmarkColor: Color
)

/** Defaults factory for [FinsibleCheckbox] colors. */
object FinsibleCheckboxDefaults {

    /** Brand-themed checkbox colors. */
    @Composable
    fun colors(
        checkedColor: Color = FinsibleTheme.colors.brandAccent,
        uncheckedColor: Color = FinsibleTheme.colors.border,
        checkmarkColor: Color = FinsibleTheme.colors.white
    ) = FinsibleCheckboxColors(checkedColor, uncheckedColor, checkmarkColor)
}

/** Themed checkbox that applies Finsible brand colors by default. */
@Composable
fun FinsibleCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: FinsibleCheckboxColors = FinsibleCheckboxDefaults.colors()
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = colors.checkedColor,
            uncheckedColor = colors.uncheckedColor,
            checkmarkColor = colors.checkmarkColor
        )
    )
}
