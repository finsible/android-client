package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Account-specific expandable chip row (for Expense/Income "FROM" / "TO" sections).
 *
 * The selected account is always pinned as the first chip (visible even in collapsed state).
 *
 * Collapsed: selected chip + remaining shortlist items (up to [collapsedCount] total).
 * Expanded: selected chip + all shortlist items (wrapping).
 */
@Composable
fun ExpenseIncomeAccountRow(
    shortlistItems: List<AccountUIModel>,
    selectedItem: AccountUIModel?,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onItemSelected: (AccountUIModel) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier,
    collapsedCount: Int = 5,
    onError: (@Composable () -> Unit)? = null
) {
    val animSpecs = FinsibleTheme.animations.specs
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
                AccountChip(
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

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)
    ) {
        AnimatedContent(
            targetState = isExpanded,
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
            transitionSpec = { animSpecs.springFadeTransition() },
            label = "ExpenseIncomeAccountRowAnimation"
        ) { targetExpanded ->
            FinsibleChipsRow(
                wrap = true,
                chips = chips,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (onError != null) {
            onError()
        }
    }
}

/** Chip representing an account in the new-transaction flow. */
@Composable
fun AccountChip(
    model: AccountUIModel,
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
        enforceMinTouchTarget = false
    )
}
