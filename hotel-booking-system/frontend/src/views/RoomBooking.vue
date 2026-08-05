<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { bookingAPI, roomTypeAPI, restaurantAPI } from '../api';
import { getToday, getTomorrow } from '../utils/date';
import { validateDateRange } from '../utils/validation';
import RoomList from '../components/RoomList.vue';
import DiningList from '../components/DiningList.vue';
import ServiceList from '../components/ServiceList.vue';
import type { RoomType, Restaurant } from '../types';

const router = useRouter();
const currentCategory = ref('rooms');
const selectedRoom = ref<RoomType | null>(null);
const selectedRestaurant = ref<Restaurant | null>(null);
const checkInDate = ref(getToday());
const checkOutDate = ref(getTomorrow());
const numberOfGuests = ref('2');
const specialRequests = ref('');
const diningDate = ref(getToday());
const diningTime = ref('');
const diningGuests = ref('');
const diningRequests = ref('');
const errors = ref<Record<string, string>>({});
const loading = ref(false);
const message = ref('');
const roomTypes = ref<RoomType[]>([]);
const restaurants = ref<Restaurant[]>([]);

const categories = [
  { id: 'rooms', name: '客房预订', icon: '🏨' },
  { id: 'dining', name: '餐饮预订', icon: '🍽️' },
  { id: 'services', name: '服务预订', icon: '💆' }
];

const servicesData = [
  { id: 1, name: 'SPA按摩', icon: '💆', description: '60分钟全身按摩', price: 298 },
  { id: 2, name: '健身课程', icon: '🏋️', description: '一对一私人教练', price: 198 },
  { id: 3, name: '游泳池', icon: '🏊', description: '全天不限时游泳', price: 88 },
  { id: 4, name: '美容护理', icon: '💅', description: '面部护理套餐', price: 398 },
  { id: 5, name: '洗衣服务', icon: '🧺', description: '衣物清洗熨烫', price: 58 },
  { id: 6, name: '机场接送', icon: '🚗', description: '专车接送服务', price: 150 }
];

const currentComponent = computed(() => {
  const map: Record<string, any> = {
    rooms: RoomList,
    dining: DiningList,
    services: ServiceList
  };
  return map[currentCategory.value];
});

const resetSelection = () => {
  selectedRoom.value = null;
  selectedRestaurant.value = null;
};

const handleRoomSelect = (item: RoomType | null) => {
  selectedRoom.value = item;
};

const handleDiningSelect = (item: Restaurant | null) => {
  selectedRestaurant.value = item;
};

const handleServiceBook = (service: { name: string; price: number }) => {
  message.value = `已预约 ${service.name}，费用 ${service.price} 元`;
};

const handleDiningBooking = async () => {
  errors.value = {};
  message.value = '';
  
  if (!diningDate.value) {
    errors.value.diningDate = '请选择用餐日期';
  }
  if (!diningTime.value) {
    errors.value.diningTime = '请选择用餐时段';
  }
  if (!diningGuests.value) {
    errors.value.diningGuests = '请选择用餐人数';
  }
  
  if (Object.keys(errors.value).length > 0) {
    return;
  }
  
  loading.value = true;
  try {
    await new Promise(resolve => setTimeout(resolve, 1000));
    message.value = `餐厅预订成功！${selectedRestaurant.value?.title} - ${diningDate.value} ${diningTime.value}，共 ${diningGuests.value} 人`;
    diningTime.value = '';
    diningGuests.value = '';
    diningRequests.value = '';
  } catch {
    message.value = '预订失败，请稍后重试';
  } finally {
    loading.value = false;
  }
};

const handleBooking = async () => {
  errors.value = {};
  message.value = '';
  
  if (!selectedRoom.value) {
    errors.value.room = '请选择客房类型';
    return;
  }
  
  const dateResult = validateDateRange(checkInDate.value, checkOutDate.value);
  if (!dateResult.valid) {
    errors.value.date = dateResult.message;
    return;
  }
  
  if (!numberOfGuests.value) {
    errors.value.guests = '请选择宾客数量';
    return;
  }
  
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  
  loading.value = true;
  try {
    const response = await bookingAPI.create({
      roomTypeId: selectedRoom.value.id,
      guestName: user.name || '未登录用户',
      guestPhone: user.phone || '',
      guestEmail: user.email || '',
      checkInDate: checkInDate.value,
      checkOutDate: checkOutDate.value,
      guestCount: parseInt(numberOfGuests.value),
      specialRequests: specialRequests.value
    });
    
    if (response.data.code === 200) {
      message.value = '预订成功！';
      setTimeout(() => {
        router.push('/');
      }, 1500);
    } else {
      message.value = response.data.message || '预订失败，请重试';
    }
  } catch {
    message.value = '预订失败，请重试';
  } finally {
    loading.value = false;
  }
};

