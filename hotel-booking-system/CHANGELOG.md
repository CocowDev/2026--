# 变更历史

> 只追加新小节，不覆盖旧历史。格式：## 日期，逐条写新增、修复、优化点。

## 2026-08-16（内容扩充）

- 新闻数据扩充：news.ts 由 6 条增至 12 条，新增"酒店设施/服务指南"分类（恒温泳池、智能健身房、机场接送、宴会场地、亲子攻略、早茶地图等，均含完整正文）
- News 新闻页分类 tab 同步新增"酒店设施/服务指南"
- About 关于页文字扩充：hero 简介、酒店故事新增段落（设施与餐饮介绍）、核心使命/企业愿景/服务理念三卡描述加长、里程碑 6→8、团队 6→8、荣誉 4→6
- 验证：npm run build 通过

## 2026-08-16（轮播图真实化）

- 首页轮播 Carousel.vue：5 张轮播图由 picsum 占位改为本地真实图片（豪华客房/海景房/豪华餐厅/行政套房/自助餐厅，与业务图复用）
- 客房环境轮播 RoomEnvironment.vue：8 张环境图改为本地真实图片（卧室/客厅/套房/海景/餐厅等复用图）
- 验证：npm run build 通过

## 2026-08-16（四类聚合模糊搜索）

- 新增 /search 聚合搜索页：顶部搜索栏输入关键词 → 前端本地模糊匹配（includes）房型/餐厅/菜品/新闻四类结果，分类卡片展示，点击直达对应页面
- 新闻数据抽取为共享模块 src/data/news.ts（News.vue 与 Search.vue 共用），消除重复
- Header 搜索框由"仅跳新闻页"升级为跳转聚合搜索页
- 零后端改动（数据量小，前端本地过滤），验证 npm run build 通过

## 2026-08-16（局域网访问配置）

- 前端 package.json：dev/preview 脚本增加 --host 0.0.0.0，永久开启局域网访问
- 后端 application.yml：server.address 显式配置 0.0.0.0，放开局域网访问
- 前端 api baseURL 改为相对路径 /api + vite.config.ts 增加 /api 代理到 3001：局域网设备访问前端时请求自动转发，不再指向设备自身（修复其他设备连不上的关键问题）
- CORS 已配置 allowedOriginPatterns("*")，无需改动
- 验证：npm run build 通过

## 2026-08-16（管理员登录页守卫修复）

- 修复普通用户已登录时无法进入 /admin/login 的 bug：路由守卫 guest 规则误拦截"已登录普通用户"访问管理员登录页（被重定向回首页形成死循环）
- 修复方案：/admin/login 移除 guest 标记 + 守卫新增"管理员已登录直接进后台"分支；恢复 requiresAuth（个人中心）守卫
- 验证：npm run build 通过

## 2026-08-16（导航遮挡/取消反馈/背景优化）

- 修复导航栏遮挡：App.vue main-content 增加 70px 顶部占位（导航 fixed 高约 70px），所有页面首屏内容不再被导航遮挡
- 修复个人中心取消无反馈：Profile.vue handleCancel 拆分"确认弹窗取消"与"接口请求失败"——取消接口失败时明确提示错误信息（此前 catch 吞错导致无反馈）
- 背景丰富化：theme.css 全局背景改为淡金渐变 + 装饰光斑（三处 radial-gradient 叠加 fixed），页面不再纯色单调
- 实测验证：取消接口后端正常（取消成功、重复取消返回"仅待处理状态的订单可取消"）；npm run build 通过、组件测试 11/11 通过

## 2026-08-16（真实图片接入完成）

