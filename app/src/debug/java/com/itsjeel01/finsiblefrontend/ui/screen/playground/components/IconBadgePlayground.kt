package com.itsjeel01.finsiblefrontend.ui.screen.playground.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleIconBadge
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.*
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun IconBadgePlayground() {
    var size by rememberSaveable { mutableStateOf(FinsibleSize.Medium) }
    var showBackground by rememberSaveable { mutableStateOf(true) }
    var alpha by rememberSaveable { mutableFloatStateOf(0.14f) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {

        FinsibleIconBadge(
            icon = { Icon(painterResource(LucideR.drawable.lucide_ic_bell), contentDescription = null) },
            size = size,
            showBackground = showBackground,
            backgroundAlpha = if (showBackground) alpha else com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleIconBadgeDefaults.DEFAULT_BACKGROUND_ALPHA,
            shapeVariant = FinsibleShape.Circle,
            contentDescription = stringResource(R.string.component_playground_iconbadge_label)
        )

        OptionDropdown(
            label = stringResource(R.string.component_playground_generic_size),
            selectedLabel = sizeLabel(size),
            options = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large),
            optionLabel = { sizeLabel(it) },
            onSelect = { size = it }
        )
        OptionToggle(
            label = stringResource(R.string.component_playground_iconbadge_background),
            checked = showBackground,
            onCheckedChange = { showBackground = it }
        )
        OptionSlider(
            label = stringResource(R.string.component_playground_iconbadge_alpha),
            value = alpha,
            valueRange = 0f .. 1f,
            steps = 10,
            enabled = showBackground,
            onValueChange = { alpha = it }
        )
    }
}


