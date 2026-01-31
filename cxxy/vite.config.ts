import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { VitePWA } from 'vite-plugin-pwa'

// https://vite.dev/config/
export default defineConfig({
  base: './', // 使用相对路径，确保在子目录部署时也能正常工作
  plugins: [
    vue(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['favicon.ico'],
      // 开发环境禁用 Service Worker
      devOptions: {
        enabled: false,
        type: 'module'
      },
      manifest: {
        name: '校园网站导航',
        short_name: '校园导航',
        description: '校园网站导航移动端应用',
        theme_color: '#1989fa',
        icons: [
          {
            src: '/favicon.ico',
            sizes: '64x64',
            type: 'image/x-icon'
          }
        ],
        display: 'standalone',
        orientation: 'portrait'
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg}'],
        // 排除大文件，不进行预缓存
        globIgnores: ['**/seuLogo*.png'],
        // 跳过等待，立即激活
        skipWaiting: true,
        clientsClaim: true,
        // 修复 Vary header 缓存问题
        runtimeCaching: [
          {
            urlPattern: /^https?:\/\/.*/,
            handler: 'NetworkFirst',
            options: {
              cacheName: 'network-first-cache',
              networkTimeoutSeconds: 3,
              cacheableResponse: {
                statuses: [0, 200]
              },
              // 忽略 Vary header
              matchOptions: {
                ignoreVary: true
              }
            }
          }
        ],
        // 禁用某些可能导致问题的功能
        navigateFallback: null,
        navigateFallbackDenylist: [/^\/_/, /\/[^/?]+\.[^/]+$/]
      }
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  build: {
    // 确保资源路径正确
    assetsDir: 'assets',
    rollupOptions: {
      output: {
        // 确保资源文件名包含哈希
        assetFileNames: 'assets/[name]-[hash][extname]',
        chunkFileNames: 'assets/[name]-[hash].js',
        entryFileNames: 'assets/[name]-[hash].js'
      }
    }
  },
  preview: {
    port: 4173,
    host: true,
    open: true
  }
})
