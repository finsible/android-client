package com.itsjeel01.finsiblefrontend.ui.component.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Radius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.roundedCornerShape
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BaseDropdownInput(
    value: T,
    options: List<T>,
    onValueChange: (T) -> Unit,
    commonProps: CommonProps = CommonProps(),
    clearFocus: (() -> Unit),
    displayText: (T) -> String = { it.toString() },
    item: @Composable (T) -> Unit,
    footer: (@Composable (closeDropdown: () -> Unit) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    if (!expanded) clearFocus()

    // --- Dropdown Input UI ---

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        modifier = commonProps.modifier
    ) {
        TextField(
            readOnly = true,
            value = displayText(value),
            onValueChange = { },
            modifier = commonProps
                .commonModifier()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            label = commonProps.label(),
            maxLines = 1,
            trailingIcon = commonProps.trailingIconComposable() ?: {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = commonProps.leadingIconComposable(),
            supportingText = commonProps.supportingText(),
            isError = commonProps.isError,
            enabled = commonProps.enabled,
            placeholder = commonProps.placeholder(),
            textStyle = commonProps.primaryTextStyle(),
            colors = finsibleTextFieldColors(accentColor = commonProps.accentColor),
        )

        // --- Dropdown Menu UI ---

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.background(
                color = getCustomColor(key = ColorKey.SecondaryBackground)
            ),
            shape = appDimensions().roundedCornerShape(Radius.SM),
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(item)
                        Log.d(Strings.BASE_DROPDOWN_INPUT, "clearFocus() called on item click")
                        expanded = false
                    },
                    text = { item(item) }
                )
            }

            footer?.let { footer ->
                footer { expanded = false }
            }
        }
    }
}
