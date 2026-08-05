<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface NewsItem {
  id: number
  category: string
  categoryName: string
  title: string
  summary: string
  imageUrl: string
  date: string
  views: number
  content: string
}

// 静态新闻数据（后端暂无新闻接口，前端内置示例数据）
const newsData: NewsItem[] = [
  {
    id: 1,
    category: 'hotel',
    categoryName: '酒店动态',
    title: '星级酒店全新升级，奢华体验再升级',
    summary: '本酒店完成全面翻新，新增智能客房与全景套房，为您带来前所未有的入住体验。',
    imageUrl: 'https://picsum.photos/seed/news1/600/400',
    date: '2026-07-28',
    views: 1280,
    content: '<p>历经半年的精心改造，本酒店以全新面貌迎接八方来客。本次升级新增 30 间智能客房，配备语音控制系统、智能灯光与全景落地窗。</p><h2>全新设施</h2><p>顶楼海景套房可 270 度饱览城市天际线，并引入米其林星级主厨团队。</p>'
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
    content: '<p>盛夏狂欢季活动火热进行中！即日起至 8 月 31 日，预订豪华间及以上房型，即可获赠价值 599 元的双人 SPA 套餐。</p><p>活动名额有限，先到先得。</p>'
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
    content: '<p>酒店地处城市核心商圈，交通便捷。我们为您精心规划了三日深度游路线。</p><h2>Day 1</h2><p>海滨公园 — 海洋馆 — 海鲜大排档。</p><h2>Day 2</h2><p>古城老街 — 博物馆 — 特色茶馆。</p>'
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
    content: '<p>本季限定菜单由米其林星级主厨团队倾力打造，选用当季新鲜食材，融合中西烹饪技法。</p><p>每道菜品都经过精心设计，限时供应至 9 月底。</p>'
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
    content: '<p>为满足家庭客群需求，酒店全新推出海洋、森林、太空、童话四大主题亲子房。</p><p>每间客房均配备儿童专属洗漱用品与趣味玩具。</p>'
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
    content: '<p>中秋佳节将至，酒店顶楼露天花园将举办赏月晚宴。</p><p>提供精选月饼礼盒与传统茶饮，更有古筝现场演奏。</p><p>席位有限，敬请提前预订。</p>'
  }
]

const newsList = ref<NewsItem[]>([])
const selectedNews = ref<NewsItem | null>(null)
const searchKeyword = ref('')
const activeCategory = ref('all')

const categories = [
  { id: 'all', name: '全部' },
  { id: 'hotel', name: '酒店动态' },
  { id: 'event', name: '活动资讯' },
  { id: 'travel', name: '旅游攻略' },
  { id: 'food', name: '美食推荐' }
]

const filteredNews = ref<NewsItem[]>([])

const filterNews = () => {
  filteredNews.value = newsList.value.filter(news => {
    const matchesCategory = activeCategory.value === 'all' || news.category === activeCategory.value
    const matchesKeyword = !searchKeyword.value || news.title.includes(searchKeyword.value) || news.summary.includes(searchKeyword.value)
    return matchesCategory && matchesKeyword
  })
}

const openNewsDetail = (news: NewsItem) => {
  selectedNews.value = news
}

const closeNewsDetail = () => {
  selectedNews.value = null
}

// 图片加载失败时回退到占位图
const handleImgError = (e: Event) => {
  const target = e.target as HTMLImageElement
  target.src = 'https://picsum.photos/seed/news-fallback/400/300'
}

const handleDetailImgError = (e: Event) => {
  const target = e.target as HTMLImageElement
  target.src = 'https://picsum.photos/seed/news-detail/1200/600'
}

onMounted(() => {
  newsList.value = newsData
  filterNews()
})
</script>

