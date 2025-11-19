package com.itsjeel01.finsiblefrontend.ui.navigation

import kotlinx.serialization.Serializable

sealed class HomeRoutes {
    @Serializable
    data object Dashboard : HomeRoutes()

    @Serializable
    data object Analytics : HomeRoutes()

    @Serializable
    data object NewTransaction : HomeRoutes()

    @Serializable
    data object Accounts : HomeRoutes()

    @Serializable
    data object Settings : HomeRoutes()
}
