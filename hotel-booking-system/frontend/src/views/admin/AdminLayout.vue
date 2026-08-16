<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '../../stores/auth';
import {
  LayoutDashboard,
  BookOpen,
  Users,
  LogOut,
  Crown,
} from 'lucide-vue-next';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const menuItems = [
  { name: '仪表盘', icon: LayoutDashboard, path: '/admin/dashboard' },
  { name: '预订管理', icon: BookOpen, path: '/admin/bookings' },
  { name: '用户管理', icon: Users, path: '/admin/users' },
];

const handleLogout = () => {
  authStore.logout();
  router.push('/');
};

const isActive = (path: string) => {
  return route.path.startsWith(path);
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
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
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
  color: #e94560;
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
  background: #e94560;
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