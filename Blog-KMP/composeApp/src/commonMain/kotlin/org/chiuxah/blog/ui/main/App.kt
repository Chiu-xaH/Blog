package org.chiuxah.blog.ui.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.chiuxah.blog.logic.bean.NavRoute
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.uitls.PreferencesManager
import org.chiuxah.blog.ui.main.home.HomeUI
import org.chiuxah.blog.ui.main.login.LoginUI
import org.chiuxah.blog.ui.main.reg.RegUI
import org.chiuxah.blog.ui.uitls.NavigateManager.ANIMATION_SPEED
import org.chiuxah.blog.ui.uitls.NavigateManager.fadeAnimation
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
    var firstRoute by remember { mutableStateOf(NavRoute.LOGIN.name) }
    @Composable
    fun MainUI() {
        NavHost(
            navController = navController,
            startDestination = firstRoute,
            enterTransition = {
                fadeAnimation.enter
            },
            exitTransition = {
                fadeAnimation.exit
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
    // 如果有cookie保存过，则验证登录，否则直接进入登录页面
    val check by remember { mutableStateOf(PreferencesManager.settings.getStringOrNull(PreferencesManager.KEY_COOKIE) != null) }

    if(check) {
        vm.fetchCheckLogin()
        var loading by remember { mutableStateOf(true) }
        val checkLoginResponse by vm.checkLoginResponse.collectAsState()
        // 检查登录
        LaunchedEffect(checkLoginResponse) {
            when(checkLoginResponse) {
                is ApiResult.Success -> {
                    val result = (checkLoginResponse as ApiResult.Success<MsgResponse>).data
                    firstRoute = if(result.state == StatusCode.SUCCESS.code) {
                        NavRoute.HOME.name
                    } else {
                        NavRoute.LOGIN.name
                    }
                    loading = false
                }
                is ApiResult.Error -> {
                    loading = false
                }
                else ->  {
                    // 等待
                }
            }
        }
        if(loading) {
            Box() {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        } else {
            MainUI()
        }
    } else {
        MainUI()
    }
}