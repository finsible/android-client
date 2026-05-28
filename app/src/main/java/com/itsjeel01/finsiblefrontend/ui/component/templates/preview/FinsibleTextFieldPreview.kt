package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleAmountTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleNoteTextField
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 900)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 900, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleTextFieldPreview() {
    FinsibleComponentPreviewScaffold {
        var text by remember { mutableStateOf("") }
        var errorText by remember { mutableStateOf("123") }
        var note by remember { mutableStateOf("Longer note") }
        var amount by remember { mutableStateOf("2500") }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackLg)
        ) {
            FinsibleAmountTextField(
                value = amount,
                onValueChange = { amount = it },
                placeholder = "0",
                label = "Amount"
            )

            FinsibleTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = "Enter text",
                label = "Label",
                size = FinsibleSize.Medium,
                leadingIcon = {
                    Icon(painter = painterResource(android.R.drawable.ic_menu_search), contentDescription = null)
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                        contentDescription = null,
                        modifier = Modifier.clickable { text = "" }
                    )
                }
            )

            FinsibleTextField(
                value = errorText,
                onValueChange = { errorText = it },
                placeholder = "Error placeholder",
                label = "Error Label",
                supportingText = "Helper text shown below",
                size = FinsibleSize.Small,
                isError = true,
                shapeVariant = FinsibleShape.Pill
            )

            FinsibleNoteTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = "Add a note",
                label = "Note",
                size = FinsibleSize.Small
            )

            FinsibleTextField(
                value = "Disabled",
                onValueChange = {},
                placeholder = "Disabled",
                label = "Disabled",
                size = FinsibleSize.Small,
                enabled = false
            )
        }
    }
}



