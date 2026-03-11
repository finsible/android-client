package com.itsjeel01.finsiblefrontend.ui.component.accountstab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.model.AccountUiModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.bold
import com.itsjeel01.finsiblefrontend.ui.theme.semiBold
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

@Composable
fun AccountItem(
    model: AccountUiModel,
    modifier: Modifier = Modifier
) {
    val cornerRadius = FinsibleTheme.dimes.d12
    val borderWidth = FinsibleTheme.dimes.d4
    val fallbackColor = if (model.isPositiveBalance) FinsibleTheme.colors.income else FinsibleTheme.colors.expense

    val borderColor = if (model.groupColor != null) {
        FinsibleTheme.resolveColor(model.groupColor, fallbackColor)
    } else {
        fallbackColor
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = borderWidth)
                .background(
                    color = FinsibleTheme.colors.surfaceContainerLow,
                    shape = RoundedCornerShape(FinsibleTheme.dimes.d8)
                )
                .padding(
                    vertical = FinsibleTheme.dimes.d16,
                    horizontal = FinsibleTheme.dimes.d12
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(FinsibleTheme.dimes.d12))
                    .background(borderColor.copy(alpha = 0.2f))
                    .padding(FinsibleTheme.dimes.d12),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(resolveIcon(model.icon, R.drawable.ic_piggybank_outlined)),
                    contentDescription = stringResource(R.string.cd_account_icon, model.name),
                    modifier = Modifier.size(FinsibleTheme.dimes.d24),
                    tint = FinsibleTheme.colors.primaryContent,
                )
            }

            Spacer(Modifier.width(FinsibleTheme.dimes.d12))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = model.name,
                    style = FinsibleTheme.typography.t16.semiBold(),
                    color = FinsibleTheme.colors.primaryContent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (model.description.isNotBlank()) {
                    Spacer(Modifier.height(FinsibleTheme.dimes.d2))
                    Text(
                        text = model.description,
                        style = FinsibleTheme.typography.t14,
                        color = FinsibleTheme.colors.secondaryContent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(FinsibleTheme.dimes.d8))

            Text(
                text = model.formattedBalance,
                style = FinsibleTheme.typography.t16.bold(),
                color = FinsibleTheme.colors.primaryContent,
            )
        }
    }
}
