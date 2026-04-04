package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleRadioButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 600)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 600, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleRadioButtonPreview() {
    FinsibleComponentPreviewScaffold {
        val d = FinsibleTheme.dimes

        FinsiblePreviewSection("Sizes and selection") {
            Column(verticalArrangement = Arrangement.spacedBy(d.d12)) {
                RadioRow(size = FinsibleSize.Small, label = "Small")
                RadioRow(size = FinsibleSize.Medium, label = "Medium")
            }
        }
    }
}

@Composable
private fun RadioRow(size: FinsibleSize, label: String) {
    var selected by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)) {
        FinsibleRadioButton(
            selected = selected,
            onSelectedChange = { selected = it },
            label = label,
            size = size
        )
        FinsibleRadioButton(
            selected = !selected,
            onSelectedChange = { selected = !selected },
            label = "$label with icon",
            size = size,
            icon = {
                Icon(
                    painter = painterResource(android.R.drawable.star_on),
                    contentDescription = null
                )
            }
        )
        FinsibleRadioButton(
            selected = false,
            onSelectedChange = {},
            label = "$label disabled",
            size = size,
            enabled = false
        )
    }
}

