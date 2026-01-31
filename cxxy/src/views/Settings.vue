<template>
  <div class="settings-page page-container">
    <van-cell-group>
      <van-cell title="主题模式" :value="themeText" @click="showThemeSheet = true" is-link>
        <template #icon>
          <van-icon name="setting-o" class="cell-icon" />
        </template>
      </van-cell>
      <van-cell title="清理缓存" @click="showClearDialog = true" is-link>
        <template #icon>
          <van-icon name="delete-o" class="cell-icon" />
        </template>
      </van-cell>
      <van-cell title="关于" @click="goToAbout" is-link>
        <template #icon>
          <van-icon name="info-o" class="cell-icon" />
        </template>
      </van-cell>
    </van-cell-group>

    <!-- 主题选择面板 -->
    <van-action-sheet
      v-model:show="showThemeSheet"
      :actions="themeActions"
      @select="onThemeSelect"
      cancel-text="取消"
    />

    <!-- 清理缓存对话框 -->
    <van-dialog
      v-model:show="showClearDialog"
      title="清理缓存"
      message="确定要清理所有缓存数据吗？此操作不可恢复。"
      show-cancel-button
      @confirm="clearCache"
    />

    <TabBar />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useAppStore } from '@/stores/app'
import type { Theme } from '@/types'
import TabBar from '@/components/TabBar.vue'

const router = useRouter()
const appStore = useAppStore()
const showClearDialog = ref(false)
const showThemeSheet = ref(false)

const themeText = computed(() => {
  const themeMap: Record<Theme, string> = {
    light: '浅色模式',
    dark: '深色模式',
    eye: '护眼模式'
  }
  return themeMap[appStore.theme] || '浅色模式'
})

const themeActions = [
  { name: '浅色模式', value: 'light' as Theme },
  { name: '深色模式', value: 'dark' as Theme },
  { name: '护眼模式', value: 'eye' as Theme }
].map(action => ({
  ...action,
  className: appStore.theme === action.value ? 'theme-selected' : ''
}))

const onThemeSelect = (action: { value: Theme; name: string }) => {
  appStore.setTheme(action.value)
  showThemeSheet.value = false
  showToast({
    message: `已切换到${action.name}`,
    position: 'top'
  })
}

const clearCache = () => {
  appStore.clearAllData()
  showToast({
    message: '缓存已清理',
    type: 'success'
  })
  showClearDialog.value = false
}

const goToAbout = () => {
  router.push('/about')
}
</script>

<style scoped>
.settings-page {
  padding: 16px;
  background: var(--van-background);
}

:deep(.van-cell) {
  min-height: 44px;
  display: flex;
  align-items: center;
}

/* 确保图标容器对齐 */
:deep(.van-cell__left-icon) {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.cell-icon {
  font-size: 18px;
  line-height: 1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 确保文字垂直居中 */
:deep(.van-cell__title) {
  display: flex;
  margin-left: 5px;
  align-items: center;
  line-height: 1.5;
}

/* 主题选择高亮 */
:deep(.theme-selected) {
  color: var(--van-primary-color);
  font-weight: 500;
}
</style>

