package org.chiuxah.blog.ui.uitls.compents

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.MarkdownTypography

@Composable
fun CustomMarkdownTypography() : MarkdownTypography {
    val h1: TextStyle = MaterialTheme.typography.headlineMedium
    val h3: TextStyle = h1.copy(fontSize = h1.fontSize * 0.8)

    val h2: TextStyle = MaterialTheme.typography.headlineSmall
    val h5: TextStyle = h3.copy(fontSize = h3.fontSize * 0.8)
    val h4: TextStyle = MaterialTheme.typography.titleLarge
    val text: TextStyle = h5.copy(fontSize = h5.fontSize * 0.8)
    val h6: TextStyle = MaterialTheme.typography.bodyLarge

    return markdownTypography(
        h1, h2, h3, h4, h5, h6, text,
//                            code = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
//                            inlineCode = text.copy(fontFamily = FontFamily.Monospace),
//                            quote = MaterialTheme.typography.bodyMedium.plus(SpanStyle(fontStyle = FontStyle.Italic)),
//                            paragraph = MaterialTheme.typography.bodyLarge,
//                            ordered = MaterialTheme.typography.bodyLarge,
//                            bullet = MaterialTheme.typography.bodyLarge,
//                            list = MaterialTheme.typography.bodyLarge,
//                            link = MaterialTheme.typography.bodyLarge.copy(
//                                fontWeight = FontWeight.Bold,
//                                textDecoration = TextDecoration.Underline
//                            ),
    )
}
