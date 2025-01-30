package org.chiuxah.blog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.chiuxah.blog.ui.main.NavRoute
import org.chiuxah.blog.ui.main.NavigateManager.turnTo

class MainViewModel : ViewModel() {
    val isLoginSuccessful = MutableStateFlow(false)


    fun login(username: String, password: String,navController: NavHostController) {
        turnTo(navController, NavRoute.HOME.name)
    }
}