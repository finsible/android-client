package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun TransactionTypeFilter(
    selectedTypes: Set<TransactionType>,
    onTypeToggle: (TransactionType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)) {
        FinsibleText(
            text = stringResource(R.string.transaction_type),
            variant = FinsibleTextVariant.MicroLabelSemiBold,
            colorVariant = FinsibleTextColorVariant.Secondary,
            uppercase = true
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
        ) {
            TransactionType.entries.forEach { type ->
                val isSelected = type in selectedTypes
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.secondaryContent,
                    animationSpec = tween(160),
                    label = "tx_type_content"
                )

                FinsibleFilterChip(
                    selected = isSelected,
                    onSelectedChange = { onTypeToggle(type) },
                    label = stringResource(type.displayText),
                    modifier = Modifier.weight(1f),
                    size = FinsibleSize.Medium,
                    shapeVariant = FinsibleShape.Rounded,
                    variant = FinsibleFilterChipVariant.OutlinedTonal,
                    selectedTint = FinsibleTheme.colors.primaryContent80,
                    icon = {
                        Icon(
                            painter = painterResource(type.icon),
                            contentDescription = type.name,
                            tint = contentColor
                        )
                    },
                    enforceMinTouchTarget = false
                )
            }
        }
    }
}