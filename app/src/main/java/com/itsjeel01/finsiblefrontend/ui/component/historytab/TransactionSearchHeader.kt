package com.itsjeel01.finsiblefrontend.ui.component.historytab

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleIconButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconButtonConfig
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

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
                .weight(1f)
                .height(FinsibleTheme.dimes.d40),
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
                    Text(
                        text = stringResource(R.string.transaction_history_title),
                        style = FinsibleTheme.typography.t24.extraBold()
                    )
                    SearchIconButton(onClick = onSearchIconClick)
                }
            }
        }

        Spacer(Modifier.width(FinsibleTheme.dimes.d12))

        FilterIconButton(
            onClick = onFilterClick,
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
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val borderColor = if (isFocused) FinsibleTheme.colors.outline else FinsibleTheme.colors.outlineVariant
    val cornerRadius = FinsibleTheme.dimes.d10
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
    val textStyle = FinsibleTheme.typography.t14.copy(color = FinsibleTheme.colors.primaryContent)

    val dismissAction = remember(keyboardController, onClose) {
        {
            keyboardController?.hide()
            onClose()
        }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        textStyle = textStyle,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        interactionSource = interactionSource,
        cursorBrush = SolidColor(FinsibleTheme.colors.primaryContent80),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(FinsibleTheme.dimes.d40)
                    .clip(shape)
                    .background(FinsibleTheme.colors.surfaceContainerLow, shape)
                    .border(FinsibleTheme.dimes.d1, borderColor, shape)
                    .padding(horizontal = FinsibleTheme.dimes.d12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
            ) {
                Icon(
                    painter = painterResource(com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
                    contentDescription = null,
                    tint = FinsibleTheme.colors.tertiaryContent,
                    modifier = Modifier.size(FinsibleTheme.dimes.d18)
                )

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_transactions_placeholder),
                            style = textStyle.copy(color = FinsibleTheme.colors.primaryContent40),
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    innerTextField()
                }

                FinsibleIconButton(
                    onClick = dismissAction,
                    icon = com.composables.icons.materialicons.outlined.R.drawable.materialicons_ic_close_outlined,
                    contentDescription = stringResource(R.string.cd_close_search),
                    config = IconButtonConfig(
                        size = ComponentSize.Small,
                        tintIcon = true,
                        customTint = FinsibleTheme.colors.onSurfaceVariant,
                        type = ComponentType.Tertiary
                    )
                )
            }
        }
    )
}

/** Icon button that toggles search expansion. */
@Composable
private fun SearchIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cornerRadius = FinsibleTheme.dimes.d10
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }

    Box(
        modifier = modifier
            .size(FinsibleTheme.dimes.d40)
            .clip(shape)
            .background(FinsibleTheme.colors.surfaceContainerLow)
            .border(
                width = FinsibleTheme.dimes.d1,
                color = FinsibleTheme.colors.border,
                shape = shape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
            contentDescription = stringResource(R.string.cd_search),
            tint = FinsibleTheme.colors.secondaryContent,
            modifier = Modifier.size(FinsibleTheme.dimes.d20)
        )
    }
}

/** Icon button that opens the filter sheet, with an optional badge for active filter count. */
@Composable
private fun FilterIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0,
    hasActiveSort: Boolean = false,
) {
    val isActive = badgeCount > 0 || hasActiveSort
    val cornerRadius = FinsibleTheme.dimes.d10
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }

    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(FinsibleTheme.dimes.d40)
                .clip(shape)
                .background(
                    if (isActive) FinsibleTheme.colors.surfaceContainerHigh
                    else FinsibleTheme.colors.surfaceContainerLow
                )
                .border(
                    width = FinsibleTheme.dimes.d1,
                    color = if (isActive) FinsibleTheme.colors.outline else FinsibleTheme.colors.outlineVariant,
                    shape = shape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_list_filter),
                contentDescription = stringResource(R.string.cd_filter),
                tint = if (isActive) FinsibleTheme.colors.link else FinsibleTheme.colors.secondaryContent,
                modifier = Modifier.size(FinsibleTheme.dimes.d20)
            )
        }

        if (badgeCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = FinsibleTheme.dimes.d4, y = -FinsibleTheme.dimes.d4)
                    .size(FinsibleTheme.dimes.d16)
                    .clip(RoundedCornerShape(FinsibleTheme.dimes.d8))
                    .background(FinsibleTheme.colors.link),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeCount.toString(),
                    style = FinsibleTheme.typography.t10.semiBold(),
                    color = FinsibleTheme.colors.same
                )
            }
        }
    }
}
