package org.chiuxah.blog.logic.network.servicecreator

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import org.chiuxah.blog.logic.network.api.ApiService
import org.chiuxah.blog.logic.network.repo.ApiServiceImpl
import org.chiuxah.blog.logic.network.config.NetworkConstants
import org.chiuxah.blog.logic.network.servicecreator.main.BaseServiceCreator
import org.chiuxah.blog.logic.uitls.PreferencesManager

//子类
object BlogServiceCreator : BaseServiceCreator(
    NetworkConstants.API_HOST,
    NetworkConstants.TYPE,
    NetworkConstants.PORT,
    clients = {
        // 基类扩展
        install(DefaultRequest) {
            // 附带Cookie
            PreferencesManager.settings.getStringOrNull(PreferencesManager.KEY_COOKIE)?.let {
                header("Cookie", "JSESSIONID=$it")
            }
        }
    }
    ) {
    val apiService : ApiService by lazy { ApiServiceImpl(client) }
}