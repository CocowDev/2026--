<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const currentIndex = ref(0)
const lightboxOpen = ref(false)
const lightboxIndex = ref(0)
let timer: number | null = null

// 环境实景轮播：使用本地真实图片（frontend/public/images/ 内复用）
const environmentImages = [
  { url: '/images/room-double.jpg', thumb: '/images/room-double.jpg', alt: '豪华卧室', desc: '宽敞舒适，温馨入梦' },
  { url: '/images/room-family.jpg', thumb: '/images/room-family.jpg', alt: '精致浴室', desc: '精致卫浴，畅享沐浴' },
  { url: '/images/room-business.jpg', thumb: '/images/room-business.jpg', alt: '舒适客厅', desc: '起居空间，惬意享受' },
  { url: '/images/room-executive.jpg', thumb: '/images/room-executive.jpg', alt: '行政套房', desc: '尊贵套房，极致体验' },
  { url: '/images/room-ocean.jpg', thumb: '/images/room-ocean.jpg', alt: '海景房', desc: '无敌海景，尽收眼底' },
  { url: '/images/restaurant-luxury.jpg', thumb: '/images/restaurant-luxury.jpg', alt: '餐厅', desc: '精致餐饮，味蕾盛宴' },
  { url: '/images/restaurant-seafood.jpg', thumb: '/images/restaurant-seafood.jpg', alt: '健身房', desc: '专业器械，随时健身' },
  { url: '/images/restaurant-buffet.jpg', thumb: '/images/restaurant-buffet.jpg', alt: '游泳池', desc: '恒温泳池，尽情畅游' }
]

const facilityList = [
  '空调', '暖气', '免费WiFi', '有线电视', '迷你冰箱', '保险箱',
  '24小时热水', '吹风机', '浴袍', '洗漱用品', '茶包咖啡', '拖鞋',
  '书桌', '台灯', '衣柜', '衣架', '熨衣设备', '行李架',
  '叫醒服务', '洗衣服务', '客房清洁', '送餐服务', '商务中心', '会议室'
]

const nextSlide = () => {
  currentIndex.value = (currentIndex.value + 1) % environmentImages.length
}

const prevSlide = () => {
  currentIndex.value = (currentIndex.value - 1 + environmentImages.length) % environmentImages.length
}

const goToSlide = (index: number) => {
  currentIndex.value = index
}

const openLightbox = (index: number) => {
  lightboxIndex.value = index
  lightboxOpen.value = true
  if (timer) clearInterval(timer)
}

const closeLightbox = () => {
  lightboxOpen.value = false
}

const nextLightbox = () => {
  lightboxIndex.value = (lightboxIndex.value + 1) % environmentImages.length
}

const prevLightbox = () => {
  lightboxIndex.value = (lightboxIndex.value - 1 + environmentImages.length) % environmentImages.length
}

const handleKeydown = (e: KeyboardEvent) => {
  if (!lightboxOpen.value) return
  if (e.key === 'Escape') closeLightbox()
  if (e.key === 'ArrowRight') nextLightbox()
  if (e.key === 'ArrowLeft') prevLightbox()
}

onMounted(() => {
  timer = setInterval(nextSlide, 5000)
  window.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
  window.removeEventListener('keydown', handleKeydown)
})
</script>