- 24 张真实图片（JPG，房型 6 / 餐厅 6 / 菜品 12）已由用户放入 frontend/public/images/，命名与清单一致
- 数据库接线完成：migrate-real-images.sql 按 ID 更新真实库 imageUrl 为 /images/*.jpg（房型 6 / 餐厅 6 / 菜品 12 全部切换，接口验证返回本地路径）
- 全部引用统一为 .jpg（seed.sql/migrate-dishes.sql/DataInitConfig/前端 fallback），残留 .avif 清零
- 全站图片由占位图切换为本地真实图（离线可用），页面加载更快

## 2026-08-16（真实图片接线准备）

- 图片本地化方案落地：创建 frontend/public/images/ 目录 + README 图片清单（24 张：房型 6 / 餐厅 6 / 菜品 12）
- 新增 database/migrate-real-images.sql 接线脚本：真实库房型/餐厅/菜品 imageUrl 由 picsum 占位图切换为本地 /images/*.jpg（用户提供 24 张 JPG 真实图）
- 新装库一致性：seed.sql、migrate-dishes.sql、DataInitConfig 图片 URL 全部改为本地路径（修复 buildDish 参数替换）
- 前端 8 个组件/页面的图片 fallback 改为本地图兜底（原 picsum 外链当前环境不可达，全部替换）
- 说明：当前执行环境无法访问外网图库（Unsplash/Pexels/Picsum 均不可达），需用户下载 24 张真实图片放入 frontend/public/images/ 后执行 migrate-real-images.sql 完成切换
- 验证：后端编译通过、前端 build 通过

## 2026-08-16（后端质量提升）

- Service 接口与实现分离：AuthService/BookingService/UserService 改为接口，新增 impl/AuthServiceImpl、BookingServiceImpl、UserServiceImpl 实现类（@Service），Controller 面向接口编程，便于单测 mock 与后续扩展
- 事务治理：所有写操作方法增加 @Transactional(rollbackFor = Exception.class)——注册、客房预订创建/更新/状态流转/删除、用户取消、服务预订、用户更新/删除；餐饮预订（bookings + booking_dishes + 总价回填三步）在 RestaurantController.book 统一事务，任一步失败整体回滚，杜绝脏数据
- 验证：后端单元测试 26/26 通过，接口冒烟（admin 登录 + 仪表盘统计）正常

## 2026-08-16（后台入口完全隐藏）

- 删除 Header 导航栏"管理后台"按钮（含 btn-admin 样式），主页对**所有角色（含管理员）**不再显示后台入口
- 管理员进后台仅通过 URL 直达（/admin/login 登录后自动跳转 /admin/dashboard，或已登录直接访问 /admin/**）
- 验证：npm run build 通过、组件测试 11/11 通过

## 2026-08-16（功能增强：餐饮菜品/服务预订/个人中心/后台提醒）

- 餐饮预订升级为两步流程：新增 dishes 表 + booking_dishes 关联表（schema.sql/seed.sql/migrate-dishes.sql），后端 GET /restaurants/{id}/dishes 菜品接口，POST /restaurants/book 支持携带 dishes 写入关联表并按菜品单价×数量计算 totalPrice；前端 DiningBooking 选择餐厅后点选菜品（数量加减 + 合计预览）
- 新增服务预订：POST /bookings/service 接口（type='service' 写入 bookings），前端 RoomBooking 服务分类点击"预约"弹出表单（日期/人数/联系方式）真实提交，Home"预约服务"跳转 /room-booking?tab=services 自动切换分类
- 新增个人中心 /profile 页面（用户信息 + 我的预订列表 + 待处理订单取消）；GET /bookings 增加权限隔离（普通用户仅本人订单、管理员全部），新增 POST /bookings/{id}/cancel 本人取消接口；Header 用户名点击进入个人中心，路由新增 requiresAuth 守卫
- 后台新增待处理预订提醒：AdminLayout 顶栏金色角标显示 pending 数量（进入加载 + 30s 轮询），点击直达待处理订单列表
- 修复潜伏 bug：RestaurantMapper 餐饮预订 INSERT 列名由 snake_case 修正为真实库驼峰列名（user_id→userId 等），餐饮预订恢复可用
- 布局紧凑化：8 个页面 hero 内边距/卡片 padding/网格间距统一收紧一档
- 验证：后端单元测试 26/26、前端组件测试 11/11、接口冒烟（餐饮带菜/取消/服务预订/权限隔离）全部通过

## 2026-08-16（表单交互优化）

- Register 注册表单：接入 utils/validation 字段级校验（手机号格式/邮箱格式/密码强度/两次密码一致），错误内联显示到字段下方，输入时即时清除错误提示
- Booking 订房表单：补充提交加载态（提交中... + 按钮禁用防重复下单）、联系电话格式校验
- Contact 留言表单：模拟提交延迟（800ms）+ 按钮加载态/禁用，成功提示保持 ✓ 对勾反馈，补充 :disabled 样式
- 验证：npm run build 通过、组件测试 11/11 通过（无回归）

## 2026-08-16（后台入口隐藏）

- 隐藏用户可见的后台入口：Header 的"管理后台"按钮仅对已登录管理员（isAdmin）显示，普通用户/游客不再看到任何后台入口
- 移除用户登录页 Login.vue 的"管理员入口"链接与样式，后台入口收敛为独立 URL（/admin/login 直达 + 管理员登录后 Header 按钮）
- 同步更新 Login.test.ts：新增"登录页不暴露管理后台入口"用例；验证 build 通过、测试 11/11 通过

## 2026-08-16（登录入口收敛）

- 收敛管理员入口：移除用户登录页 Login.vue 的"切换管理员登录"按钮与 adminLogin 分支，后台登录统一从独立入口 /admin/login 进入，前后台入口职责分离
- 用户登录页底部改为"管理员入口 →"跳转链接（分隔线 + 金色样式），引导清晰
- 同步更新 Login.test.ts：移除管理员切换用例，新增管理员入口跳转用例；验证 build 通过、测试 11/11 通过

## 2026-08-16（界面主题优化）

- 全站主题升级为酒店奢华风（深海蓝 × 香槟金）：新增 src/assets/theme.css 全局设计令牌（--hotel-navy/--hotel-gold 色板、金色/深蓝渐变、衬线标题字体、金色滚动条），main.ts 引入
- 批量替换 23 个组件/页面的紫色 SaaS 渐变主色（#667eea/#764ba2/#f093fb 等）为香槟金系，紫色残留清零
- 页面顶部 hero 大背景（About/Contact/News/RoomEnvironment/RoomBooking/DiningBooking）改为深海蓝渐变，保证白色标题可读性；登录/注册页背景统一深海蓝
- 后台管理侧栏为深蓝渐变 + 金色高亮激活项，按钮/卡片/图标统一金色渐变
- 验证：npm run build 通过、组件测试 11/11 通过（无回归）

## 2026-08-16（测试覆盖）

- 后端引入 JUnit 5 + Mockito 单元测试：新增 AuthServiceTest（8 用例：用户/管理员登录成功与失败、注册唯一性校验、注册入库）与 BookingServiceTest（13 用例：创建/更新预订 BigDecimal 总价精确计算、日期校验、状态流转、删除、仪表盘统计含 todayCount 与营收汇总、联表查询委托），`mvnw test` 21 例全部通过
- 前端集成 Vitest + @vue/test-utils + jsdom：新增 vitest.config.ts（jsdom 环境 + 显式导入模式），编写 Login 登录表单（空表单校验/用户登录/管理员登录）、Booking 预订表单（房型加载/选房提交参数）、DiningList 餐厅列表（名称渲染/选中取消）、Dashboard 统计卡片（统计渲染含 todayCount 与营收两位小数/失败兜底）共 11 例测试全部通过
- package.json 新增 test 脚本：`npm run test`（一键执行）、`npm run test:watch`（监听模式）
- 文档同步：dev-plan.md 测试覆盖三项全部勾选；README.md 补充前后端测试运行说明

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
