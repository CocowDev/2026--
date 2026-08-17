<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { bookingAPI } from '../api';
import { formatMoney } from '../utils/money';
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();
const authStore = useAuthStore();

// 未登录跳转登录页
if (!authStore.isLoggedIn) {
  router.replace('/login');
}

const bookings = ref<any[]>([]);
const loading = ref(false);
const cancelling = ref<number | null>(null);

// 预订状态展示映射
const statusMap: Record<string, { text: string; cls: string }> = {
  pending: { text: '待处理', cls: 'st-pending' },
  confirmed: { text: '已确认', cls: 'st-confirmed' },
  completed: { text: '已完成', cls: 'st-completed' },
  cancelled: { text: '已取消', cls: 'st-cancelled' },
};

// 预订类型文案
const typeText = (type: string | undefined) =>
  type === 'restaurant' ? '餐饮' : type === 'service' ? '服务' : '客房';

// 预订主标题（房型名 / 餐厅名 / 服务名）
const bookingTitle = (b: any) => {
  if (b.type === 'restaurant') return b.restaurantId ? '餐饮预订' : '餐饮预订';
  if (b.type === 'service') return (b.specialRequests || '').split('，')[0].replace('服务：', '');
  return b.roomTitle || '客房预订';
};

// 加载我的预订（后端已按当前用户过滤）
const loadBookings = async () => {
  loading.value = true;
  try {
    const res = await bookingAPI.getAll({ page: 1, pageSize: 50 });
    bookings.value = res.data.data.list || [];
  } catch {
    ElMessage.error('加载订单失败');
  } finally {
    loading.value = false;
  }
};

// 取消自己的待处理订单
const handleCancel = async (id: number) => {
  // 第一步：确认弹窗（用户点"再想想"则静默返回）
  try {
    await ElMessageBox.confirm('确定取消该预订吗？', '取消预订', {
      confirmButtonText: '确定取消',
      cancelButtonText: '再想想',
      type: 'warning',
    });
  } catch {
    return;
  }

  // 第二步：调用取消接口，失败需明确反馈
  cancelling.value = id;
  try {
    await bookingAPI.cancel(id);
    ElMessage.success('订单已取消');
    loadBookings();
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '取消失败，请稍后重试');
  } finally {
    cancelling.value = null;
  }
};

onMounted(loadBookings);
</script>

