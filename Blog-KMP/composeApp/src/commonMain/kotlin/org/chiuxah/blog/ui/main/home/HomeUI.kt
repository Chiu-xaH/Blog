package org.chiuxah.blog.ui.main.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
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
import org.chiuxah.blog.logic.uitls.PreferencesManager.KEY_EMAIL
import org.chiuxah.blog.ui.main.home.add.AddUI
import org.chiuxah.blog.ui.main.home.blog.BlogsUI
import org.chiuxah.blog.ui.main.home.blog.FollowArticlesUI
import org.chiuxah.blog.ui.main.home.cube.MyUI
import org.chiuxah.blog.ui.uitls.NavigateManager.fadeAnimation
import org.chiuxah.blog.ui.uitls.NavigateManager.turnToAndClear
import org.chiuxah.blog.viewmodel.NetworkViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUI(navController : NavHostController, vm : NetworkViewModel) {
    val navHomeController = rememberNavController()
    val isDesktop = PlatformsManager.platformType == PlatformType.DESKTOP
    val barItems = listOf(
        NavigationBarItemData(
            HomeRoute.RECOMMEND.name,
            "推荐",
            painterResource(Res.drawable.article),
            painterResource(Res.drawable.article_filled)
        ),
        NavigationBarItemData(
            HomeRoute.FOLLOW.name,
            "关注",
            painterResource(Res.drawable.add_2),
            painterResource(Res.drawable.add_2)
        ),
        NavigationBarItemData(
            HomeRoute.SEARCH.name,
            "搜索",
            painterResource(Res.drawable.deployed_code),
            painterResource(Res.drawable.deployed_code_filled)
        ),
        NavigationBarItemData(
            HomeRoute.CUBE.name,
            "我",
            painterResource(Res.drawable.deployed_code),
            painterResource(Res.drawable.deployed_code_filled)
        ),
    )

    var currentDestination by rememberSaveable { mutableStateOf(HomeRoute.RECOMMEND.name) }

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
    ) {
        NavHost(
            navController = navHomeController,
            startDestination = HomeRoute.RECOMMEND.name,
            enterTransition = {
                fadeAnimation.enter
            },
            exitTransition = {
                fadeAnimation.exit
            }
        ) {
            composable(HomeRoute.RECOMMEND.name) {
                BlogsUI(vm)
            }
            composable(HomeRoute.FOLLOW.name) {
                FollowArticlesUI(vm)
            }
            composable(HomeRoute.SEARCH.name) {

            }
            composable(HomeRoute.CUBE.name) {
                MyUI(vm,navController)
            }
        }
    }
}
fun logout(vm: NetworkViewModel, navController: NavHostController) {
    vm.fetchLogout()
    PreferencesManager.settings.remove(KEY_COOKIE)
    turnToAndClear(navController, NavRoute.LOGIN.name)
}
