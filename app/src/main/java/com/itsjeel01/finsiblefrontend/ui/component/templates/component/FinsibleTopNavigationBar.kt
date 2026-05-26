package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleHeaderDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleHeaderButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleHeaderState
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.finsibleShadow

@Composable
fun FinsibleTopNavigationBar(
    state: FinsibleHeaderState,
    modifier: Modifier = Modifier,
) {
    val targetColor = state.backgroundColor ?: FinsibleHeaderDefaults.backgroundColor()
    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "header_bg_color"
    )

    val animationSpec = FinsibleTheme.animations.specs.springFadeTransition()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .finsibleShadow(
                shadow = FinsibleTheme.elevation.raisedShadow,
                shape = RectangleShape
            )
            .background(animatedBackgroundColor)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(FinsibleHeaderDefaults.contentHeight())
                .padding(horizontal = FinsibleHeaderDefaults.horizontalPadding()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left buttons
            if (state.leftButtons.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FinsibleHeaderDefaults.contentSpacing()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.leftButtons.forEach { button ->
                        FinsibleButton(
                            onClick = button.onClick,
                            text = button.text,
                            enabled = button.enabled,
                            loading = button.loading,
                            iconOnly = button.iconOnly,
                            variant = button.variant,
                            size = button.size,
                            shapeVariant = button.shapeVariant,
                            badgeType = button.badgeType,
                            badgeCount = button.badgeCount,
                            icon = button.icon,
                            iconPosition = button.iconPosition,
                        )
                    }
                }
                Spacer(Modifier.width(FinsibleHeaderDefaults.contentSpacing()))
            }

            // Title area
            AnimatedContent(
                targetState = state.title,
                transitionSpec = { animationSpec },
                label = "header_title",
                modifier = Modifier.weight(1f)
            ) { title ->
                Column {
                    FinsibleText(
                        text = title,
                        textStyle = FinsibleTheme.typography.bodyLg.bold(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    state.subtitle?.let { subtitle ->
                        FinsibleText(
                            text = subtitle,
                            textStyle = FinsibleTheme.typography.bodySm,
                            colorVariant = FinsibleTextColorVariant.Secondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Right buttons
            if (state.rightButtons.isNotEmpty()) {
                Spacer(Modifier.width(FinsibleHeaderDefaults.contentSpacing()))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FinsibleHeaderDefaults.contentSpacing()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.rightButtons.forEach { button ->
                        FinsibleButton(
                            onClick = button.onClick,
                            text = button.text,
                            enabled = button.enabled,
                            loading = button.loading,
                            iconOnly = button.iconOnly,
                            variant = button.variant,
                            size = button.size,
                            shapeVariant = button.shapeVariant,
                            badgeType = button.badgeType,
                            badgeCount = button.badgeCount,
                            icon = button.icon,
                            iconPosition = button.iconPosition,
                        )
                    }
                }
            }
        }
    }
}
