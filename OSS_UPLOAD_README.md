# 阿里云OSS图片上传API使用说明

## 快速开始

### 1. 配置OSS参数

编辑 `src/main/resources/application.properties`，替换以下占位符为您的真实OSS配置：

```properties
aliyun.oss.endpoint=oss-cn-hangzhou.aliyuncs.com
aliyun.oss.accessKeyId=YOUR_ACCESS_KEY_ID
aliyun.oss.accessKeySecret=YOUR_ACCESS_KEY_SECRET
aliyun.oss.bucketName=your-bucket-name
aliyun.oss.urlPrefix=https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

### 3. 用户登录

**重要：上传图片前必须先登录获取JWT Token**

```bash
# 用户登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"your_username","password":"your_password"}'

# 响应会包含token
{
  "success": true,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

### 4. 上传图片

使用获取到的JWT Token上传图片：

```bash
curl -X POST http://localhost:8080/api/upload/image \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/image.jpg"
```

### 5. 响应示例

**成功响应：**

```json
{
  "code": 200,
  "success": true,
  "message": "图片上传成功",
  "data": {
    "id": 1,
    "url": "https://your-bucket.oss-cn-hangzhou.aliyuncs.com/images/abc123.jpg",
    "fileName": "test.jpg",
    "fileSize": 102400,
    "contentType": "image/jpeg",
    "uploadTime": "2025-12-03T20:45:00"
  }
}
```

**未登录错误响应：**
```json
{
  "code": 401,
  "success": false,
  "message": "请先登录后再上传图片",
  "data": null
}
```

## API接口

### POST /api/upload/image
上传图片到OSS

**请求参数：**
- `file` (MultipartFile) - 图片文件

**支持格式：** JPG, PNG, GIF, BMP, WEBP  
**文件大小限制：** 最大10MB  
**需要认证：** 是（JWT Token）

### GET /api/upload/records
获取当前用户的上传记录

**需要认证：** 是

### GET /api/upload/records/all
获取所有上传记录（管理员）

**需要认证：** 是

## 功能特性

✅ 自动生成唯一文件名（UUID）  
✅ 文件类型验证  
✅ 文件大小限制  
✅ 自动记录上传信息到数据库  
✅ 返回可访问的文件URL  
✅ 支持用户关联  

## 数据库表

系统会自动创建 `upload_records` 表来存储上传记录。

## 注意事项

1. 确保OSS Bucket已创建并配置了公共读权限
2. 确保AccessKey有上传权限
3. 上传接口需要JWT认证
4. 文件会存储在OSS的 `images/` 目录下
