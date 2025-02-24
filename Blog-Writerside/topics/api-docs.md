# Start

## Authentication

Header[Cookie]

## Base URL

http://localhost:8080/api/v1

## Rate Limiting

No.

## Error Handling

返回结果固定为
```json
{
  "state" : 200,
  "msg" : "String?",
  "data" : "Any?"
}
```
data用于数据装载，msg为提示信息，state != 200时为请求失败，具体对照表请看quick-start


## Versioning

/api/v + version/...

如需重定向，使用state=302或301