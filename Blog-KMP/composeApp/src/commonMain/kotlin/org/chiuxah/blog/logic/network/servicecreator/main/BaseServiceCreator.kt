package org.chiuxah.blog.logic.network.servicecreator.main

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import org.chiuxah.blog.logic.network.config.NetworkConstants.TYPE
import org.chiuxah.blog.logic.uitls.MultiPlatUtils

//基类
open class BaseServiceCreator(
    private val hosts : String, // 主机
    private val type : URLProtocol = TYPE, // HTTP/HTTPS
    private val ports : Int? = null, // 端口 默认
    private val clients: HttpClientConfig<*>.() -> Unit = {}  // 扩展
) {
    protected val client: HttpClient by lazy {
        HttpClient(MultiPlatUtils.createEngine()) {
            install(ContentNegotiation) { json() }
//            install(Logging) {
//                level = LogLevel.ALL
//                logger = Logger.DEFAULT
//            }
            defaultRequest {
                url {
                    protocol = type
                    host = hosts
                    if(ports != null) {
                        port = ports
                    }
                }
            }
            clients()
        }
    }
}