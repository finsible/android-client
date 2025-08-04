package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.Utils
import com.itsjeel01.finsiblefrontend.ui.theme.dime.BorderStroke
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Radius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.borderWidth
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingVertical
import com.itsjeel01.finsiblefrontend.ui.theme.dime.roundedCornerShape
import com.itsjeel01.finsiblefrontend.ui.theme.dime.size
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel

@Composable
fun TransactionTypeSegmentedControl(
    modifier: Modifier = Modifier,
) {
    val viewModel: TransactionFormViewModel = hiltViewModel()
    val types = TransactionType.entries.toTypedArray()
    val selectedType = viewModel.transactionType.collectAsState().value

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(appDimensions().size(Size.S12))
    ) {
        types.forEach { type ->
            SegmentItem(
                type = type,
                isSelected = selectedType == type,
                onSelect = { viewModel.setTransactionType(type) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SegmentItem(
    type: TransactionType,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dims = appDimensions()

    // --- Styles Computation ---

    val selectedColor = Utils.getTransactionColor(type)

    val backgroundColor = if (isSelected) {
        selectedColor.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor = if (isSelected) {
        selectedColor
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    val textStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    )

    val borderModifier = if (isSelected) {
        Modifier.border(
            width = dims.borderWidth(BorderStroke.MEDIUM),
            color = selectedColor,
            shape = dims.roundedCornerShape(Radius.SM)
        )
    } else {
        Modifier
    }

    // --- Segment Item UI ---

    Box(
        modifier = modifier
            .then(borderModifier)
            .clickable(onClick = onSelect)
            .background(
                color = backgroundColor,
                shape = dims.roundedCornerShape(Radius.SM)
            )
            .paddingVertical(Size.S8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = Utils.toTitleCase(type.name),
            color = textColor,
            style = textStyle,
        )
    }
}
