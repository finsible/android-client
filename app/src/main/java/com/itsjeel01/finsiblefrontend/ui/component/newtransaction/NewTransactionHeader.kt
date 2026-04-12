package com.itsjeel01.finsiblefrontend.ui.component.newtransaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleShape
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

@Composable
fun NewTransactionHeader(onClose: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FinsibleButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterStart),
            iconOnly = true,
            variant = FinsibleButtonVariant.Text,
            size = FinsibleSize.Large,
            shapeVariant = FinsibleShape.Circle,
            colors = FinsibleButtonDefaults.colors(
                variant = FinsibleButtonVariant.Text,
                contentColor = FinsibleTheme.colors.secondaryContent
            ),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(R.string.cd_close),
                    tint = LocalContentColor.current
                )
            }
        )
        FinsibleText(
            text = stringResource(R.string.new_transaction),
            variant = FinsibleTextVariant.MediumTitleBold,
            color = FinsibleTheme.colors.primaryContent,
            textAlign = TextAlign.Center
        )
    }
}