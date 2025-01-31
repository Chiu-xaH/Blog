package org.chiuxah.blog.logic.network.servicecreator

import org.chiuxah.blog.logic.network.api.ApiService
import org.chiuxah.blog.logic.network.repo.ApiServiceImpl
import org.chiuxah.blog.logic.network.config.NetworkConstants
import org.chiuxah.blog.logic.network.servicecreator.main.BaseServiceCreator

//子类
object BlogServiceCreator : BaseServiceCreator(NetworkConstants.API_HOST,{}) {
    val apiService : ApiService by lazy { ApiServiceImpl(client) }
}