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
    val gradients = List(size) { index ->
        FinsibleGradients.getLinearGradient(type = gradientTypeForIndex(index))
    }

    return remember(this, gradients) {
        mapIndexed { index, card ->
            card.copy(
                backgroundBrush = gradients[index]
            )
        }.toImmutableList()
    }
}

private fun gradientTypeForIndex(index: Int): GradientType = when (index) {
    0 -> GradientType.NET_WORTH
    1 -> GradientType.ASSETS
    2 -> GradientType.LIABILITIES
    else -> GradientType.BRAND
}


