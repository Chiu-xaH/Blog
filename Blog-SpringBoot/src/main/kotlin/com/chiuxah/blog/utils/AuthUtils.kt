package com.chiuxah.blog.utils

import com.chiuxah.blog.model.UserInfo
import jakarta.servlet.http.HttpServletRequest

object AuthUtils {
    fun getUserInfo(request: HttpServletRequest): UserInfo? {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
        return session as? UserInfo
    }

    fun isLogin(request: HttpServletRequest): Boolean {
        val userInfo = getUserInfo(request)
        return userInfo != null
    }
    fun isAuthor(userId : Int, articleUid : Int): Boolean {
        return userId == articleUid
    }
}