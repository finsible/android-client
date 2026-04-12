package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleScrubber
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleScrubberDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleScrubberVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Preview(name = "Scrubber - Light", showBackground = true, widthDp = 420, heightDp = 1100)
@Preview(name = "Scrubber - Dark", showBackground = true, widthDp = 420, heightDp = 1100, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleScrubberPreview() {
    FinsibleComponentPreviewScaffold {
        val colors = FinsibleTheme.colors
        val d = FinsibleTheme.dimes

        Column(verticalArrangement = Arrangement.spacedBy(d.d2)) {
            FinsibleText(
                text = "Finsible Scrubber",
                variant = FinsibleTextVariant.SmallHeadingBold,
                color = colors.brandAccent
            )
            FinsibleText(
                text = "Visual Component Guide",
                variant = FinsibleTextVariant.BodyRegular,
                color = colors.secondaryContent
            )
        }

        HorizontalDivider(color = colors.divider)

        var separateIndex by remember { mutableIntStateOf(1) }
        var continuousIndex by remember { mutableIntStateOf(2) }
        var compactIndex by remember { mutableIntStateOf(0) }

        FinsiblePreviewSection("Separate - Interactive") {
            FinsibleText(
                text = "Current: ${separateIndex + 1} / 5",
                variant = FinsibleTextVariant.SmallBodySemiBold,
                color = colors.primaryContent
            )
            FinsibleScrubber(
                currentIndex = separateIndex,
                totalCount = 5,
                onIndexChange = { separateIndex = it },
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Separate
            )
        }

        FinsiblePreviewSection("Continuous - Interactive") {
            FinsibleText(
                text = "Current: ${continuousIndex + 1} / 7",
                variant = FinsibleTextVariant.SmallBodySemiBold,
                color = colors.primaryContent
            )
            FinsibleScrubber(
                currentIndex = continuousIndex,
                totalCount = 7,
                onIndexChange = { continuousIndex = it },
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Continuous
            )
        }

        FinsiblePreviewSection("Color Overrides") {
            FinsibleScrubber(
                currentIndex = 2,
                totalCount = 6,
                onIndexChange = {},
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Separate,
                colors = FinsibleScrubberDefaults.colors(
                    currentColor = colors.info,
                    restColor = colors.infoContainer
                )
            )
            FinsibleScrubber(
                currentIndex = 4,
                totalCount = 6,
                onIndexChange = {},
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Continuous,
                colors = FinsibleScrubberDefaults.colors(
                    currentColor = colors.warning,
                    restColor = colors.warningContainer
                )
            )
        }

        FinsiblePreviewSection("Width Customization") {
            FinsibleScrubber(
                currentIndex = compactIndex,
                totalCount = 8,
                onIndexChange = { compactIndex = it },
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Separate,
                sizes = FinsibleScrubberDefaults.sizes().copy(
                    activeBarWidth = d.d20,
                    inactiveBarWidth = d.d8,
                    barSpacing = d.d6
                )
            )
        }

        FinsiblePreviewSection("Disabled") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(d.d12)
            ) {
                FinsibleScrubber(
                    currentIndex = 1,
                    totalCount = 5,
                    onIndexChange = {},
                    modifier = Modifier.weight(1f),
                    variant = FinsibleScrubberVariant.Separate,
                    enabled = false
                )
                FinsibleScrubber(
                    currentIndex = 3,
                    totalCount = 5,
                    onIndexChange = {},
                    modifier = Modifier.weight(1f),
                    variant = FinsibleScrubberVariant.Continuous,
                    enabled = false
                )
            }
        }

        FinsiblePreviewSection("Single Item") {
            FinsibleScrubber(
                currentIndex = 0,
                totalCount = 1,
                onIndexChange = {},
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Separate
            )
        }

        FinsiblePreviewSection("High Count Separate") {
            FinsibleScrubber(
                currentIndex = 7,
                totalCount = 24,
                onIndexChange = {},
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Separate
            )
        }
    }
}



