package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransactionTypeFilter(
    selectedTypes: Set<TransactionType>,
    onTypeToggle: (TransactionType) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d10)) {
        SectionLabel(stringResource(R.string.transaction_type))
        Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
        ) {
            TransactionType.entries.forEach { type ->
                TransactionTypeFilterItem(
                    modifier = Modifier.weight(1f),
                    type = type,
                    isSelected = type in selectedTypes,
                    onClick = { onTypeToggle(type) },
                    leadingIcon = type.icon
                )
            }
        }
    }
}

@Composable
fun TransactionTypeFilterItem(
    type: TransactionType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: Int? = null,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) type.getColor().copy(alpha = 0.25f)
        else FinsibleTheme.colors.surfaceContainerLow,
        animationSpec = tween(160), label = "chip_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) FinsibleTheme.colors.outline
        else FinsibleTheme.colors.outlineVariant,
        animationSpec = tween(160), label = "chip_border"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) FinsibleTheme.colors.primaryContent
        else FinsibleTheme.colors.secondaryContent,
        animationSpec = tween(160), label = "chip_content"
    )
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d8))
            .background(bgColor)
            .border(FinsibleTheme.dimes.d0dot5, borderColor, RoundedCornerShape(FinsibleTheme.dimes.d8))
            .clickable(onClick = onClick)
            .padding(horizontal = FinsibleTheme.dimes.d12, vertical = FinsibleTheme.dimes.d9),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            Icon(painterResource(it), null, tint = contentColor, modifier = Modifier.size(FinsibleTheme.dimes.d14))
            Spacer(Modifier.width(FinsibleTheme.dimes.d6))
        }
        Text(
            text = stringResource(type.displayText),
            style = FinsibleTheme.typography.t14.medium(),
            color = contentColor,
            textAlign = if (leadingIcon == null) TextAlign.Center else TextAlign.Start
        )
    }
}