<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '../../stores/auth';
import { dashboardAPI } from '../../api';
import {
  LayoutDashboard,
  BookOpen,
  Users,
  LogOut,
  Crown,
  Bell,
} from 'lucide-vue-next';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const menuItems = [
  { name: '仪表盘', icon: LayoutDashboard, path: '/admin/dashboard' },
  { name: '预订管理', icon: BookOpen, path: '/admin/bookings' },
  { name: '用户管理', icon: Users, path: '/admin/users' },
];

// —— 待处理预订提醒（进入后台加载 + 30s 轮询）——
const pendingCount = ref(0);
let timer: ReturnType<typeof setInterval> | null = null;

const loadPendingCount = async () => {
  try {
    const res = await dashboardAPI.getStats();
    if (res.data.code === 200) {
      pendingCount.value = res.data.data.pendingBookings || 0;
    }
  } catch {
    // 静默失败，下次轮询重试
  }
};

onMounted(() => {
  loadPendingCount();
  timer = setInterval(loadPendingCount, 30000);
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});

const handleLogout = () => {
  authStore.logout();
  router.push('/');
};

const isActive = (path: string) => {
  return route.path.startsWith(path);
};

// 点击提醒跳转待处理订单列表
const goPendingBookings = () => {
  router.push('/admin/bookings?status=pending');
};
</script>

<template>
  <div class="admin-layout">
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="logo">
          <Crown />
          <span>管理后台</span>
        </div>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <li v-for="item in menuItems" :key="item.path">
            <a
              :href="item.path"
              :class="{ active: isActive(item.path) }"
            >
              <component :is="item.icon" />
              <span>{{ item.name }}</span>
            </a>
          </li>
        </ul>
      </nav>
      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">
          <LogOut />
          <span>退出登录</span>
        </button>
      </div>
    </aside>
    
    <main class="main-content">
      <header class="top-bar">
        <div class="top-bar-content">
          <!-- 待处理预订提醒 -->
          <button class="pending-alert" @click="goPendingBookings" title="查看待处理订单">
            <Bell class="alert-icon" />
            <span>待处理</span>
            <span v-if="pendingCount > 0" class="alert-badge">{{ pendingCount }}</span>
          </button>
          <div class="user-info">
            <span>欢迎, {{ authStore.user?.username }}</span>
          </div>
        </div>
      </header>
      <div class="content-area">
        <router-view />
      </div>
    </main>
  </div>
</template>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 250px;
  background: linear-gradient(180deg, #10233b 0%, #1d3a5f 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1.2rem;
  font-weight: bold;
}

.logo svg {
  width: 28px;
  height: 28px;
  color: #c9a96a;
}

.sidebar-nav {
  flex: 1;
  padding: 1rem 0;
}

.sidebar-nav ul {
  list-style: none;
  padding: 0;
}

.sidebar-nav li {
  margin-bottom: 0.5rem;
}

.sidebar-nav a {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 1.5rem;
  color: #fff;
  text-decoration: none;
  transition: all 0.3s;
}

.sidebar-nav a:hover {
  background: rgba(255, 255, 255, 0.1);
}

.sidebar-nav a.active {
  background: #c9a96a;
}

.sidebar-nav a svg {
  width: 20px;
  height: 20px;
}

.sidebar-footer {
  padding: 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 12px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: #fff;
  cursor: pointer;
  transition: background 0.3s;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.logout-btn svg {
  width: 20px;
  height: 20px;
}

.main-content {
  flex: 1;
  margin-left: 250px;
  display: flex;
  flex-direction: column;
}

.top-bar {
  background: #fff;
  padding: 1rem 2rem;
  border-bottom: 1px solid #eee;
}

.top-bar-content {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 18px;
}

/* 待处理预订提醒 */
.pending-alert {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 7px 16px;
  border: 1px solid #e3d5b0;
  border-radius: 999px;
  background: #fdf9f0;
  color: #8a6d2f;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.25s;
}

.pending-alert:hover {
  background: #f5ead0;
  transform: translateY(-1px);
}

.alert-icon {
  width: 16px;
  height: 16px;
}

.alert-badge {
  min-width: 20px;
  height: 20px;
  padding: 0 6px;
  border-radius: 999px;
  background: #c8453a;
  color: #fff;
  font-size: 12px;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.user-info {
  color: #333;
}

.content-area {
  flex: 1;
  padding: 2rem;
  background: #f8f9fa;
}
</style>