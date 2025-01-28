package com.chiuxah.blog.config

import com.chiuxah.blog.util.ConstVariable
import com.chiuxah.blog.util.state.StatusCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class LoginInterception : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
        return if(session == null) {
            response.status = StatusCode.UNAUTHORIZED.code
            response.contentType = "application/json; charset=utf-8"
            // 发送 AjaxResult.fail(StatusCode.UNAUTHORIZED,"未登录")
            val failResponse = """
                {
                    "state": ${StatusCode.UNAUTHORIZED.code},
                    "msg": "未登录",
                    "data": ""
                }
            """.trimIndent()

            // 写入响应
            response.writer.use { it.write(failResponse) }
            false
        } else {
            true
        }
    }
}