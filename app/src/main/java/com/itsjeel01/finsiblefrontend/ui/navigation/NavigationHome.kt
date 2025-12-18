package com.itsjeel01.finsiblefrontend.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.itsjeel01.finsiblefrontend.ui.component.BottomNavigationBar
import com.itsjeel01.finsiblefrontend.ui.screen.AnalyticsTab
import com.itsjeel01.finsiblefrontend.ui.screen.BalanceTab
import com.itsjeel01.finsiblefrontend.ui.screen.DashboardTab
import com.itsjeel01.finsiblefrontend.ui.screen.SettingsTab
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.BalanceViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationHome() {
    val navigationState = rememberBottomNavState(
        startRoute = Route.Home.Dashboard,
        bottomTabs = BottomNavItems.toMap().keys
    )

    val navigator = remember { BottomTabNavigator(navigationState) }

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
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Home.Dashboard> {
                        DashboardTab()
                    }
                    entry<Route.Home.Analytics> {
                        AnalyticsTab()
                    }
                    entry<Route.Home.NewTransaction> {
                        NavigationNewTransaction(
                            onNavigateBack = { navigator.goBack() }
                        )
                    }
                    entry<Route.Home.Balance> {
                        val viewModel: BalanceViewModel = hiltViewModel()
                        BalanceTab(viewModel = viewModel)
                    }
                    entry<Route.Home.Settings> {
                        SettingsTab()
                    }
                }
            )
        )
    }
}