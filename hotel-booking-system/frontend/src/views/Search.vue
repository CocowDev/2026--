<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { roomTypeAPI, restaurantAPI } from '../api';
import { newsData } from '../data/news';
import type { RoomType, Restaurant, Dish } from '../types';
import { formatMoney } from '../utils/money';

const route = useRoute();
const router = useRouter();

const keyword = ref('');
const loading = ref(false);

// 数据源（前端本地加载后过滤——数据量小，无需后端搜索接口）
const roomTypes = ref<RoomType[]>([]);
const restaurants = ref<Restaurant[]>([]);
const dishes = ref<Dish[]>([]);

// 关键词命中判断（房型/餐厅/菜品：标题+描述；新闻：标题+摘要）
const matchText = (item: { title?: string; name?: string; description?: string; summary?: string }, q: string) => {
  const target = [item.title, item.name, item.description, item.summary].filter(Boolean).join(' ').toLowerCase();
  return target.includes(q.toLowerCase());
};

const matchedRooms = computed(() => keyword.value ? roomTypes.value.filter(r => matchText(r, keyword.value)) : []);
const matchedRestaurants = computed(() => keyword.value ? restaurants.value.filter(r => matchText(r, keyword.value)) : []);
const matchedDishes = computed(() => keyword.value ? dishes.value.filter(d => matchText(d, keyword.value)) : []);
const matchedNews = computed(() => keyword.value ? newsData.filter(n => matchText(n, keyword.value)) : []);

const totalCount = computed(() =>
  matchedRooms.value.length + matchedRestaurants.value.length + matchedDishes.value.length + matchedNews.value.length
);

// 跳转目标
const goRoom = () => router.push('/room-booking');
const goDining = () => router.push('/dining-booking');
const goNews = () => router.push('/news');

// 加载数据：房型、餐厅、各餐厅菜品
const loadData = async () => {
  loading.value = true;
  try {
    const [roomRes, restRes] = await Promise.all([
      roomTypeAPI.getAll(),
      restaurantAPI.getAll(),
    ]);
    roomTypes.value = roomRes.data.data || [];
    restaurants.value = restRes.data.data || [];
    // 拉取所有餐厅的菜品（6 个餐厅并发）
    const dishResults = await Promise.all(
      restaurants.value.map(r => restaurantAPI.getDishes(r.id).then(res => res.data.data || []).catch(() => []))
    );
    dishes.value = dishResults.flat();
  } catch {
    // 静默：接口异常时搜索页降级为仅新闻结果
  } finally {
    loading.value = false;
  }
};

// 从 URL 读取关键词（顶部搜索栏跳转 ?q=xxx）
const syncKeyword = () => {
  const q = typeof route.query.q === 'string' ? route.query.q.trim() : '';
  keyword.value = q;
};
watch(() => route.query.q, syncKeyword);
onMounted(async () => {
  syncKeyword();
  await loadData();
});

const submitSearch = () => {
  const q = keyword.value.trim();
  if (q) router.push({ path: '/search', query: { q } });
};
</script>

