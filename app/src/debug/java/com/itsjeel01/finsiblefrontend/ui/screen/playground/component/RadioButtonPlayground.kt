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
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleRadioButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.*
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun RadioButtonPlayground() {
    var selectedId by rememberSaveable { mutableStateOf("a") }
    var enabled by rememberSaveable { mutableStateOf(true) }
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var withIcons by rememberSaveable { mutableStateOf(true) }
    val allowedSizes = listOf(FinsibleSize.Small, FinsibleSize.Medium)

    val options = listOf(
        "a" to stringResource(R.string.component_playground_option_alpha),
        "b" to stringResource(R.string.component_playground_option_beta),
        "c" to stringResource(R.string.component_playground_option_gamma)
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
    ) {
        options.forEach { (id, label) ->
            FinsibleRadioButton(
                selected = id == selectedId,
                onSelectedChange = { if (it) selectedId = id },
                label = label,
                enabled = enabled,
                size = size,
                icon = if (withIcons) {
                    { Icon(painterResource(LucideR.drawable.lucide_ic_check), contentDescription = null) }
                } else null
            )
        }

        OptionToggle(
            label = stringResource(R.string.component_playground_generic_enabled),
            checked = enabled,
            onCheckedChange = { enabled = it }
        )
        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = allowedSizes,
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_generic_show_icons),
            checked = withIcons,
            onCheckedChange = { withIcons = it }
        )
    }
}

