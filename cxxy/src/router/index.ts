import { createRouter, createWebHistory, createWebHashHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/category',
    name: 'Category',
    component: () => import('@/views/Category.vue'),
    meta: { title: '分类' }
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('@/views/Favorites.vue'),
    meta: { title: '收藏' }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('@/views/Settings.vue'),
    meta: { title: '设置' }
  },
  {
    path: '/search',
    name: 'Search',
    component: () => import('@/views/Search.vue'),
    meta: { title: '搜索' }
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('@/views/About.vue'),
    meta: { title: '关于' }
  }
]

// 检测是否在 Android WebView 或 file:// 协议下
const isFileProtocol = () => {
  if (typeof window === 'undefined') return false
  return window.location.protocol === 'file:' || 
         window.location.href.includes('android_asset') ||
         window.location.href.includes('file://')
}

// 根据环境选择路由模式
// Android WebView 使用 Hash 模式，其他环境使用 History 模式
const useHashMode = isFileProtocol()

const router = createRouter({
  history: useHashMode ? createWebHashHistory() : createWebHistory('./'),
  routes
})

// 路由守卫 - 设置页面标题
router.beforeEach((to, from, next) => {
  console.log('Navigating to:', to.path, 'Hash mode:', useHashMode)
  // 修复 Android WebView 中导航到 /index.html 的问题
  if (to.path === '/index.html' || to.path.endsWith('index.html')) {
    next('/')
    return
  }
  document.title = (to.meta.title as string) || '校园网站导航'
  next()
})

router.onError((error) => {
  console.error('Router error:', error)
})

export default router

