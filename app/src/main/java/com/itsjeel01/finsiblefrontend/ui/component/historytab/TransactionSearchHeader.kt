package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTextFieldDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Transaction search header with vertical slide animation between title and search bar. */
@Composable
fun TransactionSearchHeader(
    isExpanded: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchIconClick: () -> Unit,
    onCancelClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeFilterCount: Int = 0,
    hasActiveSort: Boolean = false,
) {
    val headerIconSide = FinsibleTheme.dimes.d40

    val onClose = remember(onSearchQueryChange, onCancelClick) {
        {
            onSearchQueryChange("")
            onCancelClick()
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedContent(
            targetState = isExpanded,
            modifier = Modifier
                .weight(1f),
            transitionSpec = {
                if (targetState) {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                } else {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                }
            },
            contentAlignment = Alignment.CenterStart,
            label = "search_header_swap"
        ) { expanded ->
            if (expanded) {
                SearchTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    onClose = onClose
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FinsibleText(
                        text = stringResource(R.string.transaction_history_title),
                        variant = FinsibleTextVariant.LargeTitleExtraBold,
                        colorVariant = FinsibleTextColorVariant.Primary
                    )
                    SearchIconButton(
                        onClick = onSearchIconClick,
                        modifier = Modifier.size(headerIconSide)
                    )
                }
            }
        }

        Spacer(Modifier.width(FinsibleTheme.dimes.d12))

        FilterIconButton(
            onClick = onFilterClick,
            modifier = Modifier.size(headerIconSide),
            badgeCount = activeFilterCount,
            hasActiveSort = hasActiveSort
        )
    }
}

/** Search text field styled to match the icon buttons in the header. */
@Composable
private fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val dismissAction = remember(keyboardController, onClose) {
        {
            keyboardController?.hide()
            onClose()
        }
    }

    FinsibleTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = stringResource(R.string.search_transactions_placeholder),
        modifier = modifier.fillMaxWidth(),
        size = FinsibleSize.Small,
        shapeVariant = FinsibleShape.Rounded,
        focusRequester = focusRequester,
        inputConfig = FinsibleTextFieldDefaults.inputConfig(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        leadingIcon = {
            Icon(
                painter = painterResource(com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
                contentDescription = null
            )
        },
        trailingIcon = {
            FinsibleButton(
                onClick = dismissAction,
                iconOnly = true,
                variant = FinsibleButtonVariant.Text,
                size = FinsibleSize.ExtraSmall,
                shapeVariant = FinsibleShape.Circle,
                icon = {
                    Icon(
                        painter = painterResource(com.composables.icons.materialicons.outlined.R.drawable.materialicons_ic_close_outlined),
                        contentDescription = stringResource(R.string.cd_close_search)
                    )
                }
            )
        },
        colors = FinsibleTextFieldDefaults.colors(
            containerColor = FinsibleTheme.colors.transparent,
            borderColor = FinsibleTheme.colors.outlineVariant,
            focusedBorderColor = FinsibleTheme.colors.outline,
            contentColor = FinsibleTheme.colors.primaryContent,
            placeholderColor = FinsibleTheme.colors.primaryContent40,
            iconTint = FinsibleTheme.colors.tertiaryContent
        )
    )
}

/** Icon button that toggles search expansion. */
@Composable
private fun SearchIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FinsibleButton(
        onClick = onClick,
        iconOnly = true,
        modifier = modifier,
        variant = FinsibleButtonVariant.Outlined,
        size = FinsibleSize.Medium,
        shapeVariant = FinsibleShape.Rounded,
        icon = {
            Icon(
                painter = painterResource(com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
                contentDescription = stringResource(R.string.cd_search),
            )
        }
    )
}

/** Icon button that opens the filter sheet, with an optional badge for active filter count. */
@Composable
private fun FilterIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    hasActiveSort: Boolean = false,
) {
    badgeCount > 0 || hasActiveSort

    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        FinsibleButton(
            onClick = onClick,
            iconOnly = true,
            variant = FinsibleButtonVariant.Outlined,
            size = FinsibleSize.Medium,
            shapeVariant = FinsibleShape.Rounded,
            badgeType = when {
                badgeCount > 0 -> FinsibleBadgeType.Count
                hasActiveSort -> FinsibleBadgeType.Dot
                else -> FinsibleBadgeType.None
            },
            badgeCount = badgeCount,
            icon = {
                Icon(
                    painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_list_filter),
                    contentDescription = stringResource(R.string.cd_filter),
                )
            }
        )
    }
}
