package org.chiuxah.blog.ui.uitls

import androidx.navigation.NavHostController

object NavigateManager {
    fun turnTo(navController : NavHostController, route : String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun turnToAndClear(navController: NavHostController, route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }


    const val ANIMATION_SPEED = 400
}