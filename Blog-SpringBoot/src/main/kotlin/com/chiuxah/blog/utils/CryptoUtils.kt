package com.chiuxah.blog.utils

import cn.hutool.core.util.IdUtil
import cn.hutool.crypto.SecureUtil
import org.springframework.util.StringUtils
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

object CryptoUtils {
    private const val SALT_LENGTH = 16  // 盐的长度
    private const val HASH_ALGORITHM = "SHA-256"

    // 生成随机盐
    private fun generateSalt(): String {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)  // 生成随机盐
        return Base64.getEncoder().encodeToString(salt)  // 转换成 Base64 存储
    }

    // SHA-256 加密（带盐）
    private fun encrypt(password: String, salt: String): String {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val hashBytes = digest.digest((salt + password).toByteArray())  // 盐 + 密码
        return Base64.getEncoder().encodeToString(hashBytes)  // Base64 转换，方便存储
    }

    // 生成加密后的密码（加盐+哈希）
    fun hashPassword(password: String): String {
        val salt = generateSalt()  // 生成盐
        val hash = encrypt(password, salt)  // 计算哈希
        return "$salt:$hash"  // 存储格式：盐:哈希
    }

    // 验证密码
    fun verifyPassword(inputPassword: String, storedHash: String): Boolean {
        val parts = storedHash.split(":")  // 拆分 盐 和 哈希
        if (parts.size != 2) return false
        val salt = parts[0]
        val expectedHash = parts[1]
        return encrypt(inputPassword, salt) == expectedHash  // 重新计算哈希并比较
    }
}