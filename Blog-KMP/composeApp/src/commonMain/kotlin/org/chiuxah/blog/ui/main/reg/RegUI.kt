package org.chiuxah.blog.ui.main.reg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.chiuxah.blog.logic.bean.NavRoute
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.uitls.MultiPlatUtils.showMsg
import org.chiuxah.blog.ui.uitls.NavigateManager.turnToAndClear
import org.chiuxah.blog.ui.uitls.compents.CustomRow
import org.chiuxah.blog.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegUI(navController: NavHostController,vm : MainViewModel) {
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
                            text = "注册",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = {
                            turnToAndClear(navController,NavRoute.LOGIN.name)
                        }) {
                            Icon(Icons.Default.Close, contentDescription = null,tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            RegInfoUI(vm)
        }
    }
}

@Composable
fun RegInfoUI( vm : MainViewModel) {
    var inputUsername by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var inputPassword2 by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.regResponse.collect { response ->
                when (response) {
                    is ApiResult.Success -> {
                        val result = response.data
                        showMsg(result.msg)
                    }
                    is ApiResult.Error -> showMsg("网络错误")
                }
        }
    }


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
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        TextField(
            modifier = Modifier.weight(1f).padding(horizontal = 25.dp),
            value = inputPassword2,
            onValueChange = { inputPassword2 = it },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = {
                Text("再次输入密码")
            },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
            },
            shape = MaterialTheme.shapes.medium
        )
    }
    Spacer(modifier = Modifier.height(25.dp))

    CustomRow {
        Button(
            onClick = {
                vm.fetchReg(inputUsername,inputPassword)
            },
            enabled = checkPassword(inputPassword,inputPassword2).first
        ) { Text("注册") }
    }
}

fun checkPassword(pwd1 : String,pwd2 : String) : Pair<Boolean,String> {
    if(pwd2 != pwd2) return Pair(false,"两次密码不匹配")
    if(pwd1.length <= 8) return Pair(false,"密码太短 应大于8位")
    return Pair(true,"密码合理")
}
