package org.chiuxah.blog.logic.uitls

import io.ktor.client.engine.HttpClientEngineFactory
import org.chiuxah.blog.logic.bean.PlatformType

actual object MultiPlatUtils {
    actual fun createEngine(): HttpClientEngineFactory<*> {
        TODO("Not yet implemented")
    }

    actual fun getPlatformType(): PlatformType {
        return PlatformType.IOS
    }

    actual fun showMsg(msg: String) {
        TODO("Not yet implemented")
    }

    actual fun startUrl(url: String) {
        TODO("Not yet implemented")
    }
}