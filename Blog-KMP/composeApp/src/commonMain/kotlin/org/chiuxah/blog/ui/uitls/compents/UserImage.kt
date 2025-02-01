package org.chiuxah.blog.ui.uitls.compents

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import io.ktor.http.URLProtocol
import org.chiuxah.blog.logic.network.config.NetworkConstants


@Composable
fun UserImage(url : String) {
    val painter = rememberImagePainter(
        if(NetworkConstants.TYPE == URLProtocol.HTTP) {
            "http://"
        } else {
            "https://"
        } +
                NetworkConstants.API_HOST + NetworkConstants.PORT?.let { ":${it}" } +
                url
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(40.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
    )
}