package org.chiuxah.blog.logic.uitls

import com.russhwolf.settings.Settings


object PreferencesManager {
    val settings  = Settings()

    const val KEY_COOKIE = "cookie"
    const val KEY_EMAIL = "email"
    const val KEY_PASSWORD = "password"
    const val KEY_USERINFO = "userinfo"

    fun <T> putValue(key: String, value: T) {
        when (value) {
            is String -> settings.putString(key, value)
            is Int -> settings.putInt(key, value)
            is Boolean -> settings.putBoolean(key, value)
            is Float -> settings.putFloat(key, value)
            is Double -> settings.putDouble(key, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    fun <T> getValue(key: String, default: T): T {
        return when (default) {
            is String -> settings.getStringOrNull(key) as? T ?: default
            is Int -> settings.getIntOrNull(key) as? T ?: default
            is Boolean -> settings.getBooleanOrNull(key) as? T ?: default
            is Float -> settings.getFloatOrNull(key) as? T ?: default
            is Double -> settings.getDoubleOrNull(key) as? T ?: default
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}