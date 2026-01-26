package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.ui.component.FlippableCard
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleGradients
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.GradientType
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AccountListItem
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AccountUiModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AccountsViewModel

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = state.isLoading,
        transitionSpec = {
            fadeIn(animationSpec = tween(Duration.MS_300.toInt())) togetherWith
                    fadeOut(animationSpec = tween(Duration.MS_300.toInt()))
        },
        label = "AccountsScreenStateTransition",
        modifier = modifier.fillMaxSize()
    ) { isLoading ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    color = FinsibleTheme.colors.primaryContent,
                    trackColor = FinsibleTheme.colors.secondaryContent
                )
            }
        } else {
            if (state.accountCards.isEmpty()) return@AnimatedContent

            val gradientTypes = remember(state.accountCards) {
                state.accountCards.mapIndexed { index, _ ->
                    when (index) {
                        0 -> GradientType.NET_WORTH
                        1 -> GradientType.ASSETS
                        2 -> GradientType.LIABILITIES
                        else -> GradientType.BRAND
                    }
                }
            }

            val gradients = gradientTypes.map { type ->
                FinsibleGradients.getLinearGradient(type)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12)
            ) {
                Text("My Accounts", style = FinsibleTheme.typography.t24.extraBold())

                Spacer(Modifier.height(FinsibleTheme.dimes.d16))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
                ) {
                    item(key = "accounts_card") {
                        Box(modifier = Modifier.animateItem()) {
                            FlippableCard(
                                items = state.accountCards,
                                gradients = gradients,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    stickyHeader(key = "filter_chips") {
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .background(FinsibleTheme.colors.primaryBackground)
                                .padding(vertical = FinsibleTheme.dimes.d8)
                        ) {
                            AccountGroupFilterRow(
                                groups = state.accountGroups,
                                selectedGroupId = state.selectedGroupId,
                                onGroupSelected = viewModel::selectGroupFilter,
                            )
                        }
                    }

                    items(
                        items = state.listItems,
                        key = { item ->
                            when (item) {
                                is AccountListItem.Header -> "header_${item.groupName}"
                                is AccountListItem.Account -> item.uiModel.id
                            }
                        }
                    ) { item ->
                        Box(modifier = Modifier.animateItem()) {
                            when (item) {
                                is AccountListItem.Header -> {
                                    AccountGroupHeader(groupName = item.groupName)
                                }

                                is AccountListItem.Account -> {
                                    Column {
                                        AccountItem(
                                            model = item.uiModel,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(Modifier.height(FinsibleTheme.dimes.d8))
                                    }
                                }
                            }
                        }
                    }

                    item(key = "bottom_spacing") {
                        Spacer(Modifier.height(FinsibleTheme.dimes.d80))
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountGroupHeader(groupName: String) {
    Text(
        text = groupName.uppercase(),
        style = FinsibleTheme.typography.t12.semiBold(),
        color = FinsibleTheme.colors.secondaryContent,
        modifier = Modifier.padding(
            start = FinsibleTheme.dimes.d8,
            top = FinsibleTheme.dimes.d8,
            bottom = FinsibleTheme.dimes.d4
        )
    )
}

@Composable
private fun AccountGroupFilterRow(
    groups: List<AccountGroupEntity>,
    selectedGroupId: Long?,
    onGroupSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        item(key = "all") {
            AccountGroupFilterChip(
                text = "All",
                isSelected = selectedGroupId == null,
                onClick = { onGroupSelected(null) }
            )
        }

        items(items = groups, key = { it.id }) { group ->
            AccountGroupFilterChip(
                text = group.name,
                isSelected = selectedGroupId == group.id,
                onClick = { onGroupSelected(group.id) }
            )
        }
    }
}

@Composable
private fun AccountGroupFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn() + fadeIn(),
        modifier = modifier
    ) {
        FilterChip(
            selected = isSelected,
            onClick = onClick,
            label = {
                Text(
                    text = text,
                    style = FinsibleTheme.typography.t14,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                )
            },
            shape = RoundedCornerShape(FinsibleTheme.dimes.d20),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = FinsibleTheme.colors.inverse,
                selectedLabelColor = FinsibleTheme.colors.same,
                containerColor = FinsibleTheme.colors.surfaceContainer,
                labelColor = FinsibleTheme.colors.primaryContent
            ),
            border = FilterChipDefaults.filterChipBorder(
                borderColor = FinsibleTheme.colors.border,
                selectedBorderColor = FinsibleTheme.colors.brandAccent,
                enabled = true,
                selected = isSelected
            )
        )
    }
}

@Composable
private fun AccountItem(
    model: AccountUiModel,
    modifier: Modifier = Modifier
) {
    val cornerRadius = FinsibleTheme.dimes.d12
    val borderWidth = FinsibleTheme.dimes.d4
    val fallbackColor = if (model.isPositiveBalance) FinsibleTheme.colors.income else FinsibleTheme.colors.expense

    val borderColor = if (model.groupColor != null) {
        FinsibleTheme.resolveColor(model.groupColor, fallbackColor)
    } else {
        fallbackColor
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = borderWidth)
                .background(
                    color = FinsibleTheme.colors.surfaceContainerLow,
                    shape = RoundedCornerShape(FinsibleTheme.dimes.d8)
                )
                .padding(
                    vertical = FinsibleTheme.dimes.d16,
                    horizontal = FinsibleTheme.dimes.d12
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(FinsibleTheme.dimes.d12))
                    .background(borderColor.copy(alpha = 0.2f))
                    .padding(FinsibleTheme.dimes.d12),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(resolveIcon(model.icon, R.drawable.ic_piggybank_outlined)),
                    contentDescription = "Account icon for ${model.name}",
                    modifier = Modifier.size(FinsibleTheme.dimes.d24),
                    tint = FinsibleTheme.colors.primaryContent,
                )
            }

            Spacer(Modifier.width(FinsibleTheme.dimes.d12))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = model.name,
                    style = FinsibleTheme.typography.t16.semiBold(),
                    color = FinsibleTheme.colors.primaryContent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (model.description.isNotBlank()) {
                    Spacer(Modifier.height(FinsibleTheme.dimes.d2))
                    Text(
                        text = model.description,
                        style = FinsibleTheme.typography.t14,
                        color = FinsibleTheme.colors.secondaryContent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(FinsibleTheme.dimes.d8))

            Text(
                text = model.formattedBalance,
                style = FinsibleTheme.typography.t16.bold(),
                color = FinsibleTheme.colors.primaryContent,
            )
        }
    }
}