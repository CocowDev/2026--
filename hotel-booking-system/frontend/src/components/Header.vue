<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const isMenuOpen = ref(false);
const scrolled = ref(false);
const searchQuery = ref('');

const navItems = [
  { name: '首页', path: '/' },
  { name: '新闻', path: '/news' },
  { name: '客房预订', path: '/room-booking' },
  { name: '餐饮预订', path: '/dining-booking' },
  { name: '客房环境', path: '/room-environment' },
  { name: '关于我们', path: '/about' },
  { name: '联系我们', path: '/contact' }
];

const isActive = (path: string) => {
  if (path === '/') {
    return route.path === '/';
  }
  return route.path.startsWith(path);
};

const navigateTo = (path: string) => {
  if (route.path !== path) {
    router.push(path);
  }
  closeMenu();
  nextTick(() => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  });
};

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value;
};

const closeMenu = () => {
  isMenuOpen.value = false;
};

const handleLogout = () => {
  authStore.logout();
  router.push('/');
  closeMenu();
};

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    router.push({ path: '/news', query: { search: searchQuery.value.trim() } });
    searchQuery.value = '';
    closeMenu();
  }
};

const handleScroll = () => {
  scrolled.value = window.scrollY > 10;
};

watch(() => route.path, () => {
  closeMenu();
});

onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<template>
  <nav class="navbar-root" :class="{ 'is-scrolled': scrolled }">
    <div class="navbar-inner">
      <router-link class="brand" to="/" @click="navigateTo('/')">
        <span class="brand-icon">
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
            <path d="M3 21V8L12 3L21 8V21H14V14H10V21H3Z" stroke="currentColor" stroke-width="1.8" stroke-linejoin="round"/>
            <path d="M10 14H14" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/>
          </svg>
        </span>
        <span class="brand-text">
          <span class="brand-title">星级酒店</span>
          <span class="brand-sub">Luxury Hotel</span>
        </span>
      </router-link>

      <button
        class="hamburger"
        :class="{ 'is-open': isMenuOpen }"
        type="button"
        aria-label="Toggle navigation"
        @click="toggleMenu"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>

      <div class="menu-backdrop" :class="{ 'is-visible': isMenuOpen }" @click="closeMenu"></div>

      <div class="menu" :class="{ 'is-open': isMenuOpen }">
        <ul class="nav-list">
          <li v-for="item in navItems" :key="item.path" class="nav-item">
            <router-link
              class="nav-link"
              :class="{ 'active': isActive(item.path) }"
              :to="item.path"
              @click="navigateTo(item.path)"
            >
              {{ item.name }}
            </router-link>
          </li>
        </ul>

        <form class="search-form" @submit.prevent="handleSearch">
          <div class="search-input-wrap">
            <svg class="search-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
              <circle cx="11" cy="11" r="7" stroke="currentColor" stroke-width="2"/>
              <path d="m20 20-3.5-3.5" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <input
              class="search-input"
              type="search"
              placeholder="搜索客房、新闻、服务..."
              v-model="searchQuery"
              @keyup.enter="handleSearch"
            >
            <button class="search-btn" type="submit">搜索</button>
          </div>
        </form>

        <div class="actions">
          <template v-if="!authStore.isLoggedIn">
            <router-link class="btn btn-ghost" to="/login" @click="navigateTo('/login')">登录</router-link>
            <router-link class="btn btn-primary" to="/register" @click="navigateTo('/register')">注册</router-link>
          </template>
          <template v-else>
            <div class="user-chip">
              <span class="user-avatar">{{ (authStore.user?.name || '?').charAt(0).toUpperCase() }}</span>
              <span class="user-name">{{ authStore.user?.name }}</span>
            </div>
            <button class="btn btn-ghost" @click="handleLogout">退出</button>
          </template>
          <router-link
            v-if="authStore.isAdmin"
            class="btn btn-admin"
            to="/admin/dashboard"
            @click="navigateTo('/admin/dashboard')"
          >
            🛠️ 管理后台
          </router-link>
          <router-link
            v-else
            class="btn btn-admin"
            to="/admin/login"
            @click="navigateTo('/admin/login')"
          >
            🔐 管理后台
          </router-link>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.navbar-root {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 14px 24px;
  background: rgba(255, 255, 255, 0.55);
  backdrop-filter: saturate(180%) blur(18px);
  -webkit-backdrop-filter: saturate(180%) blur(18px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.08);
  transition: background 0.35s ease, box-shadow 0.35s ease, padding 0.35s ease;
}

