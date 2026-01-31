<template>
  <div class="home-page page-container">
    <!-- 搜索栏 -->
    <div class="search-section">
      <van-search
        v-model="searchKeyword"
        placeholder="搜索网站名称或描述"
        shape="round"
        @search="handleSearch"
        @click-input="goToSearch"
        readonly
      />
    </div>

    <!-- 热门网站 -->
    <div v-if="hotWebsites.length > 0" class="section">
      <div class="section-title">常用网站</div>
      <div class="hot-grid">
        <div
          v-for="website in hotWebsites"
          :key="website.id"
          class="hot-item"
          @click="openWebsite(website)"
        >
          <div class="hot-icon">
            <img
              v-if="website.icon || getIcon(website.url)"
              :src="website.icon || getIcon(website.url)"
              :alt="website.name"
              @error="handleImageError"
            />
            <van-icon v-else name="link-o" size="24px" />
          </div>
          <div class="hot-name">{{ website.name }}</div>
        </div>
      </div>
    </div>

    <!-- 分类网格 -->
    <div class="section">
      <div class="section-title">网站分类</div>
      <div class="category-grid">
        <div
          v-for="category in categories"
          :key="category.id"
          class="category-item"
          @click="goToCategory(category.id)"
        >
          <div
            class="category-icon"
            :style="{ backgroundColor: category.color + '20', color: category.color }"
          >
            <van-icon :name="category.icon || 'apps-o'" size="24px" />
          </div>
          <div class="category-name">{{ category.name }}</div>
        </div>
      </div>
    </div>

    <TabBar />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { getHotWebsites, categories } from '@/data/websites'
import { getWebsiteIcon } from '@/utils/format'
import { openExternalUrl } from '@/utils/navigation'
import type { Website } from '@/types'
import TabBar from '@/components/TabBar.vue'

const router = useRouter()
const appStore = useAppStore()

const searchKeyword = ref('')
const hotWebsites = ref<Website[]>([])

onMounted(() => {
  hotWebsites.value = getHotWebsites()
  appStore.initTheme()
})

const getIcon = (url: string) => getWebsiteIcon(url)

const handleImageError = (e: Event) => {
  const img = e.target as HTMLImageElement
  img.style.display = 'none'
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      path: '/search',
      query: { keyword: searchKeyword.value }
    })
  }
}

const goToSearch = () => {
  router.push('/search')
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

const goToCategory = (categoryId: string) => {
  router.push({
    path: '/category',
    query: { category: categoryId }
  })
}
</script>

<style scoped>
.home-page {
  padding: 0 16px;
}

.search-section {
  padding: 12px 0;
  background: var(--van-background);
  position: sticky;
  top: 0;
  z-index: 100;
}

.section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--van-text-color);
  margin-bottom: 16px;
  padding-left: 4px;
}

.hot-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.hot-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.hot-item:active {
  transform: scale(0.95);
}

.hot-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  background: var(--van-background-2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.hot-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
}

.hot-name {
  font-size: 12px;
  color: var(--van-text-color);
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.category-item:active {
  transform: scale(0.95);
}

.category-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}

.category-name {
  font-size: 12px;
  color: var(--van-text-color);
  text-align: center;
}
</style>

