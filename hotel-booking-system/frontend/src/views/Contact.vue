<script setup lang="ts">
import { ref } from 'vue'

interface FormErrors {
  name?: string
  email?: string
  phone?: string
  message?: string
}

const name = ref('')
const email = ref('')
const phone = ref('')
const message = ref('')
const errors = ref<FormErrors>({})
const submitMessage = ref('')
// 提交加载态：模拟提交延迟，按钮禁用防重复提交
const loading = ref(false)

const contactCards = [
  {
    title: '酒店地址',
    icon: '🏨',
    gradient: 'linear-gradient(135deg, #c9a96a, #b89450)',
    details: ['市中心繁华商业区XX路XX号', '距火车站 5 公里', '距机场 25 公里']
  },
  {
    title: '联系电话',
    icon: '📞',
    gradient: 'linear-gradient(135deg, #e6cf9a, #a8874a)',
    details: ['前台：400-888-8888', '预订：400-888-8889', '餐饮：400-888-8890']
  },
  {
    title: '电子邮箱',
    icon: '📧',
    gradient: 'linear-gradient(135deg, #4facfe, #00f2fe)',
    details: ['前台：reception@hotel.com', '预订：booking@hotel.com', '餐饮：dining@hotel.com']
  },
  {
    title: '营业时间',
    icon: '⏰',
    gradient: 'linear-gradient(135deg, #43e97b, #38f9d7)',
    details: ['前台：24小时', '餐厅：11:00 - 22:00', '健身房：06:00 - 22:00']
  }
]

const validateEmail = (email: string) => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}

const validatePhone = (phone: string) => {
  return /^1[3-9]\d{9}$/.test(phone)
}

const handleSubmit = async () => {
  errors.value = {}
  submitMessage.value = ''

  if (!name.value) {
    errors.value.name = '请输入姓名'
  }

  if (!email.value || !validateEmail(email.value)) {
    errors.value.email = '请输入有效的邮箱地址'
  }

  if (phone.value && !validatePhone(phone.value)) {
    errors.value.phone = '请输入有效的手机号码'
  }

  if (!message.value) {
    errors.value.message = '请输入留言内容'
  }

  if (Object.keys(errors.value).length > 0) {
    return
  }

  // 模拟提交：加载态反馈，防重复提交
  loading.value = true
  await new Promise(resolve => setTimeout(resolve, 800))
  loading.value = false

  submitMessage.value = '感谢您的留言，我们会尽快回复您！'
  name.value = ''
  email.value = ''
  phone.value = ''
  message.value = ''
}
</script>

<template>
  <div class="contact-page">
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content container">
        <span class="hero-badge">
          <span class="badge-dot"></span>
          CONTACT US · 联系我们
        </span>
        <h1 class="hero-title">
          与我们<span class="title-gradient">取得联系</span>
        </h1>
        <p class="hero-subtitle">
          无论是预订咨询、服务反馈还是合作洽谈，我们的团队都随时为您提供贴心服务
        </p>
      </div>
    </section>

    <section class="info-section">
      <div class="container">
        <div class="info-grid">
          <div
            v-for="(card, index) in contactCards"
            :key="index"
            class="info-card"
          >
            <div class="info-icon" :style="{ background: card.gradient }">
              <span>{{ card.icon }}</span>
            </div>
            <h3 class="info-title">{{ card.title }}</h3>
            <ul class="info-list">
              <li v-for="(detail, i) in card.details" :key="i">{{ detail }}</li>
            </ul>
            <div class="info-shine"></div>
          </div>
        </div>
      </div>
    </section>

    <section class="form-section">
      <div class="container">
        <div class="form-card">
          <div class="form-intro">
            <span class="section-tag">在线留言</span>
            <h2 class="form-title">给我们留言</h2>
            <p class="form-desc">
              填写下方表单，我们将在 24 小时内回复您的留言
            </p>

            <div class="form-features">
              <div class="feature-item">
                <div class="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                </div>
                <span>24小时内回复</span>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                </div>
                <span>专业团队服务</span>
              </div>
              <div class="feature-item">
                <div class="feature-icon">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                </div>
                <span>信息严格保密</span>
              </div>
            </div>
          </div>

          <form class="contact-form" @submit.prevent="handleSubmit">
            <div v-if="submitMessage" class="alert-success">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22 4 12 14.01 9 11.01"></polyline>
              </svg>
              {{ submitMessage }}
            </div>

            <div class="form-row">
              <div class="form-group">
                <label for="name">姓名</label>
                <input
                  type="text"
                  id="name"
                  v-model="name"
                  class="form-input"
                  :class="{ 'is-invalid': errors.name }"
                  placeholder="请输入您的姓名"
                >
                <div v-if="errors.name" class="error-msg">{{ errors.name }}</div>
              </div>

              <div class="form-group">
                <label for="email">邮箱</label>
                <input
                  type="email"
                  id="email"
                  v-model="email"
                  class="form-input"
                  :class="{ 'is-invalid': errors.email }"
                  placeholder="请输入您的邮箱"
                >
                <div v-if="errors.email" class="error-msg">{{ errors.email }}</div>
              </div>
            </div>

            <div class="form-group">
              <label for="phone">电话 <span class="optional-tag">(选填)</span></label>
              <input
                type="tel"
                id="phone"
                v-model="phone"
                class="form-input"
                :class="{ 'is-invalid': errors.phone }"
                placeholder="请输入您的电话号码"
              >
              <div v-if="errors.phone" class="error-msg">{{ errors.phone }}</div>
            </div>

            <div class="form-group">
              <label for="message">留言内容</label>
              <textarea
                id="message"
                v-model="message"
                class="form-input form-textarea"
                :class="{ 'is-invalid': errors.message }"
                rows="5"
                placeholder="请输入您的留言"
              ></textarea>
              <div v-if="errors.message" class="error-msg">{{ errors.message }}</div>
            </div>

            <button type="submit" class="submit-btn" :disabled="loading">
              {{ loading ? '提交中...' : '提交留言' }}
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="5" y1="12" x2="19" y2="12"></line>
                <polyline points="12 5 19 12 12 19"></polyline>
              </svg>
            </button>
          </form>
        </div>
      </div>
    </section>

    <section class="map-section">
      <div class="container">
        <div class="section-header center">
          <span class="section-tag">LOCATION</span>
          <h2 class="section-title">到访我们</h2>
          <p class="section-desc">欢迎您亲临酒店，体验豪华住宿的非凡魅力</p>
        </div>

        <div class="map-card">
          <div class="map-placeholder">
            <div class="map-pin">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5a2.5 2.5 0 010-5 2.5 2.5 0 010 5z"/>
              </svg>
            </div>
            <div class="map-info">
              <h4>星级酒店</h4>
              <p>市中心繁华商业区XX路XX号</p>
              <p class="map-hint">📍 此处显示酒店位置地图</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.contact-page {
  min-height: 100vh;
  background: #fafbfc;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC',
    'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  overflow-x: hidden;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.section-header {
  margin-bottom: 48px;
  max-width: 600px;
}

