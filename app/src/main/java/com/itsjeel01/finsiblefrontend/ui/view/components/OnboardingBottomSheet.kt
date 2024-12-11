package com.itsjeel01.finsiblefrontend.ui.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.models.AuthState
import com.itsjeel01.finsiblefrontend.data.slides
import com.itsjeel01.finsiblefrontend.ui.navigation.Routes
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel
import com.itsjeel01.finsiblefrontend.utils.signInWithGoogle

@Composable
fun OnboardingBottomSheet(
    onboardingViewModel: OnboardingViewModel,
    modifier: Modifier,
    authViewModel: AuthViewModel,
) {
    val currentSlide = onboardingViewModel.currentSlide.collectAsState().value
    val buttonLabel: String = when (currentSlide) {
        0 -> "Get Started"
        slides.lastIndex -> "Sign In with Google"
        else -> "Next"
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    fun navigateToNext() {
        if (currentSlide == slides.lastIndex) {
            signInWithGoogle(context, coroutineScope, authViewModel)
            if (authViewModel.authState.value == AuthState.Positive) {
                navController.navigate(Routes.DashboardScreen)
            }
        } else onboardingViewModel.updateSlide(currentSlide + 1)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .shadow(
                elevation = 24.dp,
                ambientColor = MaterialTheme.colorScheme.primary,
                spotColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            )
            .background(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.background,
            )
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 48.dp)
    ) {

        AnimatedContent(
            targetState = slides[currentSlide],
            transitionSpec = {
                (slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 1200)
                ) + fadeIn(animationSpec = tween(durationMillis = 300)))
                    .togetherWith(
                        slideOutVertically(
                            targetOffsetY = { -it },
                            animationSpec = tween(durationMillis = 1200)
                        ) + fadeOut(animationSpec = tween(durationMillis = 300))
                    ).using(SizeTransform(clip = true))
            },
            label = "AnimatedContent"
        ) { slide ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Headline
                Text(
                    text = slide.headline,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Description
                Text(
                    text = slide.description,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Slider Indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            slides.indices.forEach { index ->
                val size by animateDpAsState(
                    targetValue = if (index == currentSlide) 16.dp else 8.dp,
                    label = "AnimateDpAsState"
                )
                val color by animateColorAsState(
                    targetValue = if (index == currentSlide) MaterialTheme.colorScheme.primary else Color.Gray,
                    label = "AnimateColorAsState"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(size)
                        .background(color, shape = RoundedCornerShape(50))
                        .clickable { onboardingViewModel.updateSlide(index) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Dynamic Button for navigation
        FinsibleButton(
            label = buttonLabel,
            onClick = { navigateToNext() },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(corner = CornerSize(4.dp))
                ),
            cornerSize = 8.dp,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            iconDrawable = R.drawable.arrow_right_icon
        )
    }
}
