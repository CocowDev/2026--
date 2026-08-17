/**
 * 新闻数据（共享模块）
 * 后端暂无新闻接口，前端内置示例数据；
 * 供 News.vue 新闻页与 Search.vue 聚合搜索页共用
 */
export interface NewsItem {
  id: number;
  category: string;
  categoryName: string;
  title: string;
  summary: string;
  imageUrl: string;
  date: string;
  views: number;
  content: string;
}

export const newsData: NewsItem[] = [
  {
    id: 1,
    category: 'hotel',
    categoryName: '酒店动态',
    title: '星级酒店全新升级，奢华体验再升级',
    summary: '本酒店完成全面翻新，新增智能客房与全景套房，为您带来前所未有的入住体验。',
    imageUrl: 'https://picsum.photos/seed/news1/600/400',
    date: '2026-07-28',
    views: 1280,
    content: '<p>历经半年的精心改造，本酒店以全新面貌迎接八方来客。本次升级新增 30 间智能客房，配备语音控制系统、智能灯光与全景落地窗。</p><h2>全新设施</h2><p>顶楼海景套房可 270 度饱览城市天际线，并引入米其林星级主厨团队。</p>',
  },
  {
    id: 2,
    category: 'event',
    categoryName: '活动资讯',
    title: '盛夏狂欢季：住店即享SPA礼遇',
    summary: '即日起至八月底，预订豪华房型及以上，即可获赠双人 SPA 套餐一份。',
    imageUrl: 'https://picsum.photos/seed/news2/600/400',
    date: '2026-07-25',
    views: 856,
    content: '<p>盛夏狂欢季活动火热进行中！即日起至 8 月 31 日，预订豪华间及以上房型，即可获赠价值 599 元的双人 SPA 套餐。</p><p>活动名额有限，先到先得。</p>',
  },
  {
    id: 3,
    category: 'travel',
    categoryName: '旅游攻略',
    title: '周边三日游精选路线推荐',
    summary: '精选酒店周边热门景点，为您规划完美的三日深度游行程。',
    imageUrl: 'https://picsum.photos/seed/news3/600/400',
    date: '2026-07-20',
    views: 1523,
    content: '<p>酒店地处城市核心商圈，交通便捷。我们为您精心规划了三日深度游路线。</p><h2>Day 1</h2><p>海滨公园 — 海洋馆 — 海鲜大排档。</p><h2>Day 2</h2><p>古城老街 — 博物馆 — 特色茶馆。</p>',
  },
  {
    id: 4,
    category: 'food',
    categoryName: '美食推荐',
    title: '主厨私房菜：本季限定菜单发布',
    summary: '米其林主厨倾力打造夏季限定菜单，时令食材与创新烹饪的完美碰撞。',
    imageUrl: 'https://picsum.photos/seed/news4/600/400',
    date: '2026-07-18',
    views: 942,
    content: '<p>本季限定菜单由米其林星级主厨团队倾力打造，选用当季新鲜食材，融合中西烹饪技法。</p><p>每道菜品都经过精心设计，限时供应至 9 月底。</p>',
  },
  {
    id: 5,
    category: 'hotel',
    categoryName: '酒店动态',
    title: '亲子主题房温情上线',
    summary: '全新推出四大主题亲子房，让全家出行更添温馨与趣味。',
    imageUrl: 'https://picsum.photos/seed/news5/600/400',
    date: '2026-07-15',
    views: 678,
    content: '<p>为满足家庭客群需求，酒店全新推出海洋、森林、太空、童话四大主题亲子房。</p><p>每间客房均配备儿童专属洗漱用品与趣味玩具。</p>',
  },
  {
    id: 6,
    category: 'event',
    categoryName: '活动资讯',
    title: '中秋赏月晚宴预订开启',
    summary: '中秋佳节，邀您共赴顶楼露天花园，赏月品茗，共度团圆时光。',
    imageUrl: 'https://picsum.photos/seed/news6/600/400',
    date: '2026-07-10',
    views: 1102,
    content: '<p>中秋佳节将至，酒店顶楼露天花园将举办赏月晚宴。</p><p>提供精选月饼礼盒与传统茶饮，更有古筝现场演奏。</p><p>席位有限，敬请提前预订。</p>',
  },
  {
    id: 7,
    category: 'facility',
    categoryName: '酒店设施',
    title: '全新恒温泳池正式开放',
    summary: '25 米标准恒温泳池正式开放，全年 26℃ 恒温，配备专业救生员与按摩水疗区。',
    imageUrl: 'https://picsum.photos/seed/news7/600/400',
    date: '2026-08-05',
    views: 866,
    content: '<p>酒店全新恒温泳池于本月正式对外开放，泳池长 25 米、宽 10 米，全年保持 26℃ 恒温。</p><h2>配套设施</h2><p>泳池区配备干湿分离更衣室、按摩水疗区与休闲躺椅，并提供免费浴巾与饮用水。</p><h2>开放时间</h2><p>每日 06:00 - 22:00，住店客人免费使用，健身卡会员可携一位亲友入场。</p>',
  },
  {
    id: 8,
    category: 'facility',
    categoryName: '酒店设施',
    title: '智能健身房全面升级，器械焕新',
    summary: '引入 20 台全新进口健身器械，增设私教课程与体测服务，满足不同运动需求。',
    imageUrl: 'https://picsum.photos/seed/news8/600/400',
    date: '2026-07-30',
    views: 645,
    content: '<p>酒店健身房完成全面升级，引进知名品牌 20 台有氧与力量器械，并划分功能训练区、瑜伽室与拉伸区。</p><h2>特色服务</h2><p>提供一对一私教课程、身体成分检测与定制训练计划，住店客人可免费体验一次体测。</p><p>健身房开放时间：每日 06:00 - 23:00。</p>',
  },
  {
    id: 9,
    category: 'service',
    categoryName: '服务指南',
    title: '机场接送服务全新上线',
    summary: '新增机场/高铁站专车接送服务，7 座商务车，提供免费等候与行李搬运。',
    imageUrl: 'https://picsum.photos/seed/news9/600/400',
    date: '2026-07-22',
    views: 534,
    content: '<p>为提升出行便利，酒店正式推出机场与高铁站专车接送服务。</p><h2>服务说明</h2><p>车型为 7 座商务车，提供免费 30 分钟等候与行李搬运服务；司机均为持证专业驾驶员。</p><h2>预约方式</h2><p>入住前 24 小时致电前台或通过在线预订系统预约，费用按里程计费，住店客人享 8 折优惠。</p>',
  },
  {
    id: 10,
    category: 'service',
    categoryName: '服务指南',
    title: '会议与宴会场地全新开放预订',
    summary: '可容纳 300 人的多功能宴会厅投入使用，配备高清 LED 屏与专业音响设备。',
    imageUrl: 'https://picsum.photos/seed/news10/600/400',
    date: '2026-07-16',
    views: 789,
    content: '<p>酒店全新多功能宴会厅正式投入使用，可容纳 300 人，适用于婚宴、年会、发布会等各类活动。</p><h2>硬件设施</h2><p>配备 8 米宽高清 LED 显示屏、专业舞台灯光与音响系统，提供全程宴会策划服务。</p><h2>预订咨询</h2><p>拨打 400-888-8889 或前往前台与宴会部沟通档期，提前 30 天预订可享场地费 9 折。</p>',
  },
  {
    id: 11,
    category: 'travel',
    categoryName: '旅游攻略',
    title: '亲子度假攻略：带娃出行全指南',
    summary: '从儿童餐、婴儿床到亲子活动，一站式盘点酒店为亲子家庭准备的贴心服务。',
    imageUrl: 'https://picsum.photos/seed/news11/600/400',
    date: '2026-07-08',
    views: 1520,
    content: '<p>带娃出行总担心照顾不周？这份亲子攻略帮您从容安排旅程。</p><h2>入住准备</h2><p>预订时备注儿童年龄，酒店可免费提供婴儿床、儿童洗漱用品与防撞角；亲子主题房配备儿童帐篷与玩具。</p><h2>用餐建议</h2><p>自助餐厅设有儿童餐区，中餐厅可提前预订少油少盐的儿童套餐。</p><h2>活动推荐</h2><p>周末酒店亲子课堂开设烘焙、绘画与游泳体验课，可向前台预约参加。</p>',
  },
  {
    id: 12,
    category: 'food',
    categoryName: '美食推荐',
    title: '本地特色早茶地图：唤醒清晨的美味',
    summary: '精选酒店周边五家地道早茶店，从虾饺到肠粉，为您奉上清晨的味蕾之旅。',
    imageUrl: 'https://picsum.photos/seed/news12/600/400',
    date: '2026-07-02',
    views: 980,
    content: '<p>清晨的烟火气，藏在街巷的早茶铺里。以下五家本地老字号，步行均可到达。</p><h2>推荐清单</h2><p>「老字号茶楼」虾饺皇皮薄馅足；「巷口肠粉王」米浆现磨，滑嫩弹牙；「岭南点心坊」凤爪入味软糯；「晨光烧卖铺」烧卖鲜香多汁；「榕树下粥铺」皮蛋瘦肉粥绵密顺滑。</p><p>酒店前台可为您打印周边美食地图，并提供叫车服务。</p>',
  },
];
