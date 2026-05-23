package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.bottomnav.BottomNavigationBar
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTopNavigationBar
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleBadgeType
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleHeaderButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleHeaderState
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.screen.AccountsScreen
import com.itsjeel01.finsiblefrontend.ui.screen.DashboardTab
import com.itsjeel01.finsiblefrontend.ui.screen.HistoryTab
import com.itsjeel01.finsiblefrontend.ui.screen.SettingsTab
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AccountsViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.HistoryViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.NewTransactionViewModel

@Composable
fun NavigationHome() {
    val navigationState = rememberBottomNavState(
        startRoute = Route.Home.Dashboard,
        bottomTabs = BottomNavItems.toMap().keys
    )
    val navigator = remember(navigationState) { BottomTabNavigator(navigationState) }
    val showBottomBar = navigationState.activeTab != Route.Home.NewTransaction

    // THE SINGLE GLOBAL HEADER STATE
    var globalHeaderState by remember { mutableStateOf(FinsibleHeaderState(title = "")) }

    // 1. GPU-Accelerated Bottom Bar State (0f = visible, 1f = hidden)
    // This allows the bar to visually slide away WITHOUT changing the Scaffold's layout bounds.
    val bottomBarTranslation by animateFloatAsState(
        targetValue = if (showBottomBar) 0f else 1f,
        animationSpec = FinsibleTheme.animations.specs.springStiff,
        label = "bottomBarTranslation"
    )

    val systemBottomPadding = WindowInsets.systemBars.only(WindowInsetsSides.Bottom).asPaddingValues()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FinsibleTheme.colors.surfaceBase,
        // We set insets to 0 because our custom Header handles the top status bar safely,
        // and we handle the bottom nav pill manually.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        topBar = {
            FinsibleTopNavigationBar(
                modifier = Modifier.zIndex(2f), // Force header to the absolute top layer
                state = globalHeaderState
            )
        },

        bottomBar = {
            // Use Surface as the semantic container instead of Box
            Surface(
                color = Color.Transparent, // Let the bottom nav draw its own background
                modifier = Modifier
                    .zIndex(0f) // Keep it visually below the NavDisplay content
                    .graphicsLayer {
                        // Visually slide it down by its own height
                        translationY = size.height * bottomBarTranslation
                    }
            ) {
                BottomNavigationBar(
                    activeTab = navigationState.activeTab,
                    onTabSelected = { navigator.navigate(it) }
                )
            }
        }
    ) { paddingValues ->

        // LAYER 1: The Navigation Router
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f), // Crucial: Forces the sliding modal to draw OVER the bottom bar slot
            onBack = navigator::goBack,
            transitionSpec = { NavigationTransitions.homeTransition() },
            popTransitionSpec = { NavigationTransitions.homeTransition() },
            predictivePopTransitionSpec = { NavigationTransitions.homeTransition() },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Home.Dashboard> {
                        DashboardRoute(
                            modifier = Modifier.padding(paddingValues),
                            onUpdateHeader = { newState -> globalHeaderState = newState } // Explicitly name the state
                        )
                    }
                    entry<Route.Home.Accounts> {
                        AccountsRoute(
                            modifier = Modifier.padding(paddingValues),
                            onUpdateHeader = { newState -> globalHeaderState = newState }
                        )
                    }
                    entry<Route.Home.Transactions> {
                        HistoryRoute(
                            modifier = Modifier.padding(paddingValues),
                            onUpdateHeader = { newState -> globalHeaderState = newState }
                        )
                    }
                    entry<Route.Home.Settings> {
                        SettingsRoute(
                            modifier = Modifier.padding(paddingValues),
                            onUpdateHeader = { newState -> globalHeaderState = newState }
                        )
                    }
                    entry<Route.Home.NewTransaction> {
                        NewTransactionRoute(
                            modifier = Modifier.padding(
                                top = paddingValues.calculateTopPadding(),
                                bottom = systemBottomPadding.calculateBottomPadding()
                            ),
                            onNavigateBack = navigator::goBack,
                            onUpdateHeader = { newState -> globalHeaderState = newState }
                        )
                    }
                }
            )
        )
    }
}

@Composable
private fun DashboardRoute(
    modifier: Modifier = Modifier,
    onUpdateHeader: (FinsibleHeaderState) -> Unit
) {
    val title = stringResource(R.string.nav_dashboard)
    val state = remember(title) { FinsibleHeaderState(title = title) }

    // Push state to Global Header
    LaunchedEffect(state) { onUpdateHeader(state) }

    // Apply the modifier here so the layout padding is respected!
    Box(modifier = modifier.fillMaxSize()) {
        DashboardTab()
    }
}

