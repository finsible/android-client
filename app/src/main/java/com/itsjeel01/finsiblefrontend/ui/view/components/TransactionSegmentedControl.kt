package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.data.TransactionType
import com.itsjeel01.finsiblefrontend.ui.theme.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel
import java.util.Locale

@Composable
fun TransactionSegmentedControl(
    modifier: Modifier = Modifier,
    screenWidth: Int,
) {
    val viewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionTypes = TransactionType.entries.toTypedArray()
    val selectedType = viewModel.transactionTypeState.collectAsState().value

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy((0.025 * screenWidth).dp)
    ) {
        transactionTypes.forEach { type ->
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
    val selectedColor = getTransactionColor(type)

    // Format the type name for display
    val label = type.name
        .replace("_", " ")
        .lowercase()
        .replaceFirstChar { it.titlecase(Locale.ROOT) }

    // Visual properties based on selection state
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
            width = 1.dp,
            color = selectedColor,
            shape = RoundedCornerShape(4.dp)
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(borderModifier)
            .clickable(onClick = onSelect)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            style = textStyle,
        )
    }
}