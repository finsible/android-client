package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleTextFieldColors
import com.itsjeel01.finsiblefrontend.ui.theme.getCategoryColor
import com.itsjeel01.finsiblefrontend.ui.theme.getCategoryColorsList
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCategoryDropdown(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val newTransactionFormViewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionCategory =
        newTransactionFormViewModel.transactionCategoryState.collectAsState().value
    val categories = newTransactionFormViewModel.currentCategoriesState.collectAsState().value
    var openAlertDialog by remember { mutableStateOf(false) }

    when {
        openAlertDialog -> {
            NewCategoryAlertDialog(
                dialogTitle = "Add New Category",
                availableColors = getCategoryColorsList(),
                onDismissRequest = {
                    openAlertDialog = false
                },
                onConfirmation = {
                    openAlertDialog = false
                }
            )
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier,
    ) {
        TextField(
            readOnly = true,
            value = transactionCategory.name,
            onValueChange = { },
            label = { Text("Category") },
            maxLines = 1,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = finsibleTextFieldColors(
                accentColor = getCategoryColor(transactionCategory.color)
            ),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.background(
                color = getCustomColor(key = CustomColorKey.SecondaryBackground)
            ),
            shape = RoundedCornerShape(corner = CornerSize(4.dp)),
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        newTransactionFormViewModel.setTransactionCategory(categories.first { it.id == category.id })
                        expanded = false
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = getCategoryColor(category.color),
                                        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
                                    )
                                    .size(8.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = category.name)
                        }
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            DropdownMenuItem(
                onClick = {
                    openAlertDialog = true
                    expanded = false
                },
                text = {
                    Text(text = "Add New Category")
                }
            )
        }
    }
}