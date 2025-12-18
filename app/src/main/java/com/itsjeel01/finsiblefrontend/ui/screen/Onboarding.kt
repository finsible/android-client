package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.logging.Logger
import com.itsjeel01.finsiblefrontend.data.di.hiltLoadingManager
import com.itsjeel01.finsiblefrontend.data.di.hiltNotificationManager
import com.itsjeel01.finsiblefrontend.ui.component.fin.ButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsiblePageIndicators
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconPosition
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.model.AuthState
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@Composable
fun Onboarding(
    navigateToHome: () -> Unit,
    onboardingViewModel: OnboardingViewModel,
    authViewModel: AuthViewModel
) {
    val inAppNotificationManager = hiltNotificationManager()
    val loadingManager = hiltLoadingManager()
    val context = LocalContext.current

    val carouselItems = remember { OnboardingViewModel.CarouselItems().get() }
    val currentItem by onboardingViewModel.currentCarouselItem.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        loadingManager.hide()

        when (authState) {
            is AuthState.Positive -> {
                Logger.UI.d("AuthState = Positive; navigating to dashboard")
                navigateToHome()
            }

            is AuthState.Loading -> {
                Logger.UI.d("AuthState = Loading")
                loadingManager.show("Please wait...")
            }

            is AuthState.Negative -> {
                val authState = authState as AuthState.Negative

                if (authState.isFailed) {
                    Logger.UI.d("Auth status: ${authState.message}")

                    inAppNotificationManager.showError(
                        title = "Authentication failed",
                        subtitle = authState.message,
                        autoDismiss = true,
                        autoDismissDelay = Duration.MS_3000,
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        OnboardingBackground()

        OnboardingContent(
            currentItem = currentItem,
            carouselItems = carouselItems,
            isLastItem = onboardingViewModel::isLastCarouselItem,
            onNextItem = onboardingViewModel::nextCarouselItem,
            onPreviousItem = onboardingViewModel::previousCarouselItem,
            onSkip = onboardingViewModel::skipToLastCarouselItem,
            onGoogleLogin = { authViewModel.signInWithGoogle(context) }
        )
    }
}

@Composable
private fun OnboardingBackground() {

    // Vertical gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandAccent40,
                        FinsibleTheme.colors.brandAccent20,
                        FinsibleTheme.colors.brandAccent10,
                        FinsibleTheme.colors.same,
                        FinsibleTheme.colors.same,
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY,
                    tileMode = TileMode.Clamp
                )
            )
    )

    // TopLeft radial gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandAccent50,
                        FinsibleTheme.colors.transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = FinsibleTheme.dimes.d800.value,
                    tileMode = TileMode.Clamp
                )
            )
    )

    // TopRight radial gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        FinsibleTheme.colors.brandAccent20,
                        FinsibleTheme.colors.transparent,
                    ),
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    radius = FinsibleTheme.dimes.d800.value,
                    tileMode = TileMode.Clamp
                )
            )
    )

    // Noise overlay
    val noise = ImageBitmap.imageResource(R.drawable.noise)
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawImage(
            image = noise,
            dstSize = IntSize(size.width.toInt(), size.height.toInt()),
            alpha = 0.25f,
        )
    }
}

@Composable
private fun OnboardingContent(
    currentItem: Int,
    carouselItems: List<OnboardingViewModel.CarouselItems>,
    isLastItem: () -> Boolean,
    onNextItem: () -> Unit,
    onPreviousItem: () -> Unit,
    onSkip: () -> Unit = {},
    onGoogleLogin: () -> Unit
) {

    val illustrationWeight = 0.7f
    val spacerBelowIllustrationWeight = 0.2f
    val spacerBelowIndicatorsWeight = 0.2f

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
                .weight(illustrationWeight)
                .padding(horizontal = FinsibleTheme.dimes.d8),
            currentItem = currentItem,
            carouselItems = carouselItems
        )

        Spacer(Modifier.weight(spacerBelowIllustrationWeight))

        OnboardingTextContent(
            currentItem = currentItem,
            carouselItems = carouselItems
        )

        Spacer(Modifier.height(FinsibleTheme.dimes.d32))

        FinsiblePageIndicators(
            Modifier.align(Alignment.CenterHorizontally),
            currentItem,
            carouselItems.size
        )

        Spacer(Modifier.weight(spacerBelowIndicatorsWeight))

        OnboardingNavigationButtons(
            currentItem = currentItem,
            isLastItem = isLastItem,
            onSecondaryButtonClick = onPreviousItem,
            onPrimaryButtonClick = if (isLastItem()) onGoogleLogin else onNextItem
        )
    }
}

