package org.chiuxah.blog.logic.bean

import androidx.compose.ui.graphics.painter.Painter

data class NavigationBarItemData (
    val route: String,
    val label: String,
    val icon: Painter,
    val filledIcon: Painter
)
