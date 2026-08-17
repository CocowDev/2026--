<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { roomTypeAPI, restaurantAPI } from '../api'
import Carousel from '../components/Carousel.vue'
import ServiceList from '../components/ServiceList.vue'
import { Bed, UtensilsCrossed, Star, ArrowRight, Wifi, Car, Coffee } from 'lucide-vue-next'

const router = useRouter()
const roomTypes = ref<any[]>([])
const restaurants = ref<any[]>([])

const services = ref([
  {
    id: 1,
    name: '免费WiFi',
    description: '全酒店覆盖高速无线网络',
    price: 0,
    icon: '📶'
  },
  {
    id: 2,
    name: '停车场',
    description: '24小时全天候免费停车',
    price: 0,
    icon: '🚗'
  },
  {
    id: 3,
    name: '早餐服务',
    description: '丰富自助早餐，唤醒美好一天',
    price: 68,
    icon: '🍳'
  },
  {
    id: 4,
    name: 'SPA按摩',
    description: '专业SPA护理，放松身心',
    price: 298,
    icon: '💆'
  },
  {
    id: 5,
    name: '健身中心',
    description: '现代化健身器材，随时挥洒汗水',
    price: 58,
    icon: '🏋️'
  },
  {
    id: 6,
    name: '商务中心',
    description: '会议室及商务设施齐全',
    price: 200,
    icon: '💼'
  }
])

const facilities = [
  { icon: Bed, title: '舒适客房', desc: '宽敞明亮的客房，配备高品质床品' },
  { icon: UtensilsCrossed, title: '美食餐厅', desc: '多种风味餐厅，满足您的味蕾' },
  { icon: Star, title: '星级服务', desc: '专业贴心的服务团队' },
  { icon: Wifi, title: '高速WiFi', desc: '全覆盖高速无线网络' },
  { icon: Car, title: '停车场', desc: '24小时免费停车服务' },
  { icon: Coffee, title: '大堂吧', desc: '优雅休闲空间，品味时光' }
]

onMounted(async () => {
  const [roomsRes, restaurantsRes] = await Promise.all([
    roomTypeAPI.getAll(),
    restaurantAPI.getAll(),
  ])
  roomTypes.value = roomsRes.data.data
  restaurants.value = restaurantsRes.data.data
})

const handleBookService = (_service: any) => {
  // 跳转预订页并自动切换到"服务预订"分类，不再跳转联系我们
  router.push('/room-booking?tab=services')
}
</script>

