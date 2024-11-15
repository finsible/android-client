package com.itsjeel01.finsiblefrontend.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object AuthScreen : Routes()

    @Serializable
    data object DashboardScreen : Routes()
}