# API

### Login API

request：

```json
method:"post"
url:"/login"
data:{
    "username":
    "passwd":
    "verifyCode":
}
```

response:

```json
{
  "loginstate":"success"/""
  "logintime":"yyyy-MM-dd HH:mm:ss"
}
```

question：

1. 加密与解密方式，是否要考虑安全性 https://blog.csdn.net/qq_37969433/article/details/82343804
2. 前端获取token后，能否伪造http？后端有哪些措施？ https://blog.fundebug.com/2017/08/16/xss_steal_cookie/
3. 登录错误有多种情况吗？



### Account API

####  个人用户信息

request:

```json
method:"get"
url:"/account"
param:{
    "username":
}
```

response: **(login required)**

```json
{
  "username":""
  "realname":""
  "mail":""
  "rolename":""
}
```

question：

username是作为param还是url ( /account/{username} )

#### 所有用户信息

request:

```json
method:"post"
url:"/accounts"
param:{
    "search":
    "role":(多选)
}
```

response: **(role required)**

```json
{
  "users":[
    {
      "username":""
      "realname":""
      "mail":""
      "rolename":""
    },{},{}...
  ]
}
```

说明：若 "search" 和 "role" 都为空，返回所有用户。（这里可以探讨一下……是否要多做几个不同接口）（还有该用post还是get……）

#### 增加用户

request:

```json
method:"post"
url:"/accounts/add"
data:{
    "username":""
    "passwd":""
    "realname":""
    "mail":""
    "rolename":""
    "studentid":""
}
```

response: **(role required)**

```json
{
  success:"true"/"false"
  errinfo:""/"existing username"/"existing studentid"...
}
```

success时errinfo为空？

#### 删除用户

request:

```json
method:"post"
url:"/accounts/delete"
data:{
    "username"
}
```

response: **(role required)**

```json
{
  success:"true"/"false"
  errinfo:""/"nonexisting username""...
}
```

success时errinfo为空？

#### 修改用户

request:

```json
method:"post"
url:"/accounts/delete"
data:{
    "username":""
    "passwd":""
    "realname":""
    "mail":""
    "rolename":""
    "studentid":""(mandatory)
}
```

response: **(role required)**

```json
{
  success:"true"/"false"
  errinfo:""/"nonexisting username""...
}
```

studentid必填且不能修改，其他的若留空则表示不修改



### Material Management API

####  获取所有物资信息

request:

```json
method:"get"
url:"/materials"
param:{
    "search":""
    "role":
    ...(各种分类，我也不确定)
}
```

response: **(login required)**

```json
{
  materials:[
      {
          "name":
          "id":
          "quantity":
          ...(各种参数...)
      }
  ]
}
```

question：

这个接口可以获得哪些信息？按照原本的想法，普通用户不能得到物品的出借人，这种是后端过滤吗，还是把出借人之类的信息做另外的接口……

#### 获取单个物资详细信息

#### 增加条目

#### 删除条目

#### 修改条目

#### 出借物资

#### 归还物资

#### 评论或反馈状态



### Mail API

### Announcement API