<template>
  <div class="home">
    <section class="hero">
      <div class="container hero-container">
        <Carousel />
      </div>
    </section>

    <section class="features">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">酒店设施</h2>
          <p class="section-subtitle">全方位满足您的入住需求</p>
        </div>
        <div class="features-grid">
          <div
            v-for="(facility, index) in facilities"
            :key="index"
            class="feature-card"
          >
            <div class="feature-icon">
              <component :is="facility.icon" />
            </div>
            <h3>{{ facility.title }}</h3>
            <p>{{ facility.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <section class="services">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">酒店服务</h2>
          <p class="section-subtitle">尊享贴心服务，体验品质生活</p>
        </div>
        <ServiceList :services="services" @book="handleBookService" />
      </div>
    </section>

    <section class="rooms">
      <div class="container">
        <div class="section-header">
          <div>
            <h2 class="section-title">房型展示</h2>
            <p class="section-subtitle">精选房型，舒适入住</p>
          </div>
          <button class="btn btn-secondary" @click="router.push('/room-booking')">
            查看全部
            <ArrowRight class="btn-icon" />
          </button>
        </div>
        <div class="rooms-grid">
          <div
            v-for="room in roomTypes.slice(0, 4)"
            :key="room.id"
            class="room-card"
            @click="router.push('/room-booking')"
          >
            <div class="room-media">
              <img :src="room.imageUrl" :alt="room.title" />
              <div class="room-price-tag">
                <span class="price-symbol">¥</span>
                <span class="price-value">{{ room.price }}</span>
                <span class="price-unit">/晚</span>
              </div>
            </div>
            <div class="room-info">
              <h3>{{ room.title }}</h3>
              <p>{{ room.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="restaurants">
      <div class="container">
        <div class="section-header">
          <div>
            <h2 class="section-title">特色餐厅</h2>
            <p class="section-subtitle">品味美食，享受生活</p>
          </div>
          <button class="btn btn-secondary" @click="router.push('/dining-booking')">
            预订餐位
            <ArrowRight class="btn-icon" />
          </button>
        </div>
        <div class="restaurants-grid">
          <div
            v-for="restaurant in restaurants.slice(0, 4)"
            :key="restaurant.id"
            class="restaurant-card"
            @click="router.push('/dining-booking')"
          >
            <div class="restaurant-media">
              <img :src="restaurant.imageUrl" :alt="restaurant.name" />
              <div class="restaurant-overlay"></div>
            </div>
            <div class="restaurant-info">
              <h3>{{ restaurant.name }}</h3>
              <p>{{ restaurant.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="cta">
      <div class="container">
        <div class="cta-content">
          <h2>开始您的旅程</h2>
          <p>立即预订，享受专属优惠</p>
          <button class="btn btn-primary" @click="router.push('/room-booking')">
            在线预订
            <ArrowRight class="btn-icon" />
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.hero {
  padding: 40px 0;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
}

.hero-container {
  padding: 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
}

.section-title {
  font-size: 32px;
  font-weight: 800;
  color: #0f172a;
  margin: 0;
  letter-spacing: -0.5px;
}

.section-subtitle {
  font-size: 15px;
  color: #64748b;
  margin: 8px 0 0;
}

.features {
  padding: 80px 0;
  background: #ffffff;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 20px;
}

.feature-card {
  text-align: center;
  padding: 28px 20px;
  background: linear-gradient(180deg, #fafbff 0%, #f1f5f9 100%);
  border-radius: 16px;
  transition: transform 0.4s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.4s cubic-bezier(0.22, 1, 0.36, 1);
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px -12px rgba(99, 102, 241, 0.2);
}

.feature-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #c9a96a 0%, #d4b06e 100%);
  border-radius: 14px;
  color: #fff;
  font-size: 24px;
  box-shadow: 0 8px 20px -6px rgba(184, 148, 80, 0.5);
}

.feature-card h3 {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 700;
  color: #0f172a;
}

.feature-card p {
  margin: 0;
  font-size: 13px;
  line-height: 1.5;
  color: #64748b;
}

.services {
  padding: 80px 0;
  background: linear-gradient(180deg, #f1f5f9 0%, #fafbff 100%);
}

.rooms {
  padding: 80px 0;
  background: #ffffff;
}

.rooms-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 24px;
}

.room-card {
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.4s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.4s cubic-bezier(0.22, 1, 0.36, 1);
  box-shadow: 0 4px 16px -4px rgba(15, 23, 42, 0.1);
}

.room-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px -12px rgba(99, 102, 241, 0.25);
}

.room-media {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.room-media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.22, 1, 0.36, 1);
}

.room-card:hover .room-media img {
  transform: scale(1.08);
}

.room-price-tag {
  position: absolute;
  top: 12px;
  right: 12px;
  display: inline-flex;
  align-items: baseline;
  gap: 2px;
  padding: 6px 12px;
  color: #fff;
  font-weight: 700;
  font-size: 14px;
  background: linear-gradient(135deg, #c9a96a 0%, #d4b06e 100%);
  border-radius: 999px;
  box-shadow: 0 6px 16px -4px rgba(184, 148, 80, 0.5);
}

.room-price-tag .price-symbol {
  font-size: 12px;
}

.room-price-tag .price-value {
  font-size: 18px;
}

.room-price-tag .price-unit {
  font-size: 11px;
  opacity: 0.9;
}

.room-info {
  padding: 18px;
}

.room-info h3 {
  margin: 0 0 8px;
  font-size: 17px;
  font-weight: 700;
  color: #0f172a;
}

.room-info p {
  margin: 0;
  font-size: 13px;
  line-height: 1.5;
  color: #64748b;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.restaurants {
  padding: 80px 0;
  background: linear-gradient(180deg, #fafbff 0%, #f8fafc 100%);
}

.restaurants-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 24px;
}

.restaurant-card {
  position: relative;
  background: #ffffff;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.4s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.4s cubic-bezier(0.22, 1, 0.36, 1);
  box-shadow: 0 4px 16px -4px rgba(15, 23, 42, 0.1);
}

.restaurant-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px -12px rgba(212, 176, 110, 0.2);
}

.restaurant-media {
  position: relative;
  width: 100%;
  height: 160px;
  overflow: hidden;
}

.restaurant-media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.22, 1, 0.36, 1);
}

.restaurant-card:hover .restaurant-media img {
  transform: scale(1.08);
}

.restaurant-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0) 50%,
    rgba(0, 0, 0, 0.3) 100%
  );
}

.restaurant-info {
  padding: 18px;
}

.restaurant-info h3 {
  margin: 0 0 8px;
  font-size: 17px;
  font-weight: 700;
  color: #0f172a;
}

.restaurant-info p {
  margin: 0;
  font-size: 13px;
  line-height: 1.5;
  color: #64748b;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.cta {
  padding: 80px 0;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 50%, #d4b06e 100%);
  text-align: center;
  color: #fff;
}

.cta-content h2 {
  font-size: 40px;
  font-weight: 800;
  margin: 0 0 12px;
  letter-spacing: -0.5px;
}

.cta-content p {
  font-size: 18px;
  margin: 0 0 32px;
  opacity: 0.9;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  border: none;
  border-radius: 999px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.22, 1, 0.36, 1);
}

.btn-icon {
  width: 18px;
  height: 18px;
  transition: transform 0.3s cubic-bezier(0.22, 1, 0.36, 1);
}

.btn-primary {
  background: #fff;
  color: #c9a96a;
  box-shadow: 0 10px 30px -10px rgba(255, 255, 255, 0.4);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 40px -10px rgba(255, 255, 255, 0.5);
}

.btn-primary:hover .btn-icon {
  transform: translateX(3px);
}

.btn-secondary {
  background: rgba(255, 255, 255, 0.12);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(8px);
}

.btn-secondary:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.btn-secondary:hover .btn-icon {
  transform: translateX(3px);
}

@media (max-width: 992px) {
  .features-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .rooms-grid,
  .restaurants-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .cta-content h2 {
    font-size: 28px;
  }
}

@media (max-width: 640px) {
  .features-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 16px;
  }

  .rooms-grid,
  .restaurants-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .section-title {
    font-size: 24px;
  }

  .cta-content h2 {
    font-size: 24px;
  }

  .room-media,
  .restaurant-media {
    height: 160px;
  }
}
</style>