package com.itsjeel01.finsiblefrontend.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Predefined gradient types for financial cards. */
enum class CardGradientType {
    BRAND,        // Net Worth, Overview, Default
    SUCCESS,      // Assets, Positive metrics
    WARNING,      // Liabilities, Debt, Alerts
    INCOME,       // Income, Revenue, Earnings
    EXPENSE,      // Expenses, Spending, Costs
    SAVINGS,      // Savings accounts, Emergency fund
    INVESTMENT,   // Investments, Portfolio, Stocks
    BUDGET,       // Budget overview, Planning
    NEUTRAL,      // Transfers, Misc, Generic
    PREMIUM,      // Premium features, VIP
}

/** Helper object to access gradient colors for financial cards. */
object FinsibleGradients {

    /** Get gradient color pair for a specific card type. */
    @Composable
    fun getGradientColors(type: CardGradientType): List<Color> {
        val colors = FinsibleTheme.colors
        return when (type) {
            CardGradientType.BRAND -> listOf(colors.gradientBrand1, colors.gradientBrand2)
            CardGradientType.SUCCESS -> listOf(colors.gradientSuccess1, colors.gradientSuccess2)
            CardGradientType.WARNING -> listOf(colors.gradientWarning1, colors.gradientWarning2)
            CardGradientType.INCOME -> listOf(colors.gradientIncome1, colors.gradientIncome2)
            CardGradientType.EXPENSE -> listOf(colors.gradientExpense1, colors.gradientExpense2)
            CardGradientType.SAVINGS -> listOf(colors.gradientSavings1, colors.gradientSavings2)
            CardGradientType.INVESTMENT -> listOf(colors.gradientInvestment1, colors.gradientInvestment2)
            CardGradientType.BUDGET -> listOf(colors.gradientBudget1, colors.gradientBudget2)
            CardGradientType.NEUTRAL -> listOf(colors.gradientNeutral1, colors.gradientNeutral2)
            CardGradientType.PREMIUM -> listOf(colors.gradientPremium1, colors.gradientPremium2)
        }
    }

    /** Get linear gradient brush for a specific card type. */
    @Composable
    fun getLinearGradient(type: CardGradientType): Brush {
        return Brush.linearGradient(colors = getGradientColors(type))
    }

    /** Get vertical gradient brush for a specific card type. */
    @Composable
    fun getVerticalGradient(type: CardGradientType): Brush {
        return Brush.verticalGradient(colors = getGradientColors(type))
    }

    /** Get horizontal gradient brush for a specific card type. */
    @Composable
    fun getHorizontalGradient(type: CardGradientType): Brush {
        return Brush.horizontalGradient(colors = getGradientColors(type))
    }
}

