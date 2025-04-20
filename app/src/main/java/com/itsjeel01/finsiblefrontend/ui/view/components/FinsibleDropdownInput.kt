package com.itsjeel01.finsiblefrontend.ui.view.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import com.itsjeel01.finsiblefrontend.ui.view.InputCommonProps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FinsibleDropdownInput(
    value: T,
    options: List<T>,
    onValueChange: (T) -> Unit,
    commonProps: InputCommonProps = InputCommonProps(),
    clearFocus: (() -> Unit),
    displayText: (T) -> String = { it.toString() },
    itemContent: @Composable (T) -> Unit,
    footerContent: (@Composable (closeDropdown: () -> Unit) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    if (!expanded) clearFocus()

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
                .fieldModifier()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            label = commonProps.labelComposable(),
            maxLines = 1,
            trailingIcon = commonProps.trailingIconComposable() ?: {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = commonProps.leadingIconComposable(),
            supportingText = commonProps.supportingTextComposable(),
            isError = commonProps.isError,
            enabled = commonProps.enabled,
            placeholder = commonProps.placeholderComposable(),
            textStyle = commonProps.primaryTextStyle(),
            colors = finsibleTextFieldColors(accentColor = commonProps.accentColor),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.background(
                color = getCustomColor(key = CustomColorKey.SecondaryBackground)
            ),
            shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        ) {
            // Render dropdown items
            options.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(item)
                        Log.d("FinsibleDropdownInput", "clearFocus() called on item click")
                        expanded = false
                    },
                    text = { itemContent(item) }
                )
            }

            // Optional footer content
            footerContent?.let { footer ->
                footer { expanded = false }
            }
        }
    }
}