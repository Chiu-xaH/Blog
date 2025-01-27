package com.chiuxah.blog.util.state

/*
200 成功
302 重定向
400 错误请求
401 未登录，需要登录
403 禁止访问，权限不足
404 资源未找到
405 方法不允许，例如POST用GET
500 服务器内部错误
502 上游服务器不可用或响应无效
503 服务器维护或过载
 */

enum class StatusCode(val code: Int) {
    SUCCESS(200),
    REDIRECT(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503);
}