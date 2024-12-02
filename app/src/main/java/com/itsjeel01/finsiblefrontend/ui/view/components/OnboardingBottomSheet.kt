package com.itsjeel01.finsiblefrontend.ui.view.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.itsjeel01.finsiblefrontend.data.slides
import com.itsjeel01.finsiblefrontend.ui.viewmodel.OnboardingViewModel
import com.itsjeel01.finsiblefrontend.utils.signInWithGoogle

@Composable
fun OnboardingBottomSheet(viewModel: OnboardingViewModel, modifier: Modifier) {
    val currentSlide = viewModel.currentSlide.collectAsState().value
    val buttonLabel: String = when (currentSlide) {
        0 -> "Get Started"
        slides.lastIndex -> "Sign In with Google"
        else -> "Next"
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    fun nextSlide() {
        if (currentSlide == slides.lastIndex) {
            signInWithGoogle(context, coroutineScope)
            return
        } else viewModel.updateSlide(currentSlide + 1)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {

        // Headline
        Text(
            text = slides[currentSlide].headline,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Description
        Text(
            text = slides[currentSlide].description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Slider Indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            slides.indices.forEach { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(if (index == currentSlide) (16.dp) else 8.dp)
                        .background(
                            if (index == currentSlide) MaterialTheme.colorScheme.primary
                            else Color.Gray,
                            shape = RoundedCornerShape(50)
                        )
                        .clickable { viewModel.updateSlide(index) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Dynamic Button for navigation
        Button(
            onClick = { nextSlide() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = buttonLabel)
        }
    }
}

