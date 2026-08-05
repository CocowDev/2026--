<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { dashboardAPI, bookingAPI } from '../../api';

const router = useRouter();
const stats = ref({
  total: 0, pending: 0, confirmed: 0, cancelled: 0,
  todayCount: 0, revenue: 0, userCount: 0, recent: [] as any[]
});
const loading = ref(true);

const load = async () => {
  loading.value = true;
  try {
    const [statsRes, bookingsRes] = await Promise.all([
      dashboardAPI.getStats(),
      bookingAPI.getAll({ page: 1, pageSize: 5 })
    ]);
    
    if (statsRes.data.code === 200) {
      const data = statsRes.data.data;
      stats.value = {
        total: data.totalBookings || 0,
        pending: data.pendingBookings || 0,
        confirmed: data.confirmedBookings || 0,
        cancelled: (data.totalBookings || 0) - (data.pendingBookings || 0) - (data.confirmedBookings || 0),
        todayCount: data.todayCount || 0,
        revenue: data.totalRevenue || 0,
        userCount: data.activeUsers || 0,
        recent: []
      };
    }
    
    if (bookingsRes.data.code === 200) {
      stats.value.recent = bookingsRes.data.data.list || [];
    }
  } catch {
    console.error('Failed to load dashboard data');
  }
  loading.value = false;
};

onMounted(load);

const statCards = computed(() => [
  { label: '总预订数', value: stats.value.total, icon: '📋', color: '#409eff', bg: '#ecf5ff', path: '/admin/bookings' },
  { label: '待处理', value: stats.value.pending, icon: '⏳', color: '#e6a23c', bg: '#fdf6ec', path: '/admin/bookings' },
  { label: '已确认', value: stats.value.confirmed, icon: '✅', color: '#67c23a', bg: '#f0f9eb', path: '/admin/bookings' },
  { label: '已取消', value: stats.value.cancelled, icon: '❌', color: '#f56c6c', bg: '#fef0f0', path: '/admin/bookings' },
  { label: '今日新增', value: stats.value.todayCount, icon: '📈', color: '#909399', bg: '#f4f4f5', path: '/admin/bookings' },
  { label: '注册用户', value: stats.value.userCount, icon: '👥', color: '#9c27b0', bg: '#f3e5f5', path: '/admin/users' }
]);

const formatMoney = (n: number) => Number(n).toLocaleString('zh-CN');

const goBooking = (id: number) => router.push(`/admin/bookings/${id}`);
const goAll = () => router.push('/admin/bookings');

const getStatusLabel = (status: string) => {
  const map: Record<string, string> = {
    pending: '待处理',
    confirmed: '已确认',
    cancelled: '已取消',
    completed: '已完成'
  };
  return map[status] || status;
};

const getStatusClass = (status: string) => {
  const map: Record<string, string> = {
    pending: 'pending',
    confirmed: 'confirmed',
    cancelled: 'cancelled',
    completed: 'completed'
  };
  return map[status] || '';
};
</script>

