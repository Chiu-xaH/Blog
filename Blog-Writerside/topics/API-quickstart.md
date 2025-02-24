# API Quickstart

## Prerequisites

除以下API不需要用户验证(登录)外，其余均需要登录



## Authentication

登录 /user/login或/user/login-with-code

## Making Your First Request

```http
GET /api/v1/search/article HTTP/1.1
Host: localhost:8080
Cookie: JSESSIONID=your-session

keyword=搜索关键词
```

## Response Handling

正常情况下返回永远为json且固定的格式：
```json
{
  "state" : 200,
  "msg" : "String?",
  "data" : "Any?"
}
```

当返回json的state为200时，代表请求成功，可以按接口文档对data进行解析

当返回json的state为非200时，通常意味着没获取到数据，具体含义见下表


| state | 状态 | 名称  |
|-------|----|-----|
| 200   | 成功返回用户请求的数据 | OK |
| 201   | 新建或修改数据成功 | CREATED |
| 202   | 一个请求已经进入后台排队(异步任务) | ACCEPTED |
| 204   | 删除数据成功 | NO_CONTENT |
| 301   | 接口变更 | MOVED |
| 302   | 重定向 | REDIRECT | 
| 400   | 错误非法请求，例如缺少必须参数 |BAD_REQUEST|
| 401   | 未登录，需要登录 |UNAUTHORIZED|
| 403   | 禁止访问，权限不足 |FORBIDDEN|
| 404   | 资源未找到 |NOT_FOUND|
| 405   | 方法不允许，例如POST用GET |METHOD_NOT_ALLOWED|
| 406   | 请求的格式不可得（比如用户请求JSON格式，但是只有XML格式） |NOT_ACCEPTABLE|
| 410   | 请求的资源被永久删除，且不会再得到的 |GONE|
| 422   | 当创建一个对象时，发生一个验证错误 |UNPROCESSABLE_ENTITY|
| 500   | 服务器内部错误 |INTERNAL_SERVER_ERROR|
| 502   | 上游服务器不可用或响应无效 |BAD_GATEWAY|
| 503   | 服务器维护或过载 |SERVICE_UNAVAILABLE|

## Next Steps

查看所有接口列表，以便快速上手