package com.itsjeel01.finsiblefrontend.ui.navigation

import com.itsjeel01.finsiblefrontend.R

/** Navigation tab configuration data class */
data class BottomNavItem(
    val label: String,
    val route: Route,
    val isFAB: Boolean = false,
    val inactiveIcon: Int = 0,
    val activeIcon: Int = 0,
    val hasNestedNavigation: Boolean = false
)

/** Navigation tabs configuration */
object BottomNavItems {
    private val navigationTabs = listOf(
        BottomNavItem(
            label = "Dashboard",
            route = Route.Home.Dashboard,
            inactiveIcon = R.drawable.ic_home_outlined,
            activeIcon = R.drawable.ic_home_filled
        ),
        BottomNavItem(
            label = "Analytics",
            route = Route.Home.Analytics,
            inactiveIcon = R.drawable.ic_analytics_outlined,
            activeIcon = R.drawable.ic_analytics_filled
        ),
        BottomNavItem(
            label = "New Transaction",
            route = Route.Home.NewTransaction,
            isFAB = true,
            inactiveIcon = 0,
            activeIcon = R.drawable.ic_plus
        ),
        BottomNavItem(
            label = "Accounts",
            route = Route.Home.Balance,
            inactiveIcon = R.drawable.ic_piggybank_outlined,
            activeIcon = R.drawable.ic_piggybank_filled
        ),
        BottomNavItem(
            label = "Settings",
            route = Route.Home.Settings,
            inactiveIcon = R.drawable.ic_settings_outlined,
            activeIcon = R.drawable.ic_settings_filled
        )
    )

    fun getAll(): List<BottomNavItem> {
        return navigationTabs
    }

    fun toMap(): Map<Route, BottomNavItem> {
        return navigationTabs.associateBy { it.route }
    }
}
