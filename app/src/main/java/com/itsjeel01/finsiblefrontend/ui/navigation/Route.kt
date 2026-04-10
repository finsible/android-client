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
    data object Playground : Route {
        @Serializable
        data object Accordion : Route

        @Serializable
        data object Button : Route

        @Serializable
        data object Checkbox : Route

        @Serializable
        data object ChipsRow : Route

        @Serializable
        data object DatePicker : Route

        @Serializable
        data object DateRangePicker : Route

        @Serializable
        data object Dropdown : Route

        @Serializable
        data object FilterChip : Route

        @Serializable
        data object IconBadge : Route

        @Serializable
        data object Loader : Route

        @Serializable
        data object MonthYearPicker : Route

        @Serializable
        data object RadioButton : Route

        @Serializable
        data object Notification : Route

        @Serializable
        data object Scrubber : Route

        @Serializable
        data object SegmentedButtons : Route

        @Serializable
        data object Text : Route

        @Serializable
        data object TextField : Route

        @Serializable
        data object TileCards : Route

        @Serializable
        data object Toggle : Route
    }

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