.navbar-root.is-scrolled {
  background: rgba(255, 255, 255, 0.72);
  box-shadow: 0 12px 40px rgba(31, 38, 135, 0.12);
  padding: 10px 24px;
}

.navbar-inner {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  text-decoration: none;
  color: inherit;
  flex-shrink: 0;
  transition: transform 0.25s ease;
}

.brand:hover {
  transform: translateY(-1px);
}

.brand-icon {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  box-shadow: 0 8px 24px rgba(118, 75, 162, 0.35);
  transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.brand:hover .brand-icon {
  box-shadow: 0 12px 30px rgba(118, 75, 162, 0.5);
  transform: rotate(-6deg) scale(1.05);
}

.brand-icon svg {
  width: 24px;
  height: 24px;
}

.brand-text {
  display: flex;
  flex-direction: column;
  line-height: 1.1;
}

.brand-title {
  font-size: 20px;
  font-weight: 800;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f5576c 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  letter-spacing: 0.5px;
}

.brand-sub {
  font-size: 11px;
  color: #8a8f98;
  letter-spacing: 2px;
  text-transform: uppercase;
  margin-top: 2px;
}

.menu {
  display: flex;
  align-items: center;
  gap: 20px;
  flex: 1;
  justify-content: flex-end;
}

.nav-list {
  list-style: none;
  display: flex;
  align-items: center;
  gap: 4px;
  margin: 0;
  padding: 6px;
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.7);
  border-radius: 999px;
  padding: 6px 8px;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.03);
}

.nav-item {
  display: flex;
}

.nav-link {
  position: relative;
  padding: 8px 16px;
  border-radius: 999px;
  color: #4a5060;
  font-weight: 500;
  font-size: 14.5px;
  text-decoration: none;
  cursor: pointer;
  transition: color 0.3s ease, background 0.3s ease, transform 0.3s ease;
  white-space: nowrap;
  user-select: none;
}

.nav-link:hover {
  color: #4c3a8b;
  background: rgba(255, 255, 255, 0.7);
  transform: translateY(-1px);
}

.nav-link.active {
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 6px 18px rgba(102, 126, 234, 0.45);
}

.search-form {
  display: flex;
}

.search-input-wrap {
  position: relative;
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.75);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 999px;
  padding: 4px 4px 4px 14px;
  box-shadow: 0 4px 14px rgba(31, 38, 135, 0.06);
  transition: box-shadow 0.3s ease, border-color 0.3s ease, background 0.3s ease;
}

.search-input-wrap:focus-within {
  box-shadow: 0 6px 22px rgba(102, 126, 234, 0.2);
  border-color: rgba(102, 126, 234, 0.5);
  background: rgba(255, 255, 255, 0.95);
}

.search-icon {
  width: 18px;
  height: 18px;
  color: #8a8f98;
  flex-shrink: 0;
}

.search-input {
  border: none;
  outline: none;
  background: transparent;
  font-size: 14px;
  padding: 8px 10px;
  width: 180px;
  color: #2d3340;
}

.search-input::placeholder {
  color: #a3a8b3;
}

.search-btn {
  border: none;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 999px;
  color: #fff;
  font-weight: 600;
  font-size: 13.5px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transition: transform 0.25s ease, box-shadow 0.25s ease, filter 0.25s ease;
}

.search-btn:hover {
  transform: translateY(-1px);
  filter: brightness(1.05);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 9px 18px;
  border-radius: 999px;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  border: none;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease, background 0.3s ease, color 0.3s ease, border-color 0.3s ease;
  white-space: nowrap;
  user-select: none;
  font-family: inherit;
}

.btn-ghost {
  background: transparent;
  color: #4a5060;
  border: 1px solid rgba(74, 80, 96, 0.2);
}

.btn-ghost:hover {
  background: rgba(255, 255, 255, 0.7);
  color: #4c3a8b;
  border-color: rgba(102, 126, 234, 0.4);
  transform: translateY(-1px);
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f5576c 100%);
  color: #fff;
  box-shadow: 0 8px 22px rgba(245, 87, 108, 0.35);
}

