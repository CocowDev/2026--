# 星级酒店预订与管理系统

> 基于 Vue3 + SpringBoot 的前后端分离架构

## 技术栈

| 层 | 技术 | 版本 |
|----|------|------|
| 前端 | Vue 3 + TypeScript + Vite | 3.5 / 5.8 / 6.3 |
| 前端 UI | Element Plus + Lucide Icons | 2.14 |
| 前端状态 | Pinia | 3.0 |
| 后端 | Spring Boot + MyBatis-Plus | 3.2.5 / 3.5.5 |
| 安全 | Spring Security + JWT | 3.2.5 / 0.12.5 |
| 数据库 | MySQL | 8.x |
| JDK | 17 | 17.0.17 |

## 快速启动

### 环境要求

- JDK 17
- Node.js 18+
- MySQL 8.x

### 1. 配置数据库

```sql
CREATE DATABASE hotel_booking;
```

连接信息见 `backend/src/main/resources/application.yml`（默认 root/123456，端口 3306）。

### 2. 启动后端（端口 3001）

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17.0.17.10-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

cd backend
.\mvnw.cmd spring-boot:run
```

DataInitConfig 会自动初始化种子数据（管理员 admin/admin123）。

### 3. 启动前端（端口 5173）

```powershell
cd frontend
npm install
npm run dev
```

### 4. 访问

- 前台：http://localhost:5173/
- 管理后台：http://localhost:5173/admin/login（admin/admin123）
- API：http://localhost:3001/api

## 运行测试

### 后端单元测试（JUnit 5 + Mockito，21 个用例）

```powershell
cd backend
.\mvnw.cmd test
```

### 前端组件测试（Vitest + @vue/test-utils，11 个用例）

```powershell
cd frontend
npm install
npm run test        # 一键执行全部组件测试
npm run test:watch  # 监听模式，改动自动重跑
```

测试文件位置：后端 `backend/src/test/java/`，前端 `frontend/src/**/*.test.ts`（与组件同目录）。

## 项目结构

```
hotel-booking-system/
├── backend/          # Spring Boot 后端
│   ├── src/main/java/com/hotel/booking/
│   │   ├── config/       # Cors, Security, DataInit, MyBatisPlus
│   │   ├── controller/   # Auth, Booking, User, RoomType, Restaurant, Dashboard
│   │   ├── service/      # AuthService, BookingService, UserService
│   │   ├── mapper/       # MyBatis-Plus Mapper
│   │   ├── entity/       # User, Admin, Booking, RoomType, Restaurant
│   │   ├── dto/          # ApiResponse, LoginDTO, RegisterDTO, BookingDTO
│   │   ├── security/     # JwtUtil, JwtAuthenticationFilter
│   │   └── exception/    # GlobalExceptionHandler
│   ├── .mvn/wrapper/     # Maven Wrapper
│   ├── mvnw / mvnw.cmd
│   └── pom.xml
├── frontend/         # Vue 3 + TS 前端
│   ├── src/
│   │   ├── api/index.ts      # Axios 封装与接口定义
│   │   ├── router/index.ts   # 路由（前台 + 后台）
│   │   ├── stores/auth.ts    # Pinia 认证状态
│   │   ├── views/            # 页面（含 admin/ 子目录）
│   │   ├── components/       # 通用组件
│   │   └── types/index.ts    # TS 类型定义
│   └── package.json
├── database/         # init.sql, schema.sql, seed.sql
├── CHANGELOG.md      # 变更历史
├── dev-plan.md        # 开发计划
├── api.md             # 接口文档
└── README.md          # 项目说明
```

## API 统一响应格式

```json
{ "code": 200, "message": "success", "data": {} }
```

详见 [api.md](api.md)。

## 相关文档

- [CHANGELOG.md](CHANGELOG.md) — 变更历史
- [dev-plan.md](dev-plan.md) — 开发计划与待办
- [api.md](api.md) — 接口文档
