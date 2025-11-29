package com.itsjeel01.finsiblefrontend.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes {
    @Serializable
    data object Onboarding : AppRoutes()

    @Serializable
    data object Home : AppRoutes()

    @Serializable
    data object Launch : AppRoutes()

    @Serializable
    data object Test : AppRoutes()
}
