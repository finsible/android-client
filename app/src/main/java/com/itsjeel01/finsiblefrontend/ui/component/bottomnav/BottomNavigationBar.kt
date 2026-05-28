package com.itsjeel01.finsiblefrontend.ui.component.bottomnav
import androidx.compose.ui.graphics.Color


import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.ui.navigation.BottomNavItem
import com.itsjeel01.finsiblefrontend.ui.navigation.BottomNavItems
import com.itsjeel01.finsiblefrontend.ui.navigation.Route
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.coroutines.launch

private const val SELECTED_ICON_SCALE = 1.1f
private const val UNSELECTED_ICON_SCALE = 1f
private const val SELECTED_OPACITY = 1f
private const val UNSELECTED_OPACITY = 0f
private const val PRESS_SCALE = 0.95f
private const val NORMAL_SCALE = 1f
private const val FAB_SELECTED_ELEVATION = 24f
private const val FAB_UNSELECTED_ELEVATION = 0f

@Composable
fun BottomNavigationBar(
    activeTab: Route,
    onTabSelected: (Route) -> Unit
) {
    val tabs = BottomNavItems.toMap()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
            .background(FinsibleTheme.colors.surfaceBase)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = FinsibleTheme.spacing.insetMicro),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { (route, tab) ->
                if (!tab.isFAB) {
                    StandardNavigationTab(
                        isSelected = route == activeTab,
                        tab = tab,
                        onClick = { onTabSelected(route) }
                    )
                } else {
                    CentralFABTab(
                        isSelected = route == activeTab,
                        tab = tab,
                        onClick = { onTabSelected(route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StandardNavigationTab(
    isSelected: Boolean,
    tab: BottomNavItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val specs = FinsibleTheme.animations.specs
    val bounceScale = remember { Animatable(if (isSelected) SELECTED_ICON_SCALE else UNSELECTED_ICON_SCALE) }
    val dotOpacity = remember { Animatable(if (isSelected) SELECTED_OPACITY else UNSELECTED_OPACITY) }
    val pressScale = remember { Animatable(NORMAL_SCALE) }
    var hasBeenInteracted by remember { mutableStateOf(false) }

    LaunchedEffect(isSelected) {
        if (hasBeenInteracted) {
            launch {
                bounceScale.animateTo(
                    targetValue = if (isSelected) SELECTED_ICON_SCALE else UNSELECTED_ICON_SCALE,
                    animationSpec = specs.springBouncy
                )
            }
            launch {
                dotOpacity.animateTo(
                    targetValue = if (isSelected) SELECTED_OPACITY else UNSELECTED_OPACITY,
                    animationSpec = specs.springGentle
                )
            }
        } else {
            bounceScale.snapTo(if (isSelected) SELECTED_ICON_SCALE else UNSELECTED_ICON_SCALE)
            dotOpacity.snapTo(if (isSelected) SELECTED_OPACITY else UNSELECTED_OPACITY)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMicro)
    ) {
        Box(
            modifier = Modifier
                .background(
                    shape = CircleShape,
                    color = Color.Transparent
                )
                .size(FinsibleTheme.sizes.touch.xl)
                .scale(bounceScale.value * pressScale.value)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(
                        bounded = false,
                        color = FinsibleTheme.colors.borderSubtle,
                        radius = FinsibleTheme.spacing.inset2xl
                    )
                ) {
                    hasBeenInteracted = true
                    coroutineScope.launch {
                        pressScale.animateTo(PRESS_SCALE, specs.pressSpring)
                        pressScale.animateTo(NORMAL_SCALE, specs.releaseSpring)
                    }
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (isSelected) tab.activeIcon else tab.inactiveIcon),
                contentDescription = stringResource(tab.labelRes),
                tint = if (isSelected) FinsibleTheme.colors.contentPrimary else FinsibleTheme.colors.contentSecondary,
                modifier = Modifier.size(FinsibleTheme.spacing.inset2xl)
            )
        }

        Box(
            modifier = Modifier
                .size(FinsibleTheme.spacing.insetXs)
                .graphicsLayer(alpha = dotOpacity.value)
                .background(
                    color = FinsibleTheme.colors.contentPrimary,
                    shape = CircleShape
                )
        )
    }
}

@Composable
private fun CentralFABTab(
    isSelected: Boolean,
    tab: BottomNavItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val specs = FinsibleTheme.animations.specs
    val selectionScale = remember {
        Animatable(
            if (isSelected) SELECTED_ICON_SCALE
            else NORMAL_SCALE
        )
    }
    val shadowElevation = remember {
        Animatable(
            if (isSelected) FAB_SELECTED_ELEVATION
            else FAB_UNSELECTED_ELEVATION
        )
    }

    var hasBeenInteracted by remember { mutableStateOf(false) }

    LaunchedEffect(isSelected) {
        if (hasBeenInteracted) {
            launch {
                selectionScale.animateTo(
                    targetValue = if (isSelected) SELECTED_ICON_SCALE else NORMAL_SCALE,
                    animationSpec = specs.springBouncy
                )
            }
            launch {
                shadowElevation.animateTo(
                    targetValue = if (isSelected) FAB_SELECTED_ELEVATION else FAB_UNSELECTED_ELEVATION,
                    animationSpec = specs.springGentle
                )
            }
        } else {
            selectionScale.snapTo(if (isSelected) SELECTED_ICON_SCALE else NORMAL_SCALE)
            shadowElevation.snapTo(if (isSelected) FAB_SELECTED_ELEVATION else FAB_UNSELECTED_ELEVATION)
        }
    }

    Box(
        modifier = modifier
            .size(FinsibleTheme.sizes.touch.md)
            .graphicsLayer(
                scaleX = selectionScale.value,
                scaleY = selectionScale.value,
                shadowElevation = shadowElevation.value,
                shape = CircleShape,
                spotShadowColor = FinsibleTheme.colors.contentPrimary,
                ambientShadowColor = FinsibleTheme.colors.contentPrimary
            )
            .background(
                color = if (isSelected) FinsibleTheme.colors.contentPrimary else FinsibleTheme.colors.contentSecondary,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, color = FinsibleTheme.colors.borderSubtle)
            ) {
                hasBeenInteracted = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = tab.activeIcon),
            contentDescription = stringResource(tab.labelRes),
            tint = FinsibleTheme.colors.surfaceBase,
            modifier = Modifier.size(FinsibleTheme.spacing.inset2xl)
        )
    }
}