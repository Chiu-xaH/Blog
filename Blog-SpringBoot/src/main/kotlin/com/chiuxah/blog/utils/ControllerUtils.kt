package com.chiuxah.blog.utils

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.model.bean.UserSessionSummary
import jakarta.servlet.http.HttpServletRequest

object ControllerUtils {
    // 请在已经登录的情况下使用！
    fun myUserInfo(request: HttpServletRequest): UserSessionSummary = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY) as UserSessionSummary

    val INVALID_RESPONSE = ResultEntity.fail(StatusCode.BAD_REQUEST,"参数有误")

    val INVALID_PWD_RESPONSE = ResultEntity.fail(StatusCode.BAD_REQUEST,"密码应包含字母、符号、数字中的至少两种，长度至少8位")

    val CONTROLLER_ERROR_RESPONSE = ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"解析错误")

    val DATABASE_ERROR_RESPONSE = ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"操作数据库时出错")

    val USER_FORBID_RESPONSE = ResultEntity.fail(StatusCode.FORBIDDEN,"无权限")

    val EMPTY_RESPONSE = ResultEntity.fail(StatusCode.NOT_FOUND,"无资源")

    val SUCCESS_RESPONSE = ResultEntity.success()

    // 用于同一个Controller内联调用其他API
    fun isSuccessResponse(responseBody : Any) : Boolean = responseBody is Map<*, *> && responseBody["state"] == StatusCode.OK.code

    fun jsonToMap(responseJson : Any) : Map<*,*> = responseJson as Map<*,*>
}

