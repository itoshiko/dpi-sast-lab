# API

注：

* 带有""的字段表示请使用String（无论长得像不像数字，就算真的是数字也请转换成String）;
* 如果发现有一些方法返回的只有一个字符串"failed"，那么就是JSON转换错误（几乎不可能发生）。

### Login API

#### 登录

request：<u>(表单提交而非JSON)</u>

```json
method:"POST"
url:"/login"
data:{
    "username":
    "password":
    "verifyCode":
}
```

注：每次申请验证码，验证码图片都会以流的形式输出流输出图片的byte数组，response输出类型为image/jpeg，而文本的验证码将会储存在session中。

response:

```json
{
  "username":"", //只有登录成功时才有这个字段
  "code":, //见下表
  "message":"",
  "data": //一般是空的，没有内容
  "success", true //false
  "date":"" //yyyy-MM-dd
}
```

```java
public enum ResultEnum {
    SUCCESS(101,"成功"),
    FAILURE(102,"失败"),
    USER_NEED_AUTHORITIES(201,"用户未登录"),
    USER_LOGIN_FAILED(202,"用户账号或密码错误"),
    USER_LOGIN_SUCCESS(203,"用户登录成功"),
    USER_NO_ACCESS(204,"用户无权访问"),
    USER_LOGOUT_SUCCESS(205,"用户登出成功"),
    TOKEN_IS_BLACKLIST(206,"此token为黑名单"),
    LOGIN_IS_OVERDUE(207,"登录已失效"),
    ;
}
```

#### 登出

```json
method:"GET"
url:"/logout"
data:{
    "username":
    "password":
    "verifyCode":
}
```

response

```json
{
  "code":, //见下表
  "message":"",
  "data": //一般是空的，没有内容
  "success", true //false
  "date":"" //yyyy-MM-dd
}
```

#### 获取验证码

```json
method:"GET"
url:"/getVerifyCode"
```

返回流，并在session中写入验证码字符串



### Account API

####  个人用户信息

request:（请求的用户名必须与当前登录的用户名一致）

```json
method:"GET"
url:"/account/{username}"
```

response: 

```json
{
 "sysRoles":[{"roleId":1,"roleName":"ROLE_NORMAL"},{"roleId":2,"roleName":"ROLE_ADMIN"},{"roleId":3,"roleName":"ROLE_ROOT"}],
 "authorities":null,
 "username":"zznb",
 "password":"",
 "mail":"zhengc18@mails.tinghua.edu.cn",
 "realName":"郑郑子",
 "studentId":"2018010647",
 "uid":4,
 "enabled":true,
 "serialVersionUID":7171722954972237961,
 "accountNonLocked":true,
 "accountNonExpired":true,
 "credentialsNonExpired":true
}
```

#### 所有用户信息

request:（需要ROLE_ADMIN及以上的权限）

```json
method:"POST"
url:"/accounts"
param:{
    "search":"",
    "role":[] //String数组
}
```

response:

```json
"users":[]
```

说明：若 "search" 和 "role" 都为空，返回所有用户。

#### 增加用户

request:（需要ROLE_ADMIN及以上的权限）

```json
method:"POST"
url:"/accounts/add"
data:{
    "userName":"",
    "realName":"",
    "mail":"",
    "roleName":"",
    "studentId":""
    //新增加的用户默认只拥有最低的权限，需要提升权限时应后期修改
}
```

response:

```json
{
  success:"true"/"false",
  errInfo:["",""], //"username existed"/"student ID existed"/"mail existed"/"unexpected error"
  errCode: //没啥用，写着玩
}
```

success时errInfo为""

#### 删除用户

***尽量不要使用，很麻烦，借还记录之类的全部会被删除，指不定哪里就出bug了***

request:（需要ROLE_ADMIN及以上的权限）

```json
method:"POST"
url:"/accounts/delete"
data:{
    "username":""
}
```

response: **(role required)**

```json
{
  success:"true"/"false"
  errinfo:""/"user not found"/"unexpected error", //当权限不足时返回类似登陆失败的格式
  errCode: //没啥用，写着玩
}
```

success时errinfo为""

#### 使用户失效

request：（需要ROLE_ADMIN及以上的权限）

```json
method:"POST"
url:"/accounts/disable"
data:{
    "username":""
}
```

