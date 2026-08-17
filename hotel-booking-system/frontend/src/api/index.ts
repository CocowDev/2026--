import axios from 'axios';
import type { ApiResponse } from '../types';

const api = axios.create({
  // 相对路径：配合 vite.config.ts 的 /api 代理，局域网内任意设备访问前端均自动转发到后端 3001
  // （若写死 localhost，其他设备访问时请求会指向设备自身导致连不上）
  baseURL: '/api',
  timeout: 10000,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('isAdmin');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (data: { username: string; password: string }) =>
    api.post<ApiResponse<{ token: string; user: any; isAdmin: boolean }>>('/auth/login', data),
  register: (data: { name: string; phone: string; email: string; password: string }) =>
    api.post<ApiResponse>('/auth/register', data),
  adminLogin: (data: { username: string; password: string }) =>
    api.post<ApiResponse<{ token: string; user: any; isAdmin: boolean }>>('/auth/admin/login', data),
};

export const bookingAPI = {
  getAll: (params?: { page?: number; pageSize?: number; status?: string; keyword?: string }) =>
    api.get<ApiResponse<{ list: any[]; total: number }>>('/bookings', { params }),
  getById: (id: number) => api.get<ApiResponse<any>>(`/bookings/${id}`),
  create: (data: any) => api.post<ApiResponse>('/bookings', data),
  update: (id: number, data: any) => api.put<ApiResponse>(`/bookings/${id}`, data),
  updateStatus: (id: number, status: string) =>
    api.patch<ApiResponse>(`/bookings/${id}/status`, { status }),
  delete: (id: number) => api.delete<ApiResponse>(`/bookings/${id}`),
  /** 个人中心：用户取消自己的待处理订单 */
  cancel: (id: number) => api.post<ApiResponse>(`/bookings/${id}/cancel`),
  /** 服务预订（SPA/健身等） */
  createService: (data: any) => api.post<ApiResponse>('/bookings/service', data),
  export: () => api.get('/bookings/export', { responseType: 'blob' }),
};

export const userAPI = {
  getAll: (params?: { page?: number; pageSize?: number }) =>
    api.get<ApiResponse<{ list: any[]; total: number }>>('/users', { params }),
  getById: (id: number) => api.get<ApiResponse<any>>(`/users/${id}`),
  update: (id: number, data: any) => api.put<ApiResponse>(`/users/${id}`, data),
  delete: (id: number) => api.delete<ApiResponse>(`/users/${id}`),
};

export const roomTypeAPI = {
  getAll: () => api.get<ApiResponse<any[]>>('/room-types'),
  getById: (id: number) => api.get<ApiResponse<any>>(`/room-types/${id}`),
};

export const restaurantAPI = {
  getAll: () => api.get<ApiResponse<any[]>>('/restaurants'),
  /** 某餐厅的菜品列表（餐饮预订第二步） */
  getDishes: (restaurantId: number) => api.get<ApiResponse<any[]>>(`/restaurants/${restaurantId}/dishes`),
  book: (data: {
    restaurantId: number;
    reservationDate: string;
    reservationTime: string;
    numberOfGuests: number;
    specialRequests: string;
    userId: number;
    dishes?: { dishId: number; quantity: number }[];
  }) => api.post<ApiResponse>('/restaurants/book', data),
};

export const dashboardAPI = {
  getStats: () => api.get<ApiResponse<any>>('/dashboard/stats'),
  getRecentBookings: () => api.get<ApiResponse<any[]>>('/dashboard/recent-bookings'),
};

export default api;