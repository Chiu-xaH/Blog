package org.chiuxah.blog.ui.main.login

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chiuxah.blog.logic.enums.NavRoute
import org.chiuxah.blog.logic.enums.PlatformType
import org.chiuxah.blog.logic.enums.StatusCode
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.uitls.MultiPlatUtils
import org.chiuxah.blog.logic.uitls.MultiPlatUtils.showMsg
import org.chiuxah.blog.logic.uitls.PlatformsManager
import org.chiuxah.blog.ui.uitls.NavigateManager.turnTo
import org.chiuxah.blog.ui.uitls.NavigateManager.turnToAndClear
import org.chiuxah.blog.ui.uitls.compents.CustomRow
import org.chiuxah.blog.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUI(navController : NavHostController, vm : MainViewModel) {
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
fun LoginInfoUI(navController : NavHostController, vm : MainViewModel) {
//    val loginResponse by vm.loginResponse.collectAsState()
    // 监听
    LaunchedEffect(Unit) { // 监听 SharedFlow，不用传 `loginResponse`
        vm.loginResponse.collect { response ->
            when (response) {
                is ApiResult.Success -> {
                    val result = response.data
                    if (result.state == StatusCode.SUCCESS.code) {
                        turnToAndClear(navController, NavRoute.HOME.name)
                    } else {
                        showMsg(result.msg)
                    }
                }
                is ApiResult.Error -> showMsg("网络错误")
            }
        }
//        if(!isReg) {
//
//        } else {
//            vm.regResponse.collect { response ->
//                when (response) {
//                    is ApiResult.Success -> {
//                        val result = response.data
//                        showMsg(result.msg)
//                    }
//                    is ApiResult.Error -> showMsg("网络错误")
//                }
//            }
//        }
    }

//    LaunchedEffect(loginResponse) {
//        when(loginResponse) {
//            is ApiResult.Success -> {
//                val result = (loginResponse as ApiResult.Success<LoginResponse>).data
//                if(result.state == StatusCode.SUCCESS.code) {
//                    // 登陆成功
//                    turnTo(navController, NavRoute.HOME.name)
//                } else {
//                    showMsg(result.msg)
//                    // 登陆失败
//                }
//            }
//            is ApiResult.Error -> {
//                showMsg("网络错误")
//            }
//            else ->  {}
//        }
//    }

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
                vm.fetchLogin(inputUsername,inputPassword)
            },
        ) { Text("登录") }
        Spacer(Modifier.width(10.dp))
        FilledTonalButton(
            onClick = {
                turnToAndClear(navController,NavRoute.REG.name)
            },
        ) { Text("注册") }
        Spacer(Modifier.width(10.dp))
        TextButton(
            onClick = {
                showMsg("正在开发")
            },
        ) { Text("忘记密码") }
    }
}
