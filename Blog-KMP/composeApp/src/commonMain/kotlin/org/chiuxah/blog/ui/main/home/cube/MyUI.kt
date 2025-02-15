package org.chiuxah.blog.ui.main.home.cube

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.chiuxah.blog.logic.bean.NavRoute
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.FollowCountResponse
import org.chiuxah.blog.logic.network.bean.UserResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.ui.main.home.logout
import org.chiuxah.blog.ui.uitls.UserManager
import org.chiuxah.blog.ui.uitls.compents.CustomRow
import org.chiuxah.blog.ui.uitls.compents.UserImage
import org.chiuxah.blog.viewmodel.NetworkViewModel

@Composable
fun MyUI(vm : NetworkViewModel, navHostController: NavHostController) {
    val userinfo = UserManager.userinfo
    val getFollowCountResponse by vm.getFollowCountResponse.collectAsState()
    var show by remember { mutableStateOf(false) }
    var followerCount by remember { mutableStateOf(0) }
    var followeeCount by remember { mutableStateOf(0) }
    if(!show) { vm.fetchGetFollowCount(UserManager.userinfo.id) }
    LaunchedEffect(getFollowCountResponse) {
        when(getFollowCountResponse) {
            is ApiResult.Success -> {
                val result = (getFollowCountResponse as ApiResult.Success<FollowCountResponse>).data
                if(result.state == StatusCode.OK.code) {
                    val data = result.data
                    followerCount = data.followersCount
                    followeeCount = data.followeeCount
                    show = true
                }
            }
            else -> null
        }
    }
    Scaffold {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(20.dp))
            CustomRow {
                UserImage(userinfo.photo)
            }
            CustomRow {
                Text(userinfo.username)
            }
            if(show) {
                CustomRow {
                    Text("粉丝 $followerCount 关注 $followeeCount")
                }
            }

            Spacer(Modifier.height(10.dp))
            CustomRow {
                Button(
                    onClick = {
                        logout(vm,navHostController)
                    }
                ) {
                    Text("退出登录")
                }
            }
        }
    }
}