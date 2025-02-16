package com.chiuxah.blog.model

import java.sql.Timestamp

abstract class BaseSqlBean {
    open val id : Int = 0
    open val create_time : Timestamp? = null
}


// 设计规范 完全对应数据库实体类的为Bean，Summary代表经过数据简化，用于列表展示
// 所有数据库表都应包含主键id和create_time，插入数据时由数据库默认指定