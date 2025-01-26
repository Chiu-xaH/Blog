package org.chiuxah.blog

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.chiuxah.blog.ui.main.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Blog",
    ) {
        App()
    }
}