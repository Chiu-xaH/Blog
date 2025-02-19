package com.chiuxah.blog.utils

object CodeGeneratorUtil {
    // 生成六位数字验证码
    fun generateCode(): String {
        return (100000..999999).random().toString()
    }
}