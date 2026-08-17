<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { restaurantAPI } from '../api'
import DiningList from '../components/DiningList.vue'
import type { Restaurant, Dish } from '../types'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()

const restaurants = ref<Restaurant[]>([])
const selectedRestaurant = ref<Restaurant | null>(null)
const reservationDate = ref('')
const reservationTime = ref('')
const numberOfGuests = ref('2')
const specialRequests = ref('')
const loading = ref(false)

// —— 菜品选择（第二步）——
const dishes = ref<Dish[]>([])
const loadingDishes = ref(false)
const dishQuantities = ref<Record<number, number>>({})

const getToday = () => {
  const today = new Date()
  return today.toISOString().split('T')[0]
}

onMounted(async () => {
  const res = await restaurantAPI.getAll()
  restaurants.value = res.data.data
  reservationDate.value = getToday()
})

// 选择餐厅后加载该餐厅菜品
const handleRestaurantSelect = async (restaurant: Restaurant | null) => {
  selectedRestaurant.value = restaurant
  reservationTime.value = ''
  dishes.value = []
  dishQuantities.value = {}
  if (restaurant) {
    loadingDishes.value = true
    try {
      const res = await restaurantAPI.getDishes(restaurant.id)
      dishes.value = res.data.data || []
    } catch {
      dishes.value = []
    } finally {
      loadingDishes.value = false
    }
  }
}

const dishQuantity = (dishId: number): number => dishQuantities.value[dishId] || 0

const addDish = (dishId: number) => {
  dishQuantities.value = { ...dishQuantities.value, [dishId]: dishQuantity(dishId) + 1 }
}

const removeDish = (dishId: number) => {
  const qty = dishQuantity(dishId)
  if (qty <= 1) {
    const next = { ...dishQuantities.value }
    delete next[dishId]
    dishQuantities.value = next
  } else {
    dishQuantities.value = { ...dishQuantities.value, [dishId]: qty - 1 }
  }
}

// 已选菜品列表（含合计金额）
const selectedDishList = computed(() =>
  dishes.value
    .filter(d => dishQuantity(d.id) > 0)
    .map(d => ({ dishId: d.id, quantity: dishQuantity(d.id), name: d.name, price: d.price }))
)

const totalDishPrice = computed(() =>
  selectedDishList.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
)

