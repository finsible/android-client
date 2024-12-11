package com.itsjeel01.finsiblefrontend.ui.view.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.data.models.AuthState
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel

@Composable
fun LaunchScreen() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Negative -> OnboardingScreen()
        AuthState.Positive -> DashboardScreen()
        AuthState.Loading -> OnboardingScreen()
    }
}