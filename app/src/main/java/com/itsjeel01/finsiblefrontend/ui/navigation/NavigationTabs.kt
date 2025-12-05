package com.itsjeel01.finsiblefrontend.ui.navigation

import com.itsjeel01.finsiblefrontend.R

/** Navigation tab configuration data class */
data class NavigationTab(
    val label: String,
    val route: HomeRoutes,
    val isFAB: Boolean = false,
    val inactiveIcon: Int = 0,
    val activeIcon: Int = 0,
    val hasNestedNavigation: Boolean = false
)

/** Navigation tabs configuration */
object NavigationTabs {
    private val navigationTabs = listOf(
        NavigationTab(
            label = "Dashboard",
            route = HomeRoutes.Dashboard,
            inactiveIcon = R.drawable.ic_home_outlined,
            activeIcon = R.drawable.ic_home_filled
        ),
        NavigationTab(
            label = "Analytics",
            route = HomeRoutes.Analytics,
            inactiveIcon = R.drawable.ic_analytics_outlined,
            activeIcon = R.drawable.ic_analytics_filled
        ),
        NavigationTab(
            label = "New Transaction",
            route = HomeRoutes.NewTransaction,
            isFAB = true,
            inactiveIcon = 0,
            activeIcon = R.drawable.ic_plus
        ),
        NavigationTab(
            label = "Accounts",
            route = HomeRoutes.Balance,
            inactiveIcon = R.drawable.ic_piggybank_outlined,
            activeIcon = R.drawable.ic_piggybank_filled
        ),
        NavigationTab(
            label = "Settings",
            route = HomeRoutes.Settings,
            inactiveIcon = R.drawable.ic_settings_outlined,
            activeIcon = R.drawable.ic_settings_filled
        )
    )

    fun getAll(): List<NavigationTab> {
        return navigationTabs
    }

    fun getRouteFromTabIndex(index: Int): HomeRoutes {
        return navigationTabs.getOrNull(index)?.route ?: HomeRoutes.Dashboard
    }
}
