package com.itsjeel01.finsiblefrontend.ui.component.accountstab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleFilterChip
import com.itsjeel01.finsiblefrontend.ui.model.item.AccountGroupUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AccountGroupFilterRow(
    groups: ImmutableList<AccountGroupUIModel>,
    selectedGroupId: Long?,
    onGroupSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        item(key = "all") {
            AnimatedVisibility(visible = true, enter = scaleIn() + fadeIn()) {
                FinsibleFilterChip(
                    selected = selectedGroupId == null,
                    onClick = { onGroupSelected(null) },
                    label = stringResource(R.string.all)
                )
            }
        }

        items(items = groups, key = { it.id }) { group ->
            AnimatedVisibility(visible = true, enter = scaleIn() + fadeIn()) {
                FinsibleFilterChip(
                    selected = selectedGroupId == group.id,
                    onClick = { onGroupSelected(group.id) },
                    label = group.name
                )
            }
        }
    }
}
