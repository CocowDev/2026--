import { defineConfig } from 'vitest/config';
import vue from '@vitejs/plugin-vue';
import path from 'path';

/**
 * Vitest 测试配置
 * - jsdom 环境：支持 Vue 组件挂载与 DOM 交互
 * - 显式导入模式（globals: false）：测试文件内 import { describe, it, expect } from 'vitest'
 * - 测试文件约定：src 目录下 *.test.ts / *.spec.ts
 */
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    },
  },
  test: {
    environment: 'jsdom',
    globals: false,
    include: ['src/**/*.{test,spec}.ts'],
  },
});
