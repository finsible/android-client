package com.itsjeel01.finsiblefrontend.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.ButtonStyle
import com.itsjeel01.finsiblefrontend.common.ButtonVariant
import com.itsjeel01.finsiblefrontend.common.IconPosition
import com.itsjeel01.finsiblefrontend.common.InputFieldSize
import com.itsjeel01.finsiblefrontend.common.Strings
import com.itsjeel01.finsiblefrontend.data.di.hiltNotificationManager
import com.itsjeel01.finsiblefrontend.ui.component.base.BaseButton
import com.itsjeel01.finsiblefrontend.ui.component.base.BaseCarouselIndicators
import com.itsjeel01.finsiblefrontend.ui.component.base.CommonProps
import com.itsjeel01.finsiblefrontend.ui.component.base.FullScreenLoadingIndicator
import com.itsjeel01.finsiblefrontend.ui.data.AuthState
import com.itsjeel01.finsiblefrontend.ui.data.OnboardingData
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.theme.ColorKey
import com.itsjeel01.finsiblefrontend.ui.theme.dime.Size
import com.itsjeel01.finsiblefrontend.ui.theme.dime.height
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingHorizontal
import com.itsjeel01.finsiblefrontend.ui.theme.dime.paddingVertical
import com.itsjeel01.finsiblefrontend.ui.theme.dime.width
import com.itsjeel01.finsiblefrontend.ui.theme.getCustomColor
import com.itsjeel01.finsiblefrontend.ui.util.Animation
import com.itsjeel01.finsiblefrontend.ui.util.Duration
import com.itsjeel01.finsiblefrontend.ui.util.GoogleLoginUtil
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotificationManager
import com.itsjeel01.finsiblefrontend.ui.util.InAppNotificationPosition
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(navController: NavHostController) {

    // --- ViewModel and State Initialization ---

    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val inAppNotificationManager: InAppNotificationManager = hiltNotificationManager()

    val carouselItems = OnboardingData().get()
    val currentItem = onboardingViewModel.currentItem.collectAsState().value
    val authState = authViewModel.authState.collectAsState().value
    val context = LocalContext.current

    // --- Side Effects for Navigation ---

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Positive -> {
                navController.navigate(Routes.DashboardScreen)
            }

            is AuthState.Loading -> {
                Log.d(Strings.ONBOARDING_SCREEN, "Loading...")
            }

            is AuthState.Negative -> {
                if (authState.isFailed) Log.d(
                    Strings.ONBOARDING_SCREEN,
                    "Auth status: ${authState.message}"
                )
            }
        }
    }

    // --- UI Components ---

    val gradient = Brush.linearGradient(
        listOf(
            getCustomColor(ColorKey.OnboardingGradientPrimaryColor),
            getCustomColor(ColorKey.OnboardingGradientSecondaryColor),
            getCustomColor(ColorKey.OnboardingGradientPrimaryColor)
        ),
    )

    val (primaryButtonLabel, primaryButtonIcon, primaryButtonIconPosition) = when {
        currentItem == 0 -> Triple(
            Strings.GET_STARTED,
            R.drawable.ic_right_arrow_dotted,
            IconPosition.EndOfButton
        )

        onboardingViewModel.isLastItem() -> Triple(
            Strings.SIGN_IN_WITH_GOOGLE,
            R.drawable.ic_google,
            IconPosition.StartOfLabel,
        )

        else -> Triple(
            Strings.NEXT,
            null,
            IconPosition.EndOfLabel,
        )
    }

    // --- Navigation Functions ---

    fun onPrimaryButtonClick() {
        if (!onboardingViewModel.isLastItem())
            onboardingViewModel.nextItem()
        else {
            GoogleLoginUtil.login(context, authViewModel)
        }
    }

    fun onSecondaryButtonClick() {
        onboardingViewModel.previousItem()
    }

    // --- Main Scaffold Layout ---

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars,
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient)
                    .paddingHorizontal(Size.S32)
                    .paddingVertical(Size.S80),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Illustration(
                    currentItem = currentItem,
                    carouselItems = carouselItems,
                )
                Spacer(modifier = Modifier.weight(1f))
                TextContent(
                    currentItem = currentItem,
                    carouselItems = carouselItems
                )
                Spacer(modifier = Modifier.weight(1f))
                BaseCarouselIndicators(currentItem, carouselItems.size)
                Spacer(modifier = Modifier.weight(1f))
                NavigationButtons(
                    currentItem = currentItem,
                    lastItem = carouselItems.lastIndex,
                    primaryButtonLabel = primaryButtonLabel,
                    iconDrawable = primaryButtonIcon,
                    tintedIcon = !onboardingViewModel.isLastItem(),
                    iconPosition = primaryButtonIconPosition,
                    onSecondaryButtonClick = { onSecondaryButtonClick() },
                    onPrimaryButtonClick = { onPrimaryButtonClick() }
                )
            }

            if (authState is AuthState.Loading) {
                FullScreenLoadingIndicator()
            }

            if (authState is AuthState.Negative && authState.isFailed) {
                inAppNotificationManager.showError(
                    title = "Authentication Unsuccessful",
                    subtitle = authState.message,
                    actionLabel = "Retry",
                    onAction = {
                        GoogleLoginUtil.login(context, authViewModel)
                    },
                    autoDismiss = true,
                    autoDismissDelay = Duration.MSEC_2500,
                    position = InAppNotificationPosition.TOP
                )
            }
        }
    }
}

@Composable
private fun Illustration(
    currentItem: Int,
    carouselItems: List<OnboardingData>,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Size.S200),
        contentAlignment = Alignment.TopCenter
    ) {
        Crossfade(
            targetState = currentItem,
            animationSpec = Animation.fastOutSlowEasingSpec()
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
private fun TextContent(
    currentItem: Int,
    carouselItems: List<OnboardingData>,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = currentItem,
            transitionSpec = {
                (Animation.fadeInDelayed + Animation.scaleInDelayed)
                    .togetherWith(Animation.fadeOutQuickly)
                    .using(SizeTransform(clip = false))
            }
        ) { index ->
            Text(
                carouselItems[index].headline,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(Size.S24))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((4 * MaterialTheme.typography.bodyLarge.lineHeight.value).dp),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = currentItem,
                animationSpec = Animation.fastOutSlowEasingSpec()
            ) { index ->
                Text(
                    carouselItems[index].description,
                    modifier = Modifier.fillMaxHeight(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun NavigationButtons(
    currentItem: Int,
    lastItem: Int,
    primaryButtonLabel: String,
    iconDrawable: Int?,
    tintedIcon: Boolean = true,
    iconPosition: IconPosition,
    onSecondaryButtonClick: () -> Unit,
    onPrimaryButtonClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        if (currentItem > 0 && currentItem <= lastItem) {
            BaseButton(
                onClick = onSecondaryButtonClick,
                commonProps = CommonProps(
                    modifier = if (currentItem == lastItem) Modifier else Modifier.weight(1f),
                    label = Strings.BACK,
                    size = InputFieldSize.Large
                ),
                style = ButtonStyle.Secondary,
                variant = if (currentItem == lastItem) ButtonVariant.WrapContent else ButtonVariant.FullWidth
            )

            Spacer(Modifier.width(Size.S16))
        }

        BaseButton(
            onClick = onPrimaryButtonClick,
            commonProps = CommonProps(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = primaryButtonLabel,
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