package org.chiuxah.blog.logic.uitls

import io.ktor.client.engine.HttpClientEngineFactory
import org.chiuxah.blog.logic.bean.PlatformType

expect object MultiPlatUtils {
    fun showMsg(msg : String)
    fun startUrl(url : String)
    fun createEngine() : HttpClientEngineFactory<*>
    fun getPlatformType() : PlatformType
}


