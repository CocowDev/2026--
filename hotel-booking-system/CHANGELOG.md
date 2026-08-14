# 变更历史

> 只追加新小节，不覆盖旧历史。格式：## 日期，逐条写新增、修复、优化点。

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