@Composable
private fun AccountsRoute(
    modifier: Modifier = Modifier,
    onUpdateHeader: (FinsibleHeaderState) -> Unit
) {
    val title = stringResource(R.string.my_accounts)
    val subtitle = stringResource(R.string.my_accounts_subtitle)
    val viewModel: AccountsViewModel = hiltViewModel()

    val state = remember(title, subtitle) {
        FinsibleHeaderState(title = title, subtitle = subtitle)
    }

    LaunchedEffect(state) { onUpdateHeader(state) }

    Box(modifier = modifier.fillMaxSize()) {
        AccountsScreen(viewModel = viewModel)
    }
}

@Composable
private fun HistoryRoute(
    modifier: Modifier = Modifier,
    onUpdateHeader: (FinsibleHeaderState) -> Unit
) {
    val viewModel: HistoryViewModel = hiltViewModel()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val title = stringResource(R.string.transaction_history_title)

    val searchButton = remember(viewModel) {
        FinsibleHeaderButton(
            onClick = viewModel::toggleSearchExpanded,
            iconOnly = true,
            variant = FinsibleButtonVariant.Text,
            icon = { Icon(painterResource(com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline), contentDescription = null) }
        )
    }

    val filterButton = remember(viewModel, filterState.activeFilterCount, filterState.sortOption) {
        FinsibleHeaderButton(
            onClick = viewModel::toggleFilterSheet,
            iconOnly = true,
            variant = FinsibleButtonVariant.Text,
            badgeType = when {
                filterState.activeFilterCount > 0 -> FinsibleBadgeType.Count
                filterState.sortOption != SortOption.NEWEST_FIRST -> FinsibleBadgeType.Dot
                else -> FinsibleBadgeType.None
            },
            badgeCount = filterState.activeFilterCount,
            icon = { Icon(painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_list_filter), contentDescription = null) }
        )
    }

    val state = remember(title, searchButton, filterButton) {
        FinsibleHeaderState(title = title, rightButtons = listOf(searchButton, filterButton))
    }

    LaunchedEffect(state) { onUpdateHeader(state) }

    Box(modifier = modifier.fillMaxSize()) {
        HistoryTab(viewModel = viewModel)
    }
}

@Composable
private fun NewTransactionRoute(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onUpdateHeader: (FinsibleHeaderState) -> Unit
) {
    val viewModel: NewTransactionViewModel = hiltViewModel()
    val title = stringResource(R.string.new_transaction)

    val resetButton = remember(viewModel) {
        FinsibleHeaderButton(
            onClick = { viewModel.onEvent(NewTransactionUiEvent.ResetClicked) },
            iconOnly = true,
            variant = FinsibleButtonVariant.Text,
            icon = { Icon(painterResource(com.composables.icons.lucide.R.drawable.lucide_ic_refresh_cw), contentDescription = null) }
        )
    }
    val closeButton = remember(onNavigateBack) {
        FinsibleHeaderButton(
            onClick = onNavigateBack,
            iconOnly = true,
            variant = FinsibleButtonVariant.Text,
            icon = {
                Icon(
                    painterResource(com.composables.icons.materialicons.outlined.R.drawable.materialicons_ic_close_outlined),
                    contentDescription = null
                )
            }
        )
    }

    val state = remember(title, resetButton, closeButton) {
        FinsibleHeaderState(title = title, rightButtons = listOf(resetButton, closeButton))
    }

    LaunchedEffect(state) { onUpdateHeader(state) }

    Box(modifier = modifier.fillMaxSize()) {
        NavigationNewTransaction(
            viewModel = viewModel,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
private fun SettingsRoute(
    modifier: Modifier = Modifier,
    onUpdateHeader: (FinsibleHeaderState) -> Unit
) {
    val title = stringResource(R.string.nav_settings)
    val state = remember(title) { FinsibleHeaderState(title = title) }

    LaunchedEffect(state) { onUpdateHeader(state) }

    Box(modifier = modifier.fillMaxSize()) {
        SettingsTab()
    }
}

private val TabRoutes = BottomNavItems.getAll().map { it.route }
private val StringToRoute = TabRoutes.associateBy { it.toString() }

fun resolveRoute(key: Any?): Route? {
    if (key is Route) return key
    return StringToRoute[key.toString()]
}