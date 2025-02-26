package org.chiuxah.blog.logic.network.config

import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json

//放一些常量
object NetworkConstants {
    const val PC_UA = "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/115.0"
    // 主机地址 们(可扩展多个，若扩展，需要创建子类ServiceCreator)
    const val API_HOST = "localhost"

    // 接口 们(可扩展多个，若扩展，需要创建接口Service)
    object APIs {
        private const val API = "api/v1/"
        // 用户
        private const val USER = API + "user/"
        const val LOGIN = USER + "login"
        const val REG = USER + "reg"
        const val CHECK_LOGIN = USER + "check-login"
        const val LOGOUT = USER + "logout"
        const val GET_USER_BY_ID = USER + "info"
        const val GET_MY = USER + "me"
        const val SEND_CODE = USER + "send-code"
        // 博文
        private const val ARTICLE = API + "article/"
        const val GET_ALL_ARTICLES = ARTICLE + "all"
        const val DEL = ARTICLE + "del"
        // 关注
        private const val FOLLOW = API + "follow/"
        const val GET_FOLLOWERS_COUNT = FOLLOW + "count"
        // 推荐
        private const val RECOMMEND = API + "recommendation/"
        const val GET_HOT_ARTICLES = RECOMMEND + "hot"
        const val GET_FOLLOW_ARTICLES = RECOMMEND + "follow"
        // 评论
        private const val COMMENT = API + "comment/"
        const val GET_ALL_COMMENTS = COMMENT + "all"
        const val ADD_COMMENT = COMMENT + "add"
    }
    // 类型 http/https
    val TYPE =  URLProtocol.HTTP
    // 端口
    const val PORT = 8080

    inline fun <reified T> parseJson(jsonStr : String) : T {
        val json = Json { ignoreUnknownKeys = true } //忽略未定义字段
        return json.decodeFromString<T>(jsonStr)
    }
}