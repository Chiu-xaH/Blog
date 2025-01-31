package org.chiuxah.blog.logic.network.bean.main

import kotlinx.serialization.Serializable

// 基础的响应
//@Serializable
//data class ApiResponse(
//    override val state : Int,
//    override val msg : String,
//    override val data : Any?
//) : ApiBaseResponse()

// 基类
@Serializable
abstract class ApiBaseResponse {
    abstract val state: Int
    abstract val msg: String
    abstract val data: Any?
}