.btn-primary:hover {
  transform: translateY(-2px);
  filter: brightness(1.05);
  box-shadow: 0 12px 28px rgba(245, 87, 108, 0.45);
}

.btn-admin {
  background: linear-gradient(135deg, #43cea2 0%, #185a9d 100%);
  color: #fff !important;
  box-shadow: 0 8px 22px rgba(24, 90, 157, 0.35);
  font-size: 13px;
  padding: 8px 16px;
}

.btn-admin:hover {
  transform: translateY(-2px);
  filter: brightness(1.05);
  box-shadow: 0 12px 28px rgba(24, 90, 157, 0.45);
}

.user-chip {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 4px 10px 4px 4px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.75);
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 4px 14px rgba(31, 38, 135, 0.06);
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: 700;
  font-size: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.user-name {
  font-size: 13.5px;
  font-weight: 600;
  color: #2d3340;
  max-width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hamburger {
  display: none;
  flex-direction: column;
  justify-content: center;
  gap: 5px;
  width: 44px;
  height: 44px;
  padding: 0;
  border-radius: 14px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  background: rgba(255, 255, 255, 0.6);
  cursor: pointer;
  transition: background 0.3s ease, box-shadow 0.3s ease, transform 0.3s ease;
  backdrop-filter: blur(10px);
}

.hamburger:hover {
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 6px 18px rgba(31, 38, 135, 0.12);
}

.hamburger span {
  display: block;
  height: 2.5px;
  width: 22px;
  margin: 0 auto;
  background: linear-gradient(90deg, #667eea, #764ba2);
  border-radius: 2px;
  transition: transform 0.35s ease, opacity 0.25s ease;
}

.hamburger.is-open span:nth-child(1) {
  transform: translateY(7.5px) rotate(45deg);
}

.hamburger.is-open span:nth-child(2) {
  opacity: 0;
}

.hamburger.is-open span:nth-child(3) {
  transform: translateY(-7.5px) rotate(-45deg);
}

.menu-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(15, 20, 40, 0.35);
  backdrop-filter: blur(4px);
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.35s ease;
  z-index: 999;
}

.menu-backdrop.is-visible {
  opacity: 1;
  pointer-events: auto;
}

@media (max-width: 960px) {
  .hamburger {
    display: flex;
  }

  .menu {
    position: fixed;
    top: 0;
    right: 0;
    height: 100vh;
    width: min(360px, 88vw);
    max-width: 100%;
    padding: 88px 22px 28px;
    transform: translateX(105%);
    transition: transform 0.4s cubic-bezier(0.22, 1, 0.36, 1);
    flex-direction: column;
    align-items: stretch;
    justify-content: flex-start;
    gap: 18px;
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: saturate(180%) blur(24px);
    -webkit-backdrop-filter: saturate(180%) blur(24px);
    border-left: 1px solid rgba(255, 255, 255, 0.7);
    box-shadow: -20px 0 60px rgba(31, 38, 135, 0.18);
    overflow-y: auto;
    z-index: 1001;
  }

  .menu.is-open {
    transform: translateX(0);
  }

  .nav-list {
    flex-direction: column;
    align-items: stretch;
    gap: 6px;
    background: rgba(255, 255, 255, 0.5);
    padding: 10px;
    border-radius: 20px;
  }

  .nav-item {
    width: 100%;
  }

  .nav-link {
    display: block;
    padding: 12px 16px;
    border-radius: 14px;
    font-size: 15px;
  }

  .search-form {
    width: 100%;
  }

  .search-input-wrap {
    width: 100%;
    padding: 4px;
  }

  .search-input {
    flex: 1;
    width: auto;
    min-width: 0;
  }

  .actions {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .btn,
  .btn-admin {
    width: 100%;
    padding: 12px 18px;
  }

  .user-chip {
    justify-content: flex-start;
    padding: 8px 14px 8px 8px;
  }

  .user-name {
    max-width: none;
  }
}

@media (max-width: 420px) {
  .navbar-root {
    padding: 12px 16px;
  }

  .navbar-root.is-scrolled {
    padding: 8px 16px;
  }

  .brand-sub {
    display: none;
  }

  .brand-icon {
    width: 38px;
    height: 38px;
    border-radius: 12px;
  }

  .brand-title {
    font-size: 18px;
  }
}
</style>