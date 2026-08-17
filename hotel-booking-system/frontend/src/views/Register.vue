<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { validatePhone, validateEmail, validatePassword } from '../utils/validation';
import { User, Phone, Mail, Lock, Eye, EyeOff, UserPlus } from 'lucide-vue-next';
import { ElMessage } from 'element-plus';

const router = useRouter();
const authStore = useAuthStore();

const form = ref({
  name: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: '',
});

const showPassword = ref(false);
const loading = ref(false);
// 字段级错误提示：{ 字段名: 错误信息 }，内联显示在对应输入框下方
const errors = ref<Record<string, string>>({});

// 输入时即时清除该字段错误，提升交互反馈
const clearError = (field: string) => {
  if (errors.value[field]) {
    delete errors.value[field];
  }
};

const handleSubmit = async () => {
  errors.value = {};

  // —— 字段级校验（必填 + 格式 + 密码强度）——
  if (!form.value.name) {
    errors.value.name = '请输入姓名';
  }
  if (!form.value.phone) {
    errors.value.phone = '请输入联系电话';
  } else if (!validatePhone(form.value.phone)) {
    errors.value.phone = '手机号格式不正确';
  }
  if (!form.value.email) {
    errors.value.email = '请输入电子邮箱';
  } else if (!validateEmail(form.value.email)) {
    errors.value.email = '邮箱格式不正确';
  }
  if (!form.value.password) {
    errors.value.password = '请输入密码';
  } else {
    const pw = validatePassword(form.value.password);
    if (!pw.valid) {
      errors.value.password = pw.message;
    }
  }
  if (!form.value.confirmPassword) {
    errors.value.confirmPassword = '请再次输入密码';
  } else if (form.value.password !== form.value.confirmPassword) {
    errors.value.confirmPassword = '两次输入的密码不一致';
  }

  if (Object.keys(errors.value).length > 0) {
    return;
  }

  loading.value = true;

  try {
    await authStore.register(form.value.name, form.value.phone, form.value.email, form.value.password);
    ElMessage.success('注册成功，请登录');
    router.push('/login');
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '注册失败');
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="register">
    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <div class="register-icon">
            <UserPlus />
          </div>
          <h1>用户注册</h1>
          <p>欢迎加入星级酒店预订系统</p>
        </div>
        
        <form @submit.prevent="handleSubmit" class="register-form">
          <div class="form-group">
            <label>姓名</label>
            <div class="input-group">
              <User class="input-icon" />
              <input
                v-model="form.name"
                type="text"
                placeholder="请输入姓名"
                class="form-control"
                @input="clearError('name')"
              />
            </div>
            <div v-if="errors.name" class="field-error">{{ errors.name }}</div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label>联系电话</label>
              <div class="input-group">
                <Phone class="input-icon" />
                <input
                  v-model="form.phone"
                  type="tel"
                  placeholder="请输入电话"
                  class="form-control"
                  @input="clearError('phone')"
                />
              </div>
              <div v-if="errors.phone" class="field-error">{{ errors.phone }}</div>
            </div>
            <div class="form-group">
              <label>电子邮箱</label>
              <div class="input-group">
                <Mail class="input-icon" />
                <input
                  v-model="form.email"
                  type="email"
                  placeholder="请输入邮箱"
                  class="form-control"
                  @input="clearError('email')"
                />
              </div>
              <div v-if="errors.email" class="field-error">{{ errors.email }}</div>
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
                @input="clearError('password')"
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
            <div v-if="errors.password" class="field-error">{{ errors.password }}</div>
          </div>
          
          <div class="form-group">
            <label>确认密码</label>
            <div class="input-group">
              <Lock class="input-icon" />
              <input
                v-model="form.confirmPassword"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请再次输入密码"
                class="form-control"
                @input="clearError('confirmPassword')"
              />
            </div>
            <div v-if="errors.confirmPassword" class="field-error">{{ errors.confirmPassword }}</div>
          </div>
          
          <button type="submit" class="btn btn-primary btn-large" :disabled="loading">
            <UserPlus class="btn-icon" />
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </form>
        
        <div class="register-footer">
          <span>已有账号？</span>
          <button @click="router.push('/login')" class="link-btn">
            立即登录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #10233b 0%, #1d3a5f 55%, #3a5370 100%);
}

.register-container {
  width: 100%;
  max-width: 500px;
  padding: 20px;
}

.register-card {
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

.register-header {
  text-align: center;
  margin-bottom: 2rem;
}

.register-icon {
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

.register-header h1 {
  margin-bottom: 0.5rem;
  color: #10233b;
}

.register-header p {
  color: #666;
}

.register-form {
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

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
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

/* 字段级错误提示：金色系警示红 */
.field-error {
  margin-top: 6px;
  font-size: 12.5px;
  color: #d64545;
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

.register-footer {
  text-align: center;
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
  color: #c73e54;
}
</style>