<template>
  <div class="search-page">
    <section class="search-hero">
      <div class="container">
        <span class="hero-badge">SEARCH · 站内搜索</span>
        <h1 class="hero-title">搜索房型、餐厅、菜品与资讯</h1>
        <form class="search-bar" @submit.prevent="submitSearch">
          <input
            v-model="keyword"
            type="search"
            class="search-input"
            placeholder="输入关键词，如：套房 / 牛排 / 咖啡 / SPA…"
          >
          <button type="submit" class="search-btn">搜索</button>
        </form>
        <p v-if="keyword" class="hero-result">
          共找到 <strong>{{ totalCount }}</strong> 条与「{{ keyword }}」相关的结果
        </p>
      </div>
    </section>

    <div class="container search-main">
      <div v-if="loading" class="state-tip">数据加载中...</div>

      <div v-else-if="totalCount === 0" class="state-tip empty-state">
        <p>未找到与「{{ keyword }}」相关的内容</p>
        <p class="state-sub">换个关键词试试，例如：套房、海景、牛排、咖啡</p>
      </div>

      <template v-else>
        <!-- 房型结果 -->
        <section v-if="matchedRooms.length" class="result-section">
          <div class="section-header">
            <h2>房型 <span class="count-badge">{{ matchedRooms.length }}</span></h2>
            <button class="more-link" @click="goRoom">查看全部房型 →</button>
          </div>
          <div class="result-grid">
            <div v-for="room in matchedRooms" :key="room.id" class="result-card" @click="goRoom">
              <img :src="room.imageUrl" :alt="room.title" class="card-img"
                   @error="($event.target as HTMLImageElement).src = '/images/room-single.jpg'">
              <div class="card-body">
                <h3>{{ room.title }}</h3>
                <p class="card-desc">{{ room.description }}</p>
                <p class="card-price">¥{{ formatMoney(room.price) }}<span>/晚</span></p>
              </div>
            </div>
          </div>
        </section>

        <!-- 餐厅结果 -->
        <section v-if="matchedRestaurants.length" class="result-section">
          <div class="section-header">
            <h2>餐厅 <span class="count-badge">{{ matchedRestaurants.length }}</span></h2>
            <button class="more-link" @click="goDining">查看全部餐厅 →</button>
          </div>
          <div class="result-grid">
            <div v-for="rest in matchedRestaurants" :key="rest.id" class="result-card" @click="goDining">
              <img :src="rest.imageUrl" :alt="rest.name" class="card-img"
                   @error="($event.target as HTMLImageElement).src = '/images/restaurant-luxury.jpg'">
              <div class="card-body">
                <h3>{{ rest.name }}</h3>
                <p class="card-desc">{{ rest.description }}</p>
              </div>
            </div>
          </div>
        </section>

        <!-- 菜品结果 -->
        <section v-if="matchedDishes.length" class="result-section">
          <div class="section-header">
            <h2>菜品 <span class="count-badge">{{ matchedDishes.length }}</span></h2>
            <button class="more-link" @click="goDining">去餐饮预订 →</button>
          </div>
          <div class="result-grid">
            <div v-for="dish in matchedDishes" :key="dish.id" class="result-card" @click="goDining">
              <img :src="dish.imageUrl" :alt="dish.name" class="card-img"
                   @error="($event.target as HTMLImageElement).src = '/images/dish-01.jpg'">
              <div class="card-body">
                <h3>{{ dish.name }}</h3>
                <p class="card-desc">{{ dish.description }}</p>
                <p class="card-price">¥{{ formatMoney(dish.price) }}</p>
              </div>
            </div>
          </div>
        </section>

        <!-- 新闻结果 -->
        <section v-if="matchedNews.length" class="result-section">
          <div class="section-header">
            <h2>新闻资讯 <span class="count-badge">{{ matchedNews.length }}</span></h2>
            <button class="more-link" @click="goNews">查看全部资讯 →</button>
          </div>
          <div class="news-list">
            <div v-for="news in matchedNews" :key="news.id" class="news-item" @click="goNews">
              <span class="news-tag">{{ news.categoryName }}</span>
              <div class="news-text">
                <h3>{{ news.title }}</h3>
                <p>{{ news.summary }}</p>
              </div>
              <span class="news-date">{{ news.date }}</span>
            </div>
          </div>
        </section>
      </template>
    </div>
  </div>
</template>

