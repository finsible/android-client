package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.ui.component.BottomNavigationBar
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.screen.AccountsScreen
import com.itsjeel01.finsiblefrontend.ui.screen.DashboardTab
import com.itsjeel01.finsiblefrontend.ui.screen.SettingsTab
import com.itsjeel01.finsiblefrontend.ui.screen.TransactionsScreen
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AccountsViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionsViewModel

@Composable
fun NavigationHome() {
    val navigationState = rememberBottomNavState(
        startRoute = Route.Home.Dashboard,
        bottomTabs = BottomNavItems.toMap().keys
    )

    val navigator = remember(navigationState) { BottomTabNavigator(navigationState) }

    Scaffold(
        modifier = Modifier
            .background(FinsibleTheme.colors.primaryBackground)
            .systemBarsPadding(),
        bottomBar = {
            BottomNavigationBar(
                activeTab = navigationState.activeTab,
                onTabSelected = { navigator.navigate(it) }
            )
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier.padding(paddingValues),
            onBack = navigator::goBack,
            transitionSpec = {
                calculateTransition(initialState.key, targetState.key, isPop = false)
            },
            popTransitionSpec = {
                calculateTransition(initialState.key, targetState.key, isPop = true)
            },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Home.Dashboard> {
                        DashboardTab()
                    }
                    entry<Route.Home.Accounts> {
                        val viewModel: AccountsViewModel = hiltViewModel()
                        AccountsScreen(viewModel = viewModel)
                    }
                    entry<Route.Home.NewTransaction> {
                        NavigationNewTransaction(
                            onNavigateBack = { navigator.goBack() }
                        )
                    }
                    entry<Route.Home.Transactions> {
                        val viewModel: TransactionsViewModel = hiltViewModel()
                        TransactionsScreen(viewModel = viewModel)
                    }
                    entry<Route.Home.Settings> {
                        SettingsTab()
                    }
                }
            )
        )
    }
}

private val TabRoutes = BottomNavItems.getAll().map { it.route }
private val RouteToIndex = TabRoutes.withIndex().associate { it.value to it.index }
private val StringToRoute = TabRoutes.associateBy { it.toString() }

private fun calculateTransition(
    initialKey: Any?,
    targetKey: Any?,
    isPop: Boolean
): ContentTransform {

    fun resolve(key: Any?): Route? {
        if (key is Route) return key
        return StringToRoute[key.toString()]
    }

    val initialRoute = resolve(initialKey)
    val targetRoute = resolve(targetKey)

    val initialIndex = initialRoute?.let { RouteToIndex[it] } ?: 0
    val targetIndex = targetRoute?.let { RouteToIndex[it] } ?: 0

    val emphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0f, 1.0f)
    val slideSpec = tween<IntOffset>(durationMillis = Duration.MS_400.toInt(), easing = emphasizedEasing)
    val fadeSpec = tween<Float>(durationMillis = Duration.MS_400.toInt(), easing = emphasizedEasing)

    // Handle Vertical Slide for NewTransaction
    if (targetRoute == Route.Home.NewTransaction) {
        return (slideInVertically(slideSpec) { h -> h } + fadeIn(fadeSpec))
            .togetherWith(fadeOut(fadeSpec))
    } else if (initialRoute == Route.Home.NewTransaction) {
        return fadeIn(fadeSpec)
            .togetherWith(slideOutVertically(slideSpec) { h -> h } + fadeOut(fadeSpec))
    }

    // Handle Horizontal Slides for Tabs
    return if (isPop) {
        (slideInHorizontally(slideSpec) { w -> -w } + fadeIn(fadeSpec))
            .togetherWith(slideOutHorizontally(slideSpec) { w -> w } + fadeOut(fadeSpec))
    } else {
        // Push/Switch Logic: Determine direction based on tab index
        if (targetIndex > initialIndex) {
            // Moving Right -> Slide Content Left
            (slideInHorizontally(slideSpec) { w -> w } + fadeIn(fadeSpec))
                .togetherWith(slideOutHorizontally(slideSpec) { w -> -w } + fadeOut(fadeSpec))
        } else {
            // Moving Left -> Slide Content Right
            (slideInHorizontally(slideSpec) { w -> -w } + fadeIn(fadeSpec))
                .togetherWith(slideOutHorizontally(slideSpec) { w -> w } + fadeOut(fadeSpec))
        }
    }
}