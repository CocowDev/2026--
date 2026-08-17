<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { userAPI } from '../../api';
import { Search, Trash2, RefreshCw } from 'lucide-vue-next';
import { ElMessage, ElMessageBox } from 'element-plus';

const users = ref<any[]>([]);
const total = ref(0);
const searchKeyword = ref('');
const currentPage = ref(1);
const pageSize = ref(10);

const fetchUsers = async () => {
  const res = await userAPI.getAll({
    page: currentPage.value,
    pageSize: pageSize.value,
  });
  users.value = res.data.data.list;
  total.value = res.data.data.total;
};

onMounted(fetchUsers);
watch([searchKeyword, currentPage, pageSize], fetchUsers);

const handleDelete = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    await userAPI.delete(id);
    ElMessage.success('删除成功');
    fetchUsers();
  } catch {
    // cancelled
  }
};
</script>

<template>
  <div class="user-list">
    <div class="page-header">
      <h1 class="page-title">用户管理</h1>
    </div>
    
    <div class="filters">
      <div class="search-box">
        <Search class="search-icon" />
        <input
          v-model="searchKeyword"
          type="text"
          placeholder="搜索姓名、电话、邮箱..."
          class="search-input"
        />
      </div>
      <button class="btn btn-secondary" @click="fetchUsers">
        <RefreshCw />
        刷新
      </button>
    </div>
    
    <div class="data-table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>用户ID</th>
            <th>姓名</th>
            <th>联系电话</th>
            <th>电子邮箱</th>
            <th>注册时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>#{{ user.id }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.createdAt }}</td>
            <td>
              <div class="action-buttons">
                <button class="btn btn-danger btn-sm" @click="handleDelete(user.id)">
                  <Trash2 />
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="6" class="empty-row">暂无用户数据</td>
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
      <span>第 {{ currentPage }} 页 / 共 {{ Math.ceil(total / pageSize) }} 页</span>
      <button
        class="btn btn-secondary btn-sm"
        :disabled="currentPage >= Math.ceil(total / pageSize)"
        @click="currentPage++"
      >
        下一页
      </button>
    </div>
  </div>
</template>

<style scoped>
.user-list {
  max-width: 1200px;
}

.page-header {
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