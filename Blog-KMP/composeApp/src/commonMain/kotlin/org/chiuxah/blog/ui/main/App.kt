package org.chiuxah.blog.ui.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.chiuxah.blog.logic.enums.NavRoute
import org.chiuxah.blog.ui.main.home.HomeUI
import org.chiuxah.blog.ui.main.login.LoginUI
import org.chiuxah.blog.ui.main.reg.RegUI
import org.chiuxah.blog.ui.uitls.NavigateManager.ANIMATION_SPEED
import org.chiuxah.blog.viewmodel.MainViewModel

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val vm = remember { MainViewModel() }
        AppNavHost(navController,vm)
    }
}

@Composable
fun AppNavHost(navController : NavHostController,vm : MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.LOGIN.name,
        enterTransition = {
            scaleIn(animationSpec = tween(durationMillis = ANIMATION_SPEED)) + expandVertically(expandFrom = Alignment.CenterVertically,animationSpec = tween(durationMillis = ANIMATION_SPEED))
        },
        exitTransition = {
            scaleOut(animationSpec = tween(durationMillis = ANIMATION_SPEED)) + shrinkVertically(shrinkTowards = Alignment.CenterVertically,animationSpec = tween(durationMillis = ANIMATION_SPEED))
        }
    ) {
        composable(NavRoute.LOGIN.name) {
            Scaffold {
                LoginUI(navController,vm)
            }

        }
        composable(NavRoute.HOME.name) {
            Scaffold {
                HomeUI(navController,vm)
            }
        }
        composable(NavRoute.REG.name) {
            Scaffold {
                RegUI(navController,vm)
            }
        }
    }
}