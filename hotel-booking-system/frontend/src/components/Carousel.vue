<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

defineProps<{
  images?: string[]
}>()

const currentIndex = ref(0)
let timer: number | null = null

const slideImages = [
  'https://picsum.photos/seed/carousel1/1200/400',
  'https://picsum.photos/seed/carousel2/1200/400',
  'https://picsum.photos/seed/carousel3/1200/400',
  'https://picsum.photos/seed/carousel4/1200/400',
  'https://picsum.photos/seed/carousel5/1200/400'
]

const slideAlts = ['豪华客房', '海景套房', '酒店大堂', '行政套房', '泳池休闲区']

const heroContent = [
  {
    title: '尊享豪华住宿体验',
    subtitle: '极致奢华 · 非凡享受',
    cta: '立即预订',
    path: '/room-booking'
  },
  {
    title: '无敌海景套房',
    subtitle: '拥抱海洋 · 悠然假期',
    cta: '探索房型',
    path: '/room-booking'
  },
  {
    title: '典雅酒店大堂',
    subtitle: '品质生活 · 始于足下',
    cta: '了解更多',
    path: '/about'
  },
  {
    title: '行政套房专属服务',
    subtitle: '商务休闲 · 完美平衡',
    cta: '预约入住',
    path: '/room-booking'
  },
  {
    title: '泳池休闲区',
    subtitle: '放松身心 · 尽情畅游',
    cta: '查看设施',
    path: '/room-environment'
  }
]

const nextSlide = () => {
  currentIndex.value = (currentIndex.value + 1) % slideImages.length
}

const prevSlide = () => {
  currentIndex.value = (currentIndex.value - 1 + slideImages.length) % slideImages.length
}

const goToSlide = (index: number) => {
  currentIndex.value = index
}

const handleCta = () => {
  const path = heroContent[currentIndex.value].path
  if (path) {
    router.push(path)
    nextTick(() => {
      window.scrollTo({ top: 0, behavior: 'smooth' })
    })
  }
}

onMounted(() => {
  timer = window.setInterval(nextSlide, 5000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<template>
  <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
    <div class="carousel-inner">
      <div
        v-for="(image, index) in slideImages"
        :key="index"
        class="carousel-item"
        :class="{ 'active': currentIndex === index }"
      >
        <img
          :src="image"
          class="d-block w-100"
          :alt="slideAlts[index]"
          style="height: 500px; object-fit: cover;"
          @error="($event.target as HTMLImageElement).src = 'https://picsum.photos/seed/hotel-fallback/1200/400'"
        >
        <div class="gradient-overlay"></div>
        <div class="hero-content">
          <div class="hero-inner">
            <span class="hero-subtitle">{{ heroContent[index].subtitle }}</span>
            <h2 class="hero-title">{{ heroContent[index].title }}</h2>
            <button class="hero-cta" @click="handleCta">
              {{ heroContent[index].cta }}
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <line x1="5" y1="12" x2="19" y2="12"></line>
                <polyline points="12 5 19 12 12 19"></polyline>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>

    <button
      class="carousel-nav carousel-nav-prev"
      type="button"
      aria-label="上一张"
      @click.stop="prevSlide"
    >
      <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="15 18 9 12 15 6"></polyline>
      </svg>
    </button>
    <button
      class="carousel-nav carousel-nav-next"
      type="button"
      aria-label="下一张"
      @click.stop="nextSlide"
    >
      <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="9 18 15 12 9 6"></polyline>
      </svg>
    </button>

    <div class="carousel-indicators">
      <button
        v-for="(_, index) in slideImages"
        :key="index"
        type="button"
        class="indicator-dot"
        :class="{ 'active': currentIndex === index }"
        :aria-label="`切换到第 ${index + 1} 张`"
        @click="goToSlide(index)"
      ></button>
    </div>
  </div>
</template>

<style scoped>
.carousel {
  position: relative;
  width: 100%;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 60px -15px rgba(0, 0, 0, 0.35);
}

.carousel-inner {
  position: relative;
  width: 100%;
  height: 500px;
  overflow: hidden;
}

.carousel-item {
  position: absolute;
  inset: 0;
  opacity: 0;
  transform: scale(1.05);
  transition: opacity 0.9s ease, transform 1.2s ease;
  pointer-events: none;
}

.carousel-item.active {
  opacity: 1;
  transform: scale(1);
  pointer-events: auto;
}

.carousel-item img {
  width: 100%;
  height: 100%;
  display: block;
  user-select: none;
  -webkit-user-drag: none;
}

.gradient-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0.1) 0%,
    rgba(0, 0, 0, 0.35) 45%,
    rgba(0, 0, 0, 0.75) 100%
  );
  pointer-events: none;
}

