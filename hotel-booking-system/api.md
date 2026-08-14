# 接口文档

> Base URL: `http://localhost:3001/api`
> 统一响应格式: `{ "code": 200, "message": "success", "data": {} }`
> 认证: 请求头 `Authorization: Bearer <token>`（JWT）

## 认证接口

### 用户登录
- **POST** `/auth/login`
- 请求体: `{ "username": "string", "password": "string" }`
- username 支持用户名/手机号/邮箱三种方式
- 响应 data: `{ "token": "string", "user": { "id", "name", "phone", "email" }, "isAdmin": false }`

### 管理员登录
- **POST** `/auth/admin/login`
- 请求体: `{ "username": "string", "password": "string" }`
- 响应 data: `{ "token": "string", "user": { "id", "username" }, "isAdmin": true }`

### 用户注册
- **POST** `/auth/register`
- 请求体: `{ "name": "string", "phone": "string", "email": "string", "password": "string" }`
- 手机号和邮箱分别检测唯一性

## 房型接口

### 房型列表
- **GET** `/room-types`
- 无需认证
- 响应 data: `[{ "id", "title", "description", "price", "imageUrl", "createdAt", "updatedAt" }]`

### 房型详情
- **GET** `/room-types/{id}`
- 无需认证

## 预订接口

### 预订列表
- **GET** `/bookings?page=&pageSize=&status=&keyword=`
- 需认证
- 响应 data: `{ "list": [], "total": number }`

### 预订详情
- **GET** `/bookings/{id}`
- 需认证

### 创建预订
- **POST** `/bookings`
- 需认证
- 请求体: 预订信息（roomId, checkInDate, checkOutDate, guests 等）

### 更新预订
- **PUT** `/bookings/{id}`
- 需认证（ADMIN）

### 更新预订状态
- **PATCH** `/bookings/{id}/status`
- 需认证（ADMIN）
- 请求体: `{ "status": "string" }`

### 删除预订
- **DELETE** `/bookings/{id}`
- 需认证（ADMIN）

### 导出预订
- **GET** `/bookings/export`
- 需认证（ADMIN）
- 当前为占位接口，返回"导出功能开发中"

## 用户接口

### 用户列表
- **GET** `/users?page=&pageSize=`
- 需认证（ADMIN）
- 响应 data: `{ "list": [], "total": number }`

### 用户详情
- **GET** `/users/{id}`
- 需认证（ADMIN）

### 更新用户
- **PUT** `/users/{id}`
- 需认证（ADMIN）

### 删除用户
- **DELETE** `/users/{id}`
- 需认证（ADMIN）

## 餐饮接口

### 餐厅列表
- **GET** `/restaurants`
- 无需认证

### 餐厅预订
- **POST** `/restaurants/book`
- 需认证
- 请求体: `{ "restaurantId": number, "reservationDate": "string", "reservationTime": "string", "numberOfGuests": number, "specialRequests": "string", "userId": number }`

## 仪表盘接口

### 统计数据
- **GET** `/dashboard/stats`
- 需认证（ADMIN）
- 响应 data: `{ "totalBookings", "pendingBookings", "confirmedBookings", "completedBookings", "activeUsers", "totalRevenue" }`

### 近期预订
- **GET** `/dashboard/recent-bookings`
- 需认证（ADMIN）
