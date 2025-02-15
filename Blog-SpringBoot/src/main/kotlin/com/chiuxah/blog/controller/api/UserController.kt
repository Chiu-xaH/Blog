package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.ResponseEntity
import com.chiuxah.blog.model.UserInfo
import com.chiuxah.blog.service.UserService
import com.chiuxah.blog.utils.ConstVariable
import com.chiuxah.blog.utils.CryptoUtils
import com.chiuxah.blog.utils.enums.StatusCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

// 用户模块
@RestController
@RequestMapping("/api/v1/user")
class UserController {
    @Autowired lateinit var userService : UserService
    /*用户注册*/
    @PostMapping("/reg")
    fun reg(username : String,password : String) : Any {
        // 注册过
        return if(userService.hasAccount(username)) {
            // 后面完善 重定向到登录
            ResponseEntity.fail(StatusCode.REDIRECT,"用户注册过")
        } else if(!(StringUtils.hasLength(username) && StringUtils.hasLength(password))) {
            ResponseEntity.fail(StatusCode.BAD_REQUEST,"参数输入错误")
        } else {
            val result = userService.reg(username,CryptoUtils.encrypt(password))
            if(!result) {
                ResponseEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"数据库添加出错")
            } else {
                ResponseEntity.success("注册成功")
            }
        }
    }
    /*用户登录*/
    @PostMapping("/login")
    fun login(request : HttpServletRequest,username : String,password: String) : Any {
        if(!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return ResponseEntity.fail(StatusCode.BAD_REQUEST,"参数输入错误 登陆失败")
        }
        val userInfo = userService.selectByUsername(username) ?: return ResponseEntity.fail(StatusCode.FORBIDDEN,"用户未注册")
        if(!CryptoUtils.decrypt(password,userInfo.password) || userInfo.id <= 0) {
            return ResponseEntity.fail(StatusCode.FORBIDDEN,"账号或密码错误 登陆失败")
        }
        val session = request.session
        session.setAttribute(ConstVariable.USER_SESSION_KEY,userInfo)
        return ResponseEntity.success("登陆成功", mapOf(
            "JSESSIONID" to session.id,
            "userinfo" to userInfo
        ))
    }
    /*退出登录*/
    @PostMapping("/logout")
    fun logout(request : HttpServletRequest,response : HttpServletResponse) : Any {
        val session = request.session
        if(session?.getAttribute(ConstVariable.USER_SESSION_KEY) == null) {
            return ResponseEntity.fail(StatusCode.UNAUTHORIZED,"未登录")
        }
        session.setAttribute(ConstVariable.USER_SESSION_KEY,null)
        return ResponseEntity.success("已退出登录")
    }
    /*通过id搜索用户详细信息*/// 精准查找
    @GetMapping("/info")
    fun selectByUid(id : Int) : Any {
        if(id <= 0) {
            return ResponseEntity.fail(StatusCode.BAD_REQUEST,"uid <= 0")
        }
        val userInfo = userService.selectByUid(id)
        return if(userInfo != null) {
            ResponseEntity.success("查询成功", userService.convertToUserInfoDTO(userInfo))
        } else {
            ResponseEntity.fail(StatusCode.NOT_FOUND,"无用户")
        }
    }
    // 验证Cookie是仍有效
    @GetMapping("/check-login")
    fun checkLogin(request: HttpServletRequest) : Any {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
            ?: return ResponseEntity.fail(StatusCode.UNAUTHORIZED,"无凭证")
        val userinfo = session as UserInfo
        return ResponseEntity.success("有效", data = userinfo)
    }
    // 根据session得到用户信息
    @GetMapping("/me")
    fun selectByUid(request: HttpServletRequest) : Any {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
            ?: return ResponseEntity.fail(StatusCode.UNAUTHORIZED,"未登录")

        val userInfo = session as UserInfo
        return ResponseEntity.success("获取成功",userInfo)
    }

    // 修改用户信息
    // 注销账号 删除账号数据库，并删除头像文件及其数据库记录； 由用户自行决定是否保留其关联的博文 不保留博文则删除所有博文以及图片
    // 扩展任务：用户鉴权改为JWT
    // 扩展任务：使用加密传输数据 例如AES
}