response同删除用户

#### 解除用户失效状态

request：（需要ROLE_ADMIN及以上的权限）

```json
method:"POST"
url:"/accounts/enable"
data:{
    "username":""
}
```

response同删除用户

#### 修改用户
request:
```json
method:"POST"
url:"/accounts/update"
data:{
    "username":"",
    "realName":"",
    "mail":"",
    "studentId":""(mandatory)
}
```

response: **(role required)**

```json
{
  success:"true"/"false"
  errinfo:[] //可能有多个，没有时为空。"invalid student id"/"denied"/"mail existed"/"invalid email"/"username existed",
  errCode: //没啥用，写着玩
}
```

studentid必填且不能修改，其他的若留空则表示不修改

#### 修改密码

修改后会登出，只能修改自己的密码

```json
method:"POST"
url:"/accounts/update-password"
data:{
    "username":"",
    "passwd":"",
    "oldPassword":""
}
```

response:

```json
{
  success:"true"/"false"
  errInfo:""/"invalid username"/"password too weak",
  errCode: //没啥用，写着玩
}
```

#### 修改用户权限

修改权限后会登出，只有ROOT才能修改权限

request：

```json
method:"POST"
url:"/accounts/update-password"
data:{
    "username":"",
    "roles":["",""] //当空时默认赋予最低权限，只能为ROLE_NORMAL，ROLE_ADMIN，ROLE_ROOT
}
```

response：

```json
{
  success:"true"/"false"
  errInfo:""/"invalid username",
  errCode: //没啥用，写着玩
}
```







### Material Management API

####  获取/搜索所有物资信息

request:（需要登录）

```json
method:"GET"
url:"/materials"
param:{
    "classification":"", //String,分类
    "priceHigh":0, //int,最高单价
    "priceLow":0, //int,最低单价
    "isLoanable":true, //boolean,是否可外借
    "needReview":true, //boolean,外借时是否需要审核
    "dateHigh":"yyyy-MM-dd", //String,最晚入库日期
    "dateLow":"yyyy-MM-dd", //String,最早入库日期
    "remained":true, //是否有库存
    "tags":["Wireless", "MCU"] //String数组，标签
}
```

response: 

```json
[]  //结果集合，单个结果模型见下方
```

```java
public class SysMaterial implements Serializable {
    static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String classification;
    private int price;
    private boolean canLoan;
    private boolean needReview;
    private Date warehousingDate;
    private int total;
    private int remaining;
    private String storageLocation;
    private ArrayList<SysTag> tags;
}
```



#### 获取单个物资详细信息

request:（需要登录）

```json
method:"GET"
url:"/materials/detail/{id}"
```

response:

```json
{
    ... //上述SysMaterial的所有字段
    "imgList": [] //物品图片UUID集合(String)
    "docList": [] //物品文档UUID集合(String)
}
```



#### 获取单个物资的文档/图片信息

request:（需要登录）

```json
method:"GET"
url:"/materials/detail-extra/{id}"
```

response:

```json
{
    "mid":,
    "img":[],
	"doc":[]
}
```

#### 增加条目

request:（需要ROLE_ADMIN及以上的权限）

```json
method:"POST"
url:"/materials/add"
param:{
    "name":"", //名称，String
    "classification":"" //分类号，String
    "price":, //单价，int
    "canLoan":, //是否可以外借，boolean
    "needReview":, //外借是否需要审核，boolean
    "warehousingDate":"", //入库日期，String
    "total":, //总数量，int
    "remaining":, //剩余数量，int
    "storageLocation":"", //储存位置，String，可以为空
    "tags":[{"tagId":,"tagName":{}},{},{}] //标签，String数组，可以为空
}
```

response:

```json
{
  success:"true"/"false",
  errInfo:""/"unexpected error",
  id:"" //添加的物资的id，失败时为空
}
```

#### 删除条目

request:（需要ROLE_ADMIN及以上的权限）

```json
method:"POST"
url:"materials/delete"
param:{
    "materialId": //物资的ID
}
```

response:

```json
{
  success:"true"/"false",
  errInfo:""/"invalid id"/"unexpected error"
}
```

#### 修改条目



#### 出借物资

#### 归还物资

#### 评论或反馈状态



### Mail API

### Announcement API





