package com.itsjeel01.finsiblefrontend.ui.component.historytab.filtersbottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.model.SortOption
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun SortOptions(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)) {
        SectionLabel(stringResource(R.string.sort_by))

        Column(verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d4)) {
            SortOption.entries.chunked(2).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { option ->
                        RadioOption(
                            label = stringResource(option.displayText),
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(option) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}