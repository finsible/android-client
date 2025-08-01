package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.itsjeel01.finsiblefrontend.ui.util.GoogleLoginUtil
import com.itsjeel01.finsiblefrontend.ui.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(onLogout: () -> Unit) {

    val authViewModel: AuthViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    // logout button inside scaffold
    Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)) { _ ->
        Button(
            onClick = {
                GoogleLoginUtil.logout(
                    coroutineScope,
                    authViewModel
                )
                onLogout()
            }
        ) {
            Text("Logout")
        }
    }
}