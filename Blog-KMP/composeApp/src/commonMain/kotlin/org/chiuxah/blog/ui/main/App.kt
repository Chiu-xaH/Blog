package org.chiuxah.blog.ui.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.chiuxah.blog.logic.uitls.PlatformType
import org.chiuxah.blog.logic.uitls.PlatformsManager
import org.chiuxah.blog.ui.main.NavigateManager.ANIMATION_SPEED
import org.chiuxah.blog.ui.main.NavigateManager.turnTo
import org.chiuxah.blog.ui.uitls.CustomRow
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
fun HomeUI(navController : NavHostController,vm : MainViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                turnTo(navController,NavRoute.LOGIN.name)
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("退出登录")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUI(navController : NavHostController,vm : MainViewModel) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "登录",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                actions = {
                    if(PlatformsManager.platformType != PlatformType.DESKTOP) {
                        Row {
                            IconButton(onClick = {

                            }) {
                                Icon(Icons.Default.Close, contentDescription = null,tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                },
                navigationIcon = {
                    Column(modifier = Modifier
                        .padding(horizontal = 23.dp)) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "博客",
                            fontSize = 38.sp,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LoginInfoUI(navController,vm)
        }
    }
}

@Composable
fun LoginInfoUI(navController : NavHostController,vm : MainViewModel) {
    var inputUsername by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    Spacer(modifier = Modifier.height(25.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        TextField(
            modifier = Modifier.weight(1f).padding(horizontal = 25.dp),
            value = inputUsername,
            onValueChange = { inputUsername = it },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = {
                Text("手机号")
            },
            leadingIcon = {
                Icon(Icons.Default.Call, contentDescription = null)
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        inputUsername = ""
                    }
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            },
            shape = MaterialTheme.shapes.medium
        )
    }
    Spacer(modifier = Modifier.height(25.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        TextField(
            modifier = Modifier.weight(1f).padding(horizontal = 25.dp),
            value = inputPassword,
            onValueChange = { inputPassword = it },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = {
                Text("密码")
            },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
//                IconButton(
//                    onClick = {
//                        inputPassword = ""
//                    }
//                ) {
//                    Icon(Icons.Default., contentDescription = null)
//                }
            },
            shape = MaterialTheme.shapes.medium
        )
    }
    Spacer(modifier = Modifier.height(25.dp))

    CustomRow {
        Button(
            onClick = {
                vm.login(inputUsername,inputPassword,navController)
//                turnTo(navController,NavRoute.HOME.name)
            },
        ) { Text("登录") }
        Spacer(Modifier.width(10.dp))
        FilledTonalButton(
            onClick = {

            },
        ) { Text("注册") }
        Spacer(Modifier.width(10.dp))
        TextButton(
            onClick = {

            },
        ) { Text("忘记密码") }
    }
}

enum class NavRoute {
    LOGIN,HOME
}

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
    const val ANIMATION_SPEED = 400
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
    }
}