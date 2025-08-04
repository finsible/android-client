package com.itsjeel01.finsiblefrontend.ui.component

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itsjeel01.finsiblefrontend.ui.data.NavBarItem
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.size
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleNavigationBarItemColors
import com.itsjeel01.finsiblefrontend.ui.viewmodel.DashboardViewModel

@Composable
fun NavigationBar(navController: NavHostController) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val selectedTab = dashboardViewModel.tabIdx.collectAsState().value

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        NavBarItem().getItems().forEachIndexed { index, navigationItem ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                colors = finsibleNavigationBarItemColors(),
                selected = isSelected,
                alwaysShowLabel = false,
                label = {
                    Text(
                        navigationItem.label,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                icon = {
                    if (!navigationItem.isNewTransactionForm)
                        Icon(
                            painter = painterResource(id = navigationItem.icon),
                            modifier = Modifier.size(Size.S24),
                            contentDescription = navigationItem.label
                        )
                    else
                        NewTransactionButton(navigationItem)
                },
                onClick = {
                    if (selectedTab != index) {
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
