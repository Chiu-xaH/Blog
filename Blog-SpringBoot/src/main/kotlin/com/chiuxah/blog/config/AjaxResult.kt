package com.chiuxah.blog.config

import com.chiuxah.blog.util.state.StatusCode


// 自定义返回数据
object AjaxResult {
    fun success(msg : Any = "success",data : Any) : Any {
        return mapOf(
            "state" to StatusCode.SUCCESS.code,
            "msg" to msg,
            "data" to data
        )
    }
    fun fail(state : StatusCode,msg : Any,data : Any = "") : Any {
        return mapOf(
            "state" to state.code,
            "msg" to msg,
            "data" to data
        )
    }
}

