package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.composables.icons.lucide.R as LucideR
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleSegmentedButtonRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 500)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 500, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleSegmentedButtonPreview() {
    val options = listOf(
        FinsibleSegmentedButtonOption(
            id = "expenses",
            label = "Expenses",
            icon = { iconModifier ->
                Icon(
                    painter = painterResource(LucideR.drawable.lucide_ic_circle),
                    contentDescription = null,
                    modifier = iconModifier
                )
            }
        ),
        FinsibleSegmentedButtonOption(
            id = "income",
            label = "Income",
            icon = { iconModifier ->
                Icon(
                    painter = painterResource(LucideR.drawable.lucide_ic_square),
                    contentDescription = null,
                    modifier = iconModifier
                )
            }
        ),
        FinsibleSegmentedButtonOption(id = "transfers", label = "Transfers")
    )

    FinsibleComponentPreviewScaffold {
        var selected by remember { mutableStateOf<String?>("expenses") }
        var multiSelected by remember { mutableStateOf(setOf("income")) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FinsibleTheme.dimes.d12),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
        ) {
            FinsibleSegmentedButtonRow(
                options = options,
                selectedValue = selected,
                onSelectedValueChange = { selected = it },
                size = FinsibleSize.Medium
            )

            FinsibleSegmentedButtonRow(
                options = options,
                selectedValues = multiSelected,
                onSelectedValuesChange = { multiSelected = it },
                size = FinsibleSize.Small,
                variant = FinsibleSegmentedButtonVariant.Tonal,
                selectedTint = FinsibleTheme.colors.info,
                inverted = true
            )
        }
    }
}



