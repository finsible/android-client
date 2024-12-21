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
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        transactionTypes.forEach { type ->
            val isSelected = selectedType == type
            val selectedColor = getTransactionColor(type)
            val borderModifier = Modifier.border(
                width = 1.dp,
                color = selectedColor,
                shape = RoundedCornerShape(4.dp)
            )

            Box(
                modifier = Modifier
                    .then(if (isSelected) borderModifier else Modifier)
                    .clickable {
                        newTransactionFormViewModel.setTransactionType(type)
                    }
                    .background(
                        color = if (isSelected) {
                            selectedColor.copy(alpha = 0.1f)
                        } else MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(vertical = 8.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.name.replace("_", " ").lowercase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    color = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium
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