<template>
  <div class="news-page">
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content container">
        <span class="hero-badge">
          <span class="badge-dot"></span>
          NEWS CENTER · 新闻中心
        </span>
        <h1 class="hero-title">
          最新<span class="title-gradient">酒店资讯</span>
        </h1>
        <p class="hero-subtitle">
          关注我们的最新动态，获取酒店优惠活动和精彩资讯
        </p>
      </div>
    </section>

    <section class="news-section">
      <div class="container">
        <div class="filter-bar">
          <div class="category-tabs">
            <button
              v-for="category in categories"
              :key="category.id"
              class="category-tab"
              :class="{ 'is-active': activeCategory === category.id }"
              @click="activeCategory = category.id; filterNews()"
            >
              {{ category.name }}
            </button>
          </div>
          <div class="search-box">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="11" cy="11" r="8"></circle>
              <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
            <input
              type="text"
              v-model="searchKeyword"
              placeholder="搜索新闻..."
              @input="filterNews()"
            >
          </div>
        </div>

        <div class="news-grid">
          <div
            v-for="news in filteredNews"
            :key="news.id"
            class="news-card"
            @click="openNewsDetail(news)"
          >
            <div class="news-image">
              <img :src="news.imageUrl" :alt="news.title" @error="handleImgError">
              <span class="news-category">{{ news.categoryName }}</span>
            </div>
            <div class="news-content">
              <h3 class="news-title">{{ news.title }}</h3>
              <p class="news-summary">{{ news.summary }}</p>
              <div class="news-meta">
                <span class="news-date">{{ news.date }}</span>
                <span class="news-views">{{ news.views }} 阅读</span>
              </div>
            </div>
          </div>
        </div>

        <div v-if="filteredNews.length === 0" class="empty-state">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <polyline points="12 6 12 12 16 14"></polyline>
          </svg>
          <p>暂无相关新闻</p>
        </div>
      </div>
    </section>

    <transition name="fade">
      <div v-if="selectedNews" class="news-detail-overlay" @click.self="closeNewsDetail">
        <div class="news-detail-modal">
          <button class="close-btn" @click="closeNewsDetail">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <line x1="18" y1="6" x2="6" y2="18"></line>
              <line x1="6" y1="6" x2="18" y2="18"></line>
            </svg>
          </button>
          <div class="detail-image">
            <img :src="selectedNews.imageUrl" :alt="selectedNews.title" @error="handleDetailImgError">
          </div>
          <div class="detail-content">
            <span class="detail-category">{{ selectedNews.categoryName }}</span>
            <h1 class="detail-title">{{ selectedNews.title }}</h1>
            <div class="detail-meta">
              <span class="detail-date">{{ selectedNews.date }}</span>
              <span class="detail-views">{{ selectedNews.views }} 阅读</span>
            </div>
            <div class="detail-body" v-html="selectedNews.content"></div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>
.news-page {
  min-height: 100vh;
  background: #fafbfc;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC',
    'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  overflow-x: hidden;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.hero-section {
  position: relative;
  padding: 120px 24px 140px;
  overflow: hidden;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 30%, rgba(255, 255, 255, 0.35) 0%, transparent 45%),
    radial-gradient(circle at 80% 70%, rgba(236, 72, 153, 0.3) 0%, transparent 45%);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
  color: #fff;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 18px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  margin-bottom: 24px;
}

.badge-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ffd700;
  box-shadow: 0 0 12px rgba(255, 215, 0, 0.8);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.3); opacity: 0.7; }
}

.hero-title {
  font-size: 56px;
  font-weight: 800;
  line-height: 1.15;
  margin: 0 0 20px;
  letter-spacing: -1px;
  color: #fff;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.title-gradient {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 50%, #fff5b3 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.hero-subtitle {
  font-size: 18px;
  line-height: 1.7;
  margin: 0;
  color: rgba(255, 255, 255, 0.92);
  max-width: 640px;
  margin-left: auto;
  margin-right: auto;
}

.news-section {
  padding: 80px 0 120px;
  margin-top: -60px;
  position: relative;
  z-index: 3;
}

.filter-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  flex-wrap: wrap;
  gap: 16px;
}

.category-tabs {
  display: flex;
  gap: 8px;
}

