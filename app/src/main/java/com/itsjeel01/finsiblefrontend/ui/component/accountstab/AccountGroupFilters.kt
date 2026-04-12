package com.itsjeel01.finsiblefrontend.ui.component.accountstab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleFilterChipVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountGroupUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.collections.immutable.ImmutableList


@Composable
fun AccountGroupFilters(
    groups: ImmutableList<AccountGroupUIModel>,
    selectedGroupId: Long?,
    onGroupSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {

    val chips = buildList<@Composable () -> Unit> {
        add {
            val isSelected = selectedGroupId == null
            FinsibleFilterChip(
                selected = selectedGroupId == null,
                onSelectedChange = { if (!isSelected) onGroupSelected(null) },
                label = stringResource(R.string.all),
                size = FinsibleSize.Small,
                shapeVariant = FinsibleShape.Pill,
                variant = FinsibleFilterChipVariant.Filled,
                selectedTint = FinsibleTheme.colors.primaryContent,
                inverted = true,
                enforceMinTouchTarget = false
            )
        }

        groups.forEach { group ->
            add {
                val isSelected = selectedGroupId == group.id
                FinsibleFilterChip(
                    selected = isSelected,
                    onSelectedChange = { if (!isSelected) onGroupSelected(group.id) },
                    label = group.name,
                    size = FinsibleSize.Small,
                    shapeVariant = FinsibleShape.Pill,
                    variant = FinsibleFilterChipVariant.Filled,
                    selectedTint = FinsibleTheme.colors.primaryContent,
                    inverted = true,
                    enforceMinTouchTarget = false
                )
            }
        }
    }

    val chipKeys = buildList<Any> {
        add("all")
        groups.forEach { add(it.id) }
    }

    FinsibleChipsRow(
        chips = chips,
        chipKeys = chipKeys,
        wrap = false,
        modifier = modifier
    )
}