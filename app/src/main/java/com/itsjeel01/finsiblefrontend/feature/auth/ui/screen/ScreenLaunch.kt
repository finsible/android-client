package com.itsjeel01.finsiblefrontend.feature.auth.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.data.model.AuthState
import com.itsjeel01.finsiblefrontend.feature.auth.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.feature.dashboard.ui.screen.DashboardScreen

@Composable
fun LaunchScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Negative -> OnboardingScreen(navController)
        AuthState.Positive -> DashboardScreen(rememberNavController())
        AuthState.Loading -> OnboardingScreen(navController)
    }
}