.section-header.center {
  margin-left: auto;
  margin-right: auto;
  text-align: center;
}

.section-tag {
  display: inline-block;
  padding: 6px 14px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  text-transform: uppercase;
  color: #b89450;
  background: rgba(184, 148, 80, 0.1);
  border-radius: 999px;
  margin-bottom: 16px;
}

.section-title {
  font-size: 38px;
  font-weight: 800;
  color: #1f2937;
  margin: 0 0 16px;
  letter-spacing: -0.5px;
}

.section-desc {
  font-size: 16px;
  line-height: 1.7;
  color: #6b7280;
  margin: 0;
}

.hero-section {
  position: relative;
  padding: 84px 24px 100px;
  overflow: hidden;
  background: linear-gradient(135deg, #0e1c2e 0%, #1c3350 55%, #3a5370 100%);
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 30%, rgba(255, 255, 255, 0.35) 0%, transparent 45%),
    radial-gradient(circle at 80% 70%, rgba(212, 176, 110, 0.3) 0%, transparent 45%);
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
  color: #fff;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 18px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 999px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  margin-bottom: 24px;
}

.badge-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ffd700;
  box-shadow: 0 0 12px rgba(255, 215, 0, 0.8);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.3); opacity: 0.7; }
}

.hero-title {
  font-size: 56px;
  font-weight: 800;
  line-height: 1.15;
  margin: 0 0 20px;
  letter-spacing: -1px;
  color: #fff;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.title-gradient {
  background: linear-gradient(135deg, #ffd700 0%, #ffed4e 50%, #fff5b3 100%);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  display: inline-block;
}

.hero-subtitle {
  font-size: 18px;
  line-height: 1.7;
  margin: 0;
  color: rgba(255, 255, 255, 0.92);
  max-width: 640px;
  margin-left: auto;
  margin-right: auto;
}

.info-section {
  padding: 80px 0;
  margin-top: -60px;
  position: relative;
  z-index: 3;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 24px;
}

.info-card {
  position: relative;
  padding: 36px 28px;
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 30px -15px rgba(0, 0, 0, 0.12);
  transition: transform 0.4s cubic-bezier(0.25, 0.8, 0.25, 1),
              box-shadow 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.info-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 30px 60px -20px rgba(0, 0, 0, 0.22);
}

.info-icon {
  width: 64px;
  height: 64px;
  margin-bottom: 20px;
  border-radius: 20px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
  box-shadow: 0 12px 24px -8px rgba(99, 102, 241, 0.4);
  transition: transform 0.4s ease;
}

.info-card:hover .info-icon {
  transform: scale(1.08) rotate(-5deg);
}

.info-title {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 14px;
}

.info-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-list li {
  font-size: 14px;
  line-height: 1.5;
  color: #6b7280;
  padding-left: 16px;
  position: relative;
}

.info-list li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #b89450;
  font-weight: 700;
}

