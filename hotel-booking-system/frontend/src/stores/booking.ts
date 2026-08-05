import { defineStore } from 'pinia';
import { ref } from 'vue';
import { bookingAPI } from '../api';

export const useBookingStore = defineStore('booking', () => {
  const bookings = ref<any[]>([]);
  const total = ref(0);
  const loading = ref(false);

  const getBookings = async (params?: { page?: number; pageSize?: number; status?: string; keyword?: string }) => {
    loading.value = true;
    try {
      const response = await bookingAPI.getAll(params);
      bookings.value = response.data.data.list;
      total.value = response.data.data.total;
      return response.data;
    } finally {
      loading.value = false;
    }
  };

  const getBookingById = async (id: number) => {
    const response = await bookingAPI.getById(id);
    return response.data.data;
  };

  const createBooking = async (data: any) => {
    const response = await bookingAPI.create(data);
    return response.data;
  };

  const updateBooking = async (id: number, data: any) => {
    const response = await bookingAPI.update(id, data);
    return response.data;
  };

  const updateBookingStatus = async (id: number, status: string) => {
    const response = await bookingAPI.updateStatus(id, status);
    return response.data;
  };

  const deleteBooking = async (id: number) => {
    const response = await bookingAPI.delete(id);
    return response.data;
  };

  return {
    bookings,
    total,
    loading,
    getBookings,
    getBookingById,
    createBooking,
    updateBooking,
    updateBookingStatus,
    deleteBooking,
  };
});