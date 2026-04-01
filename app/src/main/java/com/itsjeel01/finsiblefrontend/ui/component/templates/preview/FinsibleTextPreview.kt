package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 1000)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 1000, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleTextPreview() {
    FinsibleComponentPreviewScaffold {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
        ) {
            FinsibleText(
                text = "XL Heading Bold",
                variant = FinsibleTextVariant.XLargeHeadingBold,
                colorVariant = FinsibleTextColorVariant.Accent
            )
            FinsibleText(text = "Large Heading Bold", variant = FinsibleTextVariant.LargeHeadingBold)
            FinsibleText(text = "Medium Heading Bold", variant = FinsibleTextVariant.MediumHeadingBold)
            FinsibleText(text = "Small Heading Bold", variant = FinsibleTextVariant.SmallHeadingBold)
            FinsibleText(text = "XL Title", variant = FinsibleTextVariant.XLargeTitleNormal)
            FinsibleText(text = "Large Title", variant = FinsibleTextVariant.LargeTitleNormal)
            FinsibleText(text = "Medium Title", variant = FinsibleTextVariant.MediumTitleNormal)
            FinsibleText(text = "Small Title", variant = FinsibleTextVariant.SmallTitleNormal)
            FinsibleText(text = "XSmall Title", variant = FinsibleTextVariant.XSmallTitleNormal)
            FinsibleText(text = "Body Regular", variant = FinsibleTextVariant.BodyRegular)
            FinsibleText(text = "Body Medium", variant = FinsibleTextVariant.BodyMedium)
            FinsibleText(text = "Body SemiBold", variant = FinsibleTextVariant.BodySemiBold)
            FinsibleText(text = "Body Bold", variant = FinsibleTextVariant.BodyBold)
            FinsibleText(text = "Small Body Regular", variant = FinsibleTextVariant.SmallBodyRegular)
            FinsibleText(text = "Small Body Medium", variant = FinsibleTextVariant.SmallBodyMedium)
            FinsibleText(text = "Small Body SemiBold", variant = FinsibleTextVariant.SmallBodySemiBold)
            FinsibleText(text = "Small Body Bold", variant = FinsibleTextVariant.SmallBodyBold)
            FinsibleText(text = "XL Label", variant = FinsibleTextVariant.XLargeLabelSemiBold)
            FinsibleText(text = "Large Label", variant = FinsibleTextVariant.LargeLabelSemiBold)
            FinsibleText(text = "Small Label", variant = FinsibleTextVariant.SmallLabelSemiBold, uppercase = true)
            FinsibleText(text = "Micro Label", variant = FinsibleTextVariant.MicroLabelMedium)
        }
    }
}