.info-shine {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent 30%,
    rgba(255, 255, 255, 0.4) 50%,
    transparent 70%
  );
  transform: translateX(-100%);
  transition: transform 0.8s ease;
  pointer-events: none;
}

.info-card:hover .info-shine {
  transform: translateX(100%);
}

.form-section {
  padding: 40px 0 80px;
}

.form-card {
  background: #fff;
  border-radius: 24px;
  padding: 48px;
  box-shadow: 0 20px 60px -20px rgba(0, 0, 0, 0.12);
  display: grid;
  grid-template-columns: 1fr 1.4fr;
  gap: 48px;
}

.form-intro {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-right: 24px;
  border-right: 1px solid #f3f4f6;
}

.form-title {
  font-size: 32px;
  font-weight: 800;
  color: #1f2937;
  margin: 16px 0 12px;
  line-height: 1.2;
}

.form-desc {
  font-size: 15px;
  line-height: 1.7;
  color: #6b7280;
  margin: 0 0 28px;
}

.form-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #4b5563;
  font-weight: 500;
}

.feature-icon {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #c9a96a, #b89450);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.feature-icon svg {
  width: 16px;
  height: 16px;
}

.contact-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.alert-success {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 18px;
  background: linear-gradient(135deg, #d1fae5, #a7f3d0);
  border-radius: 14px;
  color: #065f46;
  font-size: 14px;
  font-weight: 500;
  animation: slideDown 0.4s ease;
}

.alert-success svg {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
}

@keyframes slideDown {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.optional-tag {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 400;
}

.form-input {
  padding: 12px 16px;
  border: 1.5px solid #e5e7eb;
  border-radius: 14px;
  font-size: 14px;
  color: #1f2937;
  background: #fafbfc;
  font-family: inherit;
  transition: border-color 0.3s ease, box-shadow 0.3s ease, background 0.3s ease;
  outline: none;
}

.form-input::placeholder {
  color: #9ca3af;
}

.form-input:focus {
  border-color: rgba(184, 148, 80, 0.5);
  background: #fff;
  box-shadow: 0 0 0 4px rgba(184, 148, 80, 0.1);
}

.form-input.is-invalid {
  border-color: #f87171;
  background: #fef2f2;
}

.form-textarea {
  resize: vertical;
  min-height: 120px;
  line-height: 1.6;
}

.error-msg {
  font-size: 13px;
  color: #ef4444;
  margin-top: 2px;
}

.submit-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 16px 32px;
  font-size: 15px;
  font-weight: 600;
  color: #fff;
  border: none;
  border-radius: 14px;
  cursor: pointer;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 50%, #e6cf9a 100%);
  box-shadow: 0 10px 24px rgba(184, 148, 80, 0.4);
  transition: transform 0.25s ease, box-shadow 0.25s ease, filter 0.25s ease;
  align-self: flex-start;
}

.submit-btn svg {
  width: 18px;
  height: 18px;
  transition: transform 0.25s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  filter: brightness(1.08);
  box-shadow: 0 14px 32px rgba(184, 148, 80, 0.55);
}

.submit-btn:hover svg {
  transform: translateX(4px);
}

/* 提交加载态：禁用 + 降透明度 */
.submit-btn:disabled {
  opacity: 0.65;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.map-section {
  padding: 40px 0 120px;
}

.map-card {
  background: #fff;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 20px 60px -20px rgba(0, 0, 0, 0.12);
}

.map-placeholder {
  position: relative;
  height: 360px;
  background:
    linear-gradient(135deg, rgba(201, 169, 106, 0.08) 0%, rgba(240, 147, 251, 0.08) 100%),
    repeating-linear-gradient(0deg, transparent, transparent 40px, rgba(184, 148, 80, 0.06) 40px, rgba(184, 148, 80, 0.06) 41px),
    repeating-linear-gradient(90deg, transparent, transparent 40px, rgba(184, 148, 80, 0.06) 40px, rgba(184, 148, 80, 0.06) 41px);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 20px;
}

.map-pin {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #c9a96a, #b89450, #e6cf9a);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  box-shadow: 0 16px 36px rgba(184, 148, 80, 0.45);
  animation: bounce 2s infinite;
}

.map-pin svg {
  width: 32px;
  height: 32px;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.map-info {
  text-align: center;
}

.map-info h4 {
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 6px;
}

.map-info p {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 4px;
}

.map-hint {
  font-size: 13px !important;
  color: #9ca3af !important;
  font-style: italic;
}

@media (max-width: 968px) {
  .form-card {
    grid-template-columns: 1fr;
    padding: 32px;
  }

  .form-intro {
    padding-right: 0;
    padding-bottom: 24px;
    border-right: none;
    border-bottom: 1px solid #f3f4f6;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 100px 20px 120px;
  }

  .hero-title {
    font-size: 38px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .section-title {
    font-size: 28px;
  }

  .form-card {
    padding: 24px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .submit-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>