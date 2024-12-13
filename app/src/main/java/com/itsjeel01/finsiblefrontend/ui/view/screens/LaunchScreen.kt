package com.itsjeel01.finsiblefrontend.ui.view.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.data.models.AuthState
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel

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