.hero-content {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 0 8%;
  pointer-events: none;
}

.hero-inner {
  max-width: 560px;
  color: #fff;
  transform: translateY(30px);
  opacity: 0;
  transition: all 0.9s cubic-bezier(0.25, 0.8, 0.25, 1) 0.2s;
}

.carousel-item.active .hero-inner {
  transform: translateY(0);
  opacity: 1;
}

.hero-subtitle {
  display: inline-block;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 4px;
  text-transform: uppercase;
  padding: 6px 14px;
  margin-bottom: 18px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 999px;
}

.hero-title {
  font-size: 48px;
  font-weight: 800;
  line-height: 1.15;
  margin: 0 0 24px;
  letter-spacing: -0.5px;
  text-shadow: 0 4px 24px rgba(0, 0, 0, 0.35);
}

.hero-cta {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 28px;
  font-size: 15px;
  font-weight: 600;
  color: #fff;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%);
  box-shadow: 0 10px 25px -8px rgba(139, 92, 246, 0.7);
  transition: transform 0.3s ease, box-shadow 0.3s ease, filter 0.3s ease;
  pointer-events: auto;
}

.hero-cta:hover {
  transform: translateY(-2px);
  box-shadow: 0 15px 35px -8px rgba(139, 92, 246, 0.85);
  filter: brightness(1.08);
}

.hero-cta:active {
  transform: translateY(0);
}

.carousel-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 52px;
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  color: #fff;
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.95) 0%, rgba(236, 72, 153, 0.95) 100%);
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.45),
              inset 0 1px 0 rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
  transition: all 0.3s ease;
  z-index: 5;
}

.carousel-nav:hover {
  transform: translateY(-50%) scale(1.08);
  box-shadow: 0 15px 35px -5px rgba(0, 0, 0, 0.55),
              inset 0 1px 0 rgba(255, 255, 255, 0.3);
  filter: brightness(1.1);
}

.carousel-nav:active {
  transform: translateY(-50%) scale(0.95);
}

.carousel-nav-prev {
  left: 24px;
}

.carousel-nav-next {
  right: 24px;
}

.carousel-indicators {
  position: absolute;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 14px;
  background: rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 999px;
  z-index: 5;
}

.indicator-dot {
  width: 10px;
  height: 10px;
  padding: 0;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.45);
  transition: width 0.4s cubic-bezier(0.25, 0.8, 0.25, 1),
              background 0.4s ease,
              box-shadow 0.4s ease;
}

.indicator-dot.active {
  width: 32px;
  background: linear-gradient(135deg, #6366f1 0%, #ec4899 100%);
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.55);
}

.indicator-dot:hover:not(.active) {
  background: rgba(255, 255, 255, 0.75);
}

@media (max-width: 768px) {
  .carousel-inner,
  .carousel-item img {
    height: 320px !important;
  }

  .hero-content {
    padding: 0 6%;
  }

  .hero-title {
    font-size: 30px;
    margin-bottom: 16px;
  }

  .hero-subtitle {
    font-size: 12px;
    letter-spacing: 2px;
    margin-bottom: 12px;
  }

  .hero-cta {
    padding: 11px 22px;
    font-size: 14px;
  }

  .carousel-nav {
    width: 42px;
    height: 42px;
  }

  .carousel-nav-prev {
    left: 12px;
  }

  .carousel-nav-next {
    right: 12px;
  }

  .carousel-indicators {
    bottom: 14px;
    gap: 8px;
    padding: 6px 12px;
  }

  .indicator-dot.active {
    width: 24px;
  }
}
</style>