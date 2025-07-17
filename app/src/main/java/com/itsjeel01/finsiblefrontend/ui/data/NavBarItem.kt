package com.itsjeel01.finsiblefrontend.ui.data

import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes

data class NavBarItem(
    val label: String = "",
    val icon: Int = 0,
    val route: Routes = Routes.DashboardScreen,
    val isNewTransactionForm: Boolean = false,
) {
    fun getItems(): List<NavBarItem> {
        return listOf(
            NavBarItem(
                label = "Home",
                icon = R.drawable.ic_transactions,
                route = Routes.HomeScreen
            ),
            NavBarItem(
                label = "Insights",
                icon = R.drawable.ic_stats,
                route = Routes.InsightsScreen
            ),
            NavBarItem(
                icon = R.drawable.ic_plus,
                isNewTransactionForm = true,
                route = Routes.NewTransactionForm
            ),
            NavBarItem(
                label = "Balance",
                icon = R.drawable.ic_piggy_bank,
                route = Routes.BalanceScreen
            ),
            NavBarItem(
                label = "Settings",
                icon = R.drawable.ic_hamburger,
                route = Routes.SettingsScreen
            ),
        )
    }
}
