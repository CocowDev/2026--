import axios from 'axios';
import type { ApiResponse } from '../types';

const api = axios.create({
  baseURL: 'http://localhost:3001/api',
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
  book: (data: {
    restaurantId: number;
    reservationDate: string;
    reservationTime: string;
    numberOfGuests: number;
    specialRequests: string;
    userId: number;
  }) => api.post<ApiResponse>('/restaurants/book', data),
};

export const dashboardAPI = {
  getStats: () => api.get<ApiResponse<any>>('/dashboard/stats'),
  getRecentBookings: () => api.get<ApiResponse<any[]>>('/dashboard/recent-bookings'),
};

export default api;