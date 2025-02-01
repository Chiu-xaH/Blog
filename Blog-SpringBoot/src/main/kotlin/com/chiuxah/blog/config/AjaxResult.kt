package com.chiuxah.blog.config

import com.chiuxah.blog.utils.enums.StatusCode


// 自定义返回数据
object AjaxResult {
    fun success(msg : String = "success",data : Any? = null) : Any {
        return mapOf(
            "state" to StatusCode.SUCCESS.code,
            "msg" to msg,
            "data" to data
        )
    }
    fun fail(state : StatusCode,msg : String,data : Any? = null) : Any {
        return mapOf(
            "state" to state.code,
            "msg" to msg,
            "data" to data
        )
    }
}

