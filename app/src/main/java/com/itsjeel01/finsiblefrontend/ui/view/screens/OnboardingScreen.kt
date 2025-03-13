package com.itsjeel01.finsiblefrontend.ui.view.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.client.OnboardingData
import com.itsjeel01.finsiblefrontend.data.models.AuthState
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import com.itsjeel01.finsiblefrontend.ui.view.components.ButtonSize
import com.itsjeel01.finsiblefrontend.ui.view.components.ButtonStyle
import com.itsjeel01.finsiblefrontend.ui.view.components.ButtonVariant
import com.itsjeel01.finsiblefrontend.ui.view.components.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.view.components.IconPosition
import com.itsjeel01.finsiblefrontend.ui.view.components.OnboardingIndicators
import com.itsjeel01.finsiblefrontend.ui.view.components.RippleLoadingIndicator
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel
import com.itsjeel01.finsiblefrontend.utils.AppConstants
import com.itsjeel01.finsiblefrontend.utils.signInWithGoogle

@Composable
fun OnboardingScreen(navController: NavHostController) {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val slides = OnboardingData().getOnboardingData()
    val currentSlide = onboardingViewModel.currentSlide.collectAsState().value
    val authState = authViewModel.authState.collectAsState().value

    val (fwdNavigationLabel, iconDrawable, iconPosition) = when {
        currentSlide == 0 -> Triple("Get started", R.drawable.arrow_right_icon, IconPosition.EndOfButton)
        currentSlide < slides.lastIndex -> Triple("Next", null, IconPosition.EndOfLabel)
        else -> Triple("Sign In with Google", R.drawable.piggy_bank_icon, IconPosition.StartOfLabel)
    }

    val transition = updateTransition(targetState = currentSlide, label = "Text Transition")

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Positive -> {
                navController.navigate(Routes.DashboardScreen)
            }

            AuthState.Loading -> {
                Log.d("OnboardingScreen", "Loading...")
            }

            is AuthState.Negative -> {
                if ((authState as AuthState.Negative).isFailed) Log.d("OnboardingScreen", "Error: ${authState.message}")
            }
        }
    }

    fun forwardNavigation() {
        if (currentSlide < slides.lastIndex) {
            onboardingViewModel.nextSlide()
        } else {
            signInWithGoogle(context, coroutineScope, authViewModel)
        }
    }

    fun backwardNavigation() {
        onboardingViewModel.previousSlide()
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val backgroundGradient = Brush.linearGradient(
        listOf(
            getCustomColor(CustomColorKey.OnboardingGradientColor2),
            getCustomColor(CustomColorKey.OnboardingGradientColor1),
            getCustomColor(CustomColorKey.OnboardingGradientColor2)
        ),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.ime,
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            // SCREEN CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundGradient)
                    .padding(horizontal = 32.dp, vertical = screenHeight * 0.1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Fixed Image Container (Prevents Content Jumping)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.3f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Crossfade(
                        targetState = currentSlide,
                        animationSpec = tween(AppConstants.ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
                    ) { slide ->
                        Image(
                            painter = painterResource(id = slides[slide].illustration),
                            contentDescription = "Onboarding Illustration",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Headline and Description
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Headline
                    AnimatedContent(
                        targetState = currentSlide,
                        transitionSpec = {
                            (fadeIn(
                                tween(
                                    AppConstants.ANIMATION_DURATION_VERY_SHORT,
                                    delayMillis = AppConstants.ANIMATION_DURATION_VERY_SHORT
                                )
                            ) + scaleIn(
                                tween(
                                    durationMillis = AppConstants.ANIMATION_DURATION_SHORT,
                                    delayMillis = AppConstants.ANIMATION_DURATION_VERY_SHORT * 2,
                                ),
                                initialScale = 0.9f
                            ))
                                .togetherWith(fadeOut(tween(AppConstants.ANIMATION_DURATION_VERY_SHORT)))
                                .using(SizeTransform(clip = false))
                        }
                    ) { targetSlide ->
                        Text(
                            slides[targetSlide].headline,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // Description with fixed height
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((4 * MaterialTheme.typography.bodyLarge.lineHeight.value).dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Crossfade(
                            targetState = currentSlide,
                            animationSpec = tween(AppConstants.ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
                        ) { slide ->
                            Text(
                                slides[slide].description,
                                modifier = Modifier.fillMaxHeight(),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Indicators
                OnboardingIndicators(currentSlide, slides.size)

                Spacer(modifier = Modifier.weight(1f))

                // Navigation Buttons
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    // Backward Navigation Button
                    if (currentSlide > 0 && currentSlide <= slides.lastIndex) {
                        FinsibleButton(
                            modifier = if (currentSlide == slides.lastIndex) Modifier else Modifier.weight(1f),
                            label = "Back",
                            onClick = { backwardNavigation() },
                            style = ButtonStyle.Secondary,
                            variant = if (currentSlide == slides.lastIndex) ButtonVariant.WrapContent else ButtonVariant.FullWidth,
                            size = ButtonSize.Large
                        )

                        Spacer(Modifier.width(16.dp))
                    }

                    // Forward Navigation Button
                    FinsibleButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        label = fwdNavigationLabel,
                        onClick = { forwardNavigation() },
                        iconDrawable = iconDrawable,
                        variant = ButtonVariant.FullWidthWithIcon,
                        iconPosition = iconPosition,
                        size = ButtonSize.Large,
                        style = ButtonStyle.Primary,
                    )
                }

            }

            // CONDITIONAL LOADING INDICATOR
            if (authState == AuthState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)), // Semi-transparent overlay
                    contentAlignment = Alignment.Center
                ) {
                    RippleLoadingIndicator(
                        primaryColor = MaterialTheme.colorScheme.primary,
                        secondaryColor = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(rememberNavController())
}