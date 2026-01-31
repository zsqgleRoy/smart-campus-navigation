// localStorage 工具函数
export const storage = {
  // 设置值
  set<T>(key: string, value: T): void {
    try {
      const serialized = JSON.stringify(value)
      localStorage.setItem(key, serialized)
    } catch (error) {
      console.error('Storage set error:', error)
    }
  },

  // 获取值
  get<T>(key: string, defaultValue?: T): T | null {
    try {
      const item = localStorage.getItem(key)
      if (item === null) {
        return defaultValue ?? null
      }
      return JSON.parse(item) as T
    } catch (error) {
      console.error('Storage get error:', error)
      return defaultValue ?? null
    }
  },

  // 删除值
  remove(key: string): void {
    try {
      localStorage.removeItem(key)
    } catch (error) {
      console.error('Storage remove error:', error)
    }
  },

  // 清空所有
  clear(): void {
    try {
      localStorage.clear()
    } catch (error) {
      console.error('Storage clear error:', error)
    }
  }
}

// 存储键名常量
export const STORAGE_KEYS = {
  FAVORITES: 'campus_nav_favorites',
  VISIT_HISTORY: 'campus_nav_visit_history',
  THEME: 'campus_nav_theme',
  USER_PREFERENCES: 'campus_nav_preferences'
} as const

