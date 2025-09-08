package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.screen.AccountsTab
import com.itsjeel01.finsiblefrontend.ui.screen.AnalyticsTab
import com.itsjeel01.finsiblefrontend.ui.screen.DashboardTab
import com.itsjeel01.finsiblefrontend.ui.screen.NewTransactionTab
import com.itsjeel01.finsiblefrontend.ui.screen.SettingsTab

/** HomeNavGraph handles all internal navigation logic, similar to LaunchNavGraph */
fun NavGraphBuilder.homeNavGraph(navController: NavHostController): (Int) -> Unit {
    instantComposable<HomeRoutes.Dashboard> { DashboardTab() }
    instantComposable<HomeRoutes.Analytics> { AnalyticsTab() }
    instantComposable<HomeRoutes.NewTransaction> { NewTransactionTab() }
    instantComposable<HomeRoutes.Accounts> { AccountsTab() }
    instantComposable<HomeRoutes.Settings> { SettingsTab() }

    return { tabIndex ->
        val route = NavigationTabs.getRouteFromTabIndex(tabIndex)
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

private inline fun <reified T : HomeRoutes> NavGraphBuilder.instantComposable(
    noinline routes: @Composable () -> Unit
) {
    composable<T>(
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) { routes() }
}