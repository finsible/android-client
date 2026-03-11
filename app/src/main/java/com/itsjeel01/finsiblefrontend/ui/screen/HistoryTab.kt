package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.di.hiltCurrencyFormatter
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleIconButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconButtonShape
import com.itsjeel01.finsiblefrontend.ui.component.historytab.FilteredResultsSummary
import com.itsjeel01.finsiblefrontend.ui.component.historytab.TransactionEmptyContent
import com.itsjeel01.finsiblefrontend.ui.component.historytab.TransactionListContent
import com.itsjeel01.finsiblefrontend.ui.component.historytab.TransactionSearchHeader
import com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet.TransactionFilterSheet
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.HistoryViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val LOAD_MORE_THRESHOLD = 15
private const val SCROLL_TO_TOP_THRESHOLD = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTab(
    viewModel: HistoryViewModel,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = hiltCurrencyFormatter()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val isSearchExpanded by viewModel.isSearchExpanded.collectAsStateWithLifecycle()
    val dateFilterModes by viewModel.dateFilterModes.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showFilterSheet by remember { mutableStateOf(false) }
    val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val showGrouping = filterState.shouldShowDateGrouping && uiState.groupedTransactions.isNotEmpty()

    val onScrollToTop: () -> Unit = remember(scope, listState) {
        { scope.launch { listState.animateScrollToItem(0) } }
    }

    LaunchedEffect(filterState) {
        listState.scrollToItem(0)
    }

    LaunchedEffect(listState, uiState.hasMoreData) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            totalItems > 0 && lastVisibleIndex >= totalItems - LOAD_MORE_THRESHOLD
        }
            .distinctUntilChanged()
            .collect { shouldLoad ->
                if (shouldLoad &&
                    uiState.hasMoreData &&
                    !(uiState.isLoading && uiState.transactions.isNotEmpty())) {
                    viewModel.loadMore()
                }
            }
    }

    TransactionFilterSheet(
        isVisible = showFilterSheet,
        appliedFilters = filterState,
        onDismiss = {
            scope.launch { filterSheetState.hide() }.invokeOnCompletion { showFilterSheet = false }
        },
        onApply = { viewModel.applyFilterCriteria(it) },
        bottomSheetState = filterSheetState
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12)
        ) {
            // Title, Search and Filter
            TransactionSearchHeader(
                isExpanded = isSearchExpanded,
                searchQuery = filterState.searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onSearchIconClick = { viewModel.toggleSearchExpanded() },
                onCancelClick = { viewModel.collapseSearch() },
                onFilterClick = { showFilterSheet = true },
                activeFilterCount = filterState.activeFilterCount,
                hasActiveSort = filterState.sortOption != SortOption.NEWEST_FIRST
            )

            Spacer(Modifier.height(FinsibleTheme.dimes.d16))

            // Summary Card for filtered/sorted results
            if (filterState.hasActiveFiltersOrSearch && uiState.filteredSummary != null) {
                FilteredResultsSummary(
                    summary = uiState.filteredSummary!!,
                    currencyFormatter = currencyFormatter
                )
                Spacer(Modifier.height(FinsibleTheme.dimes.d12))
            }

            when {
                // Full screen loading indicator while freshly fetching data
                uiState.isLoading && uiState.transactions.isEmpty() -> {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = FinsibleTheme.colors.secondaryContent)
                    }
                }

                // Error message while fetching data
                uiState.error != null && uiState.transactions.isEmpty() -> {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error ?: stringResource(R.string.unknown_error),
                            color = FinsibleTheme.colors.error
                        )
                    }
                }

                // Empty state along with appropriate actions
                uiState.transactions.isEmpty() -> {
                    TransactionEmptyContent(
                        modifier = Modifier.weight(1f),
                        isFilterActive = filterState.hasActiveFiltersOrSearch,
                        onClearFilters = { viewModel.clearAllFilters() }
                    )
                }

                // List of transactions
                else -> {
                    TransactionListContent(
                        modifier = Modifier.weight(1f),
                        uiState = uiState,
                        showGrouping = showGrouping,
                        dateFilterModes = dateFilterModes,
                        hasActiveFiltersOrSearch = filterState.hasActiveFiltersOrSearch,
                        listState = listState,
                        currencyFormatter = currencyFormatter,
                        onToggleDateFilter = { viewModel.toggleDateFilter(it) }
                    )
                }
            }
        }

        val showScrollToTop by remember {
            derivedStateOf { listState.firstVisibleItemIndex >= SCROLL_TO_TOP_THRESHOLD }
        }

        // Floating action button to scroll to top
        AnimatedVisibility(
            visible = showScrollToTop,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = FinsibleTheme.dimes.d16),
            enter = fadeIn(tween(Duration.MS_200.toInt())) + slideInVertically(tween(Duration.MS_200.toInt())) { it },
            exit = fadeOut(tween(Duration.MS_150.toInt())) + slideOutVertically(tween(Duration.MS_150.toInt())) { it }
        ) {
            FinsibleIconButton(
                icon = R.drawable.ic_arrow_up,
                onClick = onScrollToTop,
                modifier = modifier,
                config = IconButtonConfig(
                    type = ComponentType.Primary,
                    size = ComponentSize.Medium,
                    shape = IconButtonShape.Circle
                )
            )
        }
    }
}