<style scoped>
.search-page {
  min-height: calc(100vh - 180px);
  background: linear-gradient(180deg, #faf7f1 0%, #f3ecdd 100%);
  padding-bottom: 70px;
}

.search-hero {
  position: relative;
  padding: 56px 0 64px;
  overflow: hidden;
  background:
    radial-gradient(1000px 420px at 85% -20%, rgba(201, 169, 106, 0.22), transparent 60%),
    linear-gradient(135deg, #0e1c2e 0%, #1c3350 60%, #3a5370 100%);
  color: #fff;
  text-align: center;
}

.hero-badge {
  display: inline-block;
  padding: 5px 14px;
  margin-bottom: 12px;
  font-size: 12px;
  letter-spacing: 2px;
  color: #e6cf9a;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(230, 207, 154, 0.4);
  border-radius: 999px;
}

.hero-title {
  margin: 0 0 24px;
  font-size: 30px;
}

.search-bar {
  display: flex;
  gap: 10px;
  max-width: 560px;
  margin: 0 auto;
}

.search-input {
  flex: 1;
  padding: 13px 18px;
  font-size: 15px;
  border: none;
  border-radius: 999px;
  outline: none;
  font-family: inherit;
}

.search-btn {
  padding: 0 28px;
  border: none;
  border-radius: 999px;
  background: linear-gradient(135deg, #b89450, #e6cf9a);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: filter 0.25s, transform 0.25s;
}

.search-btn:hover {
  filter: brightness(1.08);
  transform: translateY(-1px);
}

.hero-result {
  margin: 18px 0 0;
  font-size: 14px;
  opacity: 0.9;
}

.hero-result strong {
  color: #e6cf9a;
  font-size: 16px;
}

.search-main {
  margin-top: -26px;
  position: relative;
  z-index: 2;
}

.state-tip {
  padding: 70px 0;
  text-align: center;
  color: #8a7f6a;
  font-size: 16px;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 14px 40px -18px rgba(16, 35, 59, 0.25);
}

.state-sub {
  margin-top: 8px;
  font-size: 13px;
  color: #b0a68f;
}

.result-section {
  margin-bottom: 26px;
  background: #fff;
  border-radius: 18px;
  padding: 22px 24px;
  box-shadow: 0 14px 40px -18px rgba(16, 35, 59, 0.2);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.section-header h2 {
  margin: 0;
  font-size: 19px;
  color: #10233b;
}

.count-badge {
  display: inline-block;
  min-width: 22px;
  padding: 1px 7px;
  margin-left: 6px;
  border-radius: 999px;
  background: #f5ead0;
  color: #8a6d2f;
  font-size: 13px;
  text-align: center;
}

.more-link {
  background: none;
  border: none;
  color: #b89450;
  font-size: 13px;
  cursor: pointer;
}

.more-link:hover {
  text-decoration: underline;
}

.result-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 16px;
}

.result-card {
  border-radius: 14px;
  overflow: hidden;
  border: 1px solid #f0eadc;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  background: #fff;
}

.result-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px -12px rgba(16, 35, 59, 0.3);
}

.card-img {
  width: 100%;
  height: 130px;
  object-fit: cover;
  display: block;
}

.card-body {
  padding: 12px 14px 14px;
}

.card-body h3 {
  margin: 0 0 6px;
  font-size: 15px;
  color: #2c313a;
}

.card-desc {
  margin: 0 0 8px;
  font-size: 12.5px;
  color: #8a7f6a;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-price {
  margin: 0;
  font-size: 16px;
  font-weight: 700;
  color: #b89450;
}

.card-price span {
  font-size: 12px;
  font-weight: 400;
  color: #b0a68f;
}

.news-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.news-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 13px 16px;
  border-radius: 12px;
  background: #faf9f6;
  border: 1px solid #f0eadc;
  cursor: pointer;
  transition: border-color 0.25s, background 0.25s;
}

.news-item:hover {
  border-color: #e3d5b0;
  background: #fdf9f0;
}

.news-tag {
  flex-shrink: 0;
  padding: 3px 10px;
  border-radius: 999px;
  background: #eef3fa;
  color: #3a6ea5;
  font-size: 12px;
}

.news-text {
  flex: 1;
  min-width: 0;
}

.news-text h3 {
  margin: 0 0 3px;
  font-size: 14.5px;
  color: #2c313a;
}

.news-text p {
  margin: 0;
  font-size: 12.5px;
  color: #8a7f6a;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.news-date {
  flex-shrink: 0;
  font-size: 12px;
  color: #b0a68f;
}
</style>
