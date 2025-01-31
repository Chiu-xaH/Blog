package org.chiuxah.blog.logic.uitls

import android.content.Intent
import android.net.Uri
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.Android
import org.chiuxah.blog.MyApplication
import org.chiuxah.blog.logic.enums.PlatformType
import org.chiuxah.blog.ui.utils.myToast

actual object MultiPlatUtils {
    actual fun createEngine() : HttpClientEngineFactory<*> {
        return Android
    }

    actual fun getPlatformType(): PlatformType {
        return PlatformType.ANDROID
    }

    actual fun showMsg(msg : String) {
        myToast(msg)
    }

    actual fun startUrl(url: String) {
        try {
            val it = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            MyApplication.context.startActivity(it)
        } catch (e : Exception) {
            myToast("启动浏览器失败")
        }
    }
}