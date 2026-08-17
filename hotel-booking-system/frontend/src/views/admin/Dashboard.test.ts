import { describe, it, expect, vi, beforeEach } from 'vitest';
import { shallowMount, flushPromises } from '@vue/test-utils';

/**
 * Dashboard 仪表盘统计卡片测试
 * 依赖 mock：dashboardAPI（统计）、bookingAPI（近期预订）、vue-router
 * 覆盖：统计数据加载渲染（含 todayCount 今日新增与营收两位小数）、失败兜底
 */
const mocks = vi.hoisted(() => ({
  getStats: vi.fn(),
  getAll: vi.fn(),
  push: vi.fn(),
}));

vi.mock('../../api', () => ({
  dashboardAPI: { getStats: mocks.getStats },
  bookingAPI: { getAll: mocks.getAll },
}));
vi.mock('vue-router', () => ({ useRouter: () => ({ push: mocks.push }) }));

import Dashboard from './Dashboard.vue';

describe('Dashboard 仪表盘统计卡片', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    mocks.getStats.mockResolvedValue({
      data: {
        code: 200,
        data: {
          totalBookings: 10,
          pendingBookings: 3,
          confirmedBookings: 2,
          completedBookings: 1,
          todayCount: 4,
          totalRevenue: 300.5,
          activeUsers: 5,
        },
      },
    });
    mocks.getAll.mockResolvedValue({ data: { code: 200, data: { list: [], total: 0 } } });
  });

  it('加载后渲染各统计卡片数值（含今日新增与营收两位小数）', async () => {
    const wrapper = shallowMount(Dashboard);
    await flushPromises();

    const text = wrapper.text();
    // 总预订数 / 待处理 / 已确认 / 今日新增 / 注册用户
    expect(text).toContain('总预订数');
    expect(text).toContain('今日新增');
    expect(text).toContain('注册用户');
    expect(text).toContain('10');   // totalBookings
    expect(text).toContain('3');    // pendingBookings
    expect(text).toContain('2');    // confirmedBookings
    expect(text).toContain('4');    // todayCount
    expect(text).toContain('5');    // activeUsers
    // 营收按 BigDecimal 显示两位小数
    expect(text).toContain('300.50');
  });

  it('统计接口失败时页面仍可渲染且不抛错', async () => {
    mocks.getStats.mockRejectedValue(new Error('network error'));
    mocks.getAll.mockRejectedValue(new Error('network error'));

    const wrapper = shallowMount(Dashboard);
    await flushPromises();

    expect(wrapper.find('.loading').exists()).toBe(false);
  });
});
