package com.itsjeel01.finsiblefrontend.ui.component.newtransaction.section

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleSegmentedButtonRow
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleSegmentedButtonDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleSegmentedButtonOption
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonArrangement
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleSegmentedButtonVariant
import com.itsjeel01.finsiblefrontend.ui.model.event.NewTransactionUiEvent
import com.itsjeel01.finsiblefrontend.ui.model.state.NewTransactionFormState

@Composable
fun NewTransactionHeaderSection(
    state: NewTransactionFormState,
    onEvent: (NewTransactionUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val transactionAccentColor = state.transactionType.getColor()

    val segmentOptions = listOf(
        TransactionType.EXPENSE,
        TransactionType.INCOME,
        TransactionType.TRANSFER
    ).map { type ->
        FinsibleSegmentedButtonOption(
            id = type.name,
            label = stringResource(type.displayText),
            icon = { iconModifier ->
                Icon(
                    modifier = iconModifier,
                    painter = painterResource(type.icon),
                    contentDescription = null
                )
            }
        )
    }

    FinsibleSegmentedButtonRow(
        modifier = modifier.fillMaxWidth(),
        size = FinsibleSize.Medium,
        options = segmentOptions,
        selectedValue = state.transactionType.name,
        selectedTint = transactionAccentColor,
        arrangement = FinsibleSegmentedButtonArrangement.Separated,
        variant = FinsibleSegmentedButtonVariant.Tonal,
        onSelectedValueChange = { selected ->
            val type = selected?.let { value ->
                TransactionType.entries.firstOrNull { it.name == value }
            }
            if (type != null) {
                onEvent(NewTransactionUiEvent.TransactionTypeChanged(type))
            }
        },
        colors = FinsibleSegmentedButtonDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    )
}

