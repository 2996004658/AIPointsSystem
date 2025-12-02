# Spring Boot 用户积分系统 - 使用指南

## 项目概述

这是一个基于 Spring Boot + JPA 的后端API系统，实现了用户注册登录、JWT token鉴权和积分管理功能。

### 核心功能
- ✅ 用户注册（初始积分10分）
- ✅ 用户登录（返回JWT token）
- ✅ 3个受保护的功能API（需要token鉴权，每次调用扣除2积分）

---

## 前置要求

在运行项目之前，请确保已安装：

1. **JDK 11 或更高版本**
   - 下载地址: https://www.oracle.com/java/technologies/downloads/
   - 验证安装: `java -version`

2. **Maven 3.6+**
   - 下载地址: https://maven.apache.org/download.cgi
   - 验证安装: `mvn -version`

---

## 快速启动

### 1. 构建项目

```bash
cd e:\Project\Demo\admin\server
mvn clean install -DskipTests
```

### 2. 运行应用

```bash
mvn spring-boot:run
```

应用将在 **http://localhost:8080** 启动

---

## API 接口文档

### 认证接口

#### 1. 用户注册
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**成功响应:**
```json
{
  "success": true,
  "message": "注册成功",
  "data": {
    "username": "testuser",
    "points": 10
  }
}
```

#### 2. 用户登录
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**成功响应:**
```json
{
  "success": true,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "testuser",
    "points": 10
  }
}
```

---

### 功能接口（需要Token鉴权）

> **注意:** 所有功能接口都需要在请求头中携带JWT token

#### 3. 功能1
```http
POST http://localhost:8080/api/feature/function1
Authorization: Bearer <your-jwt-token>
```

**成功响应:**
```json
{
  "success": true,
  "message": "功能1执行成功",
  "data": null
}
```

**控制台输出:**
```
=== 功能1被调用 ===
用户: testuser
扣除积分: 2
剩余积分: 8
==================
```

#### 4. 功能2
```http
POST http://localhost:8080/api/feature/function2
Authorization: Bearer <your-jwt-token>
```

#### 5. 功能3
```http
POST http://localhost:8080/api/feature/function3
Authorization: Bearer <your-jwt-token>
```

---

## 测试步骤

### 使用 curl 测试

#### 1. 注册用户
```bash
curl -X POST http://localhost:8080/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"password123\"}"
```

#### 2. 登录获取Token
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"password123\"}"
```

复制返回的 `token` 值

#### 3. 调用功能1（替换 YOUR_TOKEN）
```bash
curl -X POST http://localhost:8080/api/feature/function1 ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

#### 4. 调用功能2
```bash
curl -X POST http://localhost:8080/api/feature/function2 ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

#### 5. 调用功能3
```bash
curl -X POST http://localhost:8080/api/feature/function3 ^
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 使用 Postman 测试

### 1. 注册用户
- Method: `POST`
- URL: `http://localhost:8080/api/auth/register`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```

### 2. 登录
- Method: `POST`
- URL: `http://localhost:8080/api/auth/login`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```
- 复制响应中的 `token`

### 3. 调用功能API
- Method: `POST`
- URL: `http://localhost:8080/api/feature/function1`
- Headers: 
  - `Authorization: Bearer <粘贴你的token>`
- Body: 无

重复以上步骤测试 `function2` 和 `function3`

---

## 积分验证

用户初始积分为 **10分**，每次调用功能API扣除 **2分**：

- 第1次调用: 10 - 2 = **8分**
- 第2次调用: 8 - 2 = **6分**
- 第3次调用: 6 - 2 = **4分**
- 第4次调用: 4 - 2 = **2分**
- 第5次调用: 2 - 2 = **0分**
- 第6次调用: **积分不足，返回错误**

---

## H2 数据库控制台

访问 H2 数据库控制台查看数据：

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (留空)

---

## 项目结构

```
server/
├── pom.xml                                    # Maven配置
├── src/main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java              # 启动类
│   │   ├── config/
│   │   │   └── SecurityConfig.java           # Spring Security配置
│   │   ├── controller/
│   │   │   ├── AuthController.java           # 认证控制器
│   │   │   └── FeatureController.java        # 功能控制器
│   │   ├── dto/
│   │   │   ├── ApiResponse.java              # 统一响应格式
│   │   │   ├── LoginRequest.java             # 登录请求
│   │   │   └── RegisterRequest.java          # 注册请求
│   │   ├── entity/
│   │   │   └── User.java                     # 用户实体
│   │   ├── repository/
│   │   │   └── UserRepository.java           # 用户仓库
│   │   ├── security/
│   │   │   └── JwtAuthenticationFilter.java  # JWT过滤器
│   │   ├── service/
│   │   │   └── UserService.java              # 用户服务
│   │   └── util/
│   │       └── JwtUtil.java                  # JWT工具类
│   └── resources/
│       └── application.properties             # 应用配置
```

---

## 常见问题

### 1. 积分不足错误
```json
{
  "success": false,
  "message": "积分不足",
  "data": null
}
```
**解决方案:** 用户积分已用完，需要重新注册新用户或手动在数据库中增加积分

### 2. Token过期
```json
{
  "success": false,
  "message": "Token已过期",
  "data": null
}
```
**解决方案:** 重新登录获取新的token（token有效期24小时）

### 3. 未携带Token
**解决方案:** 确保在请求头中添加 `Authorization: Bearer <token>`

---

## 技术栈

- **Spring Boot 2.7.14**
- **Spring Data JPA** - 数据持久化
- **Spring Security** - 安全框架
- **JWT (JJWT 0.11.5)** - Token认证
- **H2 Database** - 内存数据库
- **Lombok** - 简化代码
- **BCrypt** - 密码加密

---

## 安全特性

✅ 密码使用 BCrypt 加密存储  
✅ JWT token 认证  
✅ Token 24小时过期  
✅ 受保护的API端点  
✅ 线程安全的积分扣除（synchronized）
