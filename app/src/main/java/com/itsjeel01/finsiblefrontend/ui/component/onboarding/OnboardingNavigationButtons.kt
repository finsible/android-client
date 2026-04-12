package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun OnboardingNavigationButtons(
    currentItem: Int,
    isLastItem: () -> Boolean,
    onSecondaryButtonClick: () -> Unit,
    onPrimaryButtonClick: () -> Unit
) {
    AnimatedContent(
        targetState = isLastItem(),
        transitionSpec = {
            fadeIn(animationSpec = tween(180)) togetherWith
                    fadeOut(animationSpec = tween(120))
        },
        label = "onboarding_nav_buttons"
    ) { targetIsLastItem ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d24)
        ) {
            if (currentItem > 0 && !targetIsLastItem) {
                FinsibleButton(
                    text = stringResource(R.string.back),
                    onClick = onSecondaryButtonClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(FinsibleTheme.dimes.d48),
                    fullWidth = true,
                    variant = FinsibleButtonVariant.Outlined,
                    size = FinsibleSize.Medium,
                    shapeVariant = FinsibleShape.Rounded
                )
            }

            val label = if (currentItem == 0) stringResource(R.string.get_started)
            else if (targetIsLastItem) stringResource(R.string.continue_with_google)
            else stringResource(R.string.next)

            val iconRes = if (targetIsLastItem) R.drawable.ic_google
            else if (currentItem == 0) com.composables.icons.lucide.R.drawable.lucide_ic_arrow_right
            else null

            val iconPosition = when {
                iconRes == null -> FinsibleIconPosition.Leading
                targetIsLastItem -> FinsibleIconPosition.Leading
                else -> FinsibleIconPosition.Trailing
            }

            val isFirstItem = currentItem == 0
            val primaryColors = if (isFirstItem) {
                FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Filled,
                    containerColor = FinsibleTheme.colors.brandAccent,
                    contentColor = FinsibleTheme.colors.primaryBackground
                )
            } else {
                FinsibleButtonDefaults.colors(variant = FinsibleButtonVariant.Filled)
            }

            val primaryIcon: (@Composable () -> Unit)? = iconRes?.let { resId ->
                {
                    Icon(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        tint = if (targetIsLastItem) Color.Unspecified else LocalContentColor.current
                    )
                }
            }

            Column(
                Modifier
                    .wrapContentHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FinsibleButton(
                    text = label,
                    onClick = onPrimaryButtonClick,
                    modifier = Modifier.height(FinsibleTheme.dimes.d48),
                    fullWidth = true,
                    variant = FinsibleButtonVariant.Filled,
                    size = FinsibleSize.Medium,
                    shapeVariant = FinsibleShape.Rounded,
                    colors = primaryColors,
                    icon = primaryIcon,
                    iconPosition = iconPosition
                )
                Spacer(Modifier.height(FinsibleTheme.dimes.d8))

                if (targetIsLastItem) {
                    FinsibleButton(
                        text = stringResource(R.string.go_back),
                        onClick = onSecondaryButtonClick,
                        variant = FinsibleButtonVariant.Text,
                        size = FinsibleSize.Small,
                        shapeVariant = FinsibleShape.Rounded,
                        colors = FinsibleButtonDefaults.colors(
                            variant = FinsibleButtonVariant.Text,
                            contentColor = FinsibleTheme.colors.secondaryContent
                        )
                    )
                } else {
                    Spacer(Modifier.height(FinsibleTheme.dimes.d48))
                }
            }
        }
    }
}
