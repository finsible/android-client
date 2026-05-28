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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleToggle
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleLabelPosition
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 600)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 600, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleTogglePreview() {
    FinsibleComponentPreviewScaffold {
        var primary by remember { mutableStateOf(true) }
        var secondary by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackLg)
        ) {
            FinsibleToggle(
                checked = primary,
                onCheckedChange = { primary = it },
                label = "Notifications",
                size = FinsibleSize.Medium,
                labelPosition = FinsibleToggleLabelPosition.Leading,
                arrangement = FinsibleToggleArrangement.SpaceBetween
            )

            FinsibleToggle(
                checked = secondary,
                onCheckedChange = { secondary = it },
                label = "Analytics",
                size = FinsibleSize.Small,
                enabled = false,
                labelIconPosition = FinsibleIconPosition.Trailing,
                labelIcon = {
                    androidx.compose.material3.Icon(
                        painter = painterResource(LucideR.drawable.lucide_ic_info),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

