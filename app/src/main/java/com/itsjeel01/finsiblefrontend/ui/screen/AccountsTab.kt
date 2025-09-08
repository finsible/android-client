package com.itsjeel01.finsiblefrontend.ui.screen

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.itsjeel01.finsiblefrontend.ui.navigation.TabBackHandler

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AccountsTab() {
    TabBackHandler()

    Scaffold {
        Text("Accounts Screen")
    }
}