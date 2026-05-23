package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleToggle
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleToggleLabelPosition
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionDropdown
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.OptionToggle
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.iconPositionLabel
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.sizeLabel
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.toggleArrangementLabel
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.toggleLabelPositionLabel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun TogglePlayground() {
    var checked by rememberSaveable { mutableStateOf(true) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var withIcon by rememberSaveable { mutableStateOf(false) }
    var labelPosition by rememberSaveable { mutableStateOf(FinsibleToggleLabelPosition.Trailing) }
    var labelIconPosition by rememberSaveable { mutableStateOf(FinsibleIconPosition.Leading) }
    var arrangement by rememberSaveable { mutableStateOf(FinsibleToggleArrangement.Attached) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.gapMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd)
    ) {
        FinsibleToggle(
            checked = checked,
            onCheckedChange = { checked = it },
            label = stringResource(R.string.component_playground_toggle_label),
            enabled = enabled,
            size = size,
            labelIcon = if (withIcon) {
                { androidx.compose.material3.Icon(painterResource(LucideR.drawable.lucide_ic_bell), contentDescription = null) }
            } else null,
            labelPosition = labelPosition,
            labelIconPosition = labelIconPosition,
            arrangement = arrangement
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_generic_selected),
            checked = checked,
            onCheckedChange = { checked = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large),
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_show_icons),
            checked = withIcon,
            onCheckedChange = { withIcon = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_toggle_label_position),
            selectedLabel = toggleLabelPositionLabel(labelPosition),
            options = FinsibleToggleLabelPosition.entries,
            optionLabel = { toggleLabelPositionLabel(it) },
            onSelect = { labelPosition = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_toggle_label_icon_position),
            selectedLabel = iconPositionLabel(labelIconPosition),
            options = FinsibleIconPosition.entries,
            optionLabel = { iconPositionLabel(it) },
            enabled = withIcon,
            onSelect = { labelIconPosition = it }
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_toggle_arrangement),
            selectedLabel = toggleArrangementLabel(arrangement),
            options = FinsibleToggleArrangement.entries,
            optionLabel = { toggleArrangementLabel(it) },
            onSelect = { arrangement = it }
        )
    }
}

