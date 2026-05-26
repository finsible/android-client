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
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionValidationError
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

@Composable
fun NewTransactionAccountSection(
    state: NewTransactionFormState,
    fromAccounts: List<AccountUIModel>,
    toAccounts: List<AccountUIModel>,
    isFromExpanded: Boolean,
    onFromExpandedChange: (Boolean) -> Unit,
    isToExpanded: Boolean,
    onToExpandedChange: (Boolean) -> Unit,
    onOpenFromAccounts: () -> Unit,
    onOpenToAccounts: () -> Unit,
    accentColor: Color,
    onEvent: (NewTransactionUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val needsFrom = state.transactionType != TransactionType.INCOME
    val needsTo = state.transactionType != TransactionType.EXPENSE

    val selectedFromAccount = remember(fromAccounts, state.fromAccountId) {
        fromAccounts.find { it.id == state.fromAccountId }
    }
    val selectedToAccount = remember(toAccounts, state.toAccountId) {
        toAccounts.find { it.id == state.toAccountId }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AccountSectionDefaults.contentSpacing)
    ) {
        if (state.transactionType == TransactionType.TRANSFER) {
            TransferAccountRowInternal(
                fromAccounts = fromAccounts,
                toAccounts = toAccounts,
                selectedFromId = state.fromAccountId,
                selectedToId = state.toAccountId,
                onFromSelected = { onEvent(NewTransactionUiEvent.FromAccountSelected(it)) },
                onToSelected = { onEvent(NewTransactionUiEvent.ToAccountSelected(it)) },
                accentColor = accentColor,
                fromExpanded = isFromExpanded,
                toExpanded = isToExpanded,
                onFromExpandedChange = onFromExpandedChange,
                onToExpandedChange = onToExpandedChange,
                onOpenFromAll = onOpenFromAccounts,
                onOpenToAll = onOpenToAccounts,
                onError = if (state.validationErrors.contains(NewTransactionValidationError.TRANSFER_ACCOUNTS_MUST_DIFFER)) {
                    {
                        FinsibleText(
                            text = "From and To accounts must be different",
                            textStyle = FinsibleTheme.typography.bodyMd.medium(),
                            color = FinsibleTheme.colors.feedbackError
                        )
                    }
                } else null
            )
        } else {
            if (needsFrom) {
                Column(verticalArrangement = Arrangement.spacedBy(AccountSectionDefaults.headerSpacing)) {
                    AccountSectionLabelRowInternal(
                        label = "From",
                        onOpenAll = onOpenFromAccounts,
                        accentColor = accentColor
                    )

                    ExpenseIncomeAccountRowInternal(
                        shortlistItems = fromAccounts,
                        selectedItem = selectedFromAccount,
                        isExpanded = isFromExpanded,
                        onExpandedChange = onFromExpandedChange,
                        onItemSelected = { onEvent(NewTransactionUiEvent.FromAccountSelected(it.id)) },
                        accentColor = accentColor,
                        onError = if (state.validationErrors.contains(NewTransactionValidationError.FROM_ACCOUNT)) {
                            {
                                FinsibleText(
                                    text = "From account is required",
                                    textStyle = FinsibleTheme.typography.bodyMd.medium(),
                                    color = FinsibleTheme.colors.feedbackError
                                )
                            }
                        } else null
                    )
                }
            }

            if (needsTo) {
                Column(verticalArrangement = Arrangement.spacedBy(AccountSectionDefaults.headerSpacing)) {
                    AccountSectionLabelRowInternal(
                        label = "To",
                        onOpenAll = onOpenToAccounts,
                        accentColor = accentColor
                    )

                    ExpenseIncomeAccountRowInternal(
                        shortlistItems = toAccounts,
                        selectedItem = selectedToAccount,
                        isExpanded = isToExpanded,
                        onExpandedChange = onToExpandedChange,
                        onItemSelected = { onEvent(NewTransactionUiEvent.ToAccountSelected(it.id)) },
                        accentColor = accentColor,
                        onError = if (state.validationErrors.contains(NewTransactionValidationError.TO_ACCOUNT)) {
                            {
                                FinsibleText(
                                    text = "To account is required",
                                    textStyle = FinsibleTheme.typography.bodyMd.medium(),
                                    color = FinsibleTheme.colors.feedbackError
                                )
                            }
                        } else null
                    )
                }
            }
        }
    }
}

object AccountSectionDefaults {
    val headerSpacing: androidx.compose.ui.unit.Dp
        @Composable get() = FinsibleTheme.spacing.insetSm

    val contentSpacing: androidx.compose.ui.unit.Dp
        @Composable get() = FinsibleTheme.spacing.stackSm
}

@Composable
internal fun AccountSectionLabelRowInternal(
    label: String,
    onOpenAll: () -> Unit,
    accentColor: Color,
    hint: String? = "Frequent",
    modifier: Modifier = Modifier
) {
    AccountSectionLabelInternal(
        label = label,
        hint = hint,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(FinsibleTheme.sizes.icon.xs),
                painter = painterResource(id = com.composables.icons.lucide.R.drawable.lucide_ic_wallet),
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
                colors = FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Text,
                    contentColor = accentColor,
                ),
                enforceMinTouchTargetSize = false,
                iconPosition = FinsibleIconPosition.Trailing,
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
internal fun AccountSectionLabelInternal(
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
internal fun ExpenseIncomeAccountRowInternal(
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
                AccountChipInternal(
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
                AccountExpandToggleButton(
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
        ) { _ ->
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

@Composable
internal fun AccountChipInternal(
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

@Composable
internal fun AccountExpandToggleButton(
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

@Composable
internal fun TransferAccountRowInternal(
    fromAccounts: List<AccountUIModel>,
    toAccounts: List<AccountUIModel>,
    selectedFromId: Long?,
    selectedToId: Long?,
    onFromSelected: (Long) -> Unit,
    onToSelected: (Long) -> Unit,
    accentColor: Color,
    fromExpanded: Boolean,
    toExpanded: Boolean,
    onFromExpandedChange: (Boolean) -> Unit,
    onToExpandedChange: (Boolean) -> Unit,
    onOpenFromAll: () -> Unit,
    onOpenToAll: () -> Unit,
    modifier: Modifier = Modifier,
    onError: (@Composable () -> Unit)? = null
) {
    val selectedFromItem = remember(fromAccounts, selectedFromId) {
        fromAccounts.find { it.id == selectedFromId }
    }
    val selectedToItem = remember(toAccounts, selectedToId) {
        toAccounts.find { it.id == selectedToId }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackSm)) {
            AccountSectionLabelRowInternal(
                label = "From",
                onOpenAll = onOpenFromAll,
                accentColor = accentColor,
                hint = null
            )
            ExpenseIncomeAccountRowInternal(
                shortlistItems = fromAccounts,
                selectedItem = selectedFromItem,
                isExpanded = fromExpanded,
                onExpandedChange = onFromExpandedChange,
                onItemSelected = { onFromSelected(it.id) },
                accentColor = accentColor
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackSm)) {
            AccountSectionLabelRowInternal(
                label = "To",
                onOpenAll = onOpenToAll,
                accentColor = accentColor,
                hint = null
            )
            ExpenseIncomeAccountRowInternal(
                shortlistItems = toAccounts,
                selectedItem = selectedToItem,
                isExpanded = toExpanded,
                onExpandedChange = onToExpandedChange,
                onItemSelected = { onToSelected(it.id) },
                accentColor = accentColor
            )
        }

        if (onError != null) {
            onError()
        }
    }
}




