package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDimes.Companion.inverted
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

/** Color configuration for [FinsibleSegmentedButtonRow]. */
@Immutable
data class FinsibleSegmentedButtonColors(
    val activeContentColor: Color,
    val activeContainerColor: Color,
    val inactiveContentColor: Color,
    val inactiveBorderColor: Color,
    val inactiveContainerColor: Color
)

/** Defaults factory for [FinsibleSegmentedButtonRow] colors. */
object FinsibleSegmentedButtonDefaults {

    /** Brand-themed segmented button colors. */
    @Composable
    fun colors(
        activeContentColor: Color = FinsibleTheme.colors.primaryContent,
        activeContainerColor: Color = FinsibleTheme.colors.surface,
        inactiveContentColor: Color = FinsibleTheme.colors.secondaryContent,
        inactiveBorderColor: Color = FinsibleTheme.colors.transparent,
        inactiveContainerColor: Color = FinsibleTheme.colors.input
    ) = FinsibleSegmentedButtonColors(
        activeContentColor, activeContainerColor,
        inactiveContentColor, inactiveBorderColor, inactiveContainerColor
    )
}

/** Themed single-choice segmented button row with Finsible brand colors. */
@Composable
fun <T> FinsibleSegmentedButtonRow(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    colors: FinsibleSegmentedButtonColors = FinsibleSegmentedButtonDefaults.colors(),
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
                    activeContentColor = colors.activeContentColor,
                    activeContainerColor = colors.activeContainerColor,
                    inactiveContentColor = colors.inactiveContentColor,
                    inactiveBorderColor = colors.inactiveBorderColor,
                    inactiveContainerColor = colors.inactiveContainerColor
                ),
                selected = isSelected,
                label = { label(option) },
                icon = { icon(option, isSelected) }
            )
        }
    }
}
