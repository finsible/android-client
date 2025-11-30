package com.itsjeel01.finsiblefrontend.ui.screen.newtransaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.data.local.entity.CategoryEntity
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDimes.Companion.inverted
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel

/** Stateless category selection step with hoisted state. */
@Composable
fun Step3Category(
    transactionType: TransactionType,
    categories: Map<CategoryEntity, List<CategoryEntity>>,
    selectedCategoryId: Long?,
    onTransactionTypeChange: (TransactionType) -> Unit,
    onCategorySelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = TransactionType.toOrderedList()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        // Transaction type selector.
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
                        Text(type.displayText, style = FinsibleTheme.typography.t16.medium())
                    },
                    icon = {
                        if (isSelected) Icon(
                            painter = painterResource(id = type.icon),
                            contentDescription = type.displayText + " icon",
                            tint = type.getColor()
                        )
                    }
                )
            }
        }

        // Category groups.
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
        ) {
            categories.forEach { (parentCat, subCats) ->
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

/** ViewModel wrapper maintaining previous signature. */
@Composable
fun Step3Category(modifier: Modifier = Modifier, viewModel: NewTransactionViewModel) {
    val transactionType = viewModel.transactionType.collectAsStateWithLifecycle().value
    val categories = viewModel.categories.collectAsStateWithLifecycle().value
    val transactionCategory = viewModel.transactionCategoryId.collectAsStateWithLifecycle().value
    Step3Category(
        transactionType = transactionType,
        categories = categories,
        selectedCategoryId = transactionCategory,
        onTransactionTypeChange = { viewModel.setTransactionType(it) },
        onCategorySelected = { viewModel.setTransactionCategoryId(it) },
        modifier = modifier
    )
}

/** Category group with parent title and subcategory chips. */
@Composable
private fun CategoryGroup(
    parentCategory: CategoryEntity,
    subCategories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    transactionType: TransactionType,
    onCategorySelected: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FinsibleTheme.colors.surface, RoundedCornerShape(FinsibleTheme.dimes.d12))
            .padding(FinsibleTheme.dimes.d16),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        Text(
            text = parentCategory.name,
            style = FinsibleTheme.typography.t18.extraBold(),
            color = FinsibleTheme.colors.primaryContent
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
        ) {
            subCategories.forEach { category ->
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

/** Individual category chip with animated selection state. */
@Composable
private fun CategoryChip(
    category: CategoryEntity,
    isSelected: Boolean,
    accentColor: androidx.compose.ui.graphics.Color,
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
            Text(
                text = category.name,
                style = FinsibleTheme.typography.t16.medium(),
                color = if (isSelected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.primaryContent80
            )
        }

    }
}