# 接口文档

> Base URL: `http://localhost:3001/api`
> 统一响应格式: `{ "code": 200, "message": "success", "data": {} }`
> 认证: 请求头 `Authorization: Bearer <token>`（JWT）
> 字段类型约定：金额一律为 number（BigDecimal，两位小数）；日期一律为 `yyyy-MM-dd`（后端 LocalDate）

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
- 响应 data: `[{ "id", "title", "description", "price": number, "imageUrl", "createdAt", "updatedAt" }]`
- `price` 为 BigDecimal（number，两位小数）

### 房型详情
- **GET** `/room-types/{id}`
- 无需认证

## 预订接口

### 预订列表
- **GET** `/bookings?page=&pageSize=&status=&keyword=`
- 需认证
- 响应 data: `{ "list": [BookingVO], "total": number }`
- BookingVO 字段（联表展示，含用户名/房型名）:
  `{ "id", "userId", "roomTypeId", "restaurantId", "type", "guestName", "guestPhone", "guestEmail", "checkInDate": "yyyy-MM-dd", "checkOutDate": "yyyy-MM-dd", "guestCount", "specialRequests", "status", "totalPrice": number, "createdAt", "updatedAt", "userName", "userPhone", "roomTitle", "roomPrice": number, "roomImageUrl", "roomDescription" }`
- `type` 取值: `room`（客房）/ `restaurant`（餐饮）；餐饮预订的 `roomTitle` 等房型字段为 null

### 预订详情
- **GET** `/bookings/{id}`
- 需认证
- 响应 data: 单个 BookingVO（同列表元素结构）

### 创建预订
- **POST** `/bookings`
- 需认证（userId 由后端从登录态解析）
- 请求体: `{ "roomTypeId": number, "guestName": "string", "guestPhone": "string", "guestEmail": "string", "checkInDate": "yyyy-MM-dd", "checkOutDate": "yyyy-MM-dd", "guestCount": number, "specialRequests": "string" }`
- `totalPrice` 由后端按房型单价 × 晚数计算，前端无需提交

### 更新预订
- **PUT** `/bookings/{id}`
- 需认证（ADMIN）
- 请求体同创建预订

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
- 响应 data: `{ "list": [UserInfo], "total": number }`
- **脱敏说明**：UserInfo 仅含 `{ "id", "name", "phone", "email", "createdAt", "updatedAt" }`，响应体已剔除 `password` 敏感字段（后端 @JsonIgnore 脱敏）

### 用户详情
- **GET** `/users/{id}`
- 需认证（ADMIN）
- 响应 data: UserInfo（同上，不含 `password`）

### 更新用户
- **PUT** `/users/{id}`
- 需认证（ADMIN）
- 请求体为 UserInfo 字段；`password` 字段被忽略，**无法通过此接口修改密码**（Service 层保留原密码）

### 删除用户
- **DELETE** `/users/{id}`
- 需认证（ADMIN）

## 餐饮接口

### 餐厅列表
- **GET** `/restaurants`
- 无需认证
- 响应 data: `[{ "id", "name", "description", "imageUrl", "createdAt", "updatedAt" }]`
- **字段名为 `name`**（前端已统一，不得使用 `title`）；餐厅无价格字段

### 餐厅预订
- **POST** `/restaurants/book`
- 需认证
- 请求体: `{ "restaurantId": number, "reservationDate": "string", "reservationTime": "string", "numberOfGuests": number, "specialRequests": "string", "userId": number }`
- 说明：写入 bookings 表 `type='restaurant'`，`roomTypeId` 为 NULL，`totalPrice` 为 0

## 仪表盘接口

### 统计数据
- **GET** `/dashboard/stats`
- 需认证（ADMIN）
- 响应 data: `{ "totalBookings", "pendingBookings", "confirmedBookings", "completedBookings", "todayCount", "activeUsers", "totalRevenue": number }`
- `todayCount`：今日新增预订数（createdAt 在今天 0 点之后）；`totalRevenue` 为 BigDecimal（number，两位小数）

### 近期预订
- **GET** `/dashboard/recent-bookings`
- 需认证（ADMIN）
- 响应 data: BookingVO 数组（同预订列表元素结构）