.category-tab {
  padding: 10px 20px;
  border: none;
  background: #fff;
  border-radius: 999px;
  font-size: 14px;
  font-weight: 500;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.category-tab:hover {
  color: #8b5cf6;
  box-shadow: 0 4px 16px rgba(139, 92, 246, 0.15);
}

.category-tab.is-active {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  box-shadow: 0 4px 20px rgba(139, 92, 246, 0.4);
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  border: 1.5px solid #e5e7eb;
  transition: border-color 0.3s ease;
}

.search-box:focus-within {
  border-color: #8b5cf6;
}

.search-box svg {
  width: 18px;
  height: 18px;
  color: #9ca3af;
}

.search-box input {
  border: none;
  outline: none;
  font-size: 14px;
  color: #374151;
  background: transparent;
  width: 200px;
}

.search-box input::placeholder {
  color: #9ca3af;
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.news-card {
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
}

.news-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 24px 50px rgba(0, 0, 0, 0.15);
}

.news-image {
  position: relative;
  aspect-ratio: 4 / 3;
  overflow: hidden;
}

.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.news-card:hover .news-image img {
  transform: scale(1.1);
}

.news-category {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 12px;
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.9), rgba(240, 147, 251, 0.9));
  color: #fff;
  font-size: 12px;
  font-weight: 600;
  border-radius: 999px;
}

.news-content {
  padding: 20px;
}

.news-title {
  font-size: 18px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 10px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-summary {
  font-size: 14px;
  color: #6b7280;
  line-height: 1.6;
  margin: 0 0 14px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.news-date,
.news-views {
  font-size: 13px;
  color: #9ca3af;
}

.empty-state {
  text-align: center;
  padding: 60px;
  color: #9ca3af;
}

.empty-state svg {
  width: 64px;
  height: 64px;
  margin-bottom: 16px;
}

.news-detail-overlay {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.92);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  z-index: 1000;
  overflow-y: auto;
}

.news-detail-modal {
  max-width: 800px;
  width: 100%;
  background: #fff;
  border-radius: 24px;
  overflow: hidden;
  position: relative;
}

.close-btn {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.5);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  z-index: 10;
  transition: background 0.25s ease, transform 0.25s ease;
}

.close-btn:hover {
  background: rgba(0, 0, 0, 0.7);
  transform: rotate(90deg);
}

.close-btn svg {
  width: 20px;
  height: 20px;
}

.detail-image {
  aspect-ratio: 16 / 7;
  overflow: hidden;
}

.detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-content {
  padding: 40px;
}

.detail-category {
  display: inline-block;
  padding: 6px 14px;
  background: rgba(139, 92, 246, 0.1);
  color: #8b5cf6;
  font-size: 12px;
  font-weight: 600;
  border-radius: 999px;
  margin-bottom: 16px;
}

.detail-title {
  font-size: 32px;
  font-weight: 800;
  color: #1f2937;
  margin: 0 0 16px;
  line-height: 1.2;
}

.detail-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 32px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f3f4f6;
}

.detail-date,
.detail-views {
  font-size: 14px;
  color: #6b7280;
}

.detail-body {
  font-size: 16px;
  line-height: 1.8;
  color: #374151;
}

.detail-body h2 {
  font-size: 22px;
  font-weight: 700;
  color: #1f2937;
  margin: 24px 0 12px;
}

.detail-body p {
  margin: 0 0 16px;
}

.detail-body img {
  max-width: 100%;
  border-radius: 12px;
  margin: 16px 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 968px) {
  .news-grid {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 100px 20px 120px;
  }

  .hero-title {
    font-size: 38px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .category-tabs {
    flex-wrap: wrap;
    justify-content: center;
  }

  .search-box {
    width: 100%;
  }

  .search-box input {
    flex: 1;
  }

  .news-grid {
    grid-template-columns: 1fr;
  }

  .news-detail-overlay {
    padding: 20px;
  }

  .detail-content {
    padding: 24px;
  }

  .detail-title {
    font-size: 24px;
  }
}
</style>