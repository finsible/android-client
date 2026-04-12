package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleText
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTextVariant

@Composable
fun DashboardTab() {
    Box(modifier = Modifier.fillMaxSize()) {
        FinsibleText(text = stringResource(R.string.dashboard_screen), variant = FinsibleTextVariant.BodyRegular)
    }
}