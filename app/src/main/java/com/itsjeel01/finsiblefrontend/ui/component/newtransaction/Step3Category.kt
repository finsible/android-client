package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDimes.Companion.inverted
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

/** Stateless category selection step with hoisted state. */
@Composable
fun Step3Category(
    transactionType: TransactionType,
    categories: Map<CategoryUIModel, List<CategoryUIModel>>,
    selectedCategoryId: Long?,
    onTransactionTypeChange: (TransactionType) -> Unit,
    onCategorySelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = remember { TransactionType.toOrderedList() }

    val categoryList = remember(categories) { categories.toList() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        // Transaction type selector
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(),
            space = FinsibleTheme.dimes.d8.inverted()
        ) {
            options.forEach { type ->
                val isSelected = type == transactionType
                SegmentedButton(
                    shape = RoundedCornerShape(FinsibleTheme.dimes.d12),
                    onClick = { if (!isSelected) onTransactionTypeChange(type) },
                    colors = SegmentedButtonDefaults.colors().copy(
                        activeContentColor = FinsibleTheme.colors.primaryContent,
                        activeContainerColor = FinsibleTheme.colors.surface,
                        inactiveContentColor = FinsibleTheme.colors.secondaryContent,
                        inactiveBorderColor = FinsibleTheme.colors.transparent,
                        inactiveContainerColor = FinsibleTheme.colors.input
                    ),
                    selected = isSelected,
                    label = {
                        FinsibleText(
                            text = stringResource(type.displayText),
                            variant = FinsibleTextVariant.BodyMedium,
                            color = LocalContentColor.current
                        )
                    },
                    icon = {
                        if (isSelected) Icon(
                            painter = painterResource(id = type.icon),
                            contentDescription = stringResource(R.string.cd_type_icon, stringResource(type.displayText)),
                            tint = type.getColor()
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16),
            contentPadding = PaddingValues(bottom = FinsibleTheme.dimes.d16)
        ) {
            items(
                items = categoryList,
                key = { (parent, _) -> parent.id } // Optimization: Stable ID for smart updates
            ) { (parentCat, subCats) ->
                CategoryGroup(
                    parentCategory = parentCat,
                    subCategories = subCats,
                    selectedCategoryId = selectedCategoryId,
                    transactionType = transactionType,
                    onCategorySelected = onCategorySelected
                )
            }
        }
    }
}


/** Category group with parent title and subcategory chips. */
@Composable
private fun CategoryGroup(
    parentCategory: CategoryUIModel,
    subCategories: List<CategoryUIModel>,
    selectedCategoryId: Long?,
    transactionType: TransactionType,
    onCategorySelected: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FinsibleTheme.colors.surface, RoundedCornerShape(FinsibleTheme.dimes.d12))
            .padding(FinsibleTheme.dimes.d16)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        FinsibleText(
            text = parentCategory.name,
            variant = FinsibleTextVariant.SmallTitleExtraBold,
            color = FinsibleTheme.colors.primaryContent,
            textStyleOverride = FinsibleTheme.typography.t18.extraBold()
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
        ) {
            subCategories.forEach { category ->
                key(category.id) {
                    CategoryChip(
                        category = category,
                        isSelected = category.id == selectedCategoryId,
                        accentColor = transactionType.getColor(),
                        onSelected = { onCategorySelected(category.id) }
                    )
                }
            }
        }
    }
}

/** Individual category chip with animated selection state. */
@Composable
private fun CategoryChip(
    category: CategoryUIModel,
    isSelected: Boolean,
    accentColor: Color,
    onSelected: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else FinsibleTheme.colors.border,
        animationSpec = tween(durationMillis = 200),
        label = "borderColor"
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) accentColor.copy(alpha = 0.15f) else FinsibleTheme.colors.transparent,
        animationSpec = tween(durationMillis = 200),
        label = "backgroundColor"
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d8))
            .border(
                width = FinsibleTheme.dimes.d1,
                color = borderColor,
                shape = RoundedCornerShape(FinsibleTheme.dimes.d8)
            )
            .background(backgroundColor)
            .clickable(onClick = onSelected)
            .padding(
                horizontal = FinsibleTheme.dimes.d12,
                vertical = FinsibleTheme.dimes.d10
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d6)
        ) {
            val iconDrawable = resolveIcon(category.icon, fallbackIcon = R.drawable.ic_close)
            Icon(
                modifier = Modifier.size(FinsibleTheme.dimes.d20),
                painter = painterResource(id = iconDrawable),
                contentDescription = null,
                tint = if (isSelected) accentColor else FinsibleTheme.colors.primaryContent60
            )
            FinsibleText(
                text = category.name,
                variant = FinsibleTextVariant.BodyMedium,
                color = if (isSelected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.primaryContent80
            )
        }
    }
}