const handleReservation = async () => {
  if (!authStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (!selectedRestaurant.value) {
    ElMessage.error('请选择餐厅')
    return
  }
  
  if (!reservationTime.value) {
    ElMessage.error('请选择预订时间')
    return
  }
  
  loading.value = true
  
  try {
    await restaurantAPI.book({
      restaurantId: selectedRestaurant.value.id,
      reservationDate: reservationDate.value,
      reservationTime: reservationTime.value,
      numberOfGuests: Number(numberOfGuests.value),
      specialRequests: specialRequests.value,
      userId: authStore.user!.id,
      dishes: selectedDishList.value.map(({ dishId, quantity }) => ({ dishId, quantity })),
    })
    
    ElMessage.success('预订成功！')
    
    setTimeout(() => {
      selectedRestaurant.value = null
      reservationDate.value = getToday()
      reservationTime.value = ''
      numberOfGuests.value = '2'
      specialRequests.value = ''
      dishes.value = []
      dishQuantities.value = {}
    }, 1500)
  } catch (error) {
    ElMessage.error('预订失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="dining-booking-page">
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content container">
        <span class="hero-badge">
          <span class="badge-dot"></span>
          DINING · 餐饮预订
        </span>
        <h1 class="hero-title">
          品味<span class="title-gradient">美味佳肴</span>
        </h1>
        <p class="hero-subtitle">
          精选特色餐厅，为您提供难忘的美食体验
        </p>
      </div>
    </section>

    <section class="content-section">
      <div class="container">
        <div class="booking-section">
          <div class="section-header">
            <h2 class="section-title">选择餐厅</h2>
            <p class="section-hint">点击卡片选择或取消餐厅</p>
          </div>
          <DiningList 
            :restaurants="restaurants" 
            :selected-id="selectedRestaurant?.id" 
            @select="handleRestaurantSelect" 
          />
        </div>

        <div v-if="selectedRestaurant" class="booking-form">
          <div class="form-card">
            <div class="form-header">
              <h3>{{ selectedRestaurant.name }} - 预订</h3>
            </div>
            <div class="form-body">
              <div class="restaurant-preview">
                <img 
                  :src="selectedRestaurant.imageUrl" 
                  :alt="selectedRestaurant.name"
                  @error="($event.target as HTMLImageElement).src = '/images/restaurant-luxury.jpg'"
                >
                <div class="preview-info">
                  <p>{{ selectedRestaurant.description }}</p>
                </div>
              </div>
              
              <!-- 第二步：选择菜品（点选数量） -->
              <div class="dish-section">
                <div class="dish-header">
                  <h4>选择菜品</h4>
                  <span class="dish-hint">点击 +/- 调整数量，可多选</span>
                </div>
                <div v-if="loadingDishes" class="dish-loading">菜品加载中...</div>
                <div v-else-if="dishes.length" class="dish-list">
                  <div v-for="dish in dishes" :key="dish.id" class="dish-item">
                    <img :src="dish.imageUrl" :alt="dish.name" class="dish-img"
                         @error="($event.target as HTMLImageElement).src = '/images/dish-01.jpg'">
                    <div class="dish-info">
                      <span class="dish-name">{{ dish.name }}</span>
                      <span class="dish-desc">{{ dish.description }}</span>
                      <span class="dish-price">¥{{ dish.price }}</span>
                    </div>
                    <div class="dish-qty">
                      <button type="button" class="qty-btn" :disabled="dishQuantity(dish.id) === 0" @click="removeDish(dish.id)">−</button>
                      <span class="qty-num">{{ dishQuantity(dish.id) }}</span>
                      <button type="button" class="qty-btn" @click="addDish(dish.id)">+</button>
                    </div>
                  </div>
                  <div v-if="selectedDishList.length" class="dish-total">
                    已选 {{ selectedDishList.length }} 种菜品，合计 <strong>¥{{ totalDishPrice.toFixed(2) }}</strong>
                  </div>
                </div>
                <div v-else class="dish-loading">该餐厅暂无菜品</div>
              </div>
              
              <form @submit.prevent="handleReservation" class="reservation-form">
                <div class="form-grid">
                  <div class="form-group">
                    <label for="reservationDate">预订日期</label>
                    <input 
                      type="date" 
                      id="reservationDate" 
                      v-model="reservationDate" 
                      class="form-input"
                      :min="getToday()"
                    >
                  </div>
                  <div class="form-group">
                    <label for="reservationTime">预订时间</label>
                    <select id="reservationTime" v-model="reservationTime" class="form-input">
                      <option value="">请选择</option>
                      <option value="11:00">11:00</option>
                      <option value="11:30">11:30</option>
                      <option value="12:00">12:00</option>
                      <option value="12:30">12:30</option>
                      <option value="13:00">13:00</option>
                      <option value="17:00">17:00</option>
                      <option value="17:30">17:30</option>
                      <option value="18:00">18:00</option>
                      <option value="18:30">18:30</option>
                      <option value="19:00">19:00</option>
                      <option value="19:30">19:30</option>
                      <option value="20:00">20:00</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label for="numberOfGuests">宾客数量</label>
                    <select id="numberOfGuests" v-model="numberOfGuests" class="form-input">
                      <option value="">请选择</option>
                      <option value="1">1</option>
                      <option value="2">2</option>
                      <option value="3">3</option>
                      <option value="4">4</option>
                      <option value="5">5</option>
                      <option value="6">6</option>
                      <option value="7">7</option>
                      <option value="8">8</option>
                    </select>
                  </div>
                </div>
                
                <div class="form-group">
                  <label for="specialRequests">特殊要求</label>
                  <textarea id="specialRequests" v-model="specialRequests" class="form-input form-textarea" rows="3"></textarea>
                </div>
                
                <button type="submit" class="submit-btn" :disabled="loading">
                  {{ loading ? '预订中...' : '立即预订' }}
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.dining-booking-page {
  min-height: 100vh;
  background: #fafbfc;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC',
    'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.hero-section {
  position: relative;
  padding: 84px 24px 100px;
  overflow: hidden;
  background: linear-gradient(135deg, #0e1c2e 0%, #1c3350 55%, #3a5370 100%);
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 30%, rgba(255, 255, 255, 0.35) 0%, transparent 45%),
    radial-gradient(circle at 80% 70%, rgba(212, 176, 110, 0.3) 0%, transparent 45%);
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

.content-section {
  padding: 80px 0 120px;
  margin-top: -60px;
  position: relative;
  z-index: 3;
}

.booking-section {
  margin-bottom: 48px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-title {
  font-size: 24px;
  font-weight: 800;
  color: #0f172a;
  margin: 0;
}

.section-hint {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.booking-form {
  margin-top: 48px;
}

.form-card {
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
}

.form-header {
  padding: 24px 32px;
  background: linear-gradient(135deg, #c9a96a, #b89450);
  color: #fff;
}

.form-header h3 {
  font-size: 22px;
  font-weight: 700;
  margin: 0;
}

.form-body {
  padding: 32px;
}

.restaurant-preview {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;
}

.restaurant-preview img {
  width: 200px;
  height: 140px;
  object-fit: cover;
  border-radius: 12px;
}

.preview-info p {
  font-size: 15px;
  color: #64748b;
  margin: 0;
  line-height: 1.6;
}

.reservation-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.form-input {
  padding: 14px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 14px;
  color: #0f172a;
  background: #f8fafc;
  font-family: inherit;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
  outline: none;
}

.form-input:focus {
  border-color: #b89450;
  box-shadow: 0 0 0 4px rgba(184, 148, 80, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
  line-height: 1.6;
}

.submit-btn {
  padding: 14px 32px;
  font-size: 15px;
  font-weight: 600;
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 50%, #e6cf9a 100%);
  box-shadow: 0 10px 24px rgba(184, 148, 80, 0.4);
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  align-self: flex-start;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 14px 32px rgba(184, 148, 80, 0.55);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* —— 菜品选择区 —— */
.dish-section {
  margin: 24px 0;
  padding: 18px 20px;
  background: #faf9f6;
  border: 1px solid #efe8da;
  border-radius: 14px;
}

.dish-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.dish-header h4 {
  margin: 0;
  font-size: 16px;
  color: #10233b;
}

.dish-hint {
  font-size: 12px;
  color: #9a917f;
}

.dish-loading {
  padding: 16px;
  text-align: center;
  color: #9a917f;
  font-size: 14px;
}

.dish-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-height: 300px;
  overflow-y: auto;
}

.dish-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px;
  background: #fff;
  border-radius: 12px;
  border: 1px solid #f0eadc;
}

.dish-img {
  width: 64px;
  height: 48px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
}

.dish-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.dish-name {
  font-size: 14px;
  font-weight: 600;
  color: #2c313a;
}

.dish-desc {
  font-size: 12px;
  color: #9a917f;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dish-price {
  font-size: 14px;
  font-weight: 700;
  color: #b89450;
}

.dish-qty {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.qty-btn {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  border: 1px solid #d8c9a8;
  background: #fff;
  color: #b89450;
  font-size: 16px;
  line-height: 1;
  cursor: pointer;
  transition: all 0.2s;
}

.qty-btn:hover:not(:disabled) {
  background: #c9a96a;
  color: #fff;
}

.qty-btn:disabled {
  opacity: 0.35;
  cursor: not-allowed;
}

.qty-num {
  min-width: 18px;
  text-align: center;
  font-weight: 600;
  color: #2c313a;
}

.dish-total {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed #e3d9c2;
  text-align: right;
  font-size: 14px;
  color: #6b6456;
}

.dish-total strong {
  color: #b89450;
  font-size: 16px;
}

@media (max-width: 968px) {
  .form-grid {
    grid-template-columns: 1fr 1fr;
  }
  
  .restaurant-preview {
    flex-direction: column;
  }
  
  .restaurant-preview img {
    width: 100%;
    height: 180px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 100px 20px 120px;
  }
  
  .hero-title {
    font-size: 38px;
  }
  
  .form-grid {
    grid-template-columns: 1fr;
  }
  
  .submit-btn {
    width: 100%;
  }
  
  .form-body {
    padding: 20px;
  }
}
</style>