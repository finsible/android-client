package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium

@Composable
fun NewTransactionCategorySection(
    categories: List<CategoryUIModel>,
    allCategories: List<CategoryUIModel>,
    selectedCategoryId: Long?,
    hasError: Boolean,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOpenAllCategories: () -> Unit,
    accentColor: androidx.compose.ui.graphics.Color,
    onSelectCategory: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory = remember(allCategories, selectedCategoryId) {
        allCategories.find { it.id == selectedCategoryId }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.headerSpacing)
    ) {
        CategorySectionLabelRow(
            onOpenAll = onOpenAllCategories,
            accentColor = accentColor
        )

        Column(verticalArrangement = Arrangement.spacedBy(NewTransactionSectionDefaults.contentSpacing)) {
            CategoryExpandableRow(
                shortlistItems = categories,
                selectedItem = selectedCategory,
                isExpanded = isExpanded,
                onExpandedChange = { onExpandedChange(it) },
                onItemSelected = { onSelectCategory(it.id) },
                accentColor = accentColor
            )

            if (hasError) {
                FinsibleText(
                    text = "Category is required",
                    textStyle = FinsibleTheme.typography.bodyMd.medium(),
                    color = FinsibleTheme.colors.feedbackError
                )
            }
        }
    }
}
