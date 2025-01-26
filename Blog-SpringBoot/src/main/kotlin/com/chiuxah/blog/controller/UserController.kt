package com.chiuxah.blog.controller

import com.chiuxah.blog.config.AjaxResult
import com.chiuxah.blog.service.UserService
import com.chiuxah.blog.util.ConstVariable
import com.chiuxah.blog.util.CryptoUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 用户模块
@RestController
@RequestMapping("/user")
class UserController {
    @Autowired lateinit var userService : UserService
    val ajaxResult = AjaxResult
    /*用户注册*/
    @RequestMapping("/reg")
    fun reg(username : String,password1 : String,password2 : String) : Any {
        // 注册过
        return if(userService.hasAccount(username)) {
            ajaxResult.fail(-1,"用户已注册")
        } else if(!(StringUtils.hasLength(username) && StringUtils.hasLength(password1) && StringUtils.hasLength(password2))) {
            ajaxResult.fail(-1,"参数输入错误")
        } else {
            if(password1 != password2) {
                ajaxResult.fail(-1,"前后密码不一致")
            } else {
                val result = userService.reg(username,CryptoUtil.encrypt(password1))
                if(result == -1) {
                    ajaxResult.fail(-1,"数据库添加出错")
                } else {
                    ajaxResult.success("注册成功","1")
                }
            }
        }
    }
    /*用户登录*/
    @RequestMapping("/login")
    fun login(request : HttpServletRequest,username : String,password: String) : Any {
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return ajaxResult.fail(-1,"参数输入错误 登陆失败")
        }
        val userInfo = userService.selectByUsername(username) ?: return ajaxResult.fail(-1,"用户未注册")
        if(!CryptoUtil.decryept(password,userInfo.password) || userInfo.id <= 0) {
            return ajaxResult.fail(-1,"账号或密码错误 登陆失败")
        }
        val session = request.session
        session.setAttribute(ConstVariable.USER_SESSION_KEY,userInfo)
        return ajaxResult.success("登陆成功",1)
    }
    /*退出登录*/
    @RequestMapping("/logout")
    fun logout(request : HttpServletRequest,response : HttpServletResponse) : Any {
        val session = request.session
        if(session?.getAttribute(ConstVariable.USER_SESSION_KEY) == null) {
            return ajaxResult.fail(401,"未登录")
        }
        session.setAttribute(ConstVariable.USER_SESSION_KEY,null)
        return ajaxResult.success("已退出登录","true")
    }
    /*通过id搜索用户详细信息*/
    @RequestMapping("/get_detail")
    fun selectByUid(id : Long) : Any {
        if(id <= 0) {
            return ajaxResult.fail(-1,"uid <= 0")
        }
        val userInfo = userService.selectByUid(id) ?: return ajaxResult.fail(-1,"查询不到")
        userInfo.password = ""
        return ajaxResult.success("查询成功",userInfo)
    }
}