const loadData = async () => {
  try {
    const [roomRes, restaurantRes] = await Promise.all([
      roomTypeAPI.getAll(),
      restaurantAPI.getAll()
    ]);
    roomTypes.value = roomRes.data.data || [];
    restaurants.value = restaurantRes.data.data || [];
  } catch {
    console.error('Failed to load data');
  }
};

loadData();
</script>

<template>
  <div class="room-booking-page">
    <header class="booking-hero">
      <div class="hero-overlay"></div>
      <div class="hero-content container">
        <span class="hero-kicker">HOTEL BOOKING</span>
        <h1 class="hero-title">预订属于您的专属时光</h1>
        <p class="hero-subtitle">精选客房 · 精致餐饮 · 贴心服务，开启难忘的入住体验</p>
      </div>
    </header>

    <div class="container booking-main">
      <nav class="category-tabs" role="tablist">
        <button
          v-for="category in categories"
          :key="category.id"
          type="button"
          class="tab-button"
          :class="{ 'is-active': currentCategory === category.id }"
          @click="currentCategory = category.id; resetSelection()"
        >
          <span class="tab-icon">{{ category.icon }}</span>
          <span class="tab-label">{{ category.name }}</span>
        </button>
      </nav>

      <Transition name="fade" mode="out-in">
        <div
          v-if="message"
          :class="['booking-alert', message.includes('成功') ? 'is-success' : 'is-error']"
        >
          <span class="alert-dot"></span>
          {{ message }}
        </div>
      </Transition>

      <Transition name="fade" mode="out-in">
        <RoomList
          v-if="currentCategory === 'rooms'"
          key="rooms"
          :rooms="roomTypes"
          :selected-id="selectedRoom?.id"
          @select="handleRoomSelect"
        />
        <DiningList
          v-else-if="currentCategory === 'dining'"
          key="dining"
          :restaurants="restaurants"
          :selected-id="selectedRestaurant?.id"
          @select="handleDiningSelect"
        />
        <ServiceList
          v-else
          key="services"
          :services="servicesData"
          @book="handleServiceBook"
        />
      </Transition>

      <Transition name="slide-up">
        <section v-if="selectedRoom" class="confirm-card" key="room-confirm">
          <div class="confirm-header">
            <h3 class="confirm-title">客房预订确认</h3>
            <span class="confirm-badge">已选择</span>
          </div>
          <div class="confirm-body">
            <div class="confirm-media">
              <img
                :src="selectedRoom.imageUrl"
                class="confirm-img"
                :alt="selectedRoom.title"
                @error="($event.target as HTMLImageElement).src = 'https://picsum.photos/seed/hotel-room/600/400'"
              >
            </div>
            <div class="confirm-info">
              <h4 class="confirm-name">{{ selectedRoom.title }}</h4>
              <p class="confirm-desc">{{ selectedRoom.description }}</p>
              <p class="confirm-price">{{ selectedRoom.price }}<span>元/晚</span></p>
            </div>

            <form @submit.prevent="handleBooking" class="confirm-form">
              <div class="form-grid">
                <div class="form-field">
                  <label for="checkInDate">入住日期</label>
                  <input
                    type="date"
                    id="checkInDate"
                    v-model="checkInDate"
                    class="form-input"
                    :min="getToday()"
                  >
                </div>
                <div class="form-field">
                  <label for="checkOutDate">退房日期</label>
                  <input
                    type="date"
                    id="checkOutDate"
                    v-model="checkOutDate"
                    class="form-input"
                    :min="checkInDate"
                  >
                </div>
                <div class="form-field">
                  <label for="numberOfGuests">宾客数量</label>
                  <select id="numberOfGuests" v-model="numberOfGuests" class="form-input">
                    <option value="">请选择</option>
                    <option value="1">1 位</option>
                    <option value="2">2 位</option>
                    <option value="3">3 位</option>
                    <option value="4">4 位</option>
                  </select>
                </div>
              </div>

              <div class="form-field">
                <label for="specialRequests">特殊要求</label>
                <textarea
                  id="specialRequests"
                  v-model="specialRequests"
                  class="form-input form-textarea"
                  rows="3"
                  placeholder="如有特殊需求请在此说明…"
                ></textarea>
              </div>

              <div v-if="errors.date" class="form-error">{{ errors.date }}</div>
              <div v-if="errors.room" class="form-error">{{ errors.room }}</div>

              <button type="submit" class="btn-gradient" :disabled="loading">
                <span v-if="loading" class="btn-spinner"></span>
                {{ loading ? '预订中…' : '立即预订' }}
              </button>
            </form>
          </div>
        </section>
      </Transition>

      <Transition name="slide-up">
        <section v-if="selectedRestaurant" class="confirm-card" key="dining-confirm">
          <div class="confirm-header">
            <h3 class="confirm-title">餐厅预订确认</h3>
            <span class="confirm-badge">已选择</span>
          </div>
          <div class="confirm-body">
            <div class="confirm-media">
              <img
                :src="selectedRestaurant.imageUrl"
                class="confirm-img"
                :alt="selectedRestaurant.title"
                @error="($event.target as HTMLImageElement).src = 'https://picsum.photos/seed/restaurant/600/400'"
              >
            </div>
            <div class="confirm-info">
              <h4 class="confirm-name">{{ selectedRestaurant.title }}</h4>
              <p class="confirm-desc">{{ selectedRestaurant.description }}</p>
              <p class="confirm-price">每人 {{ selectedRestaurant.price || '面议' }}<span>元</span></p>
            </div>

            <form @submit.prevent="handleDiningBooking" class="confirm-form">
              <div class="form-grid">
                <div class="form-field">
                  <label for="diningDate">用餐日期</label>
                  <input
                    type="date"
                    id="diningDate"
                    v-model="diningDate"
                    class="form-input"
                    :min="getToday()"
                  >
                </div>
                <div class="form-field">
                  <label for="diningTime">用餐时段</label>
                  <select id="diningTime" v-model="diningTime" class="form-input">
                    <option value="">请选择时段</option>
                    <option value="11:00-14:00">午餐 11:00-14:00</option>
                    <option value="17:00-22:00">晚餐 17:00-22:00</option>
                  </select>
                </div>
                <div class="form-field">
                  <label for="diningGuests">用餐人数</label>
                  <select id="diningGuests" v-model="diningGuests" class="form-input">
                    <option value="">请选择</option>
                    <option v-for="n in 10" :key="n" :value="n">{{ n }} 人</option>
                  </select>
                </div>
              </div>

              <div class="form-field">
                <label for="diningRequests">特殊要求</label>
                <textarea
                  id="diningRequests"
                  v-model="diningRequests"
                  class="form-input form-textarea"
                  rows="3"
                  placeholder="如座位偏好、忌口等…"
                ></textarea>
              </div>

              <div v-if="errors.diningDate" class="form-error">{{ errors.diningDate }}</div>
              <div v-if="errors.diningTime" class="form-error">{{ errors.diningTime }}</div>
              <div v-if="errors.diningGuests" class="form-error">{{ errors.diningGuests }}</div>

              <button type="submit" class="btn-gradient" :disabled="loading">
                <span v-if="loading" class="btn-spinner"></span>
                {{ loading ? '预订中…' : '确认预订' }}
              </button>
            </form>
          </div>
        </section>
      </Transition>
    </div>
  </div>
