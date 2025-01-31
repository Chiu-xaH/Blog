package org.chiuxah.blog.logic.network.config

import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json

//放一些常量
object NetworkConstants {
    const val PC_UA = "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/115.0"
    // 主机地址 们(可扩展多个，若扩展，需要创建子类ServiceCreator)
    const val API_HOST = "localhost"

    // 接口 们(可扩展多个，若扩展，需要创建接口Service)
    private const val API_USER = "user/"
    const val API_LOGIN = API_USER + "login"
    const val API_REG = API_USER + "reg"
    // 类型 http/https
    val TYPE =  URLProtocol.HTTP
    // 端口
    const val PORT = 8080

    inline fun <reified T> parseJson(jsonStr : String) : T {
        val json = Json { ignoreUnknownKeys = true } //忽略未定义字段
        return json.decodeFromString<T>(jsonStr)
    }
}