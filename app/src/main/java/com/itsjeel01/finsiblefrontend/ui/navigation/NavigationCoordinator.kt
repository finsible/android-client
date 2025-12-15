package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/** Interface for navigation coordination to separate navigation logic from UI code. */
interface NavigationCoordinator {
    /** Navigate to a destination with optional configuration. */
    fun navigateTo(destination: Any, builder: NavOptionsBuilder.() -> Unit = {})
    
    /** Navigate back to the previous destination. */
    fun navigateBack(): Boolean
    
    /** Clear the back stack up to a destination. */
    fun clearBackStackTo(destination: Any, inclusive: Boolean = false)
}

/** Implementation of NavigationCoordinator for app-level navigation. */
class AppNavigationCoordinator(
    private val navController: NavController
) : NavigationCoordinator {
    
    override fun navigateTo(destination: Any, builder: NavOptionsBuilder.() -> Unit) {
        navController.navigate(destination, builder)
    }
    
    override fun navigateBack(): Boolean {
        return navController.popBackStack()
    }
    
    override fun clearBackStackTo(destination: Any, inclusive: Boolean) {
        navController.popBackStack(destination, inclusive)
    }
    
    /** Navigate to onboarding screen and clear launch screen from back stack. */
    fun navigateToOnboarding() {
        navigateTo(AppRoutes.Onboarding) {
            popUpTo<AppRoutes.Launch> { inclusive = true }
        }
    }
    
    /** Navigate to home screen and clear previous screens from back stack. */
    fun navigateToHome(from: AppRoutes) {
        navigateTo(AppRoutes.Home) {
            when (from) {
                is AppRoutes.Launch -> popUpTo<AppRoutes.Launch> { inclusive = true }
                is AppRoutes.Onboarding -> popUpTo<AppRoutes.Onboarding> { inclusive = true }
                else -> {}
            }
        }
    }
    
    /** Navigate back to onboarding from home (logout flow). */
    fun navigateToOnboardingFromHome() {
        navigateTo(AppRoutes.Onboarding) {
            popUpTo<AppRoutes.Home> { inclusive = true }
        }
    }
}

/** Implementation of NavigationCoordinator for home tab navigation. */
class HomeNavigationCoordinator(
    private val navController: NavController,
    private val onTabChanged: (Int) -> Unit
) : NavigationCoordinator {
    
    override fun navigateTo(destination: Any, builder: NavOptionsBuilder.() -> Unit) {
        navController.navigate(destination, builder)
    }
    
    override fun navigateBack(): Boolean {
        return navController.popBackStack()
    }
    
    override fun clearBackStackTo(destination: Any, inclusive: Boolean) {
        navController.popBackStack(destination, inclusive)
    }
    
    /** Navigate to a tab by index with proper state management. */
    fun navigateToTab(tabIndex: Int) {
        val route = NavigationTabs.getRouteFromTabIndex(tabIndex)
        navigateTo(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
        onTabChanged(tabIndex)
    }
    
    /** Navigate back from new transaction tab to previous tab. */
    fun navigateBackFromNewTransaction(previousTabIndex: Int) {
        val route = NavigationTabs.getRouteFromTabIndex(previousTabIndex)
        navigateTo(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
