package com.itsjeel01.finsiblefrontend.data.model.client

import com.itsjeel01.finsiblefrontend.R

data class OnboardingData(
    val illustration: Int = 0,
    val headline: String = "",
    val description: String = "",
) {
    fun getOnboardingData(): List<OnboardingData> {
        return listOf(
            OnboardingData(
                illustration = R.drawable.ill_welcome,
                headline = "Welcome to Finsible",
                description = "Discover the smarter way to take control of your finances. Finsible empowers you to track, manage, and achieve your financial goals—all in one place."
            ),
            OnboardingData(
                illustration = R.drawable.ill_transactions,
                headline = "Where your money goes?",
                description = "Effortlessly track and categorize every expense. From daily coffee runs to big purchases, Finsible ensures you always know where your money is going."
            ),
            OnboardingData(
                illustration = R.drawable.ill_graph,
                headline = "Your goals, achieved!",
                description = "Set personalized financial goals, track your progress over time, and achieve the financial freedom you’ve always wanted with actionable insights."
            ),
            OnboardingData(
                illustration = R.drawable.ill_login,
                headline = "Start your journey...",
                description = "Get started by signing in with Google. Experience a seamless, secure, and personalized way to manage your finances with Finsible."
            )
        )
    }
}