package com.itsjeel01.finsiblefrontend.ui.screen.playground.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.DebugTitleBar
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.composables.icons.lucide.R as LucideR

@Composable
fun ComponentPlaygroundListScreen(
    onBack: () -> Unit,
    onSelect: (Route) -> Unit
) {
    PlaygroundScaffold(
        title = stringResource(R.string.component_playground_title),
        onBack = onBack,
        backLabel = stringResource(R.string.component_playground_back_to_test),
        subtitle = stringResource(R.string.component_playground_list_subtitle)
    ) {
        ComponentPlaygroundList(onSelect = onSelect)
    }
}

@Composable
fun ComponentPlaygroundEntryScreen(route: Route, onBack: () -> Unit) {
    val entry = entryForRoute(route) ?: return
    PlaygroundScaffold(
        title = stringResource(entry.titleRes),
        onBack = onBack,
        backLabel = stringResource(R.string.component_playground_back_to_list),
        subtitle = stringResource(entry.descriptionRes)
    ) {
        entry.content()
    }
}

@Composable
private fun PlaygroundScaffold(
    title: String,
    onBack: () -> Unit,
    backLabel: String,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()

    Column(
        modifier = Modifier
            .background(FinsibleTheme.colors.primaryBackground)
            .padding(safePadding)
    ) {
        DebugTitleBar(
            title = title,
            subtitle = subtitle,
            onBack = onBack,
            backLabel = backLabel,
            subtitleColor = FinsibleTheme.colors.brandAccent
        )
        content()
    }
}

@Composable
private fun ComponentPlaygroundList(onSelect: (Route) -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.dimes.d12, vertical = FinsibleTheme.dimes.d8),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        FinsibleText(
            text = stringResource(R.string.component_playground_list_title),
            variant = FinsibleTextVariant.SmallBodyMedium,
            color = FinsibleTheme.colors.primaryContent
        )

        playgroundEntries.forEach { entry ->
            ComponentEntry(
                title = stringResource(entry.titleRes),
                description = stringResource(entry.descriptionRes)
            ) {
                onSelect(entry.route)
            }
        }
    }
}

@Composable
private fun ComponentEntry(title: String, description: String, onOpen: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FinsibleTheme.dimes.d12))
            .background(FinsibleTheme.colors.secondaryBackground)
            .border(FinsibleTheme.dimes.d1, FinsibleTheme.colors.divider, RoundedCornerShape(FinsibleTheme.dimes.d12))
            .clickable(role = Role.Button, onClick = onOpen)
            .padding(horizontal = FinsibleTheme.dimes.d10, vertical = FinsibleTheme.dimes.d8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d2)) {
            FinsibleText(
                text = title,
                variant = FinsibleTextVariant.SmallBodyMedium,
                color = FinsibleTheme.colors.primaryContent
            )
            FinsibleText(
                text = description,
                variant = FinsibleTextVariant.SmallLabelRegular,
                color = FinsibleTheme.colors.secondaryContent
            )
        }
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_chevron_right),
            contentDescription = null,
            tint = FinsibleTheme.colors.tertiaryContent
        )
    }
}

