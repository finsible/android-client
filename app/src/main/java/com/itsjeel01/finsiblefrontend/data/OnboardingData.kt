package com.itsjeel01.finsiblefrontend.data

import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.models.OnboardingBottomSheetDataModel

val slides = listOf(
    OnboardingBottomSheetDataModel(
        illustration = R.drawable.welcome_illustration,
        headline = "Welcome to Finsible",
        description = "Discover the smarter way to take control of your finances. Finsible empowers you to track, manage, and achieve your financial goals—all in one place."
    ),
    OnboardingBottomSheetDataModel(
        illustration = R.drawable.transactions_illustration,
        headline = "Where your money goes?",
        description = "Effortlessly track and categorize every expense. From daily coffee runs to big purchases, Finsible ensures you always know where your money is going."
    ),
    OnboardingBottomSheetDataModel(
        illustration = R.drawable.graph_illustration,
        headline = "Your Goals, Achieved",
        description = "Set personalized financial goals, track your progress over time, and achieve the financial freedom you’ve always wanted with actionable insights."
    ),
    OnboardingBottomSheetDataModel(
        illustration = R.drawable.login_illustration,
        headline = "Start Your Journey",
        description = "Get started by signing in with Google. Experience a seamless, secure, and personalized way to manage your finances with Finsible."
    )
)