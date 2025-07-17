package com.itsjeel01.finsiblefrontend.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.InputFieldSize
import com.itsjeel01.finsiblefrontend.common.Utils
import com.itsjeel01.finsiblefrontend.ui.component.base.BaseDateInput
import com.itsjeel01.finsiblefrontend.ui.component.base.CommonProps
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionFormViewModel

@Composable
fun TransactionDatePicker(modifier: Modifier = Modifier) {
    val focusManager = LocalFocusManager.current

    val transactionFormViewModel: TransactionFormViewModel = hiltViewModel()
    val transactionDate = transactionFormViewModel.transactionDate.collectAsState().value
        ?: System.currentTimeMillis()
    val transactionType = transactionFormViewModel.transactionType.collectAsState().value

    val inputProps = CommonProps(
        modifier = modifier,
        placeholder = "Date",
        enabled = true,
        accentColor = Utils.getTransactionColor(transactionType),
        trailingIcon = {
            Icon(
                painterResource(R.drawable.ic_calendar),
                contentDescription = "Calendar Icon",
                tint = MaterialTheme.colorScheme.outline
            )
        },
        size = InputFieldSize.Large,
    )

    BaseDateInput(
        date = transactionDate,
        onValueChange = { transactionFormViewModel.setTransactionDate(it) },
        commonProps = inputProps,
        clearFocus = { focusManager.clearFocus() },
    )
}
