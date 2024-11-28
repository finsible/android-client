package com.itsjeel01.finsiblefrontend.data

import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.models.OnboardingBottomSheetData

// Sample data for slides
val slides = listOf(
    OnboardingBottomSheetData(
        illustration = R.drawable.ic_launcher_foreground,
        headline = "Welcome to Finsible",
        description = "Manage your finances smartly with Finsible. Stay on top of your goals!"
    ),
    OnboardingBottomSheetData(
        illustration = R.drawable.ic_launcher_foreground,
        headline = "Track Your Expenses",
        description = "Effortlessly track and categorize your daily expenses with ease."
    ),
    OnboardingBottomSheetData(
        illustration = R.drawable.ic_launcher_foreground,
        headline = "Set Financial Goals",
        description = "Set goals, monitor your progress, and achieve financial freedom."
    ),
    OnboardingBottomSheetData(
        illustration = R.drawable.ic_launcher_foreground,
        headline = "Sign In and Get Started",
        description = "Sign in with Google to experience the power of Finsible."
    )
)