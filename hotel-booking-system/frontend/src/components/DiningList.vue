<script setup lang="ts">
import type { Restaurant } from '../types'

defineProps<{
  restaurants: Restaurant[]
  selectedId?: number | null
}>()

const emit = defineEmits<{
  select: [restaurant: Restaurant | null]
}>()
</script>

<template>
  <div class="dining-grid">
    <div class="dining-cell" v-for="restaurant in restaurants" :key="restaurant.id">
      <div
        class="dining-card"
        :class="{ 'is-selected': selectedId === restaurant.id }"
        @click="emit('select', selectedId === restaurant.id ? null : restaurant)"
      >
        <div class="dining-media">
          <img
            :src="restaurant.imageUrl"
            class="dining-image"
            :alt="restaurant.title"
            @error="($event.target as HTMLImageElement).src = 'https://picsum.photos/seed/restaurant-fallback/600/400'"
          >
          <div class="dining-media-overlay"></div>
          <div class="dining-check" v-if="selectedId === restaurant.id">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="20 6 9 17 4 12"></polyline>
            </svg>
          </div>
        </div>

        <div class="dining-body">
          <h3 class="dining-title">{{ restaurant.title }}</h3>
          <p class="dining-description">{{ restaurant.description }}</p>

          <button
            type="button"
            class="dining-action"
            :class="{ 'is-cancel': selectedId === restaurant.id }"
            @click.stop="emit('select', selectedId === restaurant.id ? null : restaurant)"
          >
            <span class="action-label">
              {{ selectedId === restaurant.id ? '取消选择' : '选择餐厅' }}
            </span>
            <svg v-if="selectedId !== restaurant.id" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"></line>
              <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
          </button>
        </div>

        <div class="selected-glow" v-if="selectedId === restaurant.id"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dining-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 28px;
}

.dining-cell {
  min-width: 0;
}

.dining-card {
  position: relative;
  background: #ffffff;
  border-radius: 20px;
  overflow: visible;
  cursor: pointer;
  transition: transform 0.45s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.45s cubic-bezier(0.22, 1, 0.36, 1);
  box-shadow: 0 10px 30px -12px rgba(15, 23, 42, 0.18),
              0 4px 10px -4px rgba(15, 23, 42, 0.08);
  will-change: transform, box-shadow;
}

.dining-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 28px 55px -15px rgba(15, 23, 42, 0.28),
              0 12px 24px -10px rgba(236, 72, 153, 0.22);
}

.dining-card.is-selected {
  transform: translateY(-10px);
  box-shadow: 0 30px 60px -15px rgba(236, 72, 153, 0.4),
              0 15px 30px -10px rgba(251, 146, 60, 0.25);
}

.dining-media {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
  border-radius: 20px 20px 0 0;
}

.dining-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.7s cubic-bezier(0.22, 1, 0.36, 1);
  user-select: none;
  -webkit-user-drag: none;
}

.dining-card:hover .dining-image {
  transform: scale(1.08);
}

.dining-media-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0) 40%,
    rgba(0, 0, 0, 0.2) 100%
  );
  pointer-events: none;
}

.dining-check {
  position: absolute;
  top: 14px;
  left: 14px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  border-radius: 50%;
  box-shadow: 0 10px 24px -8px rgba(16, 185, 129, 0.6),
              inset 0 1px 0 rgba(255, 255, 255, 0.3);
  animation: check-pop 0.45s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.dining-check svg {
  width: 20px;
  height: 20px;
}

@keyframes check-pop {
  0% { transform: scale(0) rotate(-45deg); opacity: 0; }
  60% { transform: scale(1.15) rotate(0); opacity: 1; }
  100% { transform: scale(1) rotate(0); opacity: 1; }
}

.dining-body {
  padding: 20px 22px 22px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.dining-title {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.2px;
  line-height: 1.3;
}

.dining-description {
  margin: 0;
  font-size: 13px;
  line-height: 1.6;
  color: #64748b;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.dining-action {
  margin-top: 4px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 12px 18px;
  font-size: 14px;
  font-weight: 600;
  font-family: inherit;
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  background: linear-gradient(135deg, #f97316 0%, #ec4899 100%);
  box-shadow: 0 10px 24px -10px rgba(236, 72, 153, 0.55);
  transition: transform 0.3s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.3s ease,
              filter 0.3s ease;
}

.dining-action svg {
  width: 16px;
  height: 16px;
  transition: transform 0.3s cubic-bezier(0.22, 1, 0.36, 1);
}

.dining-action:hover {
  transform: translateY(-2px);
  box-shadow: 0 16px 30px -10px rgba(236, 72, 153, 0.7);
  filter: brightness(1.05);
}

.dining-action:hover svg {
  transform: translateX(3px);
}

.dining-action:active {
  transform: translateY(0);
}

.dining-action.is-cancel {
  background: linear-gradient(135deg, #f15f5f 0%, #e11d48 100%);
  box-shadow: 0 10px 24px -10px rgba(225, 29, 72, 0.55);
}

.dining-action.is-cancel:hover {
  box-shadow: 0 16px 30px -10px rgba(225, 29, 72, 0.7);
}

.selected-glow {
  position: absolute;
  inset: -3px;
  border-radius: 22px;
  padding: 3px;
  background: linear-gradient(135deg, #f97316 0%, #ec4899 100%);
  -webkit-mask:
    linear-gradient(#fff 0 0) content-box,
    linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
          mask-composite: exclude;
  pointer-events: none;
  animation: glow-pulse 2.8s ease-in-out infinite;
}

@keyframes glow-pulse {
  0%, 100% { opacity: 0.75; }
  50% { opacity: 1; }
}

@media (max-width: 992px) {
  .dining-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 640px) {
  .dining-grid {
    grid-template-columns: 1fr;
    gap: 22px;
  }

  .dining-media {
    height: 170px;
  }

  .dining-title {
    font-size: 17px;
  }
}
</style>