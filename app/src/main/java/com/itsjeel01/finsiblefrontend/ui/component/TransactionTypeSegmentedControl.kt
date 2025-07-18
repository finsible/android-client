package com.itsjeel01.finsiblefrontend.ui.component

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
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.Utils
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel

@Composable
fun TransactionTypeSegmentedControl(
    modifier: Modifier = Modifier,
    screenWidth: Int,
) {
    val viewModel: TransactionFormViewModel = hiltViewModel()
    val types = TransactionType.entries.toTypedArray()
    val selectedType = viewModel.transactionType.collectAsState().value

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy((0.025 * screenWidth).dp)
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
            width = 1.dp,
            color = selectedColor,
            shape = RoundedCornerShape(4.dp)
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
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = Utils.toTitleCase(type.name),
            color = textColor,
            style = textStyle,
        )
    }
}
