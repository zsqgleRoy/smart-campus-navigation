<template>
  <div class="favorites-page page-container">
    <van-tabs v-model:active="activeTab">
      <van-tab title="我的收藏" name="favorites">
        <div class="content-list">
          <WebsiteCard
            v-for="website in favoriteWebsites"
            :key="website.id"
            :website="website"
            @click="openWebsite"
          />
          <van-empty
            v-if="favoriteWebsites.length === 0"
            description="暂无收藏"
            image="star-o"
          />
        </div>
      </van-tab>
      <van-tab title="最近访问" name="history">
        <div class="content-list">
          <div
            v-for="record in visitHistory"
            :key="record.websiteId + record.visitTime"
            class="history-item"
            @click="openHistory(record)"
          >
            <div class="history-icon">
              <img
                v-if="getIcon(record.websiteUrl)"
                :src="getIcon(record.websiteUrl)"
                :alt="record.websiteName"
                @error="handleImageError"
              />
              <van-icon v-else name="link-o" size="20px" />
            </div>
            <div class="history-content">
              <div class="history-name">{{ record.websiteName }}</div>
              <div class="history-time">{{ formatTime(record.visitTime) }}</div>
            </div>
            <van-icon
              name="cross"
              class="delete-icon"
              @click.stop="deleteHistory(record)"
            />
          </div>
          <van-empty
            v-if="visitHistory.length === 0"
            description="暂无访问记录"
            image="clock-o"
          />
        </div>
      </van-tab>
    </van-tabs>

    <TabBar />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useAppStore } from '@/stores/app'
import { websites } from '@/data/websites'
import { getWebsiteIcon, formatTime } from '@/utils/format'
import { openExternalUrl } from '@/utils/navigation'
import type { VisitRecord } from '@/types'
import TabBar from '@/components/TabBar.vue'
import WebsiteCard from '@/components/WebsiteCard.vue'

const appStore = useAppStore()
const activeTab = ref('favorites')

const favoriteWebsites = computed(() => {
  return websites.filter(w => appStore.favorites.includes(w.id))
})

const visitHistory = computed(() => appStore.visitHistory)

const getIcon = (url: string) => getWebsiteIcon(url)

const handleImageError = (e: Event) => {
  const img = e.target as HTMLImageElement
  img.style.display = 'none'
}

const openWebsite = (website: typeof websites[0]) => {
  appStore.addVisitRecord({
    websiteId: website.id,
    websiteName: website.name,
    websiteUrl: website.url,
    visitTime: Date.now()
  })
  openExternalUrl(website.url, '_blank')
}

const openHistory = (record: VisitRecord) => {
  appStore.addVisitRecord({
    ...record,
    visitTime: Date.now()
  })
  openExternalUrl(record.websiteUrl, '_blank')
}

const deleteHistory = (record: VisitRecord) => {
  appStore.removeVisitRecord(record)
}
</script>

<style scoped>
.favorites-page {
  background: var(--van-background);
}

.content-list {
  padding: 16px;
  min-height: calc(100vh - 100px);
}

.history-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: var(--van-background-2);
  border-radius: 8px;
  margin-bottom: 12px;
  min-height: 60px;
  cursor: pointer;
  transition: all 0.2s;
}

.history-item:active {
  transform: scale(0.98);
  background: var(--van-active-color);
}

.history-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--van-background);
  flex-shrink: 0;
  margin-right: 12px;
}

.history-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.history-content {
  flex: 1;
  min-width: 0;
}

.history-name {
  font-size: 16px;
  font-weight: 500;
  color: var(--van-text-color);
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.history-time {
  font-size: 12px;
  color: var(--van-text-color-2);
}

.delete-icon {
  font-size: 18px;
  color: var(--van-text-color-3);
  flex-shrink: 0;
  padding: 8px;
  margin: -8px;
}
</style>

