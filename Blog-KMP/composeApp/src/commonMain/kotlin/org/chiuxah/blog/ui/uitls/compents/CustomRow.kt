package org.chiuxah.blog.ui.uitls.compents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomRow(content : @Composable () -> Unit) {
    Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
        content()
    }
}