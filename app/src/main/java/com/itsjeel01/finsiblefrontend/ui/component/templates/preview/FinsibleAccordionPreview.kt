package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleAccordion
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleAccordionIconVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 900)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 900, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleAccordionPreview() {
    FinsibleComponentPreviewScaffold {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
        ) {
            FinsibleAccordion(
                title = "Account Summary",
                subtitle = "Tap to view details",
                expanded = true,
                content = { DemoAccordionContent() }
            )

            FinsibleAccordion(
                title = "Security",
                subtitle = "Two-factor authentication",
                iconVariant = FinsibleAccordionIconVariant.PlusMinus,
                content = { DemoAccordionContent() }
            )
        }
    }
}

@Composable
private fun DemoAccordionContent() {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
        com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText(text = "Item 1")
        com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText(text = "Item 2")
    }
}

