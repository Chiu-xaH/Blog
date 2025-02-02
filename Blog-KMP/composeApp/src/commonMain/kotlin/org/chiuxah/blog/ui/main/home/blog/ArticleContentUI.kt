package org.chiuxah.blog.ui.main.home.blog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.m3.Markdown
import org.chiuxah.blog.logic.network.bean.ArticleBean
import org.chiuxah.blog.ui.uitls.compents.CustomMarkdownTypography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleContentUI(articleInfo : ArticleBean, showDialog : Boolean, showChanged : () -> Unit) {
    if(showDialog) {
        Dialog(
            onDismissRequest = showChanged,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        actions = {
                            IconButton(onClick = showChanged) { Icon(Icons.Default.Close, contentDescription = null) }
                        },
                        title = { Text(articleInfo.title) }
                    )
                }
            ) { innerPadding ->
//                val state = rememberRichTextState().setMarkdown(articleInfo.content)
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
//                    RichText(
//                        state = state,
//                        modifier = Modifier
//                            .padding(horizontal = 15.dp)
//                            .verticalScroll(rememberScrollState())
//                    )
                    Markdown(
                        content = articleInfo.content,
                        modifier = Modifier.padding(horizontal = 15.dp).verticalScroll(
                            rememberScrollState()
                        ),
                        imageTransformer = Coil3ImageTransformerImpl,
                        typography = CustomMarkdownTypography()
                    )
                }
            }
        }
    }
}