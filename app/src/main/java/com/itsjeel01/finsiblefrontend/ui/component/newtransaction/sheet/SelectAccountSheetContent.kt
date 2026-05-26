package com.itsjeel01.finsiblefrontend.ui.component.newtransaction.sheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleTextField
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectAccountBottomSheetContent(
    accounts: List<AccountUIModel>,
    selectedAccountId: Long?,
    accentColor: Color,
    surfaceColor: Color,
    onAccountSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val sp = FinsibleTheme.spacing

    val groupedFilteredAccounts = remember(accounts, query) {
        val normalized = query.trim()
        val filtered = if (normalized.isEmpty()) accounts else accounts.filter {
            it.name.contains(normalized, ignoreCase = true)
        }
        filtered.groupBy { it.groupName ?: "Other" }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = sp.insetLg)
    ) {
        FinsibleTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = "Search account",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_search_outline),
                    contentDescription = stringResource(R.string.search_transactions_placeholder)
                )
            },
            modifier = Modifier.padding(horizontal = sp.insetLg)
        )

        Spacer(modifier = Modifier.height(sp.insetLg))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(sp.insetXs)
        ) {
            groupedFilteredAccounts.forEach { (groupName, groupAccounts) ->
                stickyHeader {
                    FinsibleText(
                        text = groupName.uppercase(),
                        textStyle = FinsibleTheme.typography.labelSm.semiBold(),
                        colorVariant = FinsibleTextColorVariant.Secondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(FinsibleTheme.colors.surfaceSunken)
                            .padding(horizontal = sp.insetLg, vertical = sp.inlineMd)
                    )
                }

                items(groupAccounts, key = { it.id }, contentType = { "AccountListItem" }) { account ->
                    SelectAccountBottomSheetItem(
                        account = account,
                        isSelected = account.id == selectedAccountId,
                        accentColor = accentColor,
                        surfaceColor = surfaceColor,
                        onClick = { onAccountSelected(account.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectAccountBottomSheetItem(
    account: AccountUIModel,
    isSelected: Boolean,
    accentColor: Color,
    surfaceColor: Color,
    onClick: () -> Unit
) {
    val sp = FinsibleTheme.spacing
    val type = FinsibleTheme.typography
    val backgroundColor = if (isSelected) surfaceColor else FinsibleTheme.colors.cardSurface
    val borderColor = if (isSelected) accentColor else FinsibleTheme.colors.borderSubtle
    val iconRes = remember(account.icon) {
        resolveIcon(token = account.icon.ifBlank { null }, fallbackIcon = com.composables.icons.tabler.outline.R.drawable.tabler_ic_wallet_outline)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sp.gapMd)
            .clip(RoundedCornerShape(sp.gapMd))
            .background(backgroundColor)
            .border(width = if (isSelected) FinsibleTheme.stroke.thin else FinsibleTheme.stroke.hairline, color = borderColor, shape = RoundedCornerShape(sp.gapMd))
            .clickable(onClick = onClick)
            .padding(horizontal = sp.insetLg, vertical = sp.insetLg),
        horizontalArrangement = Arrangement.spacedBy(sp.gapMd),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(FinsibleTheme.sizes.icon.lg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = FinsibleTheme.colors.contentPrimary,
                modifier = Modifier.size(FinsibleTheme.sizes.icon.md)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = sp.gapSm)
        ) {
            FinsibleText(
                text = account.name,
                textStyle = type.bodyLg.semiBold(),
                colorVariant = FinsibleTextColorVariant.Primary
            )
            Spacer(modifier = Modifier.height(sp.insetXs))
            FinsibleText(
                text = account.description,
                textStyle = type.bodyMd,
                colorVariant = FinsibleTextColorVariant.Tertiary
            )
            Spacer(modifier = Modifier.height(sp.insetXs))
            FinsibleText(
                text = account.formattedBalance,
                textStyle = type.bodyMd,
                colorVariant = FinsibleTextColorVariant.Secondary
            )
        }

        if (isSelected) {
            Icon(
                painter = painterResource(id = com.composables.icons.tabler.outline.R.drawable.tabler_ic_check_outline),
                contentDescription = "Selected",
                tint = accentColor,
                modifier = Modifier.size(FinsibleTheme.sizes.icon.lg)
            )
        }
    }
}

