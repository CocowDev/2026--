import { describe, it, expect, vi, beforeEach } from 'vitest';
import { shallowMount, flushPromises } from '@vue/test-utils';

/**
 * Booking 预订表单组件测试
 * 依赖 mock：roomTypeAPI（房型加载）、auth/booking store、element-plus、vue-router
 * 覆盖：房型加载渲染、选择房型后提交预订、createBooking 参数正确性
 */
const mocks = vi.hoisted(() => ({
  push: vi.fn(),
  getAll: vi.fn(),
  createBooking: vi.fn().mockResolvedValue(undefined),
  user: { id: 1, name: 'tom', phone: '13500000000', email: 'tom@test.com' },
  message: vi.fn(),
}));

vi.mock('../api', () => ({ roomTypeAPI: { getAll: mocks.getAll } }));
vi.mock('../stores/auth', () => ({
  useAuthStore: () => ({ user: mocks.user, isLoggedIn: true }),
}));
vi.mock('../stores/booking', () => ({
  useBookingStore: () => ({ createBooking: mocks.createBooking }),
}));
vi.mock('element-plus', () => ({
  ElMessage: { error: mocks.message, success: mocks.message, warning: mocks.message },
}));
vi.mock('vue-router', () => ({ useRouter: () => ({ push: mocks.push }) }));

import Booking from './Booking.vue';
import RoomList from '../components/RoomList.vue';
import type { RoomType } from '../types';

describe('Booking 预订表单', () => {
  const room: RoomType = {
    id: 1,
    title: '标准间',
    description: '舒适温馨的标准客房',
    price: 500,
    imageUrl: '/room.jpg',
    createdAt: '',
    updatedAt: '',
  };

  beforeEach(() => {
    vi.clearAllMocks();
    mocks.getAll.mockResolvedValue({ data: { data: [room] } });
  });

  it('加载并渲染房型列表（标题与价格）', async () => {
    const wrapper = shallowMount(Booking);
    await flushPromises();

    expect(mocks.getAll).toHaveBeenCalled();
    const roomList = wrapper.findComponent(RoomList);
    expect(roomList.exists()).toBe(true);
    expect(roomList.props('rooms')).toEqual([room]);
  });

  it('选择房型并填写完整信息后提交，createBooking 参数正确', async () => {
    const wrapper = shallowMount(Booking);
    await flushPromises();

    // 选中房型
    wrapper.findComponent(RoomList).vm.$emit('select', room);
    await wrapper.vm.$nextTick();

    // 填写表单（日期 + 人数 + 联系人 + 特殊要求）
    const inputs = wrapper.findAll('input');
    await inputs[0].setValue('2026-09-01'); // 入住
    await inputs[1].setValue('2026-09-03'); // 离店
    await inputs[2].setValue(2);            // 人数
    await wrapper.find('textarea').setValue('无烟房');

    // 提交
    await wrapper.find('.btn-primary').trigger('click');

    expect(mocks.createBooking).toHaveBeenCalledTimes(1);
    const payload = mocks.createBooking.mock.calls[0][0];
    expect(payload.roomTypeId).toBe(1);
    expect(payload.guestName).toBe('tom');
    expect(payload.guestPhone).toBe('13500000000');
    expect(payload.guestEmail).toBe('tom@test.com');
    expect(payload.checkInDate).toBe('2026-09-01');
    expect(payload.checkOutDate).toBe('2026-09-03');
    expect(payload.guestCount).toBe(2);
    expect(payload.specialRequests).toBe('无烟房');
    expect(mocks.push).toHaveBeenCalledWith('/');
  });

  it('未选择房型时不渲染预订表单（无提交入口）', async () => {
    const wrapper = shallowMount(Booking);
    await flushPromises();

    // 预订表单区块 v-if="selectedRoom"，未选房型时不渲染提交按钮
    expect(wrapper.find('.btn-primary').exists()).toBe(false);
  });
});
