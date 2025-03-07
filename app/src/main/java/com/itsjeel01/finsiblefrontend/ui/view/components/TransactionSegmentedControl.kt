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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.data.TransactionType
import com.itsjeel01.finsiblefrontend.data.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel
import java.util.Locale

@Composable
fun TransactionSegmentedControl(modifier: Modifier = Modifier) {
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionTypes = TransactionType.entries.toTypedArray()
    val selectedType = newTransactionFormViewModel.transactionTypeState.collectAsState().value

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        transactionTypes.forEach { type ->
            val isSelected = selectedType == type
            val selectedColor = getTransactionColor(type)

            // Set border and background color based on selection
            val borderModifier = if (isSelected) Modifier.border(
                width = 1.dp,
                color = selectedColor,
                shape = RoundedCornerShape(4.dp)
            ) else Modifier
            val backgroundColor = if (isSelected) {
                selectedColor.copy(alpha = 0.1f)
            } else MaterialTheme.colorScheme.secondaryContainer;

            // Set text color and style based on selection
            val label = type.name.replace("_", " ").lowercase().replaceFirstChar { it.titlecase(Locale.ROOT) }
            val textColor = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSecondaryContainer
            val textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )

            Box(
                modifier = Modifier
                    .then(borderModifier)
                    .clickable {
                        newTransactionFormViewModel.setTransactionType(type)
                    }
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(vertical = 10.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = textColor,
                    style = textStyle,
                )
            }
        }
    }
}

@Composable
@Preview
fun TransactionSegmentedControlPreview() {
    TransactionSegmentedControl()
}