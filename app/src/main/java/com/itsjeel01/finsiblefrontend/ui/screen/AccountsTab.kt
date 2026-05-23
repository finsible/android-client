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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.accountstab.AccountGroupFilters
import com.itsjeel01.finsiblefrontend.ui.component.accountstab.AccountListItem
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleLoader
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTileCards
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleLoaderDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTileCardRotationVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.mapper.toAccountsTileCards
import com.itsjeel01.finsiblefrontend.ui.model.state.AccountListItem
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
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
                FinsibleLoader(
                    size = FinsibleSize.Large,
                    colors = FinsibleLoaderDefaults.colors(
                        ballColor = FinsibleTheme.colors.contentPrimary,
                        barColor = FinsibleTheme.colors.contentSecondary
                    )
                )
            }
        } else {
            if (state.tileCards.isEmpty()) return@AnimatedContent

            val tileCards = state.tileCards.toAccountsTileCards()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.stackMd)
            ) {
                FinsibleText(
                    text = stringResource(R.string.my_accounts),
                    textStyle = FinsibleTheme.typography.headingLg
                )

                Spacer(Modifier.height(FinsibleTheme.spacing.stackLg))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackSm),
                    contentPadding = PaddingValues(bottom = FinsibleTheme.spacing.stackLg)
                ) {
                    item(key = "accounts_card") {
                        FinsibleTileCards(
                            cards = tileCards,
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(horizontal = FinsibleTheme.spacing.insetXs, vertical = FinsibleTheme.spacing.stackSm),
                            size = FinsibleSize.Medium,
                            rotationVariant = FinsibleTileCardRotationVariant.Sequential,
                        )
                    }

                    stickyHeader(key = "account_group_filter_chips") {
                        AccountGroupFilters(
                            groups = state.accountGroups,
                            selectedGroupId = state.selectedGroupId,
                            onGroupSelected = viewModel::selectGroupFilter,
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .background(FinsibleTheme.colors.surfaceBase)
                                .padding(vertical = FinsibleTheme.spacing.stackSm)
                        )
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
                        when (item) {
                            is AccountListItem.Header -> {
                                FinsibleText(
                                    text = item.groupName.uppercase(),
                                    textStyle = FinsibleTheme.typography.labelSm,
                                    colorVariant = FinsibleTextColorVariant.Secondary,
                                    modifier = Modifier
                                        .animateItem()
                                        .padding(
                                            start = FinsibleTheme.spacing.stackSm,
                                            top = FinsibleTheme.spacing.stackSm,
                                            bottom = FinsibleTheme.spacing.insetXs
                                        )
                                )
                            }

                            is AccountListItem.Account -> {
                                AccountListItem(
                                    model = item.uiModel,
                                    modifier = Modifier
                                        .animateItem()
                                        .fillMaxWidth()
                                        .padding(bottom = FinsibleTheme.spacing.stackSm)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}