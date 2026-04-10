package com.itsjeel01.finsiblefrontend.ui.component.accountstab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleIconBadge
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleIconBadgeDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.model.uimodel.AccountUIModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.util.resolveIcon

@Composable
fun AccountListItem(
    model: AccountUIModel,
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
                    vertical = FinsibleTheme.dimes.d12,
                    horizontal = FinsibleTheme.dimes.d12
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
        ) {
            FinsibleIconBadge(
                icon = {
                    Icon(
                        painter = painterResource(resolveIcon(model.icon, com.composables.icons.lucide.R.drawable.lucide_ic_piggy_bank)),
                        contentDescription = stringResource(R.string.cd_account_icon, model.name)
                    )
                },
                size = FinsibleSize.Large,
                shapeVariant = FinsibleShape.Rounded,
                colors = FinsibleIconBadgeDefaults.colors(
                    iconTint = borderColor,
                    backgroundTint = borderColor
                ),
                backgroundAlpha = 0.2f
            )

            Column(modifier = Modifier.weight(1f)) {
                FinsibleText(
                    text = model.name,
                    variant = FinsibleTextVariant.BodySemiBold,
                    colorVariant = FinsibleTextColorVariant.Primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (model.description.isNotBlank()) {
                    Spacer(Modifier.height(FinsibleTheme.dimes.d2))
                    FinsibleText(
                        text = model.description,
                        variant = FinsibleTextVariant.SmallBodyRegular,
                        colorVariant = FinsibleTextColorVariant.Secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            FinsibleText(
                text = model.formattedBalance,
                variant = FinsibleTextVariant.SmallBodyBold,
                colorVariant = FinsibleTextColorVariant.Primary
            )
        }
    }
}
