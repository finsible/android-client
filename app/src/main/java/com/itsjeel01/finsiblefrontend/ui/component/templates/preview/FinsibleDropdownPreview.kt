package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDropdown
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownOption
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 700)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 700, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleDropdownPreview() {
    val options = listOf(
        FinsibleDropdownOption(id = "food", label = "Food"),
        FinsibleDropdownOption(id = "travel", label = "Travel"),
        FinsibleDropdownOption(id = "bills", label = "Bills")
    )

    FinsibleComponentPreviewScaffold {
        var selected by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
        ) {
            FinsibleDropdown(
                options = options,
                selectedId = selected,
                onSelected = { selected = it },
                placeholder = "Select category",
                size = FinsibleSize.Medium
            )

            FinsibleDropdown(
                options = options,
                selectedId = options.first().id,
                onSelected = {},
                placeholder = "Disabled",
                size = FinsibleSize.Small,
                enabled = false
            )
        }
    }
}

