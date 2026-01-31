<template>
  <div class="search-page page-container">
    <div class="search-header">
      <van-search
        v-model="keyword"
        placeholder="搜索网站名称或描述"
        shape="round"
        autofocus
        @search="handleSearch"
        @input="handleInput"
      >
        <template #action>
          <div @click="handleSearch">搜索</div>
        </template>
      </van-search>
    </div>

    <!-- 搜索结果 -->
    <div v-if="keyword.trim()" class="search-results">
      <div v-if="searchResults.length > 0" class="results-list">
        <WebsiteCard
          v-for="website in searchResults"
          :key="website.id"
          :website="website"
          @click="openWebsite"
        />
      </div>
      <van-empty
        v-else-if="hasSearched"
        description="未找到相关网站"
        image="search"
      />
    </div>

    <!-- 搜索历史 -->
    <div v-else class="search-history">
      <div class="history-header">
        <span class="title">搜索历史</span>
        <span v-if="searchHistory.length > 0" class="clear-btn" @click="clearHistory">
          清空
        </span>
      </div>
      <div v-if="searchHistory.length > 0" class="history-tags">
        <van-tag
          v-for="(item, index) in searchHistory"
          :key="index"
          class="history-tag"
          @click="searchByHistory(item)"
        >
          {{ item }}
        </van-tag>
      </div>
      <van-empty v-else description="暂无搜索历史" image="search" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { searchWebsites } from '@/data/websites'
import { storage, STORAGE_KEYS } from '@/utils/storage'
import { openExternalUrl } from '@/utils/navigation'
import type { Website } from '@/types'
import WebsiteCard from '@/components/WebsiteCard.vue'

const route = useRoute()
const appStore = useAppStore()

const keyword = ref('')
const hasSearched = ref(false)
const searchHistory = ref<string[]>(
  storage.get<string[]>(STORAGE_KEYS.USER_PREFERENCES + '_search_history') || []
)

const searchResults = computed(() => {
  if (!keyword.value.trim()) {
    return []
  }
  return searchWebsites(keyword.value)
})

onMounted(() => {
  const queryKeyword = route.query.keyword as string
  if (queryKeyword) {
    keyword.value = queryKeyword
    handleSearch()
  }
})

const handleInput = () => {
  hasSearched.value = false
}

const handleSearch = () => {
  if (!keyword.value.trim()) {
    return
  }
  hasSearched.value = true
  
  // 添加到搜索历史
  if (!searchHistory.value.includes(keyword.value)) {
    searchHistory.value.unshift(keyword.value)
    if (searchHistory.value.length > 10) {
      searchHistory.value = searchHistory.value.slice(0, 10)
    }
    storage.set(STORAGE_KEYS.USER_PREFERENCES + '_search_history', searchHistory.value)
  }
}

const searchByHistory = (historyKeyword: string) => {
  keyword.value = historyKeyword
  handleSearch()
}

const clearHistory = () => {
  searchHistory.value = []
  storage.set(STORAGE_KEYS.USER_PREFERENCES + '_search_history', [])
}

const openWebsite = (website: Website) => {
  appStore.addVisitRecord({
    websiteId: website.id,
    websiteName: website.name,
    websiteUrl: website.url,
    visitTime: Date.now()
  })
  openExternalUrl(website.url, '_blank')
}
</script>

<style scoped>
.search-page {
  background: var(--van-background);
}

.search-header {
  background: var(--van-background);
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 8px 0;
}

.search-results {
  padding: 16px;
}

.results-list {
  animation: fadeIn 0.3s ease-out;
}

.search-history {
  padding: 16px;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: var(--van-text-color);
}

.clear-btn {
  font-size: 14px;
  color: var(--van-primary-color);
  cursor: pointer;
}

.history-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.history-tag {
  cursor: pointer;
  min-height: 32px;
  padding: 4px 12px;
}
</style>

