package com.itsjeel01.finsiblefrontend.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.ui.component.BottomNavigationBar
import com.itsjeel01.finsiblefrontend.ui.navigation.HomeRoutes
import com.itsjeel01.finsiblefrontend.ui.navigation.homeNavGraph
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.HomeViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomeScreen(navigateToOnboarding: () -> Unit) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val navController = rememberNavController()

    val activeTab by homeViewModel.activeTab.collectAsStateWithLifecycle()
    val previousTab by homeViewModel.previousTab.collectAsStateWithLifecycle()

    var navigateToTab: ((Int) -> Unit)? = null

    Scaffold(
        modifier = Modifier
            .background(FinsibleTheme.colors.primaryBackground)
            .systemBarsPadding(),
        bottomBar = {
            BottomNavigationBar(
                activeTab = activeTab,
                onTabSelected = { tabIndex ->
                    navigateToTab?.invoke(tabIndex)
                    homeViewModel.updateActiveTab(tabIndex)
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeRoutes.Dashboard,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            navigateToTab = homeNavGraph(
                navController,
                onNewTransactionBackPressed = { homeViewModel.updateActiveTab(previousTab) },
                previousTab,
            )
        }
    }
}