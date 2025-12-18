package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
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

    val shadowHeight = FinsibleTheme.dimes.d8
    val shadowColor = FinsibleTheme.colors.shadow

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .background(FinsibleTheme.colors.primaryBackground)
            .drawBehind {
                val shadowHeight = shadowHeight.toPx()

                repeat(shadowHeight.toInt()) { i ->
                    val distance = i.toFloat()
                    val normalizedDistance = distance / shadowHeight

                    val alpha = kotlin.math.exp(-normalizedDistance * 4) * 0.06f

                    drawLine(
                        color = shadowColor.copy(alpha = alpha),
                        start = Offset(0f, -distance),
                        end = Offset(size.width, -distance),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = FinsibleTheme.dimes.d6),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { (index, tab) ->
                if (!tab.isFAB) {
                    StandardNavigationTab(
                        isSelected = index == activeTab,
                        tab = tab,
                        onClick = { onTabSelected(index) }
                    )
                } else {
                    CentralFABTab(
                        isSelected = index == activeTab,
                        tab = tab,
                        onClick = { onTabSelected(index) }
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

    val bounceScale = remember { Animatable(if (isSelected) SELECTED_ICON_SCALE else UNSELECTED_ICON_SCALE) }
    val dotOpacity = remember { Animatable(if (isSelected) SELECTED_OPACITY else UNSELECTED_OPACITY) }
    val pressScale = remember { Animatable(NORMAL_SCALE) }
    var hasBeenInteracted by remember { mutableStateOf(false) }

    LaunchedEffect(isSelected) {
        if (hasBeenInteracted) {
            launch {
                bounceScale.animateTo(
                    targetValue = if (isSelected) SELECTED_ICON_SCALE else UNSELECTED_ICON_SCALE,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                dotOpacity.animateTo(
                    targetValue = if (isSelected) SELECTED_OPACITY else UNSELECTED_OPACITY,
                    animationSpec = tween(
                        durationMillis = Duration.MS_150.toInt(),
                        easing = EaseInOut
                    )
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
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    shape = CircleShape,
                    color = FinsibleTheme.colors.transparent
                )
                .size(FinsibleTheme.dimes.d56)
                .scale(bounceScale.value * pressScale.value)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(
                        bounded = false,
                        color = FinsibleTheme.colors.ripple,
                        radius = FinsibleTheme.dimes.d24
                    )
                ) {
                    hasBeenInteracted = true
                    coroutineScope.launch {
                        pressScale.animateTo(PRESS_SCALE, tween(Duration.MS_75.toInt(), easing = EaseOut))
                        pressScale.animateTo(NORMAL_SCALE, tween(Duration.MS_150.toInt(), easing = EaseOut))
                    }
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = if (isSelected) tab.activeIcon else tab.inactiveIcon),
                contentDescription = tab.label,
                tint = if (isSelected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.primaryContent80,
                modifier = Modifier.size(FinsibleTheme.dimes.d24)
            )
        }

        Box(
            modifier = Modifier
                .size(FinsibleTheme.dimes.d4)
                .graphicsLayer(alpha = dotOpacity.value)
                .background(
                    color = FinsibleTheme.colors.primaryContent,
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
                    animationSpec = tween(
                        durationMillis = Duration.MS_150.toInt(),
                        easing = EaseInOut
                    )
                )
            }
            launch {
                shadowElevation.animateTo(
                    targetValue = if (isSelected) FAB_SELECTED_ELEVATION else FAB_UNSELECTED_ELEVATION,
                    animationSpec = tween(
                        durationMillis = Duration.MS_150.toInt(),
                        easing = EaseInOut
                    )
                )
            }
        } else {
            selectionScale.snapTo(if (isSelected) SELECTED_ICON_SCALE else NORMAL_SCALE)
            shadowElevation.snapTo(if (isSelected) FAB_SELECTED_ELEVATION else FAB_UNSELECTED_ELEVATION)
        }
    }

    Box(
        modifier = modifier
            .size(FinsibleTheme.dimes.d48)
            .graphicsLayer(
                scaleX = selectionScale.value,
                scaleY = selectionScale.value,
                shadowElevation = shadowElevation.value,
                shape = CircleShape,
                spotShadowColor = FinsibleTheme.colors.primaryContent,
                ambientShadowColor = FinsibleTheme.colors.primaryContent
            )
            .background(
                color = if (isSelected) FinsibleTheme.colors.primaryContent else FinsibleTheme.colors.primaryContent80,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, color = FinsibleTheme.colors.ripple)
            ) {
                hasBeenInteracted = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = tab.activeIcon),
            contentDescription = tab.label,
            tint = FinsibleTheme.colors.primaryBackground,
            modifier = Modifier.size(FinsibleTheme.dimes.d24)
        )
    }
}