<template>
  <div class="website-card" @click="handleClick">
    <div class="card-icon">
      <img v-if="icon" :src="icon" :alt="name" @error="handleImageError" />
      <van-icon v-else name="link-o" size="24px" />
    </div>
    <div class="card-content">
      <div class="card-name">{{ name }}</div>
      <div v-if="description" class="card-desc">{{ description }}</div>
    </div>
    <van-icon
      v-if="showFavorite"
      :name="isFav ? 'star' : 'star-o'"
      :class="['favorite-icon', { active: isFav }]"
      @click.stop="toggleFavorite"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAppStore } from '@/stores/app'
import { getWebsiteIcon } from '@/utils/format'
import type { Website } from '@/types'

interface Props {
  website: Website
  showFavorite?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showFavorite: true
})

const emit = defineEmits<{
  click: [website: Website]
}>()

const appStore = useAppStore()
const isFav = computed(() => appStore.isFavorite(props.website.id))

const icon = computed(() => {
  return props.website.icon || getWebsiteIcon(props.website.url)
})

const { name, description } = props.website

const handleClick = () => {
  emit('click', props.website)
}

const toggleFavorite = () => {
  if (isFav.value) {
    appStore.removeFavorite(props.website.id)
  } else {
    appStore.addFavorite(props.website.id)
  }
}

const handleImageError = (e: Event) => {
  const img = e.target as HTMLImageElement
  img.style.display = 'none'
}
</script>

<style scoped>
.website-card {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: var(--van-background-2);
  border-radius: 8px;
  margin-bottom: 12px;
  min-height: 60px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
}

.website-card:active {
  transform: scale(0.98);
  background: var(--van-active-color);
}

.card-icon {
  width: 44px;
  height: 44px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--van-background);
  flex-shrink: 0;
  margin-right: 12px;
}

.card-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.card-content {
  flex: 1;
  min-width: 0;
}

.card-name {
  font-size: 16px;
  font-weight: 500;
  color: var(--van-text-color);
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-desc {
  font-size: 12px;
  color: var(--van-text-color-2);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.favorite-icon {
  font-size: 20px;
  color: var(--van-text-color-3);
  flex-shrink: 0;
  padding: 8px;
  margin: -8px;
  transition: color 0.2s;
}

.favorite-icon.active {
  color: #ffd21e;
}
</style>

