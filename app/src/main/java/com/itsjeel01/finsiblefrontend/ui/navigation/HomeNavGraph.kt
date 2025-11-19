package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.screen.AccountsTab
import com.itsjeel01.finsiblefrontend.ui.screen.AnalyticsTab
import com.itsjeel01.finsiblefrontend.ui.screen.DashboardTab
import com.itsjeel01.finsiblefrontend.ui.screen.SettingsTab
import com.itsjeel01.finsiblefrontend.ui.screen.newtransaction.NewTransactionTab

/** HomeNavGraph handles all internal navigation logic, similar to LaunchNavGraph */
fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    onNewTransactionBackPressed: () -> Unit,
    previousTabIndex: Int,
): (Int) -> Unit {
    instantComposable<HomeRoutes.Dashboard> { DashboardTab() }

    instantComposable<HomeRoutes.Analytics> { AnalyticsTab() }

    composable<HomeRoutes.NewTransaction>(
        enterTransition = { slideInVertically(initialOffsetY = { it }) },
        exitTransition = { slideOutVertically(targetOffsetY = { it }) },
        popEnterTransition = { slideInVertically(initialOffsetY = { it }) },
        popExitTransition = { slideOutVertically(targetOffsetY = { it }) }
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        NewTransactionTab(
            onNavigateBack = {
                keyboardController?.hide()

                val route = NavigationTabs.getRouteFromTabIndex(previousTabIndex)
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                        inclusive = false
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                onNewTransactionBackPressed()
            }
        )
    }

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
    val animationSpec = tween<Float>(Duration.MS_300.toInt())
    val enterAnimation = fadeIn(animationSpec)
    val exitAnimation = fadeOut(animationSpec)

    composable<T>(
        enterTransition = { enterAnimation },
        exitTransition = { exitAnimation },
        popEnterTransition = { enterAnimation },
        popExitTransition = { exitAnimation }
    ) { routes() }
}