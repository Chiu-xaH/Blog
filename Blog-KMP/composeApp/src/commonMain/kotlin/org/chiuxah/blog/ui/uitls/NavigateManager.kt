package org.chiuxah.blog.ui.uitls

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController

object NavigateManager {
    data class TransferAnimation(val remark : String, val enter : EnterTransition, val exit : ExitTransition)

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


    private val enterAnimation1 = scaleIn(animationSpec = tween(durationMillis = ANIMATION_SPEED)) +
            expandVertically(expandFrom = Alignment.Top,animationSpec = tween(durationMillis = ANIMATION_SPEED))
    private val exitAnimation1 = scaleOut(animationSpec = tween(durationMillis = ANIMATION_SPEED)) +
            shrinkVertically(shrinkTowards = Alignment.Top,animationSpec = tween(durationMillis = ANIMATION_SPEED))
    val upDownAnimation = TransferAnimation("底栏吸附",enterAnimation1, exitAnimation1)

    private val enterAnimation2 = scaleIn(animationSpec = tween(durationMillis = ANIMATION_SPEED)) +
            expandVertically(expandFrom = Alignment.CenterVertically,animationSpec = tween(durationMillis = ANIMATION_SPEED))
    private val exitAnimation2 = scaleOut(animationSpec = tween(durationMillis = ANIMATION_SPEED)) +
            shrinkVertically(shrinkTowards = Alignment.CenterVertically,animationSpec = tween(durationMillis = ANIMATION_SPEED))
    val centerAnimation = TransferAnimation("向中心运动",enterAnimation2, exitAnimation2)

    private val enterAnimationFade = fadeIn(animationSpec = tween(durationMillis = ANIMATION_SPEED))

    private val exitAnimationFade = fadeOut(animationSpec = tween(durationMillis = ANIMATION_SPEED))

    val fadeAnimation = TransferAnimation("淡入淡出", enterAnimationFade, exitAnimationFade)

    private val enterAnimationNull = fadeIn(animationSpec = tween(durationMillis = 0))

    private val exitAnimationNull = fadeOut(animationSpec = tween(durationMillis = 0))

    val nullAnimation = TransferAnimation("无", enterAnimationNull, exitAnimationNull)

    fun getAnimationType(index : Int) : TransferAnimation {
        return when(index) {
            0 -> upDownAnimation
            1 -> centerAnimation
            2 -> fadeAnimation
            3 -> nullAnimation
            else -> upDownAnimation
        }
    }
}