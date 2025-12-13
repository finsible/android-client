package com.itsjeel01.finsiblefrontend.ui.model

import com.itsjeel01.finsiblefrontend.R

/** Data class for representing onboarding carousel items. */
data class CarouselItem(
    val illustration: Int,
    val headline: String,
    val description: String
) {
    companion object {
        /** Returns all carousel items for onboarding. */
        fun getAll(): List<CarouselItem> = listOf(
            CarouselItem(
                illustration = R.drawable.ill_welcome,
                headline = "Welcome to Finsible",
                description = "We make finance sensible and stress-free. Start your journey to financial clarity and transform how you manage money."
            ),
            CarouselItem(
                illustration = R.drawable.ill_track,
                headline = "Never Lose Track Again",
                description = "Capture transactions in seconds and see your spending clearly. Understand your money habits without the headache."
            ),
            CarouselItem(
                illustration = R.drawable.ill_audit,
                headline = "Your money, decoded!",
                description = "Turn financial data into insights. Set goals, get personalized tips, and reach them faster with advice in your pocket."
            ),
            CarouselItem(
                illustration = R.drawable.ill_auth,
                headline = "Let's Get You Set Up",
                description = "Never lose your data, access from anywhere. Sign in with Google and start making your finances more sensible."
            )
        )
    }
}
