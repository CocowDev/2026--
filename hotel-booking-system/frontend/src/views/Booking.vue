<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useBookingStore } from '../stores/booking'
import { roomTypeAPI } from '../api'
import { formatMoney } from '../utils/money'
import RoomList from '../components/RoomList.vue'
import type { RoomType } from '../types'
import { Calendar, Users, MessageSquare, CreditCard, CheckCircle } from 'lucide-vue-next'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()
const bookingStore = useBookingStore()

const roomTypes = ref<RoomType[]>([])
const selectedRoom = ref<RoomType | null>(null)
const form = ref({
  guestName: '',
  guestPhone: '',
  guestEmail: '',
  checkInDate: '',
  checkOutDate: '',
  guestCount: 1,
  specialRequests: '',
})

const totalPrice = computed(() => {
  if (!selectedRoom.value || !form.value.checkInDate || !form.value.checkOutDate) {
    return 0
  }
  const checkIn = new Date(form.value.checkInDate)
  const checkOut = new Date(form.value.checkOutDate)
  const nights = Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24))
  return nights > 0 ? nights * selectedRoom.value.price : 0
})

const nights = computed(() => {
  if (!form.value.checkInDate || !form.value.checkOutDate) {
    return 0
  }
  const checkIn = new Date(form.value.checkInDate)
  const checkOut = new Date(form.value.checkOutDate)
  return Math.ceil((checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24))
})

onMounted(async () => {
  const res = await roomTypeAPI.getAll()
  roomTypes.value = res.data.data
  
  if (authStore.user) {
    form.value.guestName = authStore.user.name
    form.value.guestPhone = authStore.user.phone
    form.value.guestEmail = authStore.user.email
  }
})

const handleRoomSelect = (room: RoomType | null) => {
  selectedRoom.value = room
}

