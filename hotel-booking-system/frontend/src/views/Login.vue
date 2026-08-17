<script setup lang="ts">import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { User, Lock, Eye, EyeOff, LogIn } from 'lucide-vue-next';
import { ElMessage } from 'element-plus';
const router = useRouter();
const authStore = useAuthStore();
const form = ref({
 username: '',
 password: '',
});
const showPassword = ref(false);
const loading = ref(false);
// 用户登录：后台为独立入口（/admin/login），不在用户端暴露
const handleSubmit = async () => {
 if (!form.value.username || !form.value.password) {
 ElMessage.error('请输入用户名和密码');
 return;
 }
 loading.value = true;
 try {
 await authStore.login(form.value.username, form.value.password);
 ElMessage.success('登录成功');
 router.push('/');
 }
 catch (error: any) {
 ElMessage.error(error.response?.data?.message || '登录失败');
 }
 finally {
 loading.value = false;
 }
};
</script>

<template>
  <div class="login">
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="login-icon">
            <User />
          </div>
          <h1>用户登录</h1>
          <p>欢迎登录星级酒店预订系统</p>
        </div>
        
        <form @submit.prevent="handleSubmit" class="login-form">
          <div class="form-group">
            <label>用户名</label>
            <div class="input-group">
              <User class="input-icon" />
              <input
                v-model="form.username"
                type="text"
                placeholder="请输入用户名"
                class="form-control"
              />
            </div>
          </div>
          
          <div class="form-group">
            <label>密码</label>
            <div class="input-group">
              <Lock class="input-icon" />
              <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
                class="form-control"
              />
              <button
                type="button"
                class="toggle-password"
                @click="showPassword = !showPassword"
              >
                <Eye v-if="!showPassword" />
                <EyeOff v-else />
              </button>
            </div>
          </div>
          
          <button type="submit" class="btn btn-primary btn-large" :disabled="loading">
            <LogIn class="btn-icon" />
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>
        
        <div class="login-footer">
          <div class="register-link">
            <span>还没有账号？</span>
            <button @click="router.push('/register')" class="link-btn">
              立即注册
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #10233b 0%, #1d3a5f 55%, #3a5370 100%);
}

.login-container {
  width: 100%;
  max-width: 400px;
  padding: 20px;
}

.login-card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 1rem;
  background: #c9a96a;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 1.5rem;
}

.login-header h1 {
  margin-bottom: 0.5rem;
  color: #10233b;
}

.login-header p {
  color: #666;
}

.login-form {
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.input-group {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
  width: 18px;
  height: 18px;
}

.form-control {
  width: 100%;
  padding: 12px 40px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-control:focus {
  outline: none;
  border-color: #c9a96a;
}

.toggle-password {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-icon {
  width: 18px;
  height: 18px;
}

.btn-primary {
  background: #c9a96a;
  color: #fff;
}

.btn-primary:hover:not(:disabled) {
  background: #c73e54;
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.btn-large {
  width: 100%;
  padding: 16px;
  font-size: 1.1rem;
}

.login-footer {
  text-align: center;
}

.register-link {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #666;
}

.link-btn {
  background: none;
  border: none;
  color: #c9a96a;
  cursor: pointer;
  text-decoration: underline;
}

.link-btn:hover {
  color: #a8874a;
}
</style>