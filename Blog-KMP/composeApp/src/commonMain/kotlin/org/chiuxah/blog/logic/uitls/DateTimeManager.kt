package org.chiuxah.blog.logic.uitls

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeManager {
    // 获取当前日期
    val today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    // 格式化日期
    val Date_yyyy_MM: String get() = formatDate("yyyy-MM")
    val Date_MM_dd: String get() = formatDate("MM-dd")
    val Date_MM: String get() = formatDate("MM")
    val Date_dd: String get() = formatDate("dd")
    val Date_yyyy: String get() = formatDate("yyyy")
    val Date_yyyy_MM_dd: String get() = formatDate("yyyy-MM-dd")

    // 获取当前时间
    private val currentTime: LocalDateTime
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val formatterTime_HH_MM: String get() = formatTime("HH:mm")
    val formattedTime_Hour: String get() = formatTime("HH")
    val formattedTime_Minute: String get() = formatTime("mm")

    val DateTime_yyyy_MM_dd_HH_mm: String get() = "$Date_yyyy_MM_dd $formatterTime_HH_MM"

    // 辅助函数：格式化日期
    private fun formatDate(pattern: String): String {
        val date = today
        return date.toString().let {
            when (pattern) {
                "yyyy-MM" -> "${date.year}-${date.monthNumber.toString().padStart(2, '0')}"
                "MM-dd" -> "${date.monthNumber.toString().padStart(2, '0')}-${date.dayOfMonth.toString().padStart(2, '0')}"
                "MM" -> date.monthNumber.toString().padStart(2, '0')
                "dd" -> date.dayOfMonth.toString().padStart(2, '0')
                "yyyy" -> date.year.toString()
                "yyyy-MM-dd" -> date.toString()
                else -> date.toString()
            }
        }
    }

    // 辅助函数：格式化时间
    private fun formatTime(pattern: String): String {
        val time = currentTime
        return when (pattern) {
            "HH:mm" -> "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
            "HH" -> time.hour.toString().padStart(2, '0')
            "mm" -> time.minute.toString().padStart(2, '0')
            else -> time.toString()
        }
    }
}
