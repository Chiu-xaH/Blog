package com.chiuxah.blog.controller

import com.chiuxah.blog.config.AjaxResult
import com.chiuxah.blog.model.UserInfo
import com.chiuxah.blog.service.UserService
import com.chiuxah.blog.utils.ConstVariable
import com.chiuxah.blog.utils.CryptoUtils
import com.chiuxah.blog.utils.enums.StatusCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.catalina.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.Mapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 用户模块
@RestController
@RequestMapping("/user")
class UserController {
    @Autowired lateinit var userService : UserService
    /*用户注册*/
    @RequestMapping("/reg")
    fun reg(username : String,password : String) : Any {
        // 注册过
        return if(userService.hasAccount(username)) {
            // 后面完善 重定向到登录
            AjaxResult.fail(StatusCode.REDIRECT,"用户注册过")
        } else if(!(StringUtils.hasLength(username) && StringUtils.hasLength(password))) {
            AjaxResult.fail(StatusCode.BAD_REQUEST,"参数输入错误")
        } else {
            val result = userService.reg(username,CryptoUtils.encrypt(password))
            if(result == -1) {
                AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"数据库添加出错")
            } else {
                AjaxResult.success("注册成功")
            }
        }
    }
    /*用户登录*/
    @RequestMapping("/login")
    fun login(request : HttpServletRequest,username : String,password: String) : Any {
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return AjaxResult.fail(StatusCode.BAD_REQUEST,"参数输入错误 登陆失败")
        }
        val userInfo = userService.selectByUsername(username) ?: return AjaxResult.fail(StatusCode.FORBIDDEN,"用户未注册")
        if(!CryptoUtils.decryept(password,userInfo.password) || userInfo.id <= 0) {
            return AjaxResult.fail(StatusCode.FORBIDDEN,"账号或密码错误 登陆失败")
        }
        val session = request.session
        session.setAttribute(ConstVariable.USER_SESSION_KEY,userInfo)
        return AjaxResult.success("登陆成功", mapOf(
            "JSESSIONID" to session.id
        ))
    }
    /*退出登录*/
    @RequestMapping("/logout")
    fun logout(request : HttpServletRequest,response : HttpServletResponse) : Any {
        val session = request.session
        if(session?.getAttribute(ConstVariable.USER_SESSION_KEY) == null) {
            return AjaxResult.fail(StatusCode.UNAUTHORIZED,"未登录")
        }
        session.setAttribute(ConstVariable.USER_SESSION_KEY,null)
        return AjaxResult.success("已退出登录")
    }
    /*通过id搜索用户详细信息*/// 精准查找
    @GetMapping("/get_detail")
    fun selectByUid(id : Int) : Any {
        if(id <= 0) {
            return AjaxResult.fail(StatusCode.BAD_REQUEST,"uid <= 0")
        }
        val userInfo = userService.selectByUid(id)
        if (userInfo != null) {
            userInfo.password = ""
        }
        return AjaxResult.success("查询成功",userInfo)
    }
    // 验证Cookie是仍有效
    @GetMapping("/check_login")
    fun checkLogin(request: HttpServletRequest) : Any {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
            ?: return AjaxResult.fail(StatusCode.UNAUTHORIZED,"无凭证")
        return AjaxResult.success("有效")
    }
}