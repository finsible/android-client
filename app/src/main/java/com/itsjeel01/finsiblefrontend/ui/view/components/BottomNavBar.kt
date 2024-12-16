package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    val showModalSheet by dashboardViewModel.showModalSheetState.collectAsState()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        var isSelected: Boolean
        BottomNavigationItems().getBottomNavigationItems()
            .forEachIndexed { index, navigationItem ->
                isSelected = selectedTabItem == index

                NavigationBarItem(
                    selected = isSelected,
                    alwaysShowLabel = true,
                    label = {
                        val labelFontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        Text(navigationItem.label, fontWeight = labelFontWeight)
                    },
                    colors = NavigationBarItemColors(
                        selectedIndicatorColor = MaterialTheme.colorScheme.background,
                        unselectedIconColor = MaterialTheme.colorScheme.outline,
                        unselectedTextColor = MaterialTheme.colorScheme.outline,
                        disabledIconColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.outline,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    icon = {
                        if (navigationItem.opensScreen)
                            Icon(
                                painter = painterResource(id = navigationItem.icon),
                                modifier = Modifier.size(24.dp),
                                contentDescription = navigationItem.label
                            )
                        else
                            NewTransactionButton(navigationItem)
                    },
                    onClick = {
                        if (navigationItem.opensScreen && !isSelected) {
                            dashboardViewModel.changeTab(index)
                            navController.navigate(navigationItem.route) {
                                popUpTo(Routes.HomeScreen) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            dashboardViewModel.showModalSheet()
                        }
                    }
                )
            }
    }

    if (showModalSheet) {
        NewTransactionForm(dashboardViewModel)
    }
}
