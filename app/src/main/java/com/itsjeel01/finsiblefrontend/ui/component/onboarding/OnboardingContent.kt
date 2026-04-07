package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleScrubber
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleScrubberDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleScrubberSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleScrubberVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

private const val ILLUSTRATION_WEIGHT = 0.7f
private const val SPACER_BELOW_ILLUSTRATION_WEIGHT = 0.2f
private const val SPACER_BELOW_INDICATORS_WEIGHT = 0.2f

@Composable
fun OnboardingContent(
    currentItem: Int,
    carouselItems: List<OnboardingViewModel.CarouselItem>,
    isLastItem: () -> Boolean,
    onNextItem: () -> Unit,
    onPreviousItem: () -> Unit,
    onSkip: () -> Unit = {},
    onGoogleLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(
                horizontal = FinsibleTheme.dimes.d24,
                vertical = FinsibleTheme.dimes.d12
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingHeader(onSkip, isLastItem())

        Spacer(Modifier.height(FinsibleTheme.dimes.d32))

        OnboardingIllustration(
            modifier = Modifier
                .weight(ILLUSTRATION_WEIGHT)
                .padding(horizontal = FinsibleTheme.dimes.d8),
            currentItem = currentItem,
            carouselItems = carouselItems
        )

        Spacer(Modifier.weight(SPACER_BELOW_ILLUSTRATION_WEIGHT))

        OnboardingTextContent(
            currentItem = currentItem,
            carouselItems = carouselItems
        )

        Spacer(Modifier.height(FinsibleTheme.dimes.d32))

        FinsibleScrubber(
            currentIndex = currentItem,
            totalCount = carouselItems.size,
            onIndexChange = {},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            variant = FinsibleScrubberVariant.Separate,
            enabled = false,
            sizes = FinsibleScrubberSizes(
                activeBarWidth = FinsibleTheme.dimes.d48,
                inactiveBarWidth = FinsibleTheme.dimes.d12,
                barHeight = FinsibleTheme.dimes.d4,
                barSpacing = FinsibleTheme.dimes.d8,
                cornerRadius = FinsibleTheme.dimes.d2,
                minTouchTargetHeight = FinsibleTheme.dimes.d4
            ),
            colors = FinsibleScrubberDefaults.colors(
                currentColor = FinsibleTheme.colors.brandAccent,
                restColor = FinsibleTheme.colors.primaryContent40,
                disabledCurrentColor = FinsibleTheme.colors.brandAccent,
                disabledRestColor = FinsibleTheme.colors.primaryContent40
            )
        )

        Spacer(Modifier.weight(SPACER_BELOW_INDICATORS_WEIGHT))

        OnboardingNavigationButtons(
            currentItem = currentItem,
            isLastItem = isLastItem,
            onSecondaryButtonClick = onPreviousItem,
            onPrimaryButtonClick = if (isLastItem()) onGoogleLogin else onNextItem
        )
    }
}
