package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itsjeel01.finsiblefrontend.ui.data.AuthState
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel

@Composable
fun LaunchScreen(navHostController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Negative -> OnboardingScreen(navHostController)
        AuthState.Positive -> DashboardScreen(navHostController)
        AuthState.Loading -> OnboardingScreen(navHostController)
    }
}
