package com.itsjeel01.finsiblefrontend.ui.component.accountstab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.data.local.entity.AccountGroupEntity
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AccountGroupFilterRow(
    groups: ImmutableList<AccountGroupEntity>,
    selectedGroupId: Long?,
    onGroupSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)
    ) {
        item(key = "all") {
            AccountGroupFilterChip(
                text = stringResource(R.string.all),
                isSelected = selectedGroupId == null,
                onClick = { onGroupSelected(null) }
            )
        }

        items(items = groups, key = { it.id }) { group ->
            AccountGroupFilterChip(
                text = group.name,
                isSelected = selectedGroupId == group.id,
                onClick = { onGroupSelected(group.id) }
            )
        }
    }
}

@Composable
private fun AccountGroupFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn() + fadeIn(),
        modifier = modifier
    ) {
        FilterChip(
            selected = isSelected,
            onClick = onClick,
            label = {
                Text(
                    text = text,
                    style = FinsibleTheme.typography.t14,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                )
            },
            shape = RoundedCornerShape(FinsibleTheme.dimes.d20),
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = FinsibleTheme.colors.inverse,
                selectedLabelColor = FinsibleTheme.colors.same,
                containerColor = FinsibleTheme.colors.surfaceContainer,
                labelColor = FinsibleTheme.colors.primaryContent
            ),
            border = FilterChipDefaults.filterChipBorder(
                borderColor = FinsibleTheme.colors.border,
                selectedBorderColor = FinsibleTheme.colors.brandAccent,
                enabled = true,
                selected = isSelected
            )
        )
    }
}
