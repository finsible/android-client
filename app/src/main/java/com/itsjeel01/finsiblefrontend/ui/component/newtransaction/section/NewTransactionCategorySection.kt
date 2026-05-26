package com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

@Composable
fun NewTransactionCategorySection(
    categories: List<CategoryUIModel>,
    allCategories: List<CategoryUIModel>,
    selectedCategoryId: Long?,
    hasError: Boolean,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOpenAllCategories: () -> Unit,
    accentColor: Color,
    onSelectCategory: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory = remember(allCategories, selectedCategoryId) {
        allCategories.find { it.id == selectedCategoryId }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CategorySectionDefaults.headerSpacing)
    ) {
        CategorySectionLabelRow(
            onOpenAll = onOpenAllCategories,
            accentColor = accentColor
        )

        Column(verticalArrangement = Arrangement.spacedBy(CategorySectionDefaults.contentSpacing)) {
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

object CategorySectionDefaults {
    val headerSpacing: androidx.compose.ui.unit.Dp
        @Composable get() = FinsibleTheme.spacing.insetSm

    val contentSpacing: androidx.compose.ui.unit.Dp
        @Composable get() = FinsibleTheme.spacing.stackSm
}

@Composable
internal fun CategorySectionLabelRow(
    onOpenAll: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    CategorySectionLabelInternal(
        label = "Category",
        hint = "Most Used",
        leadingIcon = {
            Icon(
                modifier = Modifier.size(FinsibleTheme.sizes.icon.xs),
                painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_category_outline),
                contentDescription = null,
                tint = FinsibleTheme.colors.contentSecondary
            )
        },
        trailingContent = {
            FinsibleButton(
                onClick = onOpenAll,
                text = "All",
                size = FinsibleSize.ExtraSmall,
                shapeVariant = FinsibleShape.Pill,
                variant = FinsibleButtonVariant.Text,
                enforceMinTouchTargetSize = false,
                iconPosition = FinsibleIconPosition.Trailing,
                colors = FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Text,
                    contentColor = accentColor,
                ),
                icon = {
                    Icon(
                        modifier = Modifier.size(FinsibleTheme.sizes.icon.sm),
                        painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_chevron_right),
                        contentDescription = null,
                        tint = accentColor
                    )
                }
            )
        },
        modifier = modifier
    )
}

@Composable
internal fun CategorySectionLabelInternal(
    label: String,
    hint: String? = null,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Row(
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapXs),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            }
            FinsibleText(
                text = label,
                textStyle = FinsibleTheme.typography.labelMd.semiBold(),
                uppercase = true,
                colorVariant = com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant.Secondary
            )
            if (hint != null) {
                FinsibleText(
                    text = hint,
                    textStyle = FinsibleTheme.typography.labelSm.medium(),
                    colorVariant = com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant.Tertiary
                )
            }
        }

        trailingContent?.invoke()
    }
}

@Composable
@SuppressLint("UnusedContentLambdaTargetStateParameter")
internal fun CategoryExpandableRow(
    shortlistItems: List<CategoryUIModel>,
    selectedItem: CategoryUIModel?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (CategoryUIModel) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier,
    collapsedCount: Int = 5
) {
    val displayItems = remember(shortlistItems, selectedItem) {
        if (selectedItem == null) {
            shortlistItems
        } else {
            listOf(selectedItem) + shortlistItems.filter { it.id != selectedItem.id }
        }
    }

    val visibleItems = remember(displayItems, isExpanded, collapsedCount) {
        if (isExpanded) displayItems else displayItems.take(collapsedCount)
    }

    val chips = buildList<@Composable () -> Unit> {
        visibleItems.forEach { item ->
            add {
                CategoryChip(
                    model = item,
                    selected = selectedItem?.id == item.id,
                    onSelectedChange = {
                        if (isExpanded) {
                            onExpandedChange(false)
                        }
                        onItemSelected(item)
                    },
                    size = FinsibleSize.Small,
                    selectedTint = accentColor
                )
            }
        }

        if (displayItems.size > collapsedCount) {
            add {
                CategoryExpandToggleButton(
                    expanded = isExpanded,
                    onClick = { onExpandedChange(!isExpanded) }
                )
            }
        }
    }

    val transition = FinsibleTheme.animations.specs.springFadeTransition()

    AnimatedContent(
        targetState = isExpanded,
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        transitionSpec = { transition },
        label = "CategoryExpandableRowAnimation"
    ) { _ ->
        FinsibleChipsRow(
            wrap = true,
            chips = chips,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
internal fun CategoryChip(
    model: CategoryUIModel,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    size: FinsibleSize,
    selectedTint: Color,
    modifier: Modifier = Modifier,
) {
    FinsibleFilterChip(
        label = model.name,
        selected = selected,
        onSelectedChange = onSelectedChange,
        size = size,
        shapeVariant = FinsibleShape.Rounded,
        selectedTint = selectedTint,
        modifier = modifier,
        enforceMinTouchTarget = false,
        icon = {
            val iconRes = remember(model.icon) {
                resolveIcon(
                    token = model.icon.ifBlank { null },
                    fallbackIcon = com.composables.icons.tabler.outline.R.drawable.tabler_ic_layout_grid_outline
                )
            }
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null
            )
        }
    )
}

@Composable
internal fun CategoryExpandToggleButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FinsibleButton(
        onClick = onClick,
        text = if (expanded) "Less" else "More",
        size = FinsibleSize.ExtraSmall,
        shapeVariant = FinsibleShape.Pill,
        variant = FinsibleButtonVariant.Text,
        colors = FinsibleButtonDefaults.colors(
            variant = FinsibleButtonVariant.Text,
            contentColor = FinsibleTheme.colors.contentSecondary,
        ),
        enforceMinTouchTargetSize = false,
        modifier = modifier
    )
}


