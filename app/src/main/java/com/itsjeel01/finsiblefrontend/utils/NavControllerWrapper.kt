package com.itsjeel01.finsiblefrontend.utils

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavControllerWrapper @Inject constructor() : ViewModel() {
    var navController: NavController? = null
}