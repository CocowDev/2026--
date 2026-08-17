import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
  },
  {
    path: '/room-booking',
    name: 'RoomBooking',
    component: () => import('../views/RoomBooking.vue'),
  },
  {
    path: '/dining-booking',
    name: 'DiningBooking',
    component: () => import('../views/DiningBooking.vue'),
  },
  {
    path: '/room-environment',
    name: 'RoomEnvironment',
    component: () => import('../views/RoomEnvironment.vue'),
  },
  {
    path: '/news',
    name: 'News',
    component: () => import('../views/News.vue'),
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('../views/Search.vue'),
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue'),
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../views/Contact.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guest: true },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guest: true },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { requiresAuth: true },
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('../views/admin/AdminLogin.vue'),
    // 注意：不加 guest 标记——普通用户已登录时也需能访问此页（切换到管理员身份）
  },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    redirect: '/admin/dashboard',
    meta: { requiresAdmin: true },
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('../views/admin/Dashboard.vue'),
        meta: { title: '数据概览', requiresAdmin: true },
      },
      {
        path: 'bookings',
        name: 'BookingsList',
        component: () => import('../views/admin/BookingList.vue'),
        meta: { title: '预订列表', requiresAdmin: true },
      },
      {
        path: 'bookings/:id',
        name: 'BookingDetail',
        component: () => import('../views/admin/BookingDetail.vue'),
        meta: { title: '预订详情', requiresAdmin: true },
      },
      {
        path: 'users',
        name: 'UsersManage',
        component: () => import('../views/admin/UserList.vue'),
        meta: { title: '用户列表', requiresAdmin: true },
      },
      {
        path: 'settings',
        name: 'AdminSettings',
        component: () => import('../views/admin/AdminSettings.vue'),
        meta: { title: '系统设置', requiresAdmin: true },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/',
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore();
  
  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    // 未登录或普通用户访问后台 → 引导到管理员登录页（普通用户已登录也允许进入登录页）
    next({ path: '/admin/login', query: { redirect: to.fullPath } });
  } else if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    // 需要登录的页面（如个人中心）未登录 → 跳用户登录页
    next({ path: '/login', query: { redirect: to.fullPath } });
  } else if (to.path === '/admin/login' && authStore.isAdmin) {
    // 管理员已登录 → 无需再进登录页，直接进后台
    next('/admin/dashboard');
  } else if (to.meta.guest && authStore.isLoggedIn) {
    // 已登录用户访问登录/注册页 → 回首页（/admin/login 无 guest 标记，不受此规则影响）
    next('/');
  } else {
    next();
  }
});

export default router;