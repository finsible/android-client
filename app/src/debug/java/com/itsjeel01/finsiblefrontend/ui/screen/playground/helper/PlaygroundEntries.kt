package com.itsjeel01.finsiblefrontend.ui.screen.playground.helper

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.AccordionPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.ButtonPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.CheckboxPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.ChipsPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.DatePickerPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.DateRangePickerPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.DropdownPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.FilterChipPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.IconBadgePlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.MonthYearPickerPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.RadioButtonPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.ScrubberPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.SegmentedButtonsPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.TextFieldPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.TextPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.TileCardsPlayground
import com.itsjeel01.finsiblefrontend.ui.screen.playground.component.TogglePlayground

data class PlaygroundEntry(
    val route: Route,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val content: @Composable () -> Unit
)

val playgroundEntries = listOf(
    PlaygroundEntry(
        route = Route.Playground.Accordion,
        titleRes = R.string.component_playground_accordion_name,
        descriptionRes = R.string.component_playground_accordion_summary,
        content = { AccordionPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.Button,
        titleRes = R.string.component_playground_button_name,
        descriptionRes = R.string.component_playground_button_summary,
        content = { ButtonPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.Checkbox,
        titleRes = R.string.component_playground_checkbox_name,
        descriptionRes = R.string.component_playground_checkbox_summary,
        content = { CheckboxPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.ChipsRow,
        titleRes = R.string.component_playground_chips_name,
        descriptionRes = R.string.component_playground_chips_summary,
        content = { ChipsPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.DatePicker,
        titleRes = R.string.component_playground_datepicker_name,
        descriptionRes = R.string.component_playground_datepicker_summary,
        content = { DatePickerPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.DateRangePicker,
        titleRes = R.string.component_playground_daterange_name,
        descriptionRes = R.string.component_playground_daterange_summary,
        content = { DateRangePickerPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.Dropdown,
        titleRes = R.string.component_playground_dropdown_name,
        descriptionRes = R.string.component_playground_dropdown_summary,
        content = { DropdownPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.FilterChip,
        titleRes = R.string.component_playground_filterchip_name,
        descriptionRes = R.string.component_playground_filterchip_summary,
        content = { FilterChipPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.IconBadge,
        titleRes = R.string.component_playground_iconbadge_name,
        descriptionRes = R.string.component_playground_iconbadge_summary,
        content = { IconBadgePlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.MonthYearPicker,
        titleRes = R.string.component_playground_monthyearpicker_name,
        descriptionRes = R.string.component_playground_monthyearpicker_summary,
        content = { MonthYearPickerPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.RadioButton,
        titleRes = R.string.component_playground_radiobutton_name,
        descriptionRes = R.string.component_playground_radiobutton_summary,
        content = { RadioButtonPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.Scrubber,
        titleRes = R.string.component_playground_scrubber_name,
        descriptionRes = R.string.component_playground_scrubber_summary,
        content = { ScrubberPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.SegmentedButtons,
        titleRes = R.string.component_playground_segmentedbuttons_name,
        descriptionRes = R.string.component_playground_segmentedbuttons_summary,
        content = { SegmentedButtonsPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.Text,
        titleRes = R.string.component_playground_text_name,
        descriptionRes = R.string.component_playground_text_summary,
        content = { TextPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.TextField,
        titleRes = R.string.component_playground_textfield_name,
        descriptionRes = R.string.component_playground_textfield_summary,
        content = { TextFieldPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.TileCards,
        titleRes = R.string.component_playground_tilecards_name,
        descriptionRes = R.string.component_playground_tilecards_summary,
        content = { TileCardsPlayground() }
    ),
    PlaygroundEntry(
        route = Route.Playground.Toggle,
        titleRes = R.string.component_playground_toggle_name,
        descriptionRes = R.string.component_playground_toggle_summary,
        content = { TogglePlayground() }
    )
)

fun entryForRoute(route: Route): PlaygroundEntry? = playgroundEntries.firstOrNull { it.route == route }

