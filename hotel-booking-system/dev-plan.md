# 开发计划

> 更新待办清单，不新建计划文件。

## 已完成

- [x] 后端架构搭建（Spring Boot + MyBatis-Plus + Spring Security + JWT）
- [x] 前端架构搭建（Vue 3 + TS + Vite + Pinia + Element Plus）
- [x] 用户认证（注册/登录/管理员登录）
- [x] 房型管理（列表/详情）
- [x] 预订管理（增删改查/状态更新）
- [x] 餐饮预订（列表/预订接口）
- [x] 管理后台（Dashboard/预订管理/用户管理/系统设置）
- [x] 前端 60 个 TS 编译错误全部修复
- [x] 后端补全缺失接口（PUT bookings/users、POST restaurants/book）
- [x] 修复用户登录密码错误 bug（UserMapper 查询）
- [x] 修复注册重复检测逻辑（AuthService）
- [x] Maven Wrapper 集成
- [x] .gitignore 配置优化
- [x] Git 全局用户配置

## 待办

### P1 设计缺陷（影响数据正确性）

- [x] Booking.totalPrice 改 BigDecimal（当前 Double，DB 为 DECIMAL(10,2)）→ 已完成（实体/Service 计算/营收汇总一并改造）
- [x] Booking 日期字段 checkInDate/checkOutDate 改 LocalDate（当前 String，DB 为 DATE）→ 已完成（实体 + BookingDTO + @JsonFormat）
- [x] BookingMapper JOIN 查询字段映射（user_name/user_phone/room_title 无对应实体字段）→ 已完成（新增 BookingVO 承接联表字段，JOIN 补房型价格/图片/描述列）
- [x] 统一种子数据 admin 密码（DataInitConfig=admin123 vs seed.sql=admin）→ 已完成（seed.sql 删除 admin INSERT，admin 统一由 DataInitConfig 初始化）
- [x] Restaurant 前后端字段不一致（前端 title vs 后端 name）→ 已完成（前端统一使用 name）
- [x] 顺带修复：schema.sql/seed.sql 与真实库脱节（snake_case vs 驼峰列名）→ 已重写对齐真实库结构

### P2 代码质量

- [x] BookingController.create 改用 @AuthenticationPrincipal 替代手动解析 token → 已完成（principal 即为 userId，顺带清理 JwtUtil/RoomTypeMapper 死依赖）
- [x] BookingService.getRecentBookings() 加 LIMIT 防止全表加载 → 已完成（findAllWithDetails SQL 加 LIMIT 10）
- [x] SecurityConfig /api/admin/** 规则与实际 controller 映射不匹配 → 已完成（删除死规则，按 HttpMethod 精确匹配 bookings/users/dashboard 真实路径，管理操作收归 ADMIN）
- [x] 删除 HelloWorld.vue 脚手架遗留模板 → 已完成
- [x] tsconfig 重新启用 noUnusedLocals/noUnusedParameters 并清理导入 → 已完成（清理 8 处未使用导入/变量，构建全绿）
- [x] 新增发现：Dashboard 读取 todayCount 后端未返回，需在 getStats() 补充 → 已完成（getStats 新增 todayCount，前端已兼容）
- [x] 新增发现：User 列表响应含 password 字段，建议剔除 → 已完成（User.password 加 @JsonIgnore 脱敏，更新接口保留原密码）

### 测试覆盖

- [ ] 后端引入 JUnit 5 + Mockito，为 AuthService/BookingService 编写单元测试
- [ ] 前端引入 Vitest + @vue/test-utils，补充组件测试
- [ ] 前端 package.json 增加 test 脚本
