# 变更历史

> 只追加新小节，不覆盖旧历史。格式：## 日期，逐条写新增、修复、优化点。

## 2026-08-16（P2 代码质量）

- 优化 BookingController.create：移除手动解析 Authorization 头代码，改用 @AuthenticationPrincipal 注入当前登录用户（JwtAuthenticationFilter 的 principal 即为 userId），顺带清理 JwtUtil/RoomTypeMapper 死依赖
- 优化仪表盘性能：BookingService.getRecentBookings 对应的 findAllWithDetails SQL 增加 LIMIT 10，避免全表加载
- 修正 SecurityConfig 权限规则：删除无实际控制器映射的 /api/admin/** 死规则，改为按 HttpMethod 精确匹配——预订查询/创建（GET/POST /api/bookings）登录用户可访问，预订管理（PUT/PATCH/DELETE）、用户管理（/api/users/**）、仪表盘（/api/dashboard/**）仅 ADMIN，消除普通用户越权操作风险
- 新增仪表盘 todayCount 统计：getStats() 返回今日新增预订数（createdAt 在今天 0 点后），前端 Dashboard 原有兼容逻辑直接生效
- 用户数据脱敏：User.password 增加 @JsonIgnore，用户列表/详情响应体剔除 password 字段，防止密码泄漏；更新用户接口无法修改密码（Service 保留原密码）
- 前端清理：删除脚手架遗留 components/HelloWorld.vue；tsconfig.app.json 重新启用 noUnusedLocals/noUnusedParameters，清理 8 处未使用导入/变量（router beforeEach from、AdminLayout Menu/X、BookingDetail CreditCard、BookingList Edit、UserList User、Home handleBookService 参数、RoomBooking currentComponent）
- 文档同步：api.md 补充 todayCount 字段说明与用户接口脱敏注释；dev-plan.md P2 全部勾选

## 2026-08-16

- 修复 P1-1 预订金额精度冲突：Booking.totalPrice 由 Double 改为 BigDecimal（与 DB decimal(10,2) 一致），创建/更新预订总价改为 BigDecimal 精确乘法；RoomType.price 同类一并改为 BigDecimal（含 DataInitConfig 种子数据适配）
- 修复 P1-1 预订日期类型冲突：Booking.checkInDate/checkOutDate 由 String 改为 LocalDate，BookingDTO 同步改为 LocalDate + @JsonFormat(yyyy-MM-dd)，前端提交格式不变
- 修复 P1-2 联表字段映射：新增 BookingVO（userName/userPhone/roomTitle/roomPrice/roomImageUrl/roomDescription），BookingMapper 三处 JOIN 查询补全房型单价/图片/描述列并返回 BookingVO，预订列表/详情/仪表盘近期预订不再缺失展示字段
- 修复 P1-3 餐厅字段不一致：前端 Restaurant 类型与 Home/DiningList/DiningBooking/RoomBooking 由 title 统一改为后端 name，删除不存在的 price 字段引用
- 修复 P1-4 种子数据双轨不一致：删除 seed.sql 中 admin 的 INSERT（原 hash 对应明文 admin），admin 账号统一由 DataInitConfig 初始化 admin/admin123
- 修复 schema.sql/seed.sql 与真实库脱节问题：仓库脚本原为 snake_case 列名，与实体 @TableField 驼峰列名不一致，按真实库结构重写（bookings 含 type/restaurantId/bookingDate/bookingTime 列），确保按脚本重建库可正常运行
- 文档同步：api.md 修正创建预订请求体字段（roomId/guests → roomTypeId/guestCount），标注 BookingVO 联表字段、金额/日期类型约定；dev-plan.md P1 全部勾选
- 前端配套：新增 utils/money.ts 金额格式化（保留两位小数），Booking/BookingList/BookingDetail/Dashboard 金额展示统一使用；前台订房提交体删除冗余 userId/totalPrice 字段

## 2026-08-05

- 修复 AuthService.register 重复检测逻辑：原 findByPhoneOrEmail(phone) 同时匹配 username/phone/email 导致误判；改用两条独立 LambdaQueryWrapper 分别检测 phone 和 email 唯一性
- 优化 .gitignore：忽略 .trae/、学术文档(.docx/.doc)、项目截图(images/)、根目录草稿文档
- 生成 Maven Wrapper（mvnw + mvnw.cmd），后端无需全局安装 Maven
- 配置 Git 全局 user.name 和 user.email

## 2026-08-02

- 后端启动验证通过：端口 3001，MySQL HikariPool 连接正常，DataInitConfig 初始化 admin/admin123 + 6 房型 + 6 餐厅数据
- 修复 60 个前端 TypeScript 编译错误：vite.config.ts @types/node、auth.ts adminLogin 字段、News.vue 静态数据、DiningBooking.vue restaurantAPI.book、Dashboard/News 数组类型、$event.target 断言、Contact.vue FormErrors 接口、RoomBooking.vue emit 类型、RoomEnvironment.vue timer 类型
- 后端补全接口：PUT /api/bookings/{id}、PUT /api/users/{id}、POST /api/restaurants/book
- 修复用户登录"密码错误"bug：UserMapper.findByPhoneOrEmail 原只查 phone/email，用户用姓名登录查不到；改为 username OR phone OR email 三种方式登录
- 修复登录返回 user.name 为 null：自定义 @Select 查询不使用 @TableField 映射，改用 username AS name 别名
- 前端 npm run build 通过：21.88s，3454 模块，TS 错误全部清零
- 端到端验证通过：首页加载、admin/admin123 登录、dashboard 跳转、API 调用、无控制台错误
- 删除 IDEA 编译产物 out/、空目录、76MB 旧 Vue 学习项目 packages/

## 2026-07-12

- 项目初始搭建：Spring Boot 3.2.5 后端 + Vue 3.5 前端
- 后端架构：controller/service/mapper/entity/dto/security/exception 分层
- 前端架构：Vue Router + Pinia + Element Plus + Axios
- 数据库初始化：schema.sql 建表、seed.sql 种子数据、DataInitConfig 代码初始化
- JWT 认证：JwtUtil 生成/验证令牌，JwtAuthenticationFilter 请求拦截
- Spring Security 配置：白名单路径 + ADMIN 角色拦截
