package com.chiuxah.blog.model.bean

import java.sql.Timestamp

data class ReadHistoryInfoSummary(
    val uid : Int,
    val last_view_time : Timestamp
)
