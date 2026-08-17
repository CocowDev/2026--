<script setup lang="ts">
import { useRoute } from 'vue-router';
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';
import AdminLayout from './views/admin/AdminLayout.vue';

const route = useRoute();

const isAdminRoute = () => {
  return route.path.startsWith('/admin');
};
</script>

<template>
  <div class="app">
    <template v-if="isAdminRoute()">
      <AdminLayout />
    </template>
    <template v-else>
      <Header />
      <main class="main-content">
        <router-view />
      </main>
      <Footer />
    </template>
  </div>
</template>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  /* 顶部占位：导航栏为 fixed 定位（高约 70px），防止遮挡页面首屏内容 */
  padding-top: 70px;
}
</style>