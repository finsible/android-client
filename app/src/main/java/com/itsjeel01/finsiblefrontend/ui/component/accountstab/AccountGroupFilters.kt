package com.itsjeel01.finsiblefrontend.ui.component.accountstab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleChipsRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountGroupUIModel

/** Horizontal scrollable row of account-group filter chips. */
@Composable
fun AccountGroupFilters(
    groups: List<AccountGroupUIModel>,
    selectedGroupId: Long?,
    onGroupSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val chips = buildList<@Composable () -> Unit> {
        add {
            FinsibleFilterChip(
                label = "All",
                selected = selectedGroupId == null,
                onSelectedChange = { if (it) onGroupSelected(null) },
                size = FinsibleSize.Small,
            )
        }
        groups.forEach { group ->
            add {
                FinsibleFilterChip(
                    label = group.name,
                    selected = selectedGroupId == group.id,
                    onSelectedChange = { if (it) onGroupSelected(group.id) },
                    size = FinsibleSize.Small,
                )
            }
        }
    }

    FinsibleChipsRow(
        chips = chips,
        modifier = modifier,
    )
}