const handleSubmit = async () => {
  if (!authStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  
  if (!selectedRoom.value) {
    ElMessage.error('请选择房型')
    return
  }
  if (!form.value.checkInDate || !form.value.checkOutDate) {
    ElMessage.error('请选择入住和离店日期')
    return
  }
  if (nights.value <= 0) {
    ElMessage.error('离店日期必须晚于入住日期')
    return
  }
  
  try {
    // 注意：userId 由后端从登录态解析，totalPrice 由后端按房型单价计算，前端无需提交
    await bookingStore.createBooking({
      roomTypeId: selectedRoom.value.id,
      guestName: form.value.guestName,
      guestPhone: form.value.guestPhone,
      guestEmail: form.value.guestEmail,
      checkInDate: form.value.checkInDate,
      checkOutDate: form.value.checkOutDate,
      guestCount: form.value.guestCount,
      specialRequests: form.value.specialRequests,
    })
    ElMessage.success('预订成功！')
    router.push('/')
  } catch (error) {
    ElMessage.error('预订失败，请重试')
  }
}
</script>

<template>
  <div class="booking">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">客房预订</h1>
        <p class="page-subtitle">选择心仪的房型，开启舒适入住之旅</p>
      </div>
      
      <div class="booking-content">
        <div class="booking-form">
          <div class="booking-section">
            <div class="section-header">
              <h2 class="section-title">选择房型</h2>
              <p class="section-hint">点击卡片选择或取消房型</p>
            </div>
            <RoomList 
              :rooms="roomTypes" 
              :selected-id="selectedRoom?.id" 
              @select="handleRoomSelect" 
            />
          </div>

          <div class="booking-section" v-if="selectedRoom">
            <div class="section-header">
              <h2 class="section-title">预订信息</h2>
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">
                  <Calendar class="label-icon" />
                  入住日期
                </label>
                <input
                  v-model="form.checkInDate"
                  type="date"
                  class="form-control"
                  :min="new Date().toISOString().split('T')[0]"
                />
              </div>
              <div class="form-group">
                <label class="form-label">
                  <Calendar class="label-icon" />
                  离店日期
                </label>
                <input
                  v-model="form.checkOutDate"
                  type="date"
                  class="form-control"
                  :min="form.checkInDate"
                />
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">
                <Users class="label-icon" />
                入住人数
              </label>
              <input
                v-model.number="form.guestCount"
                type="number"
                min="1"
                max="10"
                class="form-control"
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">客人姓名</label>
              <input
                v-model="form.guestName"
                type="text"
                placeholder="请输入姓名"
                class="form-control"
              />
            </div>
            
            <div class="form-row">
              <div class="form-group">
                <label class="form-label">联系电话</label>
                <input
                  v-model="form.guestPhone"
                  type="tel"
                  placeholder="请输入电话"
                  class="form-control"
                />
              </div>
              <div class="form-group">
                <label class="form-label">电子邮箱</label>
                <input
                  v-model="form.guestEmail"
                  type="email"
                  placeholder="请输入邮箱"
                  class="form-control"
                />
              </div>
            </div>
            
            <div class="form-group">
              <label class="form-label">
                <MessageSquare class="label-icon" />
                特殊要求
              </label>
              <textarea
                v-model="form.specialRequests"
                placeholder="如有特殊要求请在此说明"
                rows="3"
                class="form-control"
              ></textarea>
            </div>
            
            <button 
              class="btn btn-primary btn-large" 
              @click="handleSubmit"
              :disabled="!selectedRoom || !form.checkInDate || !form.checkOutDate"
            >
              <CreditCard class="btn-icon" />
              确认预订
            </button>
          </div>
        </div>
        
        <div class="booking-summary">
          <div class="summary-header">
            <h2 class="summary-title">订单摘要</h2>
            <div class="summary-icon">
              <CheckCircle />
            </div>
          </div>
          
          <div class="summary-content">
            <div class="summary-item">
              <span class="item-label">房型</span>
              <span class="item-value">{{ selectedRoom?.title || '-' }}</span>
            </div>
            <div class="summary-item">
              <span class="item-label">入住日期</span>
              <span class="item-value">{{ form.checkInDate || '-' }}</span>
            </div>
            <div class="summary-item">
              <span class="item-label">离店日期</span>
              <span class="item-value">{{ form.checkOutDate || '-' }}</span>
            </div>
            <div class="summary-item">
              <span class="item-label">入住天数</span>
              <span class="item-value">{{ nights }} 晚</span>
            </div>
            <div class="summary-item">
              <span class="item-label">入住人数</span>
              <span class="item-value">{{ form.guestCount }} 人</span>
            </div>
            
            <div class="summary-divider"></div>
            
            <div class="summary-total">
              <span class="total-label">总计</span>
              <span class="total-price">¥{{ formatMoney(totalPrice) }}</span>
            </div>
          </div>
          
          <div class="booking-tips">
            <h3>预订须知</h3>
            <ul>
              <li>预订成功后将收到确认短信</li>
              <li>入住时间：14:00 后</li>
              <li>离店时间：12:00 前</li>
              <li>如需取消预订，请提前24小时通知</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.booking {
  padding: 60px 0;
  background: linear-gradient(180deg, #f8fafc 0%, #f1f5f9 100%);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 50px;
}

.page-title {
  font-size: 40px;
  font-weight: 800;
  color: #0f172a;
  margin: 0 0 12px;
  letter-spacing: -0.5px;
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

.booking-content {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 32px;
}

.booking-form {
  background: #ffffff;
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 4px 24px -8px rgba(15, 23, 42, 0.12);
}

.booking-section {
  margin-bottom: 40px;
}

.booking-section:last-child {
  margin-bottom: 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 700;
  color: #0f172a;
  margin: 0;
}

.section-hint {
  font-size: 13px;
  color: #94a3b8;
  margin: 0;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-weight: 600;
  color: #334155;
  font-size: 14px;
}

.label-icon {
  width: 16px;
  height: 16px;
  color: #6366f1;
}

.form-control {
  width: 100%;
  padding: 14px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 14px;
  transition: all 0.3s ease;
  font-family: inherit;
}

.form-control:focus {
  outline: none;
  border-color: #6366f1;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
}

.form-control::placeholder {
  color: #94a3b8;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.22, 1, 0.36, 1);
}

.btn-icon {
  width: 18px;
  height: 18px;
}

.btn-primary {
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 55%, #ec4899 100%);
  color: #fff;
  box-shadow: 0 10px 30px -10px rgba(139, 92, 246, 0.5);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 14px 40px -10px rgba(139, 92, 246, 0.6);
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-large {
  width: 100%;
  justify-content: center;
  padding: 16px;
  font-size: 16px;
}

.booking-summary {
  background: #ffffff;
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 4px 24px -8px rgba(15, 23, 42, 0.12);
  position: sticky;
  top: 100px;
}

.summary-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.summary-title {
  font-size: 20px;
  font-weight: 700;
  color: #0f172a;
  margin: 0;
}

.summary-icon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: 12px;
  color: #fff;
  font-size: 20px;
}

.summary-content {
  margin-bottom: 24px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f1f5f9;
}

.summary-item:last-child {
  border-bottom: none;
}

.item-label {
  font-size: 14px;
  color: #64748b;
}

.item-value {
  font-size: 14px;
  font-weight: 600;
  color: #0f172a;
}

.summary-divider {
  height: 2px;
  background: linear-gradient(135deg, #6366f1 0%, #ec4899 100%);
  margin: 16px 0;
  border-radius: 999px;
}

.summary-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
}

.total-label {
  font-size: 16px;
  font-weight: 600;
  color: #0f172a;
}

.total-price {
  font-size: 28px;
  font-weight: 800;
  color: #6366f1;
}

.booking-tips {
  background: linear-gradient(135deg, #f0fdf4 0%, #ecfdf5 100%);
  border-radius: 12px;
  padding: 20px;
}

.booking-tips h3 {
  margin: 0 0 12px;
  font-size: 14px;
  font-weight: 600;
  color: #059669;
}

.booking-tips ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.booking-tips li {
  font-size: 13px;
  color: #374151;
  padding: 6px 0;
  padding-left: 20px;
  position: relative;
}

.booking-tips li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 10px;
  width: 6px;
  height: 6px;
  background: #10b981;
  border-radius: 50%;
}

@media (max-width: 992px) {
  .booking-content {
    grid-template-columns: 1fr;
  }
  
  .booking-summary {
    position: static;
  }
}

@media (max-width: 640px) {
  .page-title {
    font-size: 28px;
  }
  
  .booking-form {
    padding: 20px;
  }
  
  .booking-summary {
    padding: 20px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .total-price {
    font-size: 24px;
  }
}
</style>