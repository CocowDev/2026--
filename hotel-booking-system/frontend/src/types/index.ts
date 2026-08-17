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
  /** 餐厅名称：后端字段为 name（历史缺陷：前端曾用 title，已统一） */
  name: string;
  description: string;
  imageUrl: string;
  createdAt: string;
  updatedAt: string;
}

/** 餐厅菜品（餐饮预订第二步点选） */
export interface Dish {
  id: number;
  restaurantId: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  createdAt: string;
  updatedAt: string;
}

/** 服务预订请求体 */
export interface ServiceBookingForm {
  serviceName: string;
  price: number;
  serviceDate: string;
  guests: number;
  guestName: string;
  guestPhone: string;
  remark?: string;
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
  /** 预订类型：room-客房 / restaurant-餐饮 */
  type?: string;
  guestName: string;
  guestPhone: string;
  guestEmail: string;
  /** 日期格式 yyyy-MM-dd（后端 LocalDate） */
  checkInDate: string;
  checkOutDate: string;
  guestCount: number;
  specialRequests: string;
  status: 'pending' | 'confirmed' | 'cancelled' | 'completed';
  /** 金额：BigDecimal，展示时用 formatMoney 保留两位小数 */
  totalPrice: number;
  createdAt: string;
  updatedAt: string;
  // —— 后端 BookingVO 联表字段（预订列表/详情展示用）——
  userName?: string;
  userPhone?: string;
  roomTitle?: string;
  roomPrice?: number;
  roomImageUrl?: string;
  roomDescription?: string;
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