package com.chiuxah.blog.model.base

import java.sql.Timestamp

abstract class BaseSqlBean {
    open val id : Int = 0
    open val create_time : Timestamp? = null
}
