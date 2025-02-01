package com.chiuxah.blog.utils

import cn.hutool.core.util.IdUtil
import cn.hutool.crypto.SecureUtil
import org.springframework.util.StringUtils

object CryptoUtils {
    // 加密
    fun encrypt(password : String) : String {
        val salt = IdUtil.simpleUUID()
        val finalPassword = SecureUtil.md5(salt + password)
        return "$salt$$finalPassword"
    }


    // 解密
    fun decryept(password: String, securePassword : String) : Boolean {
        var result = false
        if(StringUtils.hasLength(password) && StringUtils.hasLength(securePassword)) {
            if(securePassword.length == 65 && securePassword.contains("$")) {
                val securePasswordArr = securePassword.split("$")
                val salt = securePasswordArr[0]
                val finalPassword = securePasswordArr[1]
                // 同样的算法进行比较
                if(finalPassword == SecureUtil.md5(salt + password)) {
                    result =  true
                }
            } else if(password == securePassword) {
                result = true
            }
        }
        return result
    }
}