package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.fin.ButtonConfig
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentSize
import com.itsjeel01.finsiblefrontend.ui.component.fin.ComponentType
import com.itsjeel01.finsiblefrontend.ui.component.fin.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.fin.IconPosition
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun OnboardingNavigationButtons(
    currentItem: Int,
    isLastItem: () -> Boolean,
    onSecondaryButtonClick: () -> Unit,
    onPrimaryButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d24)
    ) {
        if (currentItem > 0 && !isLastItem()) {
            FinsibleButton(
                text = stringResource(R.string.back),
                onClick = onSecondaryButtonClick,
                modifier = Modifier.weight(1f),
                config = ButtonConfig(
                    size = ComponentSize.Medium,
                    type = ComponentType.Secondary,
                    fullWidth = true
                )
            )
        }

        val label = if (currentItem == 0) stringResource(R.string.get_started)
        else if (isLastItem()) stringResource(R.string.continue_with_google)
        else stringResource(R.string.next)

        val icon = if (isLastItem()) R.drawable.ic_google
        else if (currentItem == 0) R.drawable.ic_right_arrow_dotted
        else null

        val iconPosition = if (isLastItem()) IconPosition.BeforeLabel else IconPosition.Trailing

        val type = if (currentItem == 0) ComponentType.Brand else ComponentType.Primary

        Column(
            Modifier
                .wrapContentHeight()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FinsibleButton(
                text = label,
                onClick = onPrimaryButtonClick,
                config = ButtonConfig(
                    size = ComponentSize.Medium,
                    icon = icon,
                    iconPosition = iconPosition,
                    type = type,
                    fullWidth = true,
                    tintIcon = !isLastItem()
                )
            )
            Spacer(Modifier.height(FinsibleTheme.dimes.d8))

            if (isLastItem()) {
                FinsibleButton(
                    modifier = Modifier.padding(vertical = FinsibleTheme.dimes.d8),
                    text = stringResource(R.string.go_back),
                    onClick = onSecondaryButtonClick,
                    config = ButtonConfig(
                        size = ComponentSize.Small,
                        type = ComponentType.Tertiary,
                        fullWidth = false
                    )
                )
            } else {
                Spacer(Modifier.height(FinsibleTheme.dimes.d48))
            }
        }
    }
}
