import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'

// Vant UI
import Vant from 'vant'
import 'vant/lib/index.css'

// 样式
import './styles/common.css'

// 移动端触摸模拟（开发环境）
if (import.meta.env.DEV) {
  import('@vant/touch-emulator').catch(() => {
    // 忽略导入错误
  })
}

// 错误处理
window.addEventListener('error', (event) => {
  console.error('Global error:', event.error)
})

window.addEventListener('unhandledrejection', (event) => {
  console.error('Unhandled promise rejection:', event.reason)
})

try {
  const app = createApp(App)
  const pinia = createPinia()

  app.use(pinia)
  app.use(router)
  app.use(Vant)

  app.mount('#app')
  
  console.log('App mounted successfully')
} catch (error) {
  console.error('Failed to mount app:', error)
  // 显示错误信息
  document.body.innerHTML = `
    <div style="padding: 20px; text-align: center;">
      <h2>应用加载失败</h2>
      <p>${error instanceof Error ? error.message : String(error)}</p>
      <p>请检查浏览器控制台获取更多信息</p>
    </div>
  `
}
