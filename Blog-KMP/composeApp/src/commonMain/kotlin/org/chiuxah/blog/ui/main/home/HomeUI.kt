package org.chiuxah.blog.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.chiuxah.blog.logic.enums.NavRoute
import org.chiuxah.blog.ui.uitls.NavigateManager.turnTo
import org.chiuxah.blog.ui.uitls.NavigateManager.turnToAndClear
import org.chiuxah.blog.viewmodel.MainViewModel

@Composable
fun HomeUI(navController : NavHostController, vm : MainViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                turnToAndClear(navController, NavRoute.LOGIN.name)
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("退出登录")
        }
    }
}
