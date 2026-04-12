package com.itsjeel01.finsiblefrontend.ui.mapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleGradients
import com.itsjeel01.finsiblefrontend.ui.theme.GradientType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ImmutableList<FinsibleTileCardData>.toAccountsTileCards(): ImmutableList<FinsibleTileCardData> {
    val netWorthGradient = FinsibleGradients.getLinearGradient(type = GradientType.NET_WORTH)
    val assetsGradient = FinsibleGradients.getLinearGradient(type = GradientType.ASSETS)
    val liabilitiesGradient = FinsibleGradients.getLinearGradient(type = GradientType.LIABILITIES)
    val brandGradient = FinsibleGradients.getLinearGradient(type = GradientType.BRAND)

    return remember(this, size, netWorthGradient, assetsGradient, liabilitiesGradient, brandGradient) {
        mapIndexed { index, card ->
            val brush = when (gradientTypeForIndex(index)) {
                GradientType.NET_WORTH -> netWorthGradient
                GradientType.ASSETS -> assetsGradient
                GradientType.LIABILITIES -> liabilitiesGradient
                GradientType.BRAND -> brandGradient
                else -> brandGradient
            }
            card.copy(backgroundBrush = brush)
        }.toImmutableList()
    }
}

private fun gradientTypeForIndex(index: Int): GradientType = when (index) {
    0 -> GradientType.NET_WORTH
    1 -> GradientType.ASSETS
    2 -> GradientType.LIABILITIES
    else -> GradientType.BRAND
}