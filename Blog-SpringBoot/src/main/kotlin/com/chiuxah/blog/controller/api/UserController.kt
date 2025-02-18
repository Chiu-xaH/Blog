package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResponseEntity
import com.chiuxah.blog.service.UserService
import com.chiuxah.blog.utils.ConstVariable
import com.chiuxah.blog.utils.CryptoUtils
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.model.bean.PostUserInfo
import com.chiuxah.blog.model.bean.UserSessionSummary
import com.chiuxah.blog.utils.ControllerUtils.INVALID_PWD_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.isSuccessResponse
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import com.chiuxah.blog.utils.ValidUtils.isValidEmail
import com.chiuxah.blog.utils.ValidUtils.isValidId
import com.chiuxah.blog.utils.ValidUtils.isValidPassword
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// 用户模块
@RestController
@RequestMapping("/api/v1/user")
class UserController {
    @Autowired lateinit var userService : UserService

    // 核验 用于注册和修改信息 不可用于登录！！
    @GetMapping("/check-valid")
    fun checkVaild(email : String?, username : String?, password : String?) : Any {
        email?.let {
            // 检查是否是合法邮箱
            val isValidEmail = isValidEmail(it)
            if(!isValidEmail) {
                return ResponseEntity.fail(StatusCode.BAD_REQUEST,"邮箱格式有误")
            }
            // 检查是否已存在账号
            val hasAccount = userService.hasAccount(it)
            if(hasAccount) {
                return ResponseEntity.fail(StatusCode.FORBIDDEN,"已有账号")
            }
        }
        // 检查用户名是否已存在
        username?.let {
            val isUsernameExist = userService.isUserNameExist(it)
            if(isUsernameExist) {
                return ResponseEntity.fail(StatusCode.FORBIDDEN,"用户名重复")
            }
        }
        // 检查密码的合理性
        password?.let {
            val isValidPwd = isValidPassword(it)
            if(!isValidPwd) {
                return INVALID_PWD_RESPONSE
            }
        }
        return ResponseEntity.success("核验通过")
    }
    @GetMapping("check")
    fun checkUser(request: HttpServletRequest,password: String) : Any {
        val userInfo = myUserInfo(request)
        return if(CryptoUtils.verifyPassword(password,userInfo.password!!)) {
            ResponseEntity.success("密码正确")
        } else {
            ResponseEntity.fail(StatusCode.BAD_REQUEST,"密码错误")
        }
    }
    /*用户注册*/
    @PostMapping("/reg")
    fun reg(email : String,username : String,password : String) : Any {
        val check = checkVaild(email,username,password)
        if(!isSuccessResponse(check)) {
            return check
        }
        // 加密密码
        val encryptedPwd = CryptoUtils.hashPassword(password)
        // 存储到数据库
        val result = userService.reg(email,username,encryptedPwd)
        return if(result) {
            // 注册成功
            ResponseEntity.success("注册成功")
        } else {
            ResponseEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"注册失败")
        }
    }
    /*用户登录*/
    @PostMapping("/login")
    fun login(request : HttpServletRequest,email: String,password: String) : Any {
        // 检查是否是合法邮箱
        val isValidEmail = isValidEmail(email)
        if(!isValidEmail) {
            return ResponseEntity.fail(StatusCode.BAD_REQUEST,"邮箱格式有误")
        }
        // 验证密码
        val userInfo = userService.selectByEmail(email) ?: return ResponseEntity.fail(StatusCode.NOT_FOUND,"无账号 先注册")
        if(!CryptoUtils.verifyPassword(password,userInfo.password!!)) {
            return ResponseEntity.fail(StatusCode.BAD_REQUEST,"密码错误")
        }
        // 储存信息
        val session = request.session
        session.setAttribute(ConstVariable.USER_SESSION_KEY,userInfo)
        userInfo.password = null
        return ResponseEntity.success("登陆成功", mapOf(
            "JSESSIONID" to session.id,
            "userinfo" to userInfo
        ))
    }
    /*退出登录*/
    @PostMapping("/logout")
    fun logout(request : HttpServletRequest,response : HttpServletResponse) : Any {
        request.session.setAttribute(ConstVariable.USER_SESSION_KEY,null)
        return ResponseEntity.success("已退出登录")
    }
    /*通过id用户详细信息*/
    @GetMapping("/info")
    fun selectByUid(id : Int) : Any {
        if(!isValidId(id)) {
            return INVALID_RESPONSE
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
            ?: return ResponseEntity.fail(StatusCode.UNAUTHORIZED,"无效")
        val userinfo = session as UserSessionSummary
        userinfo.password = null
        return ResponseEntity.success("有效", data = userinfo)
    }
    // 根据session得到用户信息
    @GetMapping("/me")
    fun selectByUid(request: HttpServletRequest) : Any {
        val userInfo = myUserInfo(request)
        return ResponseEntity.success("获取成功",userInfo)
    }
    // 修改用户信息
    @PutMapping("/update")
    fun updateUserInfo(request: HttpServletRequest,@RequestBody updateRequest: PostUserInfo) : Any {
        // 修改邮箱和密码时需要验证密码
        if(updateRequest.email != null || updateRequest.password != null) {
            val check = checkUser(request,updateRequest.password!!)
            if(!isSuccessResponse(check)) {
                return check
            }
        }
        // 验证合理性
        val check = checkVaild(updateRequest.email,updateRequest.username,updateRequest.password)
        if(!isSuccessResponse(check)) {
                return check
        }
        // 提交修改的参数
        val updateFields = mutableMapOf<String, Any>()
        updateRequest.email?.let { updateFields["email"] = it }
        updateRequest.username?.let { updateFields["username"] = it }
        updateRequest.password?.let { updateFields["password"] = CryptoUtils.hashPassword(it) } // 加密存储
        updateRequest.phoneNumber?.let { updateFields["phone_number"] = it }
        updateRequest.description?.let { updateFields["description"] = it }
        updateRequest.sex?.let { updateFields["sex"] = it }
        updateRequest.bornDate?.let { updateFields["born_date"] = it }
        updateRequest.region?.let { updateFields["region"] = it }
        updateRequest.website?.let { updateFields["website"] = it }
        updateRequest.photo?.let { updateFields["photo"] = it }
        // 加上id
        val uid = myUserInfo(request).id
        updateFields["id"] = uid
        // 确保至少有一个字段需要更新
        if (updateFields.isEmpty()) {
            return ResponseEntity.fail(StatusCode.BAD_REQUEST, "未提供需要更新的字段")
        }
        // 更新
        val result = userService.update(updateFields)
        if(result) {
            val newUserInfo = userService.selectByUid(uid) ?: return ResponseEntity.fail(StatusCode.NOT_FOUND,"无用户")
            // session信息更新
            val session = request.session
            session.setAttribute(ConstVariable.USER_SESSION_KEY, UserSessionSummary(
                id = newUserInfo.id,
                username = newUserInfo.username,
                email = newUserInfo.email,
                password = newUserInfo.password,
                photo = newUserInfo.photo
            ))
            val newSession = session.getAttribute(ConstVariable.USER_SESSION_KEY) as UserSessionSummary
            newSession.password = null
            return ResponseEntity.success("更新成功", data = mapOf(
                "JSESSIONID" to session.id,
                "userinfo" to newSession
            ))
        } else {
            return ResponseEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"更新失败")
        }
    }
    // 注销账号 删除账号数据库，并删除头像文件及其数据库记录； 由用户自行决定是否保留其关联的博文 不保留博文则删除所有博文以及图片
    @DeleteMapping("/del") // 未写完！
    fun delUser(request: HttpServletRequest,password: String,retainArticles : Boolean) : Any {
        val check = checkUser(request,password)
        if(!isSuccessResponse(check)) {
            return check
        }
        val result = userService.del(myUserInfo(request).id)
        return if(result) {
            if(!retainArticles) {

            }
            ResponseEntity.success("注销成功")
        } else {
            ResponseEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"注销失败")
        }
    }
    // 扩展任务：用户鉴权改为JWT
    // 扩展任务：使用加密传输数据 例如AES
}