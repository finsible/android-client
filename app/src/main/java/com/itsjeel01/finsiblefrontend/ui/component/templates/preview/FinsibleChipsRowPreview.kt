package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 600)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 600, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleChipsRowPreview() {
    val labels = listOf("All", "Food", "Travel", "Subscriptions", "Others")
    val chips = labels.map { label ->
        @Composable {
            FinsibleFilterChip(
                selected = label == "All",
                onSelectedChange = {},
                label = label,
                size = FinsibleSize.Small,
                enforceMinTouchTarget = false
            )
        }
    }

    FinsibleComponentPreviewScaffold {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackLg)
        ) {
            FinsibleChipsRow(chips = chips, chipKeys = labels, wrap = true)
            FinsibleChipsRow(chips = chips, chipKeys = labels, wrap = false)
        }
    }
}

