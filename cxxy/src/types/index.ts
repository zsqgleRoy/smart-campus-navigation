// 网站信息类型
export interface Website {
  id: string
  name: string
  url: string
  icon?: string
  category: string
  description?: string
  isHot?: boolean // 是否热门
}

// 分类信息类型
export interface Category {
  id: string
  name: string
  icon?: string
  color?: string
}

// 访问记录类型
export interface VisitRecord {
  websiteId: string
  websiteName: string
  websiteUrl: string
  visitTime: number
}

// 主题类型
export type Theme = 'light' | 'dark' | 'eye'

// 用户偏好设置
export interface UserPreferences {
  theme: Theme
  favorites: string[] // 收藏的网站ID列表
}

