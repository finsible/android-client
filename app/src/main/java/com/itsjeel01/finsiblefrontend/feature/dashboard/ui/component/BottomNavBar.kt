package com.itsjeel01.finsiblefrontend.feature.dashboard.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itsjeel01.finsiblefrontend.data.model.client.BottomNavigationItems
import com.itsjeel01.finsiblefrontend.feature.dashboard.viewmodel.DashboardViewModel
import com.itsjeel01.finsiblefrontend.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleNavigationBarItemColors

@Composable
fun BottomNavBar(navController: NavHostController) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel()

    val selectedTabItem = dashboardViewModel.selectedTabState.collectAsState().value

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        BottomNavigationItems().getBottomNavigationItems()
            .forEachIndexed { index, navigationItem ->
                val isSelected = selectedTabItem == index

                NavigationBarItem(
                    selected = isSelected,
                    alwaysShowLabel = false,
                    label = {
                        Text(
                            navigationItem.label,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    },
                    colors = finsibleNavigationBarItemColors(),
                    icon = {
                        if (!navigationItem.isNewTransactionForm)
                            Icon(
                                painter = painterResource(id = navigationItem.icon),
                                modifier = Modifier.size(24.dp),
                                contentDescription = navigationItem.label
                            )
                        else
                            NewTransactionButton(navigationItem)
                    },
                    onClick = {
                        if (selectedTabItem != index) {
                            dashboardViewModel.changeTab(index)
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
