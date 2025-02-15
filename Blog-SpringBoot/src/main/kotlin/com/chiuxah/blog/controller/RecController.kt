package com.chiuxah.blog.controller

import com.chiuxah.blog.config.ResponseEntity
import com.chiuxah.blog.utils.ConstVariable
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/rec")
class RecController {
    @GetMapping("/login")
    fun loginHtml(request: HttpServletRequest,response: HttpServletResponse) : String {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
        if(session != null) {
            response.sendRedirect("/")
            return ResponseEntity.success("已登录").toString()
        }
        return "login"
    }
}