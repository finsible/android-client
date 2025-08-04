package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.common.Utils
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.ui.component.base.BaseDropdownInput
import com.itsjeel01.finsiblefrontend.ui.component.base.CommonProps
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Radius
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.appDimensions
import com.itsjeel01.finsiblefrontend.ui.theme.dime.roundedCornerShape
import com.itsjeel01.finsiblefrontend.ui.theme.dime.size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.width
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel

// --- TransactionCategoryDropdown Composable Function ---

@Composable
fun TransactionCategoryDropdown(modifier: Modifier = Modifier) {

    // --- ViewModel and State Initialization ---

    val viewModel: TransactionFormViewModel = hiltViewModel()
    val transactionType = viewModel.transactionType.collectAsState().value
    val category = viewModel.transactionCategory.collectAsState().value
    val categories = viewModel.categories.collectAsState().value

    // --- State and Focus Management ---

    val focusManager = LocalFocusManager.current
    var showNewCategoryDialog by remember { mutableStateOf(false) }
    val commonProps = CommonProps(
        modifier = modifier,
        placeholder = Strings.CATEGORY,
        accentColor = Utils.getTransactionColor(transactionType),
        leadingIcon = {
            CategoryColorDot(color = Utils.getCategoryColor(category.color))
        }
    )

    // --- Dropdown Input for Transaction Category ---

    BaseDropdownInput(
        value = category,
        options = categories,
        onValueChange = { viewModel.setTransactionCategory(it) },
        commonProps = commonProps,
        clearFocus = { focusManager.clearFocus(force = true) },
        displayText = { it.name },
        item = { category ->
            CategoryDropdownItem(category = category, commonProps = commonProps)
        },
        footer = { closeDropdown ->
            CategoryDropdownFooter(
                onCategoryAdd = {
                    focusManager.clearFocus()
                    showNewCategoryDialog = true
                    closeDropdown()
                },
                commonProps = commonProps
            )
        }
    )

    // --- New Category Dialog Handling ---

    if (showNewCategoryDialog) {
        NewCategoryDialog(
            title = Strings.ADD_NEW_CATEGORY,
            colors = Utils.getCategoryColorsList(),
            onDismissRequest = { showNewCategoryDialog = false },
            onConfirmation = { showNewCategoryDialog = false }
        )
    }
}

// --- Helper Composable Functions ---

@Composable
private fun CategoryDropdownItem(
    category: CategoryEntity,
    commonProps: CommonProps,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CategoryColorDot(color = Utils.getCategoryColor(category.color))
        Spacer(modifier = Modifier.width(Size.S24))
        Text(text = category.name, style = commonProps.primaryTextStyle())
    }
}

@Composable
private fun CategoryColorDot(color: Color) {
    Box(
        modifier = Modifier
            .background(
                color = color,
                shape = appDimensions().roundedCornerShape(Radius.FULL),
            )
            .size(Size.S16)
    )
}

@Composable
private fun CategoryDropdownFooter(
    onCategoryAdd: () -> Unit,
    commonProps: CommonProps,
) {
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = appDimensions().size(Size.S1),
        color = MaterialTheme.colorScheme.outlineVariant
    )

    DropdownMenuItem(
        onClick = onCategoryAdd,
        text = { Text(text = Strings.ADD_NEW_CATEGORY, style = commonProps.primaryTextStyle()) }
    )
}