<template>
  <div class="dashboard">
    <div class="page-header">
      <div>
        <h2>数据概览</h2>
        <p class="subtitle">实时展示酒店预订与运营核心指标</p>
      </div>
      <button class="btn-refresh" :disabled="loading" @click="load">
        {{ loading ? '刷新中...' : '🔄 刷新数据' }}
      </button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>

    <template v-else>
      <div class="stat-grid">
        <div
          v-for="card in statCards"
          :key="card.label"
          class="stat-card"
          :style="{ '--accent': card.color, '--bg': card.bg }"
          @click="router.push(card.path)"
        >
          <div class="stat-icon">{{ card.icon }}</div>
          <div class="stat-body">
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
          <div class="stat-arrow">→</div>
        </div>
      </div>

      <div class="row">
        <div class="col revenue-col">
          <div class="panel revenue-panel">
            <div class="panel-header">
              <h3>累计营收</h3>
              <span class="badge">基于已确认订单</span>
            </div>
            <div class="revenue-value">
              <span class="currency">¥</span>
              <span class="amount">{{ formatMoney(stats.revenue) }}</span>
            </div>
            <div class="revenue-tip">统计已确认的客房预订营收</div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <div class="panel">
            <div class="panel-header">
              <h3>最近预订</h3>
              <button class="btn-link" @click="goAll">查看全部 →</button>
            </div>
            <div class="table-wrap">
              <table class="data-table">
                <thead>
                  <tr>
                    <th>预订编号</th>
                    <th>类型</th>
                    <th>客户</th>
                    <th>日期</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="b in stats.recent" :key="b.id">
                    <td>#{{ String(b.id).slice(-6) }}</td>
                    <td>
                      <span class="tag room">客房</span>
                    </td>
                    <td>{{ b.guestName || '未登录用户' }}</td>
                    <td>{{ (b.createdAt || '').slice(0, 10) }}</td>
                    <td>
                      <span class="status-badge" :class="getStatusClass(b.status)">
                        {{ getStatusLabel(b.status) }}
                      </span>
                    </td>
                    <td>
                      <button class="btn-view" @click="goBooking(b.id)">详情</button>
                    </td>
                  </tr>
                  <tr v-if="!stats.recent || stats.recent.length === 0">
                    <td colspan="6" class="empty">暂无预订数据</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.dashboard { padding: 0; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
}
.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #1f2d3d;
  font-weight: 600;
}
.subtitle { margin: 6px 0 0; color: #909399; font-size: 13px; }

.btn-refresh {
  background: linear-gradient(135deg, #409eff, #667eea);
  color: #fff;
  border: none;
  padding: 9px 22px;
  border-radius: 10px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
  transition: all 0.2s;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}
.btn-refresh:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}
.btn-refresh:disabled { opacity: 0.7; cursor: not-allowed; }

.loading { text-align: center; padding: 60px; color: #909399; }

.stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 18px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 14px;
  padding: 22px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  position: relative;
  overflow: hidden;
}
.stat-card::after {
  content: '';
  position: absolute;
  top: -40px;
  right: -40px;
  width: 100px;
  height: 100px;
  background: var(--bg);
  border-radius: 50%;
  opacity: 0.5;
}
.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}
.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 14px;
  background: var(--bg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  flex-shrink: 0;
  position: relative;
  z-index: 1;
}
.stat-body { flex: 1; position: relative; z-index: 1; }
.stat-value {
  font-size: 30px;
  font-weight: 700;
  color: var(--accent);
  line-height: 1.1;
}
.stat-label {
  font-size: 13px;
  color: #606266;
  margin-top: 6px;
}
.stat-arrow {
  color: #c0ccda;
  font-size: 20px;
  position: relative;
  z-index: 1;
}

.row {
  display: grid;
  grid-template-columns: 1fr;
  gap: 18px;
  margin-bottom: 18px;
}

.panel {
  background: #fff;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
}
.panel-header h3 {
  margin: 0;
  font-size: 17px;
  color: #1f2d3d;
  font-weight: 600;
}
.badge {
  font-size: 12px;
  color: #909399;
  background: #f4f4f5;
  padding: 4px 12px;
  border-radius: 12px;
}
.btn-link {
  background: none;
  border: none;
  color: #409eff;
  cursor: pointer;
  font-size: 13px;
  font-weight: 500;
}
.btn-link:hover { text-decoration: underline; }

.revenue-panel {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  position: relative;
  overflow: hidden;
}
.revenue-panel::before {
  content: '';
  position: absolute;
  top: -60px;
  right: -60px;
  width: 200px;
  height: 200px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}
.revenue-panel::after {
  content: '';
  position: absolute;
  bottom: -80px;
  left: 40%;
  width: 180px;
  height: 180px;
  background: rgba(255, 255, 255, 0.06);
  border-radius: 50%;
}
.revenue-panel .panel-header h3,
.revenue-panel .revenue-tip { color: #fff; position: relative; z-index: 1; }
.revenue-panel .badge {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
}
.revenue-value {
  display: flex;
  align-items: baseline;
  gap: 6px;
  margin: 12px 0 8px;
  position: relative;
  z-index: 1;
}
.revenue-value .currency { font-size: 28px; }
.revenue-value .amount {
  font-size: 48px;
  font-weight: 700;
  letter-spacing: 1px;
}
.revenue-tip { opacity: 0.9; font-size: 13px; margin: 0; }

.table-wrap { overflow-x: auto; }
.data-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 13px;
}
.data-table th,
.data-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #f0f2f5;
}
.data-table th {
  background: #fafbfc;
  color: #606266;
  font-weight: 600;
  font-size: 13px;
}
.data-table th:first-child { border-radius: 8px 0 0 0; }
.data-table th:last-child { border-radius: 0 8px 0 0; }
.data-table tr:hover td { background: #f8fafc; }
.data-table .empty {
  text-align: center;
  color: #909399;
  padding: 48px;
}

.tag {
  padding: 3px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  background: #f4f4f5;
  color: #606266;
}
.tag.room { background: #ecf5ff; color: #409eff; }
.tag.dining { background: #fdf6ec; color: #e6a23c; }

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}
.status-badge.pending { background: #fdf6ec; color: #e6a23c; }
.status-badge.confirmed { background: #f0f9eb; color: #67c23a; }
.status-badge.cancelled { background: #fef0f0; color: #f56c6c; }
.status-badge.completed { background: #ecf5ff; color: #409eff; }

.btn-view {
  background: #ecf5ff;
  color: #409eff;
  border: none;
  padding: 5px 14px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.2s;
}
.btn-view:hover { background: #409eff; color: #fff; }
</style>