package com.itsjeel01.finsiblefrontend.data

import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes

data class BottomNavigationItems(
    val label: String = "",
    val icon: Int = 0,
    val route: Routes = Routes.DashboardScreen,
    val opensScreen: Boolean = true,
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
                icon = R.drawable.plus_icon,
                route = Routes.DashboardScreen,
                opensScreen = false
            ),
            BottomNavigationItems(
                label = "Balance",
                icon = R.drawable.piggy_bank_icon,
                route = Routes.BalanceScreen
            ),
            BottomNavigationItems(
                label = "Settings",
                icon = R.drawable.hamburger_menu_icon,
                route = Routes.SettingsScreen
            ),
        )
    }
}