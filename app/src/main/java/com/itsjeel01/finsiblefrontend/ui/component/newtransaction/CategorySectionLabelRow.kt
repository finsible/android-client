package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Section label row for the Category expandable row.
 * Displays "CATEGORY" label with a trailing "All" micro button that opens the bottom sheet.
 */
@Composable
fun CategorySectionLabelRow(
    onOpenAll: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    NewTransactionSectionLabel(
        label = "Category",
        hint = "Most Used",
        leadingIcon = {
            Icon(
                modifier = Modifier.size(FinsibleTheme.sizes.icon.xs),
                painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_category_outline),
                contentDescription = null,
                tint = FinsibleTheme.colors.contentSecondary
            )
        },
        trailingContent = {
            FinsibleButton(
                onClick = onOpenAll,
                text = "All",
                size = FinsibleSize.ExtraSmall,
                shapeVariant = FinsibleShape.Pill,
                variant = FinsibleButtonVariant.Text,
                enforceMinTouchTargetSize = false,
                iconPosition = com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition.Trailing,
                colors = FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Text,
                    contentColor = accentColor,
                ),
                icon = {
                    Icon(
                        modifier = Modifier.size(FinsibleTheme.sizes.icon.sm),
                        painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_chevron_right),
                        contentDescription = null,
                        tint = accentColor
                    )
                }
            )
        },
        modifier = modifier
    )
}
