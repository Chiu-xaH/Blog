package com.chiuxah.blog.utils

object ValidUtils {
    fun isValidId(id : Int) : Boolean = id > 0

    fun isValidEmail(email : String) : Boolean {
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return regex.matches(email)
    }

    // 包含字母、符号、数字中的至少两种，长度至少8位
    fun isValidPassword(password : String) : Boolean {
        if (password.length < 8) return false // 长度必须大于8

        val hasLetter = password.any { it.isLetter() }  // 是否有字母
        val hasDigit = password.any { it.isDigit() }    // 是否有数字
        val hasSymbol = password.any { !it.isLetterOrDigit() } // 是否有符号
        // 至少包含其中两种
        return (hasLetter && hasDigit) || (hasLetter && hasSymbol) || (hasDigit && hasSymbol)
    }

}