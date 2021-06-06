# myCrowdfunding
## 基于 Springboot 和 Springcloud 搭建的众筹项目
### 后台管理员系统：
#### 使用的技术：
jQuery + Ajax + Bootstrap + zTree + Layer + Spring + SpringMVC+ MyBatis + MySQL + Springsecurity
#### 实现的功能：
使用 SpringMVC 的异常映射机制实现项目中错误信息的统一管理 <br>
使用 MyBatis 的 PageHelper 插件实现数据的分页显示 <br>
使用 Springsecurity 接管项目的登录、登录检查和权限验证，完成了基于角色的权限管理（RBAC 权限模型） <br>
实现了为管理员分配权限的功能 <br>
### 前台会员系统：
#### 使用的技术：
jQuery + Ajax + Bootstrap +  Layer + Springboot + Springcloud + MySQL+ Redis 
#### 实现的功能：
调用第三方接口给用户手机发送短信 <br>
使用 BcryptPasswordEncoder 实现盐值加密 <br>
使用 Springsession 解决分布式环境下 Session 不一致的问题 <br>
使用 Redis 实现单点登录和作为 Springsession 的 Session 库 <br>
使用阿里云OOS对象存储服务保存用户上传的图片 <br>
调用支付宝开放平台提供的支付接口，完成沙箱环境下的支付功能
