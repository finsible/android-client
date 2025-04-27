package com.itsjeel01.finsiblefrontend.feature.auth.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.itsjeel01.finsiblefrontend.BuildConfig
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.core.constants.AppConstants
import com.itsjeel01.finsiblefrontend.data.model.AuthState
import com.itsjeel01.finsiblefrontend.data.model.client.OnboardingData
import com.itsjeel01.finsiblefrontend.feature.auth.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.feature.auth.viewmodel.OnboardingViewModel
import com.itsjeel01.finsiblefrontend.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.common.InputCommonProps
import com.itsjeel01.finsiblefrontend.ui.common.InputFieldSize
import com.itsjeel01.finsiblefrontend.ui.common.component.button.BaseButton
import com.itsjeel01.finsiblefrontend.ui.common.component.button.ButtonStyle
import com.itsjeel01.finsiblefrontend.ui.common.component.button.ButtonVariant
import com.itsjeel01.finsiblefrontend.ui.common.component.button.IconPosition
import com.itsjeel01.finsiblefrontend.ui.common.component.indicator.OnboardingIndicators
import com.itsjeel01.finsiblefrontend.ui.common.component.indicator.RippleLoadingIndicator
import com.itsjeel01.finsiblefrontend.ui.theme.CustomColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun OnboardingScreen(navController: NavHostController) {
    // View models
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    // State
    val slides = OnboardingData().getOnboardingData()
    val currentSlide = onboardingViewModel.currentSlide.collectAsState().value
    val authState = authViewModel.authState.collectAsState().value
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // Background gradient
    val backgroundGradient = Brush.linearGradient(
        listOf(
            getCustomColor(CustomColorKey.OnboardingGradientColor2),
            getCustomColor(CustomColorKey.OnboardingGradientColor1),
            getCustomColor(CustomColorKey.OnboardingGradientColor2)
        ),
    )

    // Navigation button configuration
    val (fwdNavigationLabel, iconDrawable, iconPosition) = when {
        currentSlide == 0 -> Triple("Get started", R.drawable.ic_right_arrow_dotted, IconPosition.EndOfButton)
        currentSlide < slides.lastIndex -> Triple("Next", null, IconPosition.EndOfLabel)
        else -> Triple("Sign In with Google", R.drawable.ic_google, IconPosition.StartOfLabel)
    }

    // Icon tinting
    val tintedIcon = when (currentSlide) {
        slides.lastIndex -> false
        else -> true
    }

    // Auth state monitoring
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Positive -> {
                navController.navigate(Routes.DashboardScreen)
            }

            AuthState.Loading -> {
                Log.d("OnboardingScreen", "Loading...")
            }

            is AuthState.Negative -> {
                if (authState.isFailed) Log.d("OnboardingScreen", "Error: ${authState.message}")
            }
        }
    }

    // Sign in with Google
    fun signInWithGoogle(
        context: Context,
        coroutineScope: CoroutineScope,
        authViewModel: AuthViewModel,
    ) {
        val credentialsManager = CredentialManager.create(context)
        val clientId = BuildConfig.SERVER_CLIENT_ID

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val hashedNonce = MessageDigest.getInstance("SHA-256").digest(bytes)
            .fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId) // web client ID
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialsManager.getCredential(request = request, context = context)
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                authViewModel.authenticate(clientId = clientId, token = googleIdToken)
            } catch (e: Exception) {
                Log.e("SignInWithGoogle", e.message.toString())
            }
        }
    }

    // Navigation functions
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.ime,
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundGradient)
                    .padding(horizontal = 32.dp, vertical = screenHeight * 0.1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // SECTION: Illustration
                OnboardingIllustration(
                    currentSlide = currentSlide,
                    slides = slides,
                    screenHeight = screenHeight
                )

                Spacer(modifier = Modifier.weight(1f))

                // SECTION: Text Content
                OnboardingTextContent(
                    currentSlide = currentSlide,
                    slides = slides
                )

                Spacer(modifier = Modifier.weight(1f))

                // SECTION: Indicators
                OnboardingIndicators(currentSlide, slides.size)

                Spacer(modifier = Modifier.weight(1f))

                // SECTION: Navigation Buttons
                OnboardingNavigationButtons(
                    currentSlide = currentSlide,
                    slidesLastIndex = slides.lastIndex,
                    fwdNavigationLabel = fwdNavigationLabel,
                    iconDrawable = iconDrawable,
                    tintedIcon = tintedIcon,
                    iconPosition = iconPosition,
                    onBackClick = { backwardNavigation() },
                    onForwardClick = { forwardNavigation() }
                )
            }

            // SECTION: Loading overlay
            if (authState == AuthState.Loading) {
                LoadingOverlay()
            }
        }
    }
}

@Composable
private fun OnboardingIllustration(
    currentSlide: Int,
    slides: List<OnboardingData>,
    screenHeight: Dp,
) {
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
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun OnboardingTextContent(
    currentSlide: Int,
    slides: List<OnboardingData>,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Animated headline
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
                        durationMillis = AppConstants.ANIMATION_DURATION_VERY_SHORT,
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
}

@Composable
private fun OnboardingNavigationButtons(
    currentSlide: Int,
    slidesLastIndex: Int,
    fwdNavigationLabel: String,
    iconDrawable: Int?,
    tintedIcon: Boolean = true,
    iconPosition: IconPosition,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        // Back button (conditionally shown)
        if (currentSlide > 0 && currentSlide <= slidesLastIndex) {
            BaseButton(
                onClick = onBackClick,
                commonProps = InputCommonProps(
                    modifier = if (currentSlide == slidesLastIndex) Modifier else Modifier.weight(1f),
                    label = "Back",
                    size = InputFieldSize.Large
                ),
                style = ButtonStyle.Secondary,
                variant = if (currentSlide == slidesLastIndex) ButtonVariant.WrapContent else ButtonVariant.FullWidth
            )

            Spacer(Modifier.width(16.dp))
        }

        // Forward/Next/Sign in button
        BaseButton(
            onClick = onForwardClick,
            commonProps = InputCommonProps(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = fwdNavigationLabel,
                size = InputFieldSize.Large
            ),
            style = ButtonStyle.Primary,
            variant = ButtonVariant.FullWidth,
            icon = iconDrawable,
            tintedIcon = tintedIcon,
            iconPosition = iconPosition
        )
    }
}

@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        RippleLoadingIndicator(
            primaryColor = MaterialTheme.colorScheme.primary,
            secondaryColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(rememberNavController())
}
