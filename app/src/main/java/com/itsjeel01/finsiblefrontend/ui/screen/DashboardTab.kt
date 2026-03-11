package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.itsjeel01.finsiblefrontend.R

@Composable
fun DashboardTab() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(stringResource(R.string.dashboard_screen))
    }
}