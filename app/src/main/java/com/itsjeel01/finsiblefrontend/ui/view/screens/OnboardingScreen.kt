package com.itsjeel01.finsiblefrontend.ui.view.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itsjeel01.finsiblefrontend.data.slides
import com.itsjeel01.finsiblefrontend.ui.view.components.OnboardingBottomSheet
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    val topAppBarColors = TopAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.background,
        actionIconContentColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.background,
        navigationIconContentColor = MaterialTheme.colorScheme.background,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                colors = topAppBarColors
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            // Dynamic illustration at the top
            AnimatedContent(
                targetState = slides[onboardingViewModel.currentSlide.collectAsState().value],
                transitionSpec = {
                    (slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 1200)
                    ) + fadeIn(animationSpec = tween(durationMillis = 300)))
                        .togetherWith(
                            slideOutHorizontally(
                                targetOffsetX = { -it },
                                animationSpec = tween(durationMillis = 1200)
                            ) + fadeOut(animationSpec = tween(durationMillis = 300))
                        ).using(SizeTransform(clip = true))
                },
                label = "AnimatedContent"
            ) { slide ->
                Image(
                    painter = painterResource(id = slide.illustration),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(bottom = 250.dp, start = 16.dp, end = 16.dp),
                    contentDescription = "Onboarding Illustration",
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.Center
                )
            }

            // Sticky Bottom Sheet
            OnboardingBottomSheet(
                onboardingViewModel = onboardingViewModel,
                modifier = Modifier.align(Alignment.BottomCenter),
                authViewModel = authViewModel,
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(rememberNavController())
}