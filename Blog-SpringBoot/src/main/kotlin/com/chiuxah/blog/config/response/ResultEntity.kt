package com.chiuxah.blog.config.response


// 自定义返回数据
object ResultEntity {
    fun success(msg : String = "success",data : Any? = null) : Any {
        return mapOf(
            "state" to StatusCode.OK.code,// 前端根据state=200判断是否成功
            "msg" to msg,
            "data" to data
        )
    }
    fun fail(state : StatusCode, msg : String, data : Any? = null) : Any {
        return mapOf(
            "state" to state.code,
            "msg" to msg,
            "data" to data
        )
    }
}

