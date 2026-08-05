<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authAPI } from '../../api';
import { useAuthStore } from '../../stores/auth';

const router = useRouter();
const authStore = useAuthStore();
const username = ref('admin');
const password = ref('');
const loading = ref(false);
const errorMsg = ref('');

if (authStore.isAdmin) {
  router.replace('/admin/dashboard');
}

const handleLogin = async () => {
  errorMsg.value = '';
  if (!username.value || !password.value) {
    errorMsg.value = '请输入账号和密码';
    return;
  }
  loading.value = true;
  try {
    const response = await authAPI.adminLogin({
      username: username.value,
      password: password.value
    });
    if (response.data.code === 200) {
      authStore.setAdminData(response.data.data);
      router.push('/admin/dashboard');
    } else {
      errorMsg.value = response.data.message || '登录失败';
    }
  } catch {
    errorMsg.value = '登录失败，请检查网络或账号密码';
  } finally {
    loading.value = false;
  }
};

const goBack = () => router.push('/');
</script>

<template>
  <div class="admin-login-page">
    <div class="bg-decoration">
      <div class="circle c1"></div>
      <div class="circle c2"></div>
      <div class="circle c3"></div>
    </div>
    <div class="login-card">
      <div class="login-header">
        <div class="logo-icon">🏨</div>
        <h1>酒店管理后台</h1>
        <p>Admin System · 请使用管理员账号登录</p>
      </div>
      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <div class="input-wrapper">
            <span class="input-icon">👤</span>
            <input
              v-model="username"
              type="text"
              placeholder="请输入管理员账号"
              autocomplete="username"
            />
          </div>
        </div>
        <div class="form-group">
          <div class="input-wrapper">
            <span class="input-icon">🔒</span>
            <input
              v-model="password"
              type="password"
              placeholder="请输入密码"
              autocomplete="current-password"
            />
          </div>
        </div>
        <div v-if="errorMsg" class="error-msg">
          <span>⚠️</span> {{ errorMsg }}
        </div>
        <button type="submit" class="btn-submit" :disabled="loading">
          {{ loading ? '登录中...' : '登 录' }}
        </button>
        <div class="tips">
          <span>默认账号：admin / admin123</span>
        </div>
        <button type="button" class="btn-back" @click="goBack">← 返回前台</button>
      </form>
    </div>
    <div class="copyright">© 2026 星级酒店 · 管理系统 v1.0</div>
  </div>
</template>

<style scoped>
.admin-login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #4a90e2 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}
.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.15;
  background: #fff;
}
.c1 { width: 300px; height: 300px; top: -100px; left: -100px; animation: float 6s ease-in-out infinite; }
.c2 { width: 200px; height: 200px; bottom: -50px; right: -50px; animation: float 8s ease-in-out infinite reverse; }
.c3 { width: 120px; height: 120px; top: 40%; right: 15%; animation: float 10s ease-in-out infinite; }
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

.login-card {
  width: 100%;
  max-width: 440px;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 44px 40px;
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.4);
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}
.login-header .logo-icon {
  font-size: 52px;
  margin-bottom: 12px;
  filter: drop-shadow(0 8px 20px rgba(64, 158, 255, 0.4));
}
.login-header h1 {
  font-size: 26px;
  color: #1f2d3d;
  margin: 0 0 8px;
  font-weight: 700;
  background: linear-gradient(135deg, #1f2d3d, #409eff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
.login-header p {
  color: #909399;
  font-size: 13px;
  margin: 0;
  letter-spacing: 0.5px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  border: 1.5px solid #e4e7ed;
  border-radius: 12px;
  transition: all 0.25s;
  background: #f8fafc;
}
.input-wrapper:focus-within {
  border-color: #409eff;
  background: #fff;
  box-shadow: 0 0 0 4px rgba(64, 158, 255, 0.1);
}
.input-icon {
  padding: 0 14px;
  font-size: 16px;
  opacity: 0.6;
}
.input-wrapper input {
  flex: 1;
  height: 48px;
  border: none;
  background: transparent;
  font-size: 14px;
  outline: none;
  padding-right: 14px;
}

.error-msg {
  background: #fef0f0;
  color: #f56c6c;
  padding: 10px 14px;
  border-radius: 10px;
  font-size: 13px;
  border: 1px solid #fbc4c4;
  display: flex;
  align-items: center;
  gap: 6px;
}

.btn-submit {
  height: 50px;
  background: linear-gradient(135deg, #409eff, #2a5298);
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  letter-spacing: 4px;
  transition: all 0.25s;
  margin-top: 4px;
  box-shadow: 0 8px 24px rgba(64, 158, 255, 0.35);
}
.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(64, 158, 255, 0.45);
}
.btn-submit:active:not(:disabled) { transform: translateY(0); }
.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.tips {
  text-align: center;
  font-size: 12px;
  color: #909399;
  padding: 10px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px dashed #e4e7ed;
}

.btn-back {
  background: transparent;
  border: 1.5px solid #e4e7ed;
  color: #606266;
  height: 44px;
  border-radius: 12px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}
.btn-back:hover {
  background: #f5f7fa;
  color: #409eff;
  border-color: #c6e2ff;
}

.copyright {
  margin-top: 24px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 12px;
  position: relative;
  z-index: 1;
  letter-spacing: 0.5px;
}
</style>