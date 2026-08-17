<script setup lang="ts">import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useBookingStore } from '../../stores/booking';
import { formatMoney } from '../../utils/money';
import { ArrowLeft, Calendar, Users, Phone, Mail, MessageSquare } from 'lucide-vue-next';
import { ElMessage } from 'element-plus';
const router = useRouter();
const route = useRoute();
const bookingStore = useBookingStore();
const booking = ref<any>(null);
const statusOptions = [
 { value: 'pending', label: '待处理' },
 { value: 'confirmed', label: '已确认' },
 { value: 'completed', label: '已完成' },
 { value: 'cancelled', label: '已取消' },
];
const getStatusClass = (status: string) => {
 switch (status) {
 case 'pending':
 return 'status-pending';
 case 'confirmed':
 return 'status-confirmed';
 case 'completed':
 return 'status-completed';
 case 'cancelled':
 return 'status-cancelled';
 default:
 return '';
 }
};
const getStatusText = (status: string) => {
 switch (status) {
 case 'pending':
 return '待处理';
 case 'confirmed':
 return '已确认';
 case 'completed':
 return '已完成';
 case 'cancelled':
 return '已取消';
 default:
 return status;
 }
};
onMounted(async () => {
 const id = Number(route.params.id);
 booking.value = await bookingStore.getBookingById(id);
});
const handleStatusChange = async (status: string) => {
 if (!booking.value)
 return;
 try {
 await bookingStore.updateBookingStatus(booking.value.id, status);
 booking.value.status = status;
 ElMessage.success('状态更新成功');
 } catch (error) {
 ElMessage.error('状态更新失败');
 }
};
</script>

<template>
  <div class="booking-detail">
    <div class="page-header">
      <button class="btn btn-secondary" @click="router.push('/admin/bookings')">
        <ArrowLeft />
        返回列表
      </button>
      <h1 class="page-title">预订详情</h1>
    </div>
    
    <div v-if="booking" class="detail-content">
      <div class="detail-header">
        <div class="booking-info">
          <h2>预订编号: #{{ booking.id }}</h2>
          <span :class="['status-badge', getStatusClass(booking.status)]">
            {{ getStatusText(booking.status) }}
          </span>
        </div>
        <div class="status-selector">
          <span>更改状态:</span>
          <select
            :value="booking.status"
            @change="handleStatusChange(($event.target as HTMLSelectElement).value)"
            class="status-select"
          >
            <option v-for="option in statusOptions" :key="option.value" :value="option.value">
              {{ option.label }}
            </option>
          </select>
        </div>
      </div>
      
      <div class="detail-grid">
        <div class="detail-card">
          <h3>客人信息</h3>
          <div class="info-row">
            <Phone class="info-icon" />
            <div class="info-content">
              <span class="info-label">姓名</span>
              <span class="info-value">{{ booking.guestName }}</span>
            </div>
          </div>
          <div class="info-row">
            <Phone class="info-icon" />
            <div class="info-content">
              <span class="info-label">联系电话</span>
              <span class="info-value">{{ booking.guestPhone }}</span>
            </div>
          </div>
          <div class="info-row">
            <Mail class="info-icon" />
            <div class="info-content">
              <span class="info-label">电子邮箱</span>
              <span class="info-value">{{ booking.guestEmail }}</span>
            </div>
          </div>
          <div class="info-row">
            <Users class="info-icon" />
            <div class="info-content">
              <span class="info-label">入住人数</span>
              <span class="info-value">{{ booking.guestCount }} 人</span>
            </div>
          </div>
        </div>
        
        <div class="detail-card">
          <h3>预订信息</h3>
          <div class="room-preview">
            <img :src="booking.roomImageUrl" :alt="booking.roomTitle" />
            <div class="room-info">
              <!-- 联表 VO 字段：餐饮预订无房型信息，显示类型标记 -->
              <h4>{{ booking.type === 'restaurant' ? '餐饮预订' : (booking.roomTitle || '未知房型') }}</h4>
              <p>{{ booking.roomDescription }}</p>
              <p class="price">¥{{ formatMoney(booking.roomPrice) }}/晚</p>
            </div>
          </div>
          <div class="info-row">
            <Calendar class="info-icon" />
            <div class="info-content">
              <span class="info-label">入住日期</span>
              <span class="info-value">{{ booking.checkInDate }}</span>
            </div>
          </div>
          <div class="info-row">
            <Calendar class="info-icon" />
            <div class="info-content">
              <span class="info-label">离店日期</span>
              <span class="info-value">{{ booking.checkOutDate }}</span>
            </div>
          </div>
        </div>
        
        <div class="detail-card">
          <h3>订单金额</h3>
          <div class="price-breakdown">
            <div class="price-row">
              <span>房型单价</span>
              <span>¥{{ formatMoney(booking.roomPrice) }}</span>
            </div>
            <div class="price-row">
              <span>入住天数</span>
              <span>{{ booking.guestCount }} 晚</span>
            </div>
            <div class="price-divider"></div>
            <div class="price-total">
              <span>总计</span>
              <span>¥{{ formatMoney(booking.totalPrice) }}</span>
            </div>
          </div>
        </div>
        
        <div class="detail-card">
          <h3>特殊要求</h3>
          <div class="special-requests">
            <MessageSquare class="request-icon" />
            <p>{{ booking.specialRequests || '无' }}</p>
          </div>
        </div>
      </div>
      
      <div class="detail-footer">
        <p class="created-at">创建时间: {{ booking.createdAt }}</p>
        <p class="updated-at">更新时间: {{ booking.updatedAt }}</p>
      </div>
    </div>
    
    <div v-else class="loading">
      <p>加载中...</p>
    </div>
  </div>