<template>
  <div class="profile-page">
    <section class="profile-hero">
      <div class="container">
        <span class="hero-badge">PERSONAL CENTER · 个人中心</span>
        <h1 class="hero-title">欢迎回来，{{ authStore.user?.name }}</h1>
        <p class="hero-subtitle">在这里查看和管理您的全部预订记录</p>
      </div>
    </section>

    <div class="container profile-main">
      <div class="profile-grid">
        <!-- 用户信息卡片 -->
        <aside class="info-card">
          <div class="avatar">{{ (authStore.user?.name || '?').charAt(0).toUpperCase() }}</div>
          <h3 class="info-name">{{ authStore.user?.name }}</h3>
          <p class="info-phone">{{ authStore.user?.phone || '未填写手机号' }}</p>
          <p class="info-email">{{ authStore.user?.email || '未填写邮箱' }}</p>
          <div class="info-divider"></div>
          <div class="info-stats">
            <div class="stat">
              <span class="stat-num">{{ bookings.length }}</span>
              <span class="stat-label">全部订单</span>
            </div>
            <div class="stat">
              <span class="stat-num">{{ bookings.filter(b => b.status === 'pending').length }}</span>
              <span class="stat-label">待处理</span>
            </div>
          </div>
        </aside>

        <!-- 我的预订列表 -->
        <section class="orders-card">
          <div class="orders-header">
            <h2>我的预订</h2>
            <button class="btn-refresh" :disabled="loading" @click="loadBookings">
              {{ loading ? '刷新中...' : '🔄 刷新' }}
            </button>
          </div>

          <div v-if="loading" class="orders-empty">加载中...</div>

          <div v-else-if="bookings.length === 0" class="orders-empty">
            <p>暂无预订记录</p>
            <button class="btn-go" @click="router.push('/room-booking')">去预订客房</button>
          </div>

          <div v-else class="order-list">
            <div v-for="b in bookings" :key="b.id" class="order-item">
              <div class="order-left">
                <div class="order-top">
                  <span class="order-type" :class="`type-${b.type || 'room'}`">{{ typeText(b.type) }}</span>
                  <span class="order-title">{{ bookingTitle(b) }}</span>
                </div>
                <div class="order-meta">
                  <span v-if="b.checkInDate">📅 {{ b.checkInDate }} ~ {{ b.checkOutDate || '' }}</span>
                  <span v-else-if="b.bookingDate || b.createdAt">📅 {{ (b.bookingDate || b.createdAt || '').slice(0, 10) }}</span>
                  <span>👥 {{ b.guestCount || 1 }} 人</span>
                  <span v-if="b.specialRequests" class="order-remark" :title="b.specialRequests">{{ b.specialRequests }}</span>
                </div>
              </div>
              <div class="order-right">
                <span class="order-price">¥{{ formatMoney(b.totalPrice) }}</span>
                <span :class="['status-badge', statusMap[b.status]?.cls || '']">{{ statusMap[b.status]?.text || b.status }}</span>
                <button
                  v-if="b.status === 'pending'"
                  class="btn-cancel"
                  :disabled="cancelling === b.id"
                  @click="handleCancel(b.id)"
                >
                  {{ cancelling === b.id ? '取消中...' : '取消' }}
                </button>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-page {
  min-height: calc(100vh - 180px);
  background: linear-gradient(180deg, #faf7f1 0%, #f3eee3 100%);
  padding-bottom: 60px;
}

.profile-hero {
  position: relative;
  padding: 64px 0 72px;
  overflow: hidden;
  background:
    radial-gradient(1000px 420px at 85% -20%, rgba(201, 169, 106, 0.22), transparent 60%),
    linear-gradient(135deg, #0e1c2e 0%, #1c3350 60%, #3a5370 100%);
  color: #fff;
}

.hero-badge {
  display: inline-block;
  padding: 5px 14px;
  margin-bottom: 14px;
  font-size: 12px;
  letter-spacing: 2px;
  color: #e6cf9a;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(230, 207, 154, 0.4);
  border-radius: 999px;
}

.hero-title {
  margin: 0 0 8px;
  font-size: 32px;
}

.hero-subtitle {
  margin: 0;
  font-size: 15px;
  opacity: 0.85;
}

.profile-main {
  margin-top: -34px;
  position: relative;
  z-index: 2;
}

.profile-grid {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 22px;
  align-items: start;
}

.info-card {
  background: #fff;
  border-radius: 18px;
  padding: 30px 24px;
  text-align: center;
  box-shadow: 0 14px 40px -18px rgba(16, 35, 59, 0.25);
  border: 1px solid rgba(226, 232, 240, 0.7);
}

.avatar {
  width: 72px;
  height: 72px;
  margin: 0 auto 14px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  font-weight: 700;
  color: #fff;
  background: linear-gradient(135deg, #b89450, #e6cf9a);
  box-shadow: 0 10px 24px -8px rgba(201, 169, 106, 0.6);
}

.info-name {
  margin: 0 0 4px;
  font-size: 20px;
  color: #10233b;
}

.info-phone,
.info-email {
  margin: 2px 0;
  font-size: 13px;
  color: #8a7f6a;
  word-break: break-all;
}

.info-divider {
  height: 1px;
  margin: 18px 0;
  background: #f0eadc;
}

.info-stats {
  display: flex;
  gap: 12px;
}

.stat {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: 10px 0;
  background: #faf9f6;
  border-radius: 12px;
}

.stat-num {
  font-size: 22px;
  font-weight: 700;
  color: #b89450;
}

.stat-label {
  font-size: 12px;
  color: #9a917f;
}

.orders-card {
  background: #fff;
  border-radius: 18px;
  padding: 24px 26px;
  box-shadow: 0 14px 40px -18px rgba(16, 35, 59, 0.25);
  border: 1px solid rgba(226, 232, 240, 0.7);
  min-height: 320px;
}

.orders-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 18px;
}

.orders-header h2 {
  margin: 0;
  font-size: 20px;
  color: #10233b;
}

.btn-refresh {
  padding: 7px 16px;
  border: none;
  border-radius: 999px;
  background: #f5f0e6;
  color: #8a7f6a;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.25s;
}

.btn-refresh:hover:not(:disabled) {
  background: #eadfc6;
}

.btn-refresh:disabled {
  opacity: 0.6;
}

.orders-empty {
  padding: 60px 0;
  text-align: center;
  color: #9a917f;
}

.btn-go {
  margin-top: 14px;
  padding: 10px 26px;
  border: none;
  border-radius: 999px;
  color: #fff;
  background: linear-gradient(135deg, #b89450, #d9bc7f);
  cursor: pointer;
  font-weight: 600;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 18px;
  border-radius: 14px;
  background: #faf9f6;
  border: 1px solid #f0eadc;
  transition: border-color 0.25s, box-shadow 0.25s;
}

.order-item:hover {
  border-color: #e3d5b0;
  box-shadow: 0 6px 18px -8px rgba(16, 35, 59, 0.15);
}

.order-left {
  flex: 1;
  min-width: 0;
}

.order-top {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.order-type {
  flex-shrink: 0;
  padding: 3px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
}

.type-room {
  background: #eef3fa;
  color: #3a6ea5;
}

.type-restaurant {
  background: #fdf3e7;
  color: #b26a1d;
}

.type-service {
  background: #f2edfa;
  color: #7a4fb0;
}

.order-title {
  font-size: 15px;
  font-weight: 600;
  color: #2c313a;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 16px;
  font-size: 12.5px;
  color: #8a7f6a;
}

.order-remark {
  max-width: 260px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.order-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.order-price {
  font-size: 16px;
  font-weight: 700;
  color: #b89450;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
}

.st-pending { background: #fdf3e7; color: #b26a1d; }
.st-confirmed { background: #e8f6ee; color: #1d8a5a; }
.st-completed { background: #eef3fa; color: #3a6ea5; }
.st-cancelled { background: #f6e9e9; color: #b04a4a; }

.btn-cancel {
  padding: 6px 14px;
  border: 1px solid #e3b4b4;
  border-radius: 999px;
  background: #fff;
  color: #b04a4a;
  cursor: pointer;
  font-size: 12.5px;
  transition: all 0.25s;
}

.btn-cancel:hover:not(:disabled) {
  background: #b04a4a;
  color: #fff;
}

.btn-cancel:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 860px) {
  .profile-grid {
    grid-template-columns: 1fr;
  }

  .order-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .order-right {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>
