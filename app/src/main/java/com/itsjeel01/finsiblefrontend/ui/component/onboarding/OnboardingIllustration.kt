package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingIllustration(
    modifier: Modifier = Modifier,
    currentItem: Int,
    carouselItems: List<OnboardingViewModel.CarouselItem>
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = currentItem,
            animationSpec = tween(
                durationMillis = Duration.MS_350.toInt(),
                easing = FastOutSlowInEasing
            )
        ) { index ->
            Image(
                painter = painterResource(id = carouselItems[index].illustration),
                contentDescription = stringResource(R.string.cd_onboarding_illustration, index),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}
