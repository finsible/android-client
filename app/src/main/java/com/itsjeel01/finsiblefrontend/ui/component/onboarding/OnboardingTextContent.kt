package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingTextContent(
    modifier: Modifier = Modifier,
    currentItem: Int,
    carouselItems: List<OnboardingViewModel.CarouselItem>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        AnimatedContent(
            targetState = currentItem,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = FinsibleDurations.values.expandMs,
                        delayMillis = FinsibleDurations.values.focusMs,
                        easing = LinearOutSlowInEasing
                    )
                ) + slideInVertically(
                    animationSpec = tween(
                        durationMillis = FinsibleDurations.values.expandMs,
                        delayMillis = FinsibleDurations.values.focusMs,
                        easing = LinearOutSlowInEasing
                    ),
                    initialOffsetY = { it / 3 }
                ) togetherWith fadeOut(
                    animationSpec = tween(
                        durationMillis = FinsibleDurations.values.fadeMs,
                        easing = FastOutLinearInEasing
                    )
                ) + slideOutVertically(
                    animationSpec = tween(
                        durationMillis = FinsibleDurations.values.fadeMs,
                        easing = FastOutLinearInEasing
                    ),
                    targetOffsetY = { -it / 4 }
                )
            }
        ) { index ->
            FinsibleText(
                text = stringResource(carouselItems[index].headline),
                textStyle = FinsibleTheme.typography.displayXl.bold(),
                textAlign = TextAlign.Start
            )
        }

        Spacer(Modifier.height(FinsibleTheme.spacing.gapMd))

        AnimatedContent(
            targetState = currentItem,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = FinsibleDurations.values.slideMs,
                        delayMillis = FinsibleDurations.values.slideMs,
                        easing = LinearOutSlowInEasing
                    )
                ) + slideInVertically(
                    animationSpec = tween(
                        durationMillis = FinsibleDurations.values.slideMs,
                        delayMillis = FinsibleDurations.values.slideMs,
                        easing = LinearOutSlowInEasing
                    ),
                    initialOffsetY = { it / 4 }
                ) togetherWith fadeOut(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = LinearEasing
                    )
                )
            }
        ) { index ->
            val textStyle = FinsibleTheme.typography.bodyLg

            FinsibleText(
                text = stringResource(carouselItems[index].description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = FinsibleTheme.spacing.insetMicro)
                    .height((textStyle.lineHeight.value.times(3)).dp),
                textStyle = FinsibleTheme.typography.bodyLg,
                colorVariant = FinsibleTextColorVariant.Secondary,
                textAlign = TextAlign.Start,
                maxLines = Int.MAX_VALUE
            )
        }
    }
}
