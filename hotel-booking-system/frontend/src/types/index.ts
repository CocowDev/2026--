export interface User {
  id: number;
  name: string;
  phone: string;
  email: string;
  createdAt: string;
  updatedAt: string;
}

export interface Admin {
  id: number;
  username: string;
  createdAt: string;
  updatedAt: string;
}

export interface RoomType {
  id: number;
  title: string;
  description: string;
  price: number;
  imageUrl: string;
  createdAt: string;
  updatedAt: string;
}

export interface Restaurant {
  id: number;
  title: string;
  description: string;
  price?: number;
  imageUrl: string;
  createdAt: string;
  updatedAt: string;
}

export interface Service {
  id: number;
  name: string;
  description: string;
  price: number;
  icon: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Booking {
  id: number;
  userId: number;
  roomTypeId: number;
  guestName: string;
  guestPhone: string;
  guestEmail: string;
  checkInDate: string;
  checkOutDate: string;
  guestCount: number;
  specialRequests: string;
  status: 'pending' | 'confirmed' | 'cancelled' | 'completed';
  totalPrice: number;
  createdAt: string;
  updatedAt: string;
  user?: User;
  roomType?: RoomType;
}

export interface LoginForm {
  username: string;
  password: string;
}

export interface RegisterForm {
  name: string;
  phone: string;
  email: string;
  password: string;
}

export interface BookingForm {
  roomTypeId: number;
  guestName: string;
  guestPhone: string;
  guestEmail: string;
  checkInDate: string;
  checkOutDate: string;
  guestCount: number;
  specialRequests: string;
}

export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
}

export interface DashboardStats {
  totalBookings: number;
  pendingBookings: number;
  confirmedBookings: number;
  completedBookings: number;
  totalRevenue: number;
  activeUsers: number;
}