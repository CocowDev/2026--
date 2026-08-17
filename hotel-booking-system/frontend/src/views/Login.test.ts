import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount } from '@vue/test-utils';

/**
 * Login 登录表单组件测试
 * 依赖 mock：vue-router（路由跳转）、stores/auth（登录动作）、element-plus（消息提示）
 * 覆盖：空表单校验、用户登录成功、管理员入口跳转（前后台入口分离）
 */
const mocks = vi.hoisted(() => ({
  push: vi.fn(),
  login: vi.fn().mockResolvedValue(undefined),
  messageError: vi.fn(),
  messageSuccess: vi.fn(),
}));

vi.mock('vue-router', () => ({ useRouter: () => ({ push: mocks.push }) }));
vi.mock('../stores/auth', () => ({
  useAuthStore: () => ({ login: mocks.login }),
}));
vi.mock('element-plus', () => ({
  ElMessage: { error: mocks.messageError, success: mocks.messageSuccess, warning: vi.fn() },
}));

import Login from './Login.vue';

describe('Login 登录表单', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('用户名或密码为空时提示错误且不发起登录', async () => {
    const wrapper = mount(Login);

    await wrapper.find('form').trigger('submit');

    expect(mocks.messageError).toHaveBeenCalledWith('请输入用户名和密码');
    expect(mocks.login).not.toHaveBeenCalled();
    expect(mocks.push).not.toHaveBeenCalled();
  });

  it('填写用户名密码后提交，调用用户登录并跳转首页', async () => {
    const wrapper = mount(Login);
    const inputs = wrapper.findAll('input');

    await inputs[0].setValue('tom');
    await inputs[1].setValue('abc123');
    await wrapper.find('form').trigger('submit');

    expect(mocks.login).toHaveBeenCalledWith('tom', 'abc123');
    expect(mocks.push).toHaveBeenCalledWith('/');
  });

  it('登录页不暴露管理后台入口（后台为独立 URL，仅管理员可见）', () => {
    const wrapper = mount(Login);

    // 页面中不应存在任何指向后台的入口（管理员入口已收敛为仅 Header 对 isAdmin 显示）
    expect(wrapper.find('.admin-link').exists()).toBe(false);
    expect(wrapper.text()).not.toContain('管理员');
  });
});
