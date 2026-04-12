package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTileCards
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTileCardRotationVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.StatEntryUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.collections.immutable.persistentListOf

@Preview(name = "Light Mode", showBackground = true, widthDp = 700, heightDp = 900)
@Preview(name = "Dark Mode", showBackground = true, widthDp = 700, heightDp = 900, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleTileCardsPreview() {
    val cards = persistentListOf(
        FinsibleTileCardData(
            title = "Net Worth",
            subtitle = "Portfolio health",
            heroText = "$42.5k",
            pillText = "Live",
            kpiText = "+8.4% this month",
            statistics = persistentListOf(
                StatEntryUIModel("Assets", "$60k"),
                StatEntryUIModel("Liabilities", "$17.5k")
            ),
            backgroundBrush = Brush.linearGradient(colors = listOf(Color(0xFF3C4BDA), Color(0xFF5F74F2)))
        ),
        FinsibleTileCardData(
            title = "Savings",
            subtitle = "Goal pace",
            heroText = "$12.5k",
            pillText = "On track",
            kpiText = "+2.1% this week",
            statistics = persistentListOf(
                StatEntryUIModel("Goal", "$20k"),
                StatEntryUIModel("Progress", "62%"),
                StatEntryUIModel("ETA", "5 mo")
            )
        )
    )

    FinsibleComponentPreviewScaffold {
        var carouselIndex by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d16)
        ) {
            FinsibleTileCards(
                cards = cards,
                size = com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize.Medium,
                rotationVariant = FinsibleTileCardRotationVariant.Carousel,
                currentIndex = carouselIndex,
                onIndexChange = { carouselIndex = it }
            )

            FinsibleTileCards(
                cards = cards,
                size = com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize.Small,
                rotationVariant = FinsibleTileCardRotationVariant.Sequential,
                inverted = true
            )
        }
    }
}

