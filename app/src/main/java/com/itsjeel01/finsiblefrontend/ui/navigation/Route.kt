package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Launch : Route

    @Serializable
    data object Test : Route

    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Home : Route {
        @Serializable
        data object Dashboard : Route

        @Serializable
        data object Accounts : Route

        @Serializable
        data object NewTransaction : Route {
            @Serializable
            data object Amount : Route

            @Serializable
            data object Date : Route

            @Serializable
            data object Category : Route

            @Serializable
            data object TransactionAccounts : Route

            @Serializable
            data object Description : Route
        }

        @Serializable
        data object Transactions : Route

        @Serializable
        data object Settings : Route
    }
}
