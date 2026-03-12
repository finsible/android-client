package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDimes.Companion.inverted
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Configuration for [FinsibleSegmentedButtonRow]. */
data class SegmentedButtonConfig(
    val activeContentColor: Color = Color.Unspecified,
    val activeContainerColor: Color = Color.Unspecified,
    val inactiveContentColor: Color = Color.Unspecified,
    val inactiveBorderColor: Color = Color.Unspecified,
    val inactiveContainerColor: Color = Color.Unspecified
)

/** Themed single-choice segmented button row with Finsible brand colors. */
@Composable
fun <T> FinsibleSegmentedButtonRow(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    config: SegmentedButtonConfig = SegmentedButtonConfig(),
    label: @Composable (T) -> Unit,
    icon: @Composable (T, Boolean) -> Unit = { _, _ -> }
) {
    val cornerRadius = FinsibleTheme.dimes.d12

    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
        space = FinsibleTheme.dimes.d8.inverted()
    ) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            SegmentedButton(
                shape = RoundedCornerShape(cornerRadius),
                onClick = { if (!isSelected) onOptionSelected(option) },
                colors = SegmentedButtonDefaults.colors().copy(
                    activeContentColor = config.activeContentColor.takeOrDefault(FinsibleTheme.colors.primaryContent),
                    activeContainerColor = config.activeContainerColor.takeOrDefault(FinsibleTheme.colors.surface),
                    inactiveContentColor = config.inactiveContentColor.takeOrDefault(FinsibleTheme.colors.secondaryContent),
                    inactiveBorderColor = config.inactiveBorderColor.takeOrDefault(FinsibleTheme.colors.transparent),
                    inactiveContainerColor = config.inactiveContainerColor.takeOrDefault(FinsibleTheme.colors.input)
                ),
                selected = isSelected,
                label = { label(option) },
                icon = { icon(option, isSelected) }
            )
        }
    }
}
