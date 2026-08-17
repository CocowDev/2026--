import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import DiningList from './DiningList.vue';
import type { Restaurant } from '../types';

/**
 * DiningList 餐厅列表组件测试
 * 覆盖：餐厅名称/描述渲染、选中与取消选中的事件派发
 */
describe('DiningList 餐厅列表', () => {
  const restaurants: Restaurant[] = [
    { id: 1, name: '中餐厅', description: '正宗中式菜肴', imageUrl: '/a.jpg', createdAt: '', updatedAt: '' },
    { id: 2, name: '西餐厅', description: '精致西餐料理', imageUrl: '/b.jpg', createdAt: '', updatedAt: '' },
  ];

  it('渲染所有餐厅的名称与描述（后端字段 name）', () => {
    const wrapper = mount(DiningList, { props: { restaurants } });

    expect(wrapper.text()).toContain('中餐厅');
    expect(wrapper.text()).toContain('西餐厅');
    expect(wrapper.text()).toContain('正宗中式菜肴');
  });

  it('点击餐厅卡片派发 select 事件并携带对应餐厅对象', async () => {
    const wrapper = mount(DiningList, { props: { restaurants } });

    await wrapper.find('.dining-card').trigger('click');

    const emitted = wrapper.emitted('select');
    expect(emitted).toBeTruthy();
    expect(emitted![0]).toEqual([restaurants[0]]);
  });

  it('已选中卡片再次点击派发 select(null) 表示取消选择', async () => {
    const wrapper = mount(DiningList, { props: { restaurants, selectedId: 1 } });

    await wrapper.find('.dining-card').trigger('click');

    expect(wrapper.emitted('select')![0]).toEqual([null]);
  });
});
