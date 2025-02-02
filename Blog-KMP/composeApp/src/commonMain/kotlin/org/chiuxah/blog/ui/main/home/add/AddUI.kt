package org.chiuxah.blog.ui.main.home.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.chiuxah.blog.viewmodel.MainViewModel

@Composable
fun AddUI(vm : MainViewModel) {
//    val state = rememberRichTextState()
//    val html = state.setMarkdown("# Hello World\n## Hello")
    Scaffold (

    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
//            RichText(
//                state = html,
//            )
        }
    }
}

