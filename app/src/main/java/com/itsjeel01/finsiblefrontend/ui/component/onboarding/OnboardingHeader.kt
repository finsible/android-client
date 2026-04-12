package com.itsjeel01.finsiblefrontend.ui.component.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun OnboardingHeader(onSkip: () -> Unit = {}, isLastItem: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(R.string.cd_finsible_logo),
            modifier = Modifier.height(FinsibleTheme.dimes.d64),
            contentScale = ContentScale.Fit
        )

        if (!isLastItem) {
            FinsibleButton(
                onClick = onSkip,
                text = stringResource(R.string.skip),
                variant = FinsibleButtonVariant.Text,
                size = FinsibleSize.Small,
                shapeVariant = FinsibleShape.Rounded,
                colors = FinsibleButtonDefaults.colors(
                    variant = FinsibleButtonVariant.Text,
                    contentColor = FinsibleTheme.colors.secondaryContent
                )
            )
        }
    }
}
