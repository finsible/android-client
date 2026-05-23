package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

private data class CategorySelectionGroup(
    val parent: CategoryUIModel,
    val children: List<CategoryUIModel>
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CategorySelectionSheetContent(
    categories: Map<CategoryUIModel, List<CategoryUIModel>>,
    selectedCategoryId: Long?,
    accentColor: Color,
    surfaceColor: Color,
    onCategorySelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val sp = FinsibleTheme.spacing

    val filteredGroups = remember(categories, query) {
        val normalized = query.trim()

        categories.entries.mapNotNull { (parent, children) ->
            val sortedChildren = children.sortedSelectionOrder()

            if (normalized.isEmpty()) {
                CategorySelectionGroup(parent = parent, children = sortedChildren)
            } else {
                val parentMatches = parent.name.contains(normalized, ignoreCase = true)
                val matchingChildren = sortedChildren.filter { it.name.contains(normalized, ignoreCase = true) }

                when {
                    parentMatches -> CategorySelectionGroup(parent = parent, children = matchingChildren)
                    matchingChildren.isNotEmpty() -> CategorySelectionGroup(parent = parent, children = matchingChildren)
                    else -> null
                }
            }
        }.sortedWith(
            compareByDescending<CategorySelectionGroup> { it.parent.usageCount }
                .thenByDescending { it.parent.lastUsedAt ?: 0L }
                .thenBy { it.parent.name.lowercase() }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = sp.insetLg)
    ) {
        FinsibleTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search category",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
                    contentDescription = stringResource(R.string.search_transactions_placeholder)
                )
            },
            modifier = Modifier.padding(horizontal = sp.insetLg)
        )

        Spacer(modifier = Modifier.height(sp.insetLg))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(sp.insetXs)
        ) {
            filteredGroups.forEach { group ->
                stickyHeader {
                    // Parent row — clickable/selectable, icon NOT accented
                    CategorySelectionParentRow(
                        category = group.parent,
                        isSelected = group.parent.id == selectedCategoryId,
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        onSelect = { onCategorySelected(group.parent.id) }
                    )
                }

                if (group.children.isNotEmpty()) {
                    // Children rendered as a wrapping row of filter chips
                    item(key = "children_${group.parent.id}") {
                        CategoryChildrenChipRow(
                            children = group.children,
                            selectedCategoryId = selectedCategoryId,
                            accentColor = accentColor,
                            onCategorySelected = onCategorySelected,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = sp.insetLg, vertical = sp.insetSm)
                        )
                    }
                }
            }
        }
    }
}

/** Selectable parent category row — sticky header with group icon and name. */
@Composable
private fun CategorySelectionParentRow(
    category: CategoryUIModel,
    isSelected: Boolean,
    accentColor: Color,
    surfaceColor: Color,
    onSelect: () -> Unit
) {
    val sp = FinsibleTheme.spacing
    val backgroundColor = if (isSelected) surfaceColor else FinsibleTheme.colors.surfaceSunken
    val borderColor = if (isSelected) accentColor else Color.Transparent
    val iconRes = remember(category.icon) {
        resolveIcon(token = category.icon.ifBlank { null }, fallbackIcon = com.composables.icons.tabler.outline.R.drawable.tabler_ic_layout_grid_outline)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.insetLg)
            .clip(RoundedCornerShape(sp.gapMd))
            .background(backgroundColor)
            .border(
                width = if (isSelected) FinsibleTheme.stroke.thin else FinsibleTheme.stroke.hairline,
                color = borderColor,
                shape = RoundedCornerShape(sp.gapMd)
            )
            .clickable(onClick = onSelect)
            .padding(horizontal = sp.insetLg, vertical = sp.inlineMd),
        horizontalArrangement = Arrangement.spacedBy(sp.gapMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = FinsibleTheme.colors.contentSecondary,
            modifier = Modifier.size(FinsibleTheme.sizes.icon.md)
        )

        FinsibleText(
            text = category.name.uppercase(),
            textStyle = FinsibleTheme.typography.labelSm.semiBold(),
            colorVariant = if (isSelected) FinsibleTextColorVariant.Primary else FinsibleTextColorVariant.Secondary,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(
                painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_check_outline),
                contentDescription = "Selected",
                tint = accentColor,
                modifier = Modifier.size(FinsibleTheme.sizes.icon.lg)
            )
        }
    }
}

/** Wrapping row of category chips representing children of a parent group. */
@Composable
private fun CategoryChildrenChipRow(
    children: List<CategoryUIModel>,
    selectedCategoryId: Long?,
    accentColor: Color,
    onCategorySelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val chips = children.map { category ->
        @Composable {
            FinsibleFilterChip(
                label = category.name,
                selected = category.id == selectedCategoryId,
                onSelectedChange = { onCategorySelected(category.id) },
                size = FinsibleSize.Small,
                shapeVariant = FinsibleShape.Rounded,
                selectedTint = accentColor,
                icon = {
                    val iconRes = remember(category.icon) {
                        resolveIcon(
                            token = category.icon.ifBlank { null },
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
    }

    FinsibleChipsRow(
        wrap = true,
        chips = chips,
        modifier = modifier
    )
}

private fun List<CategoryUIModel>.sortedSelectionOrder(): List<CategoryUIModel> {
    return sortedWith(
        compareByDescending<CategoryUIModel> { it.usageCount }
            .thenByDescending { it.lastUsedAt ?: 0L }
            .thenBy { it.name.lowercase() }
    )
}
