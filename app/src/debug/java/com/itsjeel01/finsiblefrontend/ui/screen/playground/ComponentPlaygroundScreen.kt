package com.itsjeel01.finsiblefrontend.ui.screen.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTopNavigationBar
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleHeaderButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleHeaderState
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.entryForRoute
import com.itsjeel01.finsiblefrontend.ui.screen.playground.helper.playgroundEntries
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.medium
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
    val backButton = remember(onBack) {
        FinsibleHeaderButton(
            onClick = onBack,
            iconOnly = true,
            variant = FinsibleButtonVariant.Text,
            icon = {
                Icon(
                    painter = painterResource(com.composables.icons.materialicons.outlined.R.drawable.materialicons_ic_arrow_back_outlined),
                    contentDescription = backLabel
                )
            }
        )
    }
    val headerState = remember(title, subtitle, backButton) {
        FinsibleHeaderState(
            title = title,
            subtitle = subtitle,
            leftButtons = listOf(backButton)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FinsibleTheme.colors.surfaceBase,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FinsibleTopNavigationBar(state = headerState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(FinsibleTheme.colors.surfaceBase)
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
        ) {
            Column(modifier = Modifier.padding(top = FinsibleTheme.spacing.inlineMd)) {
                content()
            }
        }
    }
}

@Composable
private fun ComponentPlaygroundList(onSelect: (Route) -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FinsibleTheme.spacing.gapMd, vertical = FinsibleTheme.spacing.inlineMd),
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.gapLg)
    ) {
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
            .clip(RoundedCornerShape(FinsibleTheme.radius.md))
            .background(FinsibleTheme.colors.surfaceDefault)
            .border(FinsibleTheme.stroke.thin, FinsibleTheme.colors.borderSubtle, RoundedCornerShape(FinsibleTheme.radius.md))
            .clickable(role = Role.Button, onClick = onOpen)
            .padding(horizontal = FinsibleTheme.spacing.gapLg, vertical = FinsibleTheme.spacing.gapMd),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackXs)) {
            FinsibleText(
                text = title,
                textStyle = FinsibleTheme.typography.bodyMd.medium(),
                color = FinsibleTheme.colors.contentPrimary
            )
            FinsibleText(
                text = description,
                textStyle = FinsibleTheme.typography.bodySm,
                color = FinsibleTheme.colors.contentSecondary
            )
        }
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_chevron_right),
            contentDescription = null,
            tint = FinsibleTheme.colors.contentTertiary
        )
    }
}