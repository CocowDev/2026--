<script setup lang="ts">import { ref, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useBookingStore } from '../../stores/booking';
import { bookingAPI } from '../../api';
import { formatMoney } from '../../utils/money';
import { Search, Filter, Download, Eye, Trash2, RefreshCw } from 'lucide-vue-next';
import { ElMessage, ElMessageBox } from 'element-plus';
const router = useRouter();
const bookingStore = useBookingStore();
const searchKeyword = ref('');
const statusFilter = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
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
const fetchBookings = async () => {
 await bookingStore.getBookings({
 page: currentPage.value,
 pageSize: pageSize.value,
 status: statusFilter.value || undefined,
 keyword: searchKeyword.value || undefined,
 });
};
onMounted(fetchBookings);
watch([searchKeyword, statusFilter, currentPage, pageSize], fetchBookings);
const handleView = (id: number) => {
 router.push(`/admin/bookings/${id}`);
};
const handleDelete = async (id: number) => {
 try {
 await ElMessageBox.confirm('确定要删除该预订吗？', '提示', {
 confirmButtonText: '确定',
 cancelButtonText: '取消',
 type: 'warning',
 });
 await bookingStore.deleteBooking(id);
 ElMessage.success('删除成功');
 fetchBookings();
 } catch {
 // cancelled
 }
};
const handleExport = async () => {
 try {
 const response = await bookingAPI.export();
 const blob = new Blob([response.data], { type: 'text/csv' });
 const url = window.URL.createObjectURL(blob);
 const a = document.createElement('a');
 a.href = url;
 a.download = 'bookings.csv';
 document.body.appendChild(a);
 a.click();
 document.body.removeChild(a);
 window.URL.revokeObjectURL(url);
 ElMessage.success('导出成功');
 } catch (error) {
 ElMessage.error('导出失败');
 }
};
</script>

<template>
  <div class="booking-list">
    <div class="page-header">
      <h1 class="page-title">预订管理</h1>
      <button class="btn btn-primary" @click="handleExport">
        <Download />
        导出数据
      </button>
    </div>
    
    <div class="filters">
      <div class="search-box">
        <Search class="search-icon" />
        <input
          v-model="searchKeyword"
          type="text"
          placeholder="搜索客人姓名、电话..."
          class="search-input"
        />
      </div>
      <div class="filter-group">
        <Filter class="filter-icon" />
        <select v-model="statusFilter" class="filter-select">
          <option value="">全部状态</option>
          <option value="pending">待处理</option>
          <option value="confirmed">已确认</option>
          <option value="completed">已完成</option>
          <option value="cancelled">已取消</option>
        </select>
      </div>
      <button class="btn btn-secondary" @click="fetchBookings">
        <RefreshCw />
        刷新
      </button>
    </div>
    
    <div class="data-table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>预订编号</th>
            <th>客人姓名</th>
            <th>联系电话</th>
            <th>房型</th>
            <th>入住日期</th>
            <th>离店日期</th>
            <th>人数</th>
            <th>总价</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="booking in bookingStore.bookings" :key="booking.id">
            <td>#{{ booking.id }}</td>
            <td>{{ booking.guestName }}</td>
            <td>{{ booking.guestPhone }}</td>
            <!-- 房型列：餐饮预订显示类型标记，客房预订显示联表房型名 -->
            <td>{{ booking.type === 'restaurant' ? '餐饮预订' : (booking.roomTitle || '—') }}</td>
            <td>{{ booking.checkInDate }}</td>
            <td>{{ booking.checkOutDate }}</td>
            <td>{{ booking.guestCount }}人</td>
            <td>¥{{ formatMoney(booking.totalPrice) }}</td>
            <td>
              <span :class="['status-badge', getStatusClass(booking.status)]">
                {{ getStatusText(booking.status) }}
              </span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="btn btn-secondary btn-sm" @click="handleView(booking.id)">
                  <Eye />
                </button>
                <button class="btn btn-danger btn-sm" @click="handleDelete(booking.id)">
                  <Trash2 />
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="bookingStore.bookings.length === 0">
            <td colspan="10" class="empty-row">暂无预订数据</td>
          </tr>
        </tbody>
      </table>
    </div>
    
    <div class="pagination">
      <button
        class="btn btn-secondary btn-sm"
        :disabled="currentPage <= 1"
        @click="currentPage--"
      >
        上一页
      </button>
      <span>第 {{ currentPage }} 页 / 共 {{ Math.ceil(bookingStore.total / pageSize) }} 页</span>
      <button
        class="btn btn-secondary btn-sm"
        :disabled="currentPage >= Math.ceil(bookingStore.total / pageSize)"
        @click="currentPage++"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<style scoped>
.booking-list {
  max-width: 1400px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.page-title {
  font-size: 1.8rem;
  color: #10233b;
}

.filters {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.search-box {
  position: relative;
  flex: 1;
  max-width: 300px;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

.search-input {
  width: 100%;
  padding: 10px 36px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-input:focus {
  outline: none;
  border-color: #c9a96a;
}

.filter-group {
  position: relative;
}

.filter-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

.filter-select {
  padding: 10px 36px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: #fff;
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

.btn-primary {
  background: #c9a96a;
  color: #fff;
}

.btn-primary:hover {
  background: #c73e54;
}

.btn-secondary {
  background: #f8f9fa;
  color: #333;
  border: 1px solid #ddd;
}

.btn-secondary:hover {
  background: #eee;
}

.btn-danger {
  background: #dc2626;
  color: #fff;
}

.btn-danger:hover {
  background: #b91c1c;
}

.btn-sm {
  padding: 4px 10px;
}

.data-table-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
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

.action-buttons {
  display: flex;
  gap: 6px;
}

.empty-row {
  text-align: center;
  color: #999;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 1.5rem;
}

.pagination span {
  color: #666;
}
</style>