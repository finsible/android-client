package com.itsjeel01.finsiblefrontend.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object OnboardingScreen : Routes()

    @Serializable
    data object DashboardScreen : Routes()

    @Serializable
    data object LaunchScreen : Routes()

    @Serializable
    data object HomeScreen : Routes()

    @Serializable
    data object InsightsScreen : Routes()

    @Serializable
    data object BalanceScreen : Routes()

    @Serializable
    data object SettingsScreen : Routes()

    @Serializable
    data object NewTransactionForm : Routes()
}