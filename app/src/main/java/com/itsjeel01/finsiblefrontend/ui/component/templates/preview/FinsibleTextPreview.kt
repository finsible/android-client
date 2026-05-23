
package com.itsjeel01.finsiblefrontend.ui.component.templates.preview
import com.itsjeel01.finsiblefrontend.ui.theme.normal


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.theme.bold

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 1000)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 1000, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleTextPreview() {
    FinsibleComponentPreviewScaffold {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd)
        ) {
            FinsibleText(
                text = "XL Heading Bold",
                textStyle = FinsibleTheme.typography.displayXl.bold(),
                colorVariant = FinsibleTextColorVariant.Accent
            )
            FinsibleText(text = "Large Heading Bold", textStyle = FinsibleTheme.typography.displayLg.bold())
            FinsibleText(text = "Medium Heading Bold", textStyle = FinsibleTheme.typography.displayMd.bold())
            FinsibleText(text = "Small Heading Bold", textStyle = FinsibleTheme.typography.displaySm.bold())
            FinsibleText(text = "XL Title", textStyle = FinsibleTheme.typography.headingLg.normal())
            FinsibleText(text = "Large Title", textStyle = FinsibleTheme.typography.headingMd.normal())
            FinsibleText(text = "Medium Title", textStyle = FinsibleTheme.typography.headingSm.normal())
            FinsibleText(text = "Small Title", textStyle = FinsibleTheme.typography.bodyLg.normal())
            FinsibleText(text = "XSmall Title", textStyle = FinsibleTheme.typography.bodyLg)
            FinsibleText(text = "Body Regular", textStyle = FinsibleTheme.typography.bodyLg)
            FinsibleText(text = "Body Medium", textStyle = FinsibleTheme.typography.bodyLg.medium())
            FinsibleText(text = "Body SemiBold", textStyle = FinsibleTheme.typography.bodyLg.semiBold())
            FinsibleText(text = "Body Bold", textStyle = FinsibleTheme.typography.bodyLg.bold())
            FinsibleText(text = "Small Body Regular", textStyle = FinsibleTheme.typography.bodyMd)
            FinsibleText(text = "Small Body Medium", textStyle = FinsibleTheme.typography.bodyMd.medium())
            FinsibleText(text = "Small Body SemiBold", textStyle = FinsibleTheme.typography.bodyMd.semiBold())
            FinsibleText(text = "Small Body Bold", textStyle = FinsibleTheme.typography.bodyMd.bold())
            FinsibleText(text = "XL Label", textStyle = FinsibleTheme.typography.bodyLg.semiBold())
            FinsibleText(text = "Large Label", textStyle = FinsibleTheme.typography.bodyMd.semiBold())
            FinsibleText(text = "Small Label", textStyle = FinsibleTheme.typography.bodySm.semiBold(), uppercase = true)
            FinsibleText(text = "Micro Label", textStyle = FinsibleTheme.typography.labelSm.medium())
        }
    }
}


