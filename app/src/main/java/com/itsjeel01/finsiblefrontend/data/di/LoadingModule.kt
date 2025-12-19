package com.itsjeel01.finsiblefrontend.data.di

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.itsjeel01.finsiblefrontend.ui.loading.LoadingIndicatorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    val loadingIndicatorManager: LoadingIndicatorManager
) : ViewModel()

@Composable
fun hiltLoadingManager(): LoadingIndicatorManager {
    val viewModel: LoadingViewModel = hiltViewModel()
    return viewModel.loadingIndicatorManager
}