</template>

<style scoped>
.booking-detail {
  max-width: 1000px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 1.8rem;
  color: #10233b;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary {
  background: #f8f9fa;
  color: #333;
  border: 1px solid #ddd;
}

.btn-secondary:hover {
  background: #eee;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.booking-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.booking-info h2 {
  font-size: 1.5rem;
}

.status-badge {
  display: inline-block;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
}

.status-pending {
  background: #fef3c7;
  color: #d97706;
}

.status-confirmed {
  background: #dcfce7;
  color: #16a34a;
}

.status-completed {
  background: #dbeafe;
  color: #2563eb;
}

.status-cancelled {
  background: #fee2e2;
  color: #dc2626;
}

.status-selector {
  display: flex;
  align-items: center;
  gap: 10px;
}

.status-selector span {
  color: #666;
}

.status-select {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: #fff;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.detail-card {
  background: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.detail-card h3 {
  margin-bottom: 1rem;
  color: #10233b;
  border-bottom: 2px solid #c9a96a;
  padding-bottom: 0.5rem;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0.5rem 0;
}

.info-icon {
  width: 20px;
  height: 20px;
  color: #c9a96a;
}

.info-content {
  display: flex;
  flex-direction: column;
}

.info-label {
  font-size: 0.8rem;
  color: #999;
}

.info-value {
  font-weight: 500;
}

.room-preview {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.room-preview img {
  width: 120px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
}

.room-info h4 {
  margin-bottom: 0.3rem;
}

.room-info p {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 0.3rem;
}

.room-info .price {
  font-weight: bold;
  color: #c9a96a;
}

.price-breakdown {
  padding: 0.5rem 0;
}

.price-row {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
}

.price-divider {
  height: 2px;
  background: #eee;
  margin: 0.5rem 0;
}

.price-total {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  font-size: 1.2rem;
  font-weight: bold;
  color: #c9a96a;
}

.special-requests {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.request-icon {
  width: 24px;
  height: 24px;
  color: #c9a96a;
}

.detail-footer {
  background: #fff;
  padding: 1rem 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 1.5rem;
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 0.9rem;
}

.loading {
  text-align: center;
  padding: 2rem;
}
</style>