<template>
  <div class="room-environment-page">
    <section class="hero-section">
      <div class="hero-bg"></div>
      <div class="hero-content container">
        <span class="hero-badge">
          <span class="badge-dot"></span>
          ROOMS & SUITES · 客房环境
        </span>
        <h1 class="hero-title">
          精致<span class="title-gradient">客房环境</span>
        </h1>
        <p class="hero-subtitle">
          每一间客房都经过精心设计，为您带来舒适与优雅的住宿体验
        </p>
      </div>
    </section>

    <section class="gallery-section">
      <div class="container">
        <div class="gallery-carousel">
          <div class="carousel-main">
            <img
              :src="environmentImages[currentIndex].url"
              :alt="environmentImages[currentIndex].alt"
              class="carousel-image"
              @error="(($event.target as HTMLImageElement).src = '/images/room-ocean.jpg')"
            >
            <div class="carousel-overlay">
              <div class="carousel-caption">
                <h3>{{ environmentImages[currentIndex].alt }}</h3>
                <p>{{ environmentImages[currentIndex].desc }}</p>
              </div>
            </div>
            <button class="carousel-btn prev" @click="prevSlide" aria-label="上一张">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="15 18 9 12 15 6"></polyline>
              </svg>
            </button>
            <button class="carousel-btn next" @click="nextSlide" aria-label="下一张">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <polyline points="9 18 15 12 9 6"></polyline>
              </svg>
            </button>
          </div>

          <div class="carousel-dots">
            <button
              v-for="(_, index) in environmentImages"
              :key="index"
              class="carousel-dot"
              :class="{ 'is-active': currentIndex === index }"
              @click="goToSlide(index)"
              :aria-label="`第${index + 1}张`"
            ></button>
          </div>
        </div>

        <div class="section-header center">
          <span class="section-tag">GALLERY</span>
          <h2 class="section-title">客房画廊</h2>
          <p class="section-desc">点击任意图片查看大图</p>
        </div>

        <div class="gallery-grid">
          <div
            v-for="(img, index) in environmentImages"
            :key="index"
            class="gallery-item"
            @click="openLightbox(index)"
          >
            <img
              :src="img.thumb"
              :alt="img.alt"
              class="gallery-thumb"
              @error="(($event.target as HTMLImageElement).src = '/images/room-ocean.jpg')"
            >
            <div class="gallery-overlay">
              <div class="gallery-info">
                <h4>{{ img.alt }}</h4>
                <p>{{ img.desc }}</p>
              </div>
              <div class="gallery-zoom">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="11" cy="11" r="8"></circle>
                  <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                  <line x1="11" y1="8" x2="11" y2="14"></line>
                  <line x1="8" y1="11" x2="14" y2="11"></line>
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="facilities-section">
      <div class="container">
        <div class="section-header center">
          <span class="section-tag">FACILITIES</span>
          <h2 class="section-title">客房设施与服务</h2>
          <p class="section-desc">全方位的配套设施，让您享受每一刻的舒适与便利</p>
        </div>

        <div class="facilities-wrap">
          <div class="facility-column">
            <h3 class="column-title">
              <span class="column-icon" style="background: linear-gradient(135deg, #c9a96a, #b89450);">🏨</span>
              客房设施
            </h3>
            <ul class="facility-list">
              <li v-for="(item, index) in facilityList.slice(0, 12)" :key="index">
                <span class="facility-check" style="background: linear-gradient(135deg, #c9a96a, #b89450);">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                </span>
                {{ item }}
              </li>
            </ul>
          </div>

          <div class="facility-column">
            <h3 class="column-title">
              <span class="column-icon" style="background: linear-gradient(135deg, #e6cf9a, #a8874a);">✨</span>
              客房服务
            </h3>
            <ul class="facility-list">
              <li v-for="(item, index) in facilityList.slice(12)" :key="index">
                <span class="facility-check" style="background: linear-gradient(135deg, #e6cf9a, #a8874a);">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                  </svg>
                </span>
                {{ item }}
              </li>
            </ul>

            <div class="promise-card">
              <h4>服务承诺</h4>
              <p>24小时前台服务 · 免费高速WiFi · 每日清洁 · 行李寄存</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <transition name="fade">
      <div v-if="lightboxOpen" class="lightbox-overlay" @click.self="closeLightbox">
        <button class="lightbox-close" @click="closeLightbox" aria-label="关闭">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18"></line>
            <line x1="6" y1="6" x2="18" y2="18"></line>
          </svg>
        </button>
        <button class="lightbox-nav prev" @click="prevLightbox" aria-label="上一张">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="15 18 9 12 15 6"></polyline>
          </svg>
        </button>
        <button class="lightbox-nav next" @click="nextLightbox" aria-label="下一张">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6"></polyline>
          </svg>
        </button>

        <div class="lightbox-content">
          <img
            :src="environmentImages[lightboxIndex].url"
            :alt="environmentImages[lightboxIndex].alt"
            class="lightbox-image"
            @error="(($event.target as HTMLImageElement).src = '/images/room-ocean.jpg')"
          >
          <div class="lightbox-caption">
            <h3>{{ environmentImages[lightboxIndex].alt }}</h3>
            <p>{{ environmentImages[lightboxIndex].desc }}</p>
            <span class="lightbox-counter">{{ lightboxIndex + 1 }} / {{ environmentImages.length }}</span>
          </div>
        </div>

        <div class="lightbox-thumbs">
          <button
            v-for="(img, index) in environmentImages"
            :key="index"
            class="lightbox-thumb"
            :class="{ 'is-active': lightboxIndex === index }"
            @click="lightboxIndex = index"
          >
            <img :src="img.thumb" :alt="img.alt"
              @error="(($event.target as HTMLImageElement).src = '/images/room-ocean.jpg')">
          </button>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>
.room-environment-page {
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

.gallery-section {
  padding: 80px 0 40px;
  margin-top: -60px;
  position: relative;
  z-index: 3;
}

.gallery-carousel {
  margin-bottom: 80px;
}

.carousel-main {
  position: relative;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 30px 60px -20px rgba(0, 0, 0, 0.25);
  aspect-ratio: 16 / 7;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.carousel-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, transparent 50%, rgba(0, 0, 0, 0.6) 100%);
  display: flex;
  align-items: flex-end;
  padding: 40px;
  pointer-events: none;
}

.carousel-caption {
  color: #fff;
  max-width: 500px;
}

.carousel-caption h3 {
  font-size: 28px;
  font-weight: 800;
  margin: 0 0 8px;
}

.carousel-caption p {
  font-size: 15px;
  margin: 0;
  opacity: 0.9;
}

.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #374151;
  z-index: 10;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.25);
  transition: transform 0.25s ease, background 0.25s ease;
}

.carousel-btn:hover {
  transform: translateY(-50%) scale(1.1);
  background: #fff;
}

.carousel-btn svg {
  width: 20px;
  height: 20px;
}

.carousel-btn.prev {
  left: 20px;
}