@Composable
private fun OnboardingHeader(onSkip: () -> Unit = {}, isLastItem: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Finsible Logo ---

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Finsible Logo",
            modifier = Modifier.height(FinsibleTheme.dimes.d64),
            contentScale = ContentScale.Fit
        )

        // --- Skip Button ---

        if (!isLastItem) {
            FinsibleButton(
                "Skip",
                onClick = onSkip,
                config = ButtonConfig(
                    size = ComponentSize.Small,
                    type = ComponentType.Tertiary,
                    fullWidth = false
                )
            )
        }
    }
}

@Composable
private fun OnboardingIllustration(
    modifier: Modifier = Modifier,
    currentItem: Int,
    carouselItems: List<OnboardingViewModel.CarouselItems>
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
                contentDescription = "${index}th Onboarding Illustration",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun OnboardingTextContent(
    modifier: Modifier = Modifier,
    currentItem: Int,
    carouselItems: List<OnboardingViewModel.CarouselItems>
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
                        durationMillis = Duration.MS_250.toInt(),
                        delayMillis = Duration.MS_100.toInt(),
                        easing = LinearOutSlowInEasing
                    )
                ) + slideInVertically(
                    animationSpec = tween(
                        durationMillis = Duration.MS_250.toInt(),
                        delayMillis = Duration.MS_100.toInt(),
                        easing = LinearOutSlowInEasing
                    ),
                    initialOffsetY = { it / 3 }
                ) togetherWith fadeOut(
                    animationSpec = tween(
                        durationMillis = Duration.MS_150.toInt(),
                        easing = FastOutLinearInEasing
                    )
                ) + slideOutVertically(
                    animationSpec = tween(
                        durationMillis = Duration.MS_150.toInt(),
                        easing = FastOutLinearInEasing
                    ),
                    targetOffsetY = { -it / 4 }
                )
            }
        ) { index ->
            Text(
                carouselItems[index].headline,
                style = FinsibleTheme.typography.t56.bold(),
                textAlign = TextAlign.Start,
            )
        }

        Spacer(Modifier.height(FinsibleTheme.dimes.d12))

        AnimatedContent(
            targetState = currentItem,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = Duration.MS_200.toInt(),
                        delayMillis = Duration.MS_200.toInt(),
                        easing = LinearOutSlowInEasing
                    )
                ) + slideInVertically(
                    animationSpec = tween(
                        durationMillis = Duration.MS_200.toInt(),
                        delayMillis = Duration.MS_200.toInt(),
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
            val textStyle = FinsibleTheme.typography.t16

            Text(
                carouselItems[index].description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = FinsibleTheme.dimes.d2)
                    .height((textStyle.lineHeight.value.times(3)).dp),
                style = textStyle,
                color = FinsibleTheme.colors.secondaryContent,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
private fun OnboardingNavigationButtons(
    currentItem: Int,
    isLastItem: () -> Boolean,
    onSecondaryButtonClick: () -> Unit,
    onPrimaryButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d24)
    ) {
        if (currentItem > 0 && !isLastItem()) {
            FinsibleButton(
                text = "Back",
                onClick = onSecondaryButtonClick,
                modifier = Modifier.weight(1f),
                config = ButtonConfig(
                    size = ComponentSize.Medium,
                    type = ComponentType.Secondary,
                    fullWidth = true
                )
            )
        }

        val label = if (currentItem == 0) "Get started"
        else if (isLastItem()) "Continue with Google"
        else "Next"

        val icon = if (isLastItem()) R.drawable.ic_google
        else if (currentItem == 0) R.drawable.ic_right_arrow_dotted
        else null

        val iconPosition = if (isLastItem()) IconPosition.BeforeLabel else IconPosition.Trailing

        val type = if (currentItem == 0) ComponentType.Brand else ComponentType.Primary

        Column(
            Modifier
                .wrapContentHeight()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FinsibleButton(
                text = label,
                onClick = onPrimaryButtonClick,
                config = ButtonConfig(
                    size = ComponentSize.Medium,
                    icon = icon,
                    iconPosition = iconPosition,
                    type = type,
                    fullWidth = true,
                    tintIcon = !isLastItem()
                )
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d8))

            if (isLastItem()) {
                FinsibleButton(
                    modifier = Modifier.padding(vertical = FinsibleTheme.dimes.d8),
                    text = "Go back",
                    onClick = onSecondaryButtonClick,
                    config = ButtonConfig(
                        size = ComponentSize.Small,
                        type = ComponentType.Tertiary,
                        fullWidth = false
                    )
                )
            } else {
                Spacer(Modifier.height(FinsibleTheme.dimes.d48))
            }
        }
    }
}