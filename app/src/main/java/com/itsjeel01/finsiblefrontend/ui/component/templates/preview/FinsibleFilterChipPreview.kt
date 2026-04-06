package com.itsjeel01.finsiblefrontend.ui.component.templates.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleIconPosition
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

private val SupportedFilterChipSizes = listOf(FinsibleSize.Small, FinsibleSize.Medium, FinsibleSize.Large)

@Preview(name = "Filter Chip - Light", showBackground = true, widthDp = 850, heightDp = 1000)
@Preview(name = "Filter Chip - Dark", showBackground = true, widthDp = 850, heightDp = 1000, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FinsibleFilterChipPreview() {
    val searchIcon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(android.R.drawable.ic_menu_search),
            contentDescription = null
        )
    }

    FinsibleComponentPreviewScaffold {
        val dimes = FinsibleTheme.dimes
        val colors = FinsibleTheme.colors

        Column {
            FinsibleText(
                text = "Finsible Filter Chips",
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

        FinsiblePreviewSection("Label Variations") {
            var labelOnlySelected by remember { mutableStateOf(true) }
            var iconLeadingSelected by remember { mutableStateOf(false) }
            var iconTrailingSelected by remember { mutableStateOf(true) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(dimes.d10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FinsibleFilterChip(
                    selected = labelOnlySelected,
                    onSelectedChange = { labelOnlySelected = it },
                    label = "All"
                )
                FinsibleFilterChip(
                    selected = iconLeadingSelected,
                    onSelectedChange = { iconLeadingSelected = it },
                    label = "Search",
                    icon = searchIcon
                )
                FinsibleFilterChip(
                    selected = iconTrailingSelected,
                    onSelectedChange = { iconTrailingSelected = it },
                    label = "Sort",
                    icon = searchIcon,
                    iconPosition = FinsibleIconPosition.Trailing
                )
            }
        }

        FinsiblePreviewSection("Sizes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimes.d10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SupportedFilterChipSizes.forEach { chipSize ->
                    var selected by remember(chipSize) { mutableStateOf(chipSize != FinsibleSize.Small) }
                    FinsibleFilterChip(
                        selected = selected,
                        onSelectedChange = { selected = it },
                        label = chipSize.name,
                        size = chipSize
                    )
                }
            }
        }

        FinsiblePreviewSection("Shapes") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimes.d10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShapeSample(shapeVariant = FinsibleShape.Pill, label = "Pill")
                ShapeSample(shapeVariant = FinsibleShape.Rounded, label = "Rounded")
                ShapeSample(shapeVariant = FinsibleShape.Sharp, label = "Sharp")
            }
        }

        FinsiblePreviewSection("States") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimes.d10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FinsibleFilterChip(selected = true, onSelectedChange = {}, label = "Selected")
                FinsibleFilterChip(selected = false, onSelectedChange = {}, label = "Unselected")
                FinsibleFilterChip(selected = true, onSelectedChange = {}, label = "Disabled On", enabled = false)
                FinsibleFilterChip(selected = false, onSelectedChange = {}, label = "Disabled Off", enabled = false)
            }
        }

        FinsiblePreviewSection("Variants") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimes.d10),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FinsibleFilterChip(
                    selected = true,
                    onSelectedChange = {},
                    label = "Filled",
                    variant = FinsibleFilterChipVariant.Filled
                )
                FinsibleFilterChip(
                    selected = true,
                    onSelectedChange = {},
                    label = "Tonal",
                    variant = FinsibleFilterChipVariant.Tonal
                )
                FinsibleFilterChip(
                    selected = true,
                    onSelectedChange = {},
                    label = "Outlined",
                    variant = FinsibleFilterChipVariant.Outlined
                )
                FinsibleFilterChip(
                    selected = true,
                    onSelectedChange = {},
                    label = "Outlined Tonal",
                    variant = FinsibleFilterChipVariant.OutlinedTonal
                )
            }
        }

        FinsiblePreviewSection("Coverage Matrix") {
            SupportedFilterChipSizes.forEach { chipSize ->
                FinsibleText(
                    text = chipSize.name,
                    variant = FinsibleTextVariant.SmallBodySemiBold,
                    color = colors.brandAccent
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimes.d8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FinsibleFilterChip(selected = false, onSelectedChange = {}, label = "Label", size = chipSize)
                    FinsibleFilterChip(selected = true, onSelectedChange = {}, label = "Selected", size = chipSize)
                    FinsibleFilterChip(selected = false, onSelectedChange = {}, label = "Leading", size = chipSize, icon = searchIcon)
                    FinsibleFilterChip(
                        selected = true,
                        onSelectedChange = {},
                        label = "Trailing",
                        size = chipSize,
                        icon = searchIcon,
                        iconPosition = FinsibleIconPosition.Trailing
                    )
                    FinsibleFilterChip(selected = true, onSelectedChange = {}, label = "Disabled", size = chipSize, enabled = false)
                    FinsibleFilterChip(selected = false, onSelectedChange = {}, label = "Disabled Off", size = chipSize, enabled = false)
                }

                HorizontalDivider(color = colors.divider)
            }
        }
    }
}

@Composable
private fun ShapeSample(shapeVariant: FinsibleShape, label: String) {
    var selected by remember(shapeVariant) { mutableStateOf(shapeVariant != FinsibleShape.Sharp) }

    FinsibleFilterChip(
        selected = selected,
        onSelectedChange = { selected = it },
        label = label,
        shapeVariant = shapeVariant
    )
}