.carousel-btn.next {
  right: 20px;
}

.carousel-dots {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.carousel-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: none;
  background: rgba(184, 148, 80, 0.25);
  cursor: pointer;
  transition: background 0.3s ease, transform 0.3s ease, width 0.3s ease;
}

.carousel-dot.is-active {
  width: 28px;
  border-radius: 10px;
  background: linear-gradient(135deg, #c9a96a 0%, #e6cf9a 100%);
}

.gallery-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.gallery-item {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  aspect-ratio: 4 / 3;
  box-shadow: 0 10px 30px -15px rgba(0, 0, 0, 0.15);
  transition: transform 0.4s ease, box-shadow 0.4s ease;
}

.gallery-item:hover {
  transform: translateY(-6px);
  box-shadow: 0 24px 50px -15px rgba(0, 0, 0, 0.3);
}

.gallery-thumb {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.gallery-item:hover .gallery-thumb {
  transform: scale(1.12);
}

.gallery-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, transparent 50%, rgba(15, 23, 42, 0.85) 100%);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 20px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.gallery-item:hover .gallery-overlay {
  opacity: 1;
}

.gallery-info h4 {
  font-size: 17px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 4px;
}

.gallery-info p {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.gallery-zoom {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #374151;
  transform: scale(0.8);
  transition: transform 0.3s ease;
}

.gallery-item:hover .gallery-zoom {
  transform: scale(1);
}

.gallery-zoom svg {
  width: 20px;
  height: 20px;
}

.facilities-section {
  padding: 40px 0 120px;
}

.facilities-wrap {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
}

.facility-column {
  background: #fff;
  border-radius: 20px;
  padding: 36px;
  box-shadow: 0 10px 30px -15px rgba(0, 0, 0, 0.12);
}

.column-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 20px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f3f4f6;
}

.column-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.facility-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.facility-list li {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 15px;
  color: #374151;
  font-weight: 500;
}

.facility-check {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.facility-check svg {
  width: 14px;
  height: 14px;
}

.promise-card {
  margin-top: 28px;
  padding: 20px 24px;
  background: linear-gradient(135deg, #f5f3ff 0%, #fce7f3 100%);
  border-radius: 16px;
}

.promise-card h4 {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 6px;
}

.promise-card p {
  font-size: 13px;
  line-height: 1.6;
  color: #6b7280;
  margin: 0;
}

.lightbox-overlay {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.92);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 40px;
  z-index: 1000;
}

.lightbox-content {
  max-width: 1000px;
  width: 100%;
  max-height: 75vh;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.lightbox-image {
  max-width: 100%;
  max-height: 70vh;
  object-fit: contain;
  border-radius: 16px;
  box-shadow: 0 40px 80px rgba(0, 0, 0, 0.5);
}

.lightbox-caption {
  text-align: center;
  color: #fff;
  padding: 20px 0;
}

.lightbox-caption h3 {
  font-size: 22px;
  font-weight: 700;
  margin: 0 0 6px;
}

.lightbox-caption p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
}

.lightbox-counter {
  display: block;
  margin-top: 8px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
  letter-spacing: 1px;
}

.lightbox-close {
  position: absolute;
  top: 24px;
  right: 24px;
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: background 0.25s ease, transform 0.25s ease;
  z-index: 10;
}

.lightbox-close:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: rotate(90deg);
}

.lightbox-close svg {
  width: 22px;
  height: 22px;
}

.lightbox-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: background 0.25s ease, transform 0.25s ease;
  z-index: 10;
}

.lightbox-nav:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-50%) scale(1.1);
}

.lightbox-nav.prev {
  left: 32px;
}

.lightbox-nav.next {
  right: 32px;
}

.lightbox-nav svg {
  width: 24px;
  height: 24px;
}

.lightbox-thumbs {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  justify-content: center;
  flex-wrap: wrap;
  max-width: 800px;
}

.lightbox-thumb {
  width: 60px;
  height: 44px;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid transparent;
  padding: 0;
  cursor: pointer;
  opacity: 0.5;
  transition: opacity 0.25s ease, border-color 0.25s ease, transform 0.25s ease;
}

.lightbox-thumb img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.lightbox-thumb:hover {
  opacity: 0.85;
  transform: scale(1.08);
}

.lightbox-thumb.is-active {
  opacity: 1;
  border-color: #e6cf9a;
  box-shadow: 0 0 0 2px rgba(240, 147, 251, 0.5);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 968px) {
  .facilities-wrap {
    grid-template-columns: 1fr;
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

  .carousel-main {
    aspect-ratio: 4 / 3;
  }

  .carousel-caption h3 {
    font-size: 22px;
  }

  .carousel-btn {
    width: 40px;
    height: 40px;
  }

  .carousel-btn.prev {
    left: 12px;
  }

  .carousel-btn.next {
    right: 12px;
  }

  .gallery-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }

  .lightbox-nav.prev {
    left: 12px;
  }

  .lightbox-nav.next {
    right: 12px;
  }

  .lightbox-nav {
    width: 44px;
    height: 44px;
  }

  .facility-column {
    padding: 24px;
  }
}
</style>