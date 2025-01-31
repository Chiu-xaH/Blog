package org.chiuxah.blog.logic.network.servicecreator.main

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import org.chiuxah.blog.logic.network.config.NetworkConstants.PORT
import org.chiuxah.blog.logic.uitls.MultiPlatUtils

//基类
open class BaseServiceCreator(
    private val hosts: String,
    private val clientBuilder: HttpClient.() -> Unit
) {
    protected val client: HttpClient by lazy {
        HttpClient(MultiPlatUtils.createEngine()) {
            install(ContentNegotiation) { json() }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = hosts
                    port = PORT
                }
            }
            clientBuilder
        }
    }
}