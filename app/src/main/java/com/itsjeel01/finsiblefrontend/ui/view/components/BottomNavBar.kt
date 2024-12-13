package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itsjeel01.finsiblefrontend.data.BottomNavigationItems
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.viewmodel.DashboardViewModel

@Composable
fun BottomNavBar(navController: NavHostController) {

    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val selectedTabItem by dashboardViewModel.selectedTabState.collectAsState()

    NavigationBar {
        BottomNavigationItems().getBottomNavigationItems()
            .forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = selectedTabItem == index,
                    alwaysShowLabel = false,
                    label = {
                        Text(navigationItem.label)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = navigationItem.icon),
                            modifier = Modifier.size(24.dp),
                            contentDescription = navigationItem.label
                        )
                    },
                    onClick = {
                        dashboardViewModel.changeTab(index)
                        if (selectedTabItem != index) {
                            navController.navigate(navigationItem.route) {
                                popUpTo(Routes.HomeScreen) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }

                    }
                )
            }
    }
}