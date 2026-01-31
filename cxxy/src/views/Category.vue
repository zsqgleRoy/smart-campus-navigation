<template>
  <div class="category-page page-container">
    <!-- 分类标签 -->
    <div class="category-tabs">
      <van-tabs v-model:active="activeTab" @change="handleTabChange" swipeable>
        <van-tab
          v-for="category in categories"
          :key="category.id"
          :title="category.name"
          :name="category.id"
        >
          <div class="website-list">
            <WebsiteCard
              v-for="website in currentWebsites"
              :key="website.id"
              :website="website"
              @click="openWebsite"
            />
            <van-empty
              v-if="currentWebsites.length === 0"
              description="暂无网站"
              image="search"
            />
          </div>
        </van-tab>
      </van-tabs>
    </div>

    <TabBar />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { categories, getWebsitesByCategory } from '@/data/websites'
import { openExternalUrl } from '@/utils/navigation'
import type { Website } from '@/types'
import TabBar from '@/components/TabBar.vue'
import WebsiteCard from '@/components/WebsiteCard.vue'

const route = useRoute()
const appStore = useAppStore()

const activeTab = ref<string>('all')
const currentWebsites = ref<Website[]>([])

onMounted(() => {
  // 从路由参数获取初始分类
  const categoryId = route.query.category as string
  if (categoryId) {
    activeTab.value = categoryId
  }
  loadWebsites()
})

const handleTabChange = () => {
  loadWebsites()
}

const loadWebsites = () => {
  currentWebsites.value = getWebsitesByCategory(activeTab.value)
}

const openWebsite = (website: Website) => {
  // 添加访问记录
  appStore.addVisitRecord({
    websiteId: website.id,
    websiteName: website.name,
    websiteUrl: website.url,
    visitTime: Date.now()
  })
  // 打开网站（兼容 Android WebView）
  openExternalUrl(website.url, '_blank')
}
</script>

<style scoped>
.category-page {
  background: var(--van-background);
}

.category-tabs {
  min-height: calc(100vh - 50px);
}

.website-list {
  padding: 16px;
}
</style>

