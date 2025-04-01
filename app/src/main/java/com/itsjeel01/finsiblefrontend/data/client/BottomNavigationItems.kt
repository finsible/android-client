package com.itsjeel01.finsiblefrontend.data.client

import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes

data class BottomNavigationItems(
    val label: String = "",
    val icon: Int = 0,
    val route: Routes = Routes.DashboardScreen,
    val isNewTransactionForm: Boolean = false,
) {
    fun getBottomNavigationItems(): List<BottomNavigationItems> {
        return listOf(
            BottomNavigationItems(
                label = "Home",
                icon = R.drawable.ic_transactions,
                route = Routes.HomeScreen
            ),
            BottomNavigationItems(
                label = "Insights",
                icon = R.drawable.ic_stats,
                route = Routes.InsightsScreen
            ),
            BottomNavigationItems(
                icon = R.drawable.ic_plus,
                isNewTransactionForm = true,
                route = Routes.NewTransactionForm
            ),
            BottomNavigationItems(
                label = "Balance",
                icon = R.drawable.ic_piggy_bank,
                route = Routes.BalanceScreen
            ),
            BottomNavigationItems(
                label = "Settings",
                icon = R.drawable.ic_hamburger,
                route = Routes.SettingsScreen
            ),
        )
    }
}