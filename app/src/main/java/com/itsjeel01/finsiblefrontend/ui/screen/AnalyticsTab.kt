package com.itsjeel01.finsiblefrontend.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.navigation.TabBackHandler

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalyticsTab() {
    TabBackHandler()

    Scaffold {
        Text("Analytics Screen")
    }
}