</template>

<style scoped>
.room-booking-page {
  min-height: calc(100vh - 180px);
  background: linear-gradient(180deg, #f7f9fc 0%, #eef2f7 100%);
  padding-bottom: 80px;
}

.booking-hero {
  position: relative;
  overflow: hidden;
  padding: 80px 0 96px;
  background:
    radial-gradient(1200px 500px at 10% -10%, rgba(255, 184, 107, 0.35), transparent 60%),
    radial-gradient(1000px 500px at 110% 10%, rgba(120, 119, 198, 0.35), transparent 60%),
    linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  color: #fff;
  border-radius: 0 0 32px 32px;
  box-shadow: 0 20px 60px -30px rgba(102, 126, 234, 0.55);
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 30% 50%, rgba(255, 255, 255, 0.12), transparent 60%);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.hero-kicker {
  display: inline-block;
  padding: 6px 14px;
  margin-bottom: 18px;
  font-size: 12px;
  letter-spacing: 3px;
  font-weight: 600;
  color: #fff;
  background: rgba(255, 255, 255, 0.18);
  backdrop-filter: blur(8px);
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.hero-title {
  font-size: 42px;
  font-weight: 700;
  margin: 0 0 14px;
  letter-spacing: 1px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.18);
}

.hero-subtitle {
  font-size: 17px;
  margin: 0;
  opacity: 0.95;
  letter-spacing: 0.5px;
}

.booking-main {
  margin-top: -48px;
  position: relative;
  z-index: 2;
}

.category-tabs {
  display: flex;
  gap: 12px;
  padding: 10px;
  margin-bottom: 28px;
  background: #fff;
  border-radius: 999px;
  box-shadow: 0 12px 32px -16px rgba(31, 38, 135, 0.25);
  overflow-x: auto;
}

.tab-button {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 26px;
  font-size: 15px;
  font-weight: 600;
  color: #6b7280;
  background: transparent;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  white-space: nowrap;
}

.tab-button:hover {
  color: #374151;
  background: rgba(102, 126, 234, 0.08);
}

.tab-button.is-active {
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 8px 20px -6px rgba(118, 75, 162, 0.55);
  transform: translateY(-1px);
}

.tab-icon {
  font-size: 17px;
}

.booking-alert {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 20px;
  margin-bottom: 22px;
  font-weight: 500;
  border-radius: 14px;
  border: 1px solid transparent;
}

.booking-alert.is-success {
  color: #065f46;
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.12), rgba(16, 185, 129, 0.04));
  border-color: rgba(16, 185, 129, 0.3);
}

