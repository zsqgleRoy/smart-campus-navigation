import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { Theme, VisitRecord } from '@/types'
import { storage, STORAGE_KEYS } from '@/utils/storage'

export const useAppStore = defineStore('app', () => {
  // 主题
  const theme = ref<Theme>(
    (storage.get<Theme>(STORAGE_KEYS.THEME) as Theme) || 'light'
  )

  // 收藏列表
  const favorites = ref<string[]>(
    storage.get<string[]>(STORAGE_KEYS.FAVORITES) || []
  )

  // 访问历史
  const visitHistory = ref<VisitRecord[]>(
    storage.get<VisitRecord[]>(STORAGE_KEYS.VISIT_HISTORY) || []
  )

  // 设置主题
  function setTheme(newTheme: Theme) {
    if (newTheme) {
      theme.value = newTheme
      storage.set(STORAGE_KEYS.THEME, theme.value)
      document.documentElement.setAttribute('data-theme', theme.value)
    }
  }

  // 切换主题（保留兼容性）
  function toggleTheme() {
    const themes: Theme[] = ['light', 'dark', 'eye']
    const currentIndex = themes.indexOf(theme.value)
    const nextIndex = (currentIndex + 1) % themes.length
    const nextTheme = themes[nextIndex]
    if (nextTheme) {
      setTheme(nextTheme)
    }
  }

  // 初始化主题
  function initTheme() {
    document.documentElement.setAttribute('data-theme', theme.value)
  }

  // 添加收藏
  function addFavorite(websiteId: string) {
    if (!favorites.value.includes(websiteId)) {
      favorites.value.push(websiteId)
      storage.set(STORAGE_KEYS.FAVORITES, favorites.value)
    }
  }

  // 移除收藏
  function removeFavorite(websiteId: string) {
    const index = favorites.value.indexOf(websiteId)
    if (index > -1) {
      favorites.value.splice(index, 1)
      storage.set(STORAGE_KEYS.FAVORITES, favorites.value)
    }
  }

  // 检查是否收藏
  const isFavorite = computed(() => {
    return (websiteId: string) => favorites.value.includes(websiteId)
  })

  // 添加访问记录
  function addVisitRecord(record: VisitRecord) {
    // 移除重复记录
    visitHistory.value = visitHistory.value.filter(
      r => r.websiteId !== record.websiteId
    )
    // 添加到开头
    visitHistory.value.unshift(record)
    // 最多保留50条
    if (visitHistory.value.length > 50) {
      visitHistory.value = visitHistory.value.slice(0, 50)
    }
    storage.set(STORAGE_KEYS.VISIT_HISTORY, visitHistory.value)
  }

  // 清空访问历史
  function clearVisitHistory() {
    visitHistory.value = []
    storage.set(STORAGE_KEYS.VISIT_HISTORY, [])
  }

  // 删除单条访问记录
  function removeVisitRecord(record: VisitRecord) {
    visitHistory.value = visitHistory.value.filter(
      r => !(r.websiteId === record.websiteId && r.visitTime === record.visitTime)
    )
    storage.set(STORAGE_KEYS.VISIT_HISTORY, visitHistory.value)
  }

  // 清空所有数据
  function clearAllData() {
    favorites.value = []
    visitHistory.value = []
    storage.remove(STORAGE_KEYS.FAVORITES)
    storage.remove(STORAGE_KEYS.VISIT_HISTORY)
  }

  return {
    theme,
    favorites,
    visitHistory,
    setTheme,
    toggleTheme,
    initTheme,
    addFavorite,
    removeFavorite,
    isFavorite,
    addVisitRecord,
    clearVisitHistory,
    removeVisitRecord,
    clearAllData
  }
})

