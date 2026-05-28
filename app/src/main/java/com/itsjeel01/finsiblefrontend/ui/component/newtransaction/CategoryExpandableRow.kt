package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.CategoryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

/** Category-specific expandable chip row with D-1 Context-Aware Selection.
 *
 * The selected leaf category is always pinned as the first chip (visible even in collapsed state).
 * Parent categories are never shown as selectable chips — only leaf categories appear.
 *
 * Collapsed state: selected chip + remaining shortlist items (up to [collapsedCount] total).
 * Expanded state: selected chip + all shortlist items (wrapping).
 *
 * If the [selectedItem] is not in the [shortlist] (e.g., chosen from bottom sheet),
 * it is injected at position 0; the shortlist fills remaining slots.
 */
@Composable
@SuppressLint("UnusedContentLambdaTargetStateParameter")
fun CategoryExpandableRow(
    shortlistItems: List<CategoryUIModel>,
    selectedItem: CategoryUIModel?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (CategoryUIModel) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier,
    collapsedCount: Int = 5
) {
    // Always pin selected item first. Deduplicate by ID.
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
                NewTransactionExpandToggleButton(
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
fun CategoryChip(
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
