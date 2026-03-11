package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.accountstab.AccountGroupFilterRow
import com.itsjeel01.finsiblefrontend.ui.component.accountstab.AccountGroupHeader
import com.itsjeel01.finsiblefrontend.ui.component.accountstab.AccountRow
import com.itsjeel01.finsiblefrontend.ui.component.accountstab.FlippableCard
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleGradients
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.GradientType
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AccountsViewModel
import com.itsjeel01.finsiblefrontend.ui.model.state.AccountListItem

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
                Text(stringResource(R.string.my_accounts), style = FinsibleTheme.typography.t24.extraBold())

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
                                        AccountRow(
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