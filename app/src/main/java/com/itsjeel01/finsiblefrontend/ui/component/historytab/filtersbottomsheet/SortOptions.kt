package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleRadioButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleRadioButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold

@Composable
fun SortOptions(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FilterSheetSpacing.sectionHeaderGap)) {
        FinsibleText(
            text = stringResource(R.string.sort_by),
            textStyle = FinsibleTheme.typography.labelSm.semiBold(),
            colorVariant = FinsibleTextColorVariant.Secondary,
            uppercase = true
        )

        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd)) {
            SortOption.entries.chunked(2).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.inlineMd),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { option ->
                        val isSelected = option == selectedOption
                        FinsibleRadioButton(
                            label = stringResource(option.displayText),
                            selected = isSelected,
                            onSelectedChange = { onOptionSelected(option) },
                            size = FinsibleSize.Small,
                            colors = FinsibleRadioButtonDefaults.colors(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}