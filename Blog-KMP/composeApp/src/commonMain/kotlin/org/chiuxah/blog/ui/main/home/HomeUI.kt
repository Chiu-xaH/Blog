package org.chiuxah.blog.ui.main.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import blog.composeapp.generated.resources.Res
import blog.composeapp.generated.resources.add_2
import blog.composeapp.generated.resources.article
import blog.composeapp.generated.resources.article_filled
import blog.composeapp.generated.resources.deployed_code
import blog.composeapp.generated.resources.deployed_code_filled
import org.chiuxah.blog.logic.bean.HomeRoute
import org.chiuxah.blog.logic.bean.NavRoute
import org.chiuxah.blog.logic.bean.NavigationBarItemData
import org.chiuxah.blog.logic.bean.PlatformType
import org.chiuxah.blog.logic.uitls.PlatformsManager
import org.chiuxah.blog.logic.uitls.PreferencesManager
import org.chiuxah.blog.logic.uitls.PreferencesManager.KEY_COOKIE
import org.chiuxah.blog.logic.uitls.PreferencesManager.KEY_USERNAME
import org.chiuxah.blog.ui.main.home.blog.BlogsUI
import org.chiuxah.blog.ui.uitls.NavigateManager.fadeAnimation
import org.chiuxah.blog.ui.uitls.NavigateManager.turnToAndClear
import org.chiuxah.blog.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUI(navController : NavHostController, vm : MainViewModel) {
    val navHomeController = rememberNavController()
    val isDesktop = PlatformsManager.platformType == PlatformType.DESKTOP
    val username = PreferencesManager.settings.getStringOrNull(KEY_USERNAME) ?: "游客"
    val barItems = listOf(
        NavigationBarItemData(
            HomeRoute.BLOG.name,
            "博客",
            painterResource(Res.drawable.article),
            painterResource(Res.drawable.article_filled)
        ),
        NavigationBarItemData(
            HomeRoute.ADD.name,
            "发布",
            painterResource(Res.drawable.add_2),
            painterResource(Res.drawable.add_2)
        ),
        NavigationBarItemData(
            HomeRoute.CUBE.name,
            "我",
            painterResource(Res.drawable.deployed_code),
            painterResource(Res.drawable.deployed_code_filled)
        ),
    )
    @Composable
    fun BarItemUI() {
        barItems.forEach { item ->
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale = animateFloatAsState(
                targetValue = if (isPressed) 0.8f else 1f, // 按下时为0.9，松开时为1
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                label = "" // 使用弹簧动画
            )
            val route = item.route
            val selected = navController.currentBackStackEntryAsState().value?.destination?.route == route
            NavigationRailItem(
                selected = selected,
                modifier = Modifier.scale(scale.value),
                interactionSource = interactionSource,
                onClick = {
                    if (!selected) { turnToAndClear(navHomeController, route) }
                },
                label = { Text(text = item.label) },
                icon = {
                    Icon(if(selected)item.filledIcon else item.icon, contentDescription = item.label)
                }
            )
        }
    }

    var currentDestination by rememberSaveable { mutableStateOf(HomeRoute.BLOG.name) }

    NavigationSuiteScaffold(
        navigationSuiteItems =  {
            barItems.forEach { items ->
                val route = items.route
                val selected = items.route == currentDestination
                item(
                    selected = selected,
                    onClick = {
                        currentDestination = items.route
                        if (!selected) { turnToAndClear(navHomeController, route) }
                    },
                    label = { Text(text = items.label) },
                    icon = {
                        Icon(if(selected)items.filledIcon else items.icon, contentDescription = items.label)
                    }
                )
            }
        }
//        topBar = {
//            TopAppBar(
//                colors = TopAppBarDefaults.mediumTopAppBarColors(
//                    containerColor = Color.Transparent,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//                title = { Text("欢迎 $username") },
//                actions = {
//                    IconButton(onClick = {
//
//                    }) {
//                        Icon(Icons.Filled.Close, contentDescription = "")
//                    }
//                },
//            )
//        },
//        bottomBar = {
//            if (isDesktop) {
//                // 为电脑平台单独适配UI:侧边
//                NavigationRail(
//                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
//                ) {
//                    BarItemUI()
//                }
//            } else {
//                NavigationBar() {
//                    BarItemUI()
//                }
//            }
//        }
    ) {
        NavHost(
            navController = navHomeController,
            startDestination = HomeRoute.BLOG.name,
            enterTransition = {
                fadeAnimation.enter
            },
            exitTransition = {
                fadeAnimation.exit
            }
        ) {
            composable(HomeRoute.BLOG.name) {
                BlogsUI(vm)
            }
            composable(HomeRoute.ADD.name) {
                Scaffold {

                }
            }
            composable(HomeRoute.CUBE.name) {
                Scaffold {

                }
            }
        }
    }
}
fun logout(vm: MainViewModel,navController: NavHostController) {
    vm.fetchLogout()
    PreferencesManager.settings.remove(KEY_COOKIE)
    turnToAndClear(navController, NavRoute.LOGIN.name)
}
