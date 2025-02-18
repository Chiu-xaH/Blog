package com.chiuxah.blog.utils

import com.chiuxah.blog.config.response.ResponseEntity
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.model.bean.UserSessionSummary
import jakarta.servlet.http.HttpServletRequest

object ControllerUtils {

    // 请在已经登录的情况下使用！
    fun myUserInfo(request: HttpServletRequest): UserSessionSummary = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY) as UserSessionSummary

    val INVALID_RESPONSE = ResponseEntity.fail(StatusCode.BAD_REQUEST,"参数有误")

    val INVALID_PWD_RESPONSE = ResponseEntity.fail(StatusCode.BAD_REQUEST,"密码应包含字母、符号、数字中的至少两种，长度至少8位")
    // 用于同一个Controller内联调用其他API
    fun isSuccessResponse(responseBody : Any) : Boolean = responseBody is Map<*, *> && responseBody["state"] == StatusCode.OK.code

    fun jsonToMap(responseJson : Any) : Map<*,*> {
        return responseJson as Map<*,*>
    }
}

