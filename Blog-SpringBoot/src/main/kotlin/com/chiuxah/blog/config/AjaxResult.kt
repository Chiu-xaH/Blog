package com.chiuxah.blog.config

import java.util.Objects


// 自定义返回数据
object AjaxResult {
    // 成功
    fun success(data: Any) : Any {
        return mapOf(
            "state" to 200,
            "msg" to "success",
            "data" to data
        )
    }
    //
    fun success(msg : Any,data : Any) : Any {
        return mapOf(
            "state" to 200,
            "msg" to msg,
            "data" to data
        )
    }
    // 无data
    fun fail(state : Int,msg : Any) : Any {
        return mapOf(
            "state" to state,
            "msg" to msg,
            "data" to ""
        )
    }
    // 有data
    fun fail(state : Int,msg : Any,data : Any) : Any {
        return mapOf(
            "state" to state,
            "msg" to msg,
            "data" to data
        )
    }
}