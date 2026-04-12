package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun DebugTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    subtitleColor: Color = FinsibleTheme.colors.secondaryContent,
    showBack: Boolean = true,
    backLabel: String = "Back",
    onBack: (() -> Unit)? = null,
    showDivider: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = FinsibleTheme.dimes.d12, vertical = FinsibleTheme.dimes.d8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        if (showBack) {
            val resolvedOnBack = requireNotNull(onBack) { "onBack is required when showBack is true." }
            FinsibleButton(
                onClick = resolvedOnBack,
                iconOnly = true,
                variant = FinsibleButtonVariant.Text,
                size = FinsibleSize.Small,
                shapeVariant = FinsibleShape.Circle,
                icon = {
                    Icon(
                        painter = painterResource(LucideR.drawable.lucide_ic_arrow_left),
                        contentDescription = backLabel
                    )
                }
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d2)) {
            FinsibleText(
                text = title,
                variant = FinsibleTextVariant.SmallTitleExtraBold,
                color = FinsibleTheme.colors.primaryContent
            )
            if (!subtitle.isNullOrBlank()) {
                FinsibleText(
                    text = subtitle,
                    variant = FinsibleTextVariant.SmallLabelMedium,
                    color = subtitleColor
                )
            }
        }
    }

    if (showDivider) {
        HorizontalDivider(color = FinsibleTheme.colors.divider)
    }
}

