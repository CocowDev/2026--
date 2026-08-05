import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { authAPI } from '../api';

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '');
  const user = ref<any>(localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')!) : null);
  const isAdmin = ref(localStorage.getItem('isAdmin') === 'true');

  const isLoggedIn = computed(() => !!token.value);

  const login = async (username: string, password: string) => {
    const response = await authAPI.login({ username, password });
    token.value = response.data.data.token;
    user.value = response.data.data.user;
    isAdmin.value = response.data.data.isAdmin;
    localStorage.setItem('token', token.value);
    localStorage.setItem('user', JSON.stringify(user.value));
    localStorage.setItem('isAdmin', String(isAdmin.value));
    return response.data;
  };

  const adminLogin = async (username: string, password: string) => {
    const response = await authAPI.adminLogin({ username, password });
    token.value = response.data.data.token;
    user.value = response.data.data.user;
    isAdmin.value = response.data.data.isAdmin;
    localStorage.setItem('token', token.value);
    localStorage.setItem('user', JSON.stringify(user.value));
    localStorage.setItem('isAdmin', String(isAdmin.value));
    return response.data;
  };

  const register = async (name: string, phone: string, email: string, password: string) => {
    const response = await authAPI.register({ name, phone, email, password });
    return response.data;
  };

  // 供已通过 authAPI.adminLogin 获取到响应后，直接写入登录态（避免重复请求）
  const setAdminData = (data: { token: string; user: any; isAdmin: boolean }) => {
    token.value = data.token;
    user.value = data.user;
    isAdmin.value = data.isAdmin;
    localStorage.setItem('token', token.value);
    localStorage.setItem('user', JSON.stringify(user.value));
    localStorage.setItem('isAdmin', String(isAdmin.value));
  };

  const logout = () => {
    token.value = '';
    user.value = null;
    isAdmin.value = false;
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('isAdmin');
  };

  return {
    token,
    user,
    isAdmin,
    isLoggedIn,
    login,
    adminLogin,
    register,
    logout,
    setAdminData,
  };
});