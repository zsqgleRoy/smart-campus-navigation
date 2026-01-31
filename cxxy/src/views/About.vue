<template>
  <div class="about-page page-container">
    <van-nav-bar title="关于" left-arrow @click-left="goBack" />

    <div class="about-content">
      <!-- 应用信息 -->
      <div class="info-section">
        <div class="app-logo">
          <img src="@/assets/seuLogo.png" alt="SEU Logo" />
        </div>
        <div class="app-name">数智成贤</div>
        <div class="app-version">v37.11.105</div>
        <div class="app-desc">
          一个专为移动端设计的校园网站导航应用，帮助师生快速访问常用网站。
        </div>
      </div>

      <!-- 功能特点 -->
      <van-cell-group class="feature-section">
        <van-cell title="功能特点" />
        <van-cell title="快速搜索和分类浏览" icon="search" />
        <van-cell title="收藏常用网站" icon="star-o" />
        <van-cell title="访问历史记录" icon="clock-o" />
        <van-cell title="支持PWA离线访问" icon="wifi-o" />
      </van-cell-group>

      <!-- 操作按钮 -->
      <div class="action-section">
        <van-button
          type="primary"
          block
          icon="replay"
          @click="checkUpdate"
        >
          检测更新
        </van-button>
      </div>

      <!-- 其他信息 -->
      <div class="other-section">
        <van-cell-group>
          <van-cell title="作者主页" is-link @click="openAuthorPage">
            <template #right-icon>
              <van-icon name="arrow" />
            </template>
          </van-cell>
        </van-cell-group>
      </div>

      <!-- 版权信息 -->
      <div class="copyright">
        <div>© 2026 数智成贤 版权所有</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { showToast, showLoadingToast, closeToast } from 'vant'
import { useRouter } from 'vue-router'
import { openExternalUrl } from '@/utils/navigation'

const router = useRouter()

const goBack = () => {
  router.back()
}

const checkUpdate = () => {
  showLoadingToast({
    message: '正在检测更新...',
    duration: 0,
    forbidClick: true
  })

  // 模拟检测更新，1秒后跳转
  setTimeout(() => {
    closeToast()
    // 跳转到更新页面（兼容 Android WebView）
    openExternalUrl('https://pan.baidu.com/s/1RCq6gG20WPMlKgQi_U2imA?pwd=cxxy', '_blank')
    showToast({
      message: '已跳转到更新页面',
      type: 'success'
    })
  }, 1000)
}

const openAuthorPage = () => {
  openExternalUrl('https://github.com/zsqgleRoy/', '_blank')
}
</script>

<style scoped>
.about-page {
  background: var(--van-background);
  min-height: 100vh;
}

.about-content {
  padding: 16px;
}

.info-section {
  text-align: center;
  padding: 32px 16px;
  background: var(--van-background-2);
  border-radius: 12px;
  margin-bottom: 16px;
}

.app-logo {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.app-logo img {
  width: 64px;
  height: 64px;
  object-fit: contain;
}

.app-name {
  font-size: 24px;
  font-weight: 600;
  color: var(--van-text-color);
  margin-bottom: 8px;
}

.app-version {
  font-size: 14px;
  color: var(--van-text-color-2);
  margin-bottom: 16px;
}

.app-desc {
  font-size: 14px;
  color: var(--van-text-color-2);
  line-height: 1.6;
  max-width: 280px;
  margin: 0 auto;
}

.feature-section {
  margin-bottom: 16px;
  border-radius: 8px;
  overflow: hidden;
}

.action-section {
  margin-bottom: 16px;
}

.other-section {
  margin-bottom: 24px;
}

.copyright {
  text-align: center;
  padding: 24px 0;
  font-size: 12px;
  color: var(--van-text-color-3);
}

:deep(.van-cell) {
  min-height: 44px;
}

:deep(.van-nav-bar) {
  background: var(--van-background-2);
}
</style>

