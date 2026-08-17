<script setup lang="ts">
import type { RoomType } from '../types'

defineProps<{
  rooms: RoomType[]
  selectedId?: number | null
}>()

const emit = defineEmits<{
  select: [room: RoomType | null]
}>()
</script>

<template>
  <div class="room-grid">
    <div class="room-cell" v-for="room in rooms" :key="room.id">
      <div
        class="room-card"
        :class="{ 'is-selected': selectedId === room.id }"
        @click="emit('select', selectedId === room.id ? null : room)"
      >
        <div class="room-media">
          <img
            :src="room.imageUrl"
            class="room-image"
            :alt="room.title"
            @error="($event.target as HTMLImageElement).src = '/images/room-single.jpg'"
          >
          <div class="room-media-overlay"></div>
          <div class="room-price-tag">
            <span class="price-symbol">¥</span>
            <span class="price-value">{{ room.price }}</span>
            <span class="price-unit">/ 晚</span>
          </div>
          <div class="room-check" v-if="selectedId === room.id">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
              <polyline points="20 6 9 17 4 12"></polyline>
            </svg>
          </div>
        </div>

        <div class="room-body">
          <h3 class="room-title">{{ room.title }}</h3>
          <p class="room-description">{{ room.description }}</p>

          <button
            type="button"
            class="room-action"
            :class="{ 'is-cancel': selectedId === room.id }"
            @click.stop="emit('select', selectedId === room.id ? null : room)"
          >
            <span class="action-label">
              {{ selectedId === room.id ? '取消选择' : '选择此房型' }}
            </span>
            <svg v-if="selectedId !== room.id" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"></line>
              <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
          </button>
        </div>

        <div class="selected-glow" v-if="selectedId === room.id"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.room-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 32px;
}

.room-cell {
  min-width: 0;
}

.room-card {
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

.room-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 28px 55px -15px rgba(15, 23, 42, 0.28),
              0 12px 24px -10px rgba(99, 102, 241, 0.25);
}

.room-card.is-selected {
  transform: translateY(-10px);
  box-shadow: 0 30px 60px -15px rgba(99, 102, 241, 0.45),
              0 15px 30px -10px rgba(212, 176, 110, 0.3);
}

.room-media {
  position: relative;
  width: 100%;
  height: 220px;
  overflow: hidden;
  border-radius: 20px 20px 0 0;
}

.room-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.7s cubic-bezier(0.22, 1, 0.36, 1);
  user-select: none;
  -webkit-user-drag: none;
}

.room-card:hover .room-image {
  transform: scale(1.08);
}

.room-media-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    180deg,
    rgba(0, 0, 0, 0) 40%,
    rgba(0, 0, 0, 0.15) 100%
  );
  pointer-events: none;
}

.room-price-tag {
  position: absolute;
  top: 18px;
  right: 18px;
  display: inline-flex;
  align-items: baseline;
  gap: 2px;
  padding: 8px 16px;
  color: #fff;
  font-weight: 700;
  letter-spacing: 0.3px;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 55%, #d4b06e 100%);
  border-radius: 999px;
  box-shadow: 0 10px 24px -8px rgba(184, 148, 80, 0.55),
              inset 0 1px 0 rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
}

.room-card:hover .room-price-tag {
  transform: translateY(-2px) scale(1.03);
  box-shadow: 0 14px 28px -8px rgba(184, 148, 80, 0.7);
}

.price-symbol {
  font-size: 14px;
  font-weight: 600;
  opacity: 0.9;
}

.price-value {
  font-size: 22px;
  line-height: 1;
  font-variant-numeric: tabular-nums;
}

.price-unit {
  font-size: 12px;
  font-weight: 500;
  opacity: 0.9;
  margin-left: 4px;
}

.room-check {
  position: absolute;
  top: 18px;
  left: 18px;
  width: 42px;
  height: 42px;
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

.room-check svg {
  width: 22px;
  height: 22px;
}

@keyframes check-pop {
  0% { transform: scale(0) rotate(-45deg); opacity: 0; }
  60% { transform: scale(1.15) rotate(0); opacity: 1; }
  100% { transform: scale(1) rotate(0); opacity: 1; }
}

.room-body {
  padding: 22px 24px 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.room-title {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.2px;
  line-height: 1.3;
}

.room-description {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
  color: #64748b;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.room-action {
  margin-top: 4px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  width: 100%;
  padding: 13px 20px;
  font-size: 14px;
  font-weight: 600;
  font-family: inherit;
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 55%, #d4b06e 100%);
  box-shadow: 0 10px 24px -10px rgba(184, 148, 80, 0.6);
  transition: transform 0.3s cubic-bezier(0.22, 1, 0.36, 1),
              box-shadow 0.3s ease,
              filter 0.3s ease,
              background 0.4s ease;
}

.room-action svg {
  width: 16px;
  height: 16px;
  transition: transform 0.3s cubic-bezier(0.22, 1, 0.36, 1);
}

.room-action:hover {
  transform: translateY(-2px);
  box-shadow: 0 16px 30px -10px rgba(184, 148, 80, 0.75);
  filter: brightness(1.05);
}

.room-action:hover svg {
  transform: translateX(3px);
}

.room-action:active {
  transform: translateY(0);
}

.room-action.is-cancel {
  background: linear-gradient(135deg, #f15f5f 0%, #e11d48 100%);
  box-shadow: 0 10px 24px -10px rgba(225, 29, 72, 0.55);
}

.room-action.is-cancel:hover {
  box-shadow: 0 16px 30px -10px rgba(225, 29, 72, 0.7);
}

.selected-glow {
  position: absolute;
  inset: -3px;
  border-radius: 22px;
  padding: 3px;
  background: linear-gradient(135deg, #c9a96a 0%, #b89450 50%, #d4b06e 100%);
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

@media (max-width: 768px) {
  .room-grid {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .room-media {
    height: 200px;
  }

  .room-title {
    font-size: 18px;
  }

  .price-value {
    font-size: 20px;
  }
}
</style>