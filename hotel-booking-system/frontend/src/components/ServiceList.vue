<script setup lang="ts">
import type { Service } from '../types'

defineProps<{
  services: Service[]
}>()

const emit = defineEmits<{
  book: [service: Service]
}>()
</script>

<template>
  <div class="service-grid">
    <div class="service-cell" v-for="service in services" :key="service.id">
      <div class="service-card">
        <div class="service-icon-wrap">
          <span class="service-icon">{{ service.icon }}</span>
          <div class="service-icon-glow"></div>
        </div>

        <div class="service-body">
          <h3 class="service-name">{{ service.name }}</h3>
          <p class="service-description">{{ service.description }}</p>

          <div class="service-price">
            <span class="price-symbol">¥</span>
            <span class="price-value">{{ service.price }}</span>
          </div>

          <button
            type="button"
            class="service-action"
            @click="emit('book', service)"
          >
            <span class="action-label">预约服务</span>
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"></line>
              <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.service-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 22px;
}

.service-cell {
  min-width: 0;
}

.service-card {
  position: relative;
  background: linear-gradient(180deg, #ffffff 0%, #fafbff 100%);
  border-radius: 22px;
  padding: 24px 22px 22px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.5s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.5s cubic-bezier(0.22, 1, 0.36, 1);
  box-shadow: 0 10px 30px -12px rgba(15, 23, 42, 0.15),
              0 4px 10px -4px rgba(15, 23, 42, 0.06);
  overflow: hidden;
  will-change: transform, box-shadow;
}

.service-card::before {
  content: '';
  position: absolute;
  top: -60%;
  left: -20%;
  width: 140%;
  height: 140%;
  background: radial-gradient(
    circle at center,
    rgba(184, 148, 80, 0.12) 0%,
    rgba(212, 176, 110, 0.08) 40%,
    rgba(255, 255, 255, 0) 70%
  );
  opacity: 0;
  transition: opacity 0.5s ease, transform 0.8s cubic-bezier(0.22, 1, 0.36, 1);
  pointer-events: none;
  z-index: 0;
}

.service-card:hover {
  transform: translateY(-12px);
  box-shadow: 0 32px 60px -15px rgba(99, 102, 241, 0.28),
              0 16px 32px -10px rgba(212, 176, 110, 0.18);
}

.service-card:hover::before {
  opacity: 1;
  transform: translateY(10%) scale(1.05);
}

.service-icon-wrap {
  position: relative;
  width: 72px;
  height: 72px;
  margin: 0 auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 20px;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 55%, #d4b06e 100%);
  box-shadow: 0 14px 30px -10px rgba(184, 148, 80, 0.55),
              inset 0 1px 0 rgba(255, 255, 255, 0.25);
  transition: transform 0.5s cubic-bezier(0.34, 1.56, 0.64, 1),
              box-shadow 0.5s ease;
  z-index: 1;
}

.service-icon-glow {
  position: absolute;
  inset: -8px;
  border-radius: 24px;
  background: linear-gradient(135deg, #c9a96a 0%, #d4b06e 100%);
  opacity: 0.25;
  filter: blur(18px);
  z-index: -1;
  transition: opacity 0.5s ease;
}

.service-card:hover .service-icon-wrap {
  transform: translateY(-4px) scale(1.05) rotate(-2deg);
  box-shadow: 0 20px 40px -10px rgba(184, 148, 80, 0.7),
              inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.service-card:hover .service-icon-glow {
  opacity: 0.45;
}

.service-icon {
  font-size: 34px;
  line-height: 1;
  filter: drop-shadow(0 4px 10px rgba(0, 0, 0, 0.2));
  transition: transform 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.service-card:hover .service-icon {
  transform: scale(1.1);
}

.service-body {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.service-name {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.2px;
  line-height: 1.3;
}

.service-description {
  margin: 0;
  font-size: 13px;
  line-height: 1.65;
  color: #64748b;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.service-price {
  display: inline-flex;
  align-items: baseline;
  gap: 2px;
  padding: 6px 14px;
  color: #fff;
  font-weight: 700;
  letter-spacing: 0.3px;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 55%, #d4b06e 100%);
  border-radius: 999px;
  box-shadow: 0 8px 20px -8px rgba(184, 148, 80, 0.5);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
}

.service-card:hover .service-price {
  transform: translateY(-2px);
  box-shadow: 0 12px 24px -8px rgba(184, 148, 80, 0.7);
}

.service-price .price-symbol {
  font-size: 13px;
  font-weight: 600;
  opacity: 0.9;
}

.service-price .price-value {
  font-size: 20px;
  line-height: 1;
  font-variant-numeric: tabular-nums;
}

.service-action {
  margin-top: 6px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 28px;
  font-size: 14px;
  font-weight: 600;
  font-family: inherit;
  color: #fff;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 55%, #d4b06e 100%);
  box-shadow: 0 10px 24px -10px rgba(184, 148, 80, 0.6);
  transition: transform 0.3s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.3s ease,
              filter 0.3s ease;
}

.service-action svg {
  width: 16px;
  height: 16px;
  transition: transform 0.3s cubic-bezier(0.22, 1, 0.36, 1);
}

.service-action:hover {
  transform: translateY(-2px);
  box-shadow: 0 18px 34px -10px rgba(184, 148, 80, 0.8);
  filter: brightness(1.06);
}

.service-action:hover svg {
  transform: translateX(3px);
}

.service-action:active {
  transform: translateY(0);
}

@media (max-width: 992px) {
  .service-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 640px) {
  .service-grid {
    grid-template-columns: 1fr;
    gap: 22px;
  }

  .service-card {
    padding: 28px 22px 24px;
  }

  .service-name {
    font-size: 17px;
  }
}
</style>