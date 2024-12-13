package com.itsjeel01.finsiblefrontend.data

import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes

data class BottomNavigationItems(
    val label: String = "",
    val icon: Int = 0,
    val route: Routes = Routes.DashboardScreen,
) {
    fun getBottomNavigationItems(): List<BottomNavigationItems> {
        return listOf(
            BottomNavigationItems(
                label = "Home",
                icon = R.drawable.transactions_icon,
                route = Routes.HomeScreen
            ),
            BottomNavigationItems(
                label = "Insights",
                icon = R.drawable.stats_icon,
                route = Routes.InsightsScreen
            ),
            BottomNavigationItems(
                label = "New Transaction",
                icon = R.drawable.square_plus_icon,
                route = Routes.DashboardScreen
            ),
            BottomNavigationItems(
                label = "Balance",
                icon = R.drawable.coins_icon,
                route = Routes.DashboardScreen
            ),
            BottomNavigationItems(
                label = "Settings",
                icon = R.drawable.user_gear_icon,
                route = Routes.DashboardScreen
            ),
        )
    }
}