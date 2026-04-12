package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextColorVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold

@Composable
fun StepTitle(currentStep: Int, totalSteps: Int) {
    val stepTitles = listOf(
        stringResource(R.string.step_enter_amount),
        stringResource(R.string.step_date_schedule),
        stringResource(R.string.step_select_category),
        stringResource(R.string.step_select_accounts),
        stringResource(R.string.step_add_description)
    )
    val title = stepTitles.getOrElse(currentStep) { stepTitles.first() }

    Spacer(Modifier.height(FinsibleTheme.dimes.d16))
    Column(Modifier.padding(vertical = FinsibleTheme.dimes.d16)) {
        FinsibleText(
            text = title,
            variant = FinsibleTextVariant.LargeTitleExtraBold,
            colorVariant = FinsibleTextColorVariant.Primary,
            textStyleOverride = FinsibleTheme.typography.t24.extraBold()
        )
        Spacer(Modifier.height(FinsibleTheme.dimes.d4))

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d8)) {
            FinsibleText(
                text = stringResource(R.string.step_counter, currentStep + 1, totalSteps),
                variant = FinsibleTextVariant.BodyRegular,
                colorVariant = FinsibleTextColorVariant.Secondary,
                textStyleOverride = FinsibleTheme.typography.t16
            )

            val progress by animateFloatAsState(
                targetValue = currentStep.toFloat() / totalSteps.toFloat(),
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing
                ),
                label = "progress"
            )

            Box(
                modifier = Modifier
                    .height(FinsibleTheme.dimes.d3)
                    .width(FinsibleTheme.dimes.d96)
                    .clip(RoundedCornerShape(FinsibleTheme.dimes.d2))
                    .background(FinsibleTheme.colors.border)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(RoundedCornerShape(FinsibleTheme.dimes.d2))
                        .background(FinsibleTheme.colors.brandAccent)
                )
            }
        }
    }
}