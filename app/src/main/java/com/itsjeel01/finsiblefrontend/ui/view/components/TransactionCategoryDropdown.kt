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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.data.objectbox.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.ui.theme.getCategoryColor
import com.itsjeel01.finsiblefrontend.ui.theme.getCategoryColorsList
import com.itsjeel01.finsiblefrontend.ui.theme.getTransactionColor
import com.itsjeel01.finsiblefrontend.ui.view.InputCommonProps
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionFormViewModel

@Composable
fun TransactionCategoryDropdown(modifier: Modifier = Modifier) {
    val viewModel: NewTransactionFormViewModel = hiltViewModel()
    val transactionType = viewModel.transactionTypeState.collectAsState().value
    val transactionCategory = viewModel.transactionCategoryState.collectAsState().value
    val categories = viewModel.currentCategoriesState.collectAsState().value
    val focusManager = LocalFocusManager.current
    var showNewCategoryDialog by remember { mutableStateOf(false) }

    val commonProps = InputCommonProps(
        modifier = modifier,
        placeholder = "Category",
        accentColor = getTransactionColor(transactionType),
        leadingIcon = {
            CategoryColorDot(color = getCategoryColor(transactionCategory.color))
        }
    )

    if (showNewCategoryDialog) {
        NewCategoryAlertDialog(
            dialogTitle = "Add New Category",
            availableColors = getCategoryColorsList(),
            onDismissRequest = { showNewCategoryDialog = false },
            onConfirmation = { showNewCategoryDialog = false }
        )
    }

    FinsibleDropdownInput(
        value = transactionCategory,
        options = categories,
        onValueChange = { viewModel.setTransactionCategory(it) },
        commonProps = commonProps,
        clearFocus = { focusManager.clearFocus(force = true) },
        displayText = { it.name },
        itemContent = { category ->
            CategoryDropdownItem(category = category, commonProps = commonProps)
        },
        footerContent = { closeDropdown ->
            CategoryDropdownFooter(
                onAddClick = {
                    focusManager.clearFocus()
                    showNewCategoryDialog = true
                    closeDropdown()
                },
                commonProps = commonProps
            )
        }
    )
}

@Composable
private fun CategoryColorDot(color: Color) {
    Box(
        modifier = Modifier
            .background(
                color = color,
                shape = RoundedCornerShape(corner = CornerSize(100)),
            )
            .size(16.dp)
    )
}

@Composable
private fun CategoryDropdownItem(
    category: CategoryEntity,
    commonProps: InputCommonProps,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CategoryColorDot(color = getCategoryColor(category.color))
        Spacer(modifier = Modifier.width(24.dp))
        Text(text = category.name, style = commonProps.primaryTextStyle())
    }
}

@Composable
private fun CategoryDropdownFooter(
    onAddClick: () -> Unit,
    commonProps: InputCommonProps,
) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )

    DropdownMenuItem(
        onClick = onAddClick,
        text = { Text(text = "Add New Category", style = commonProps.primaryTextStyle()) }
    )
}