.booking-alert.is-error {
  color: #991b1b;
  background: linear-gradient(135deg, rgba(239, 68, 68, 0.12), rgba(239, 68, 68, 0.04));
  border-color: rgba(239, 68, 68, 0.3);
}

.alert-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: currentColor;
  box-shadow: 0 0 0 4px rgba(255, 255, 255, 0.5);
}

.confirm-card {
  margin-top: 32px;
  background: #fff;
  border-radius: 22px;
  overflow: hidden;
  box-shadow: 0 24px 60px -30px rgba(31, 38, 135, 0.35);
  border: 1px solid rgba(226, 232, 240, 0.8);
}

.confirm-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24px 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.confirm-title {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.confirm-badge {
  padding: 4px 12px;
  font-size: 12px;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.22);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 999px;
  backdrop-filter: blur(6px);
}

.confirm-body {
  padding: 32px;
}

.confirm-media {
  margin-bottom: 24px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px -15px rgba(31, 38, 135, 0.3);
}

.confirm-img {
  width: 100%;
  height: 240px;
  object-fit: cover;
  display: block;
  transition: transform 0.6s ease;
}

.confirm-media:hover .confirm-img {
  transform: scale(1.03);
}

.confirm-info {
  margin-bottom: 28px;
}

.confirm-name {
  font-size: 22px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 10px;
}

.confirm-desc {
  font-size: 15px;
  color: #6b7280;
  line-height: 1.7;
  margin: 0 0 14px;
}

.confirm-price {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 6px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.confirm-price span {
  font-size: 14px;
  font-weight: 500;
  color: #6b7280;
  -webkit-text-fill-color: #6b7280;
  margin-left: 4px;
}

.confirm-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 18px;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-field label {
  font-size: 13px;
  font-weight: 600;
  color: #374151;
  letter-spacing: 0.3px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  font-size: 15px;
  color: #1f2937;
  background: #f8fafc;
  border: 1.5px solid #e2e8f0;
  border-radius: 12px;
  transition: all 0.25s ease;
  font-family: inherit;
  outline: none;
}

.form-input:hover {
  border-color: #c7d2fe;
  background: #fff;
}

.form-input:focus {
  border-color: #764ba2;
  background: #fff;
  box-shadow: 0 0 0 4px rgba(118, 75, 162, 0.15);
}

.form-input::placeholder {
  color: #94a3b8;
}

.form-textarea {
  resize: vertical;
  min-height: 90px;
  line-height: 1.6;
}

.form-error {
  padding: 10px 14px;
  font-size: 14px;
  color: #b91c1c;
  background: rgba(239, 68, 68, 0.08);
  border-left: 3px solid #ef4444;
  border-radius: 8px;
  font-weight: 500;
}

.btn-gradient {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  padding: 14px 28px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-size: 200% 200%;
  background-position: 0% 50%;
  border: none;
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 10px 25px -10px rgba(118, 75, 162, 0.65);
  overflow: hidden;
}

.btn-gradient::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
  opacity: 0;
  transition: opacity 0.35s ease;
}

.btn-gradient:hover:not(:disabled) {
  background-position: 100% 50%;
  transform: translateY(-2px);
  box-shadow: 0 16px 35px -10px rgba(118, 75, 162, 0.75);
}

.btn-gradient:hover:not(:disabled)::before {
  opacity: 1;
}

.btn-gradient:active:not(:disabled) {
  transform: translateY(0);
  box-shadow: 0 6px 18px -6px rgba(118, 75, 162, 0.6);
}

.btn-gradient:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.btn-gradient > * {
  position: relative;
  z-index: 1;
}

.btn-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.4);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.45s cubic-bezier(0.4, 0, 0.2, 1);
}
.slide-up-enter-from,
.slide-up-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

@media (max-width: 768px) {
  .booking-hero {
    padding: 60px 0 72px;
    border-radius: 0 0 24px 24px;
  }

  .hero-title {
    font-size: 30px;
  }

  .hero-subtitle {
    font-size: 15px;
  }

  .booking-main {
    margin-top: -32px;
  }

  .category-tabs {
    padding: 8px;
    gap: 8px;
  }

  .tab-button {
    padding: 10px 18px;
    font-size: 14px;
  }

  .confirm-body {
    padding: 24px 20px;
  }

  .confirm-header {
    padding: 20px 24px;
  }

  .confirm-img {
    height: 180px;
  }
}
</style>