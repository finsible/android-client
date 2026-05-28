package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleDropdown
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleDropdownOption
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.*
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun DropdownPlayground() {
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var shape by rememberSaveable { mutableStateOf(FinsibleShape.Rounded) }
    var fullWidth by rememberSaveable { mutableStateOf(true) }

    val options = listOf(
        FinsibleDropdownOption(
            id = "alpha",
            label = stringResource(R.string.component_playground_option_alpha),
            icon = { Icon(painterResource(LucideR.drawable.lucide_ic_sparkles), contentDescription = null) }
        ),
        FinsibleDropdownOption(
            id = "beta",
            label = stringResource(R.string.component_playground_option_beta),
            icon = { Icon(painterResource(LucideR.drawable.lucide_ic_circle), contentDescription = null) }
        ),
        FinsibleDropdownOption(
            id = "gamma",
            label = stringResource(R.string.component_playground_option_gamma),
            icon = null
        )
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.spacing.insetLg, vertical = FinsibleTheme.spacing.gapMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd)
    ) {
        FinsibleDropdown(
            options = options,
            selectedId = selectedId,
            onSelected = { selectedId = it },
            placeholder = stringResource(R.string.component_playground_dropdown_placeholder),
            enabled = enabled,
            size = size,
            shapeVariant = shape,
            fullWidth = fullWidth
        )

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = FinsibleSize.entries,
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_shape),
            selectedLabel = shapeLabel(shape),
            options = listOf(FinsibleShape.Rounded, FinsibleShape.Sharp, FinsibleShape.Pill),
            optionLabel = { shapeLabel(it) },
            onSelect = { shape = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_dropdown_fullwidth),
            checked = fullWidth,
            onCheckedChange = { fullWidth = it }
        )
    }
}


