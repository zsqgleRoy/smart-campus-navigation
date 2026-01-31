<script setup lang="ts">
import { onMounted, onErrorCaptured } from 'vue'
import { useAppStore } from '@/stores/app'

const appStore = useAppStore()

onMounted(() => {
  console.log('App.vue mounted')
  try {
    appStore.initTheme()
    console.log('Theme initialized')
  } catch (error) {
    console.error('Theme initialization error:', error)
  }
})

onErrorCaptured((err, instance, info) => {
  console.error('Component error captured:', err, info)
  return false
})
</script>

<template>
  <router-view v-if="$route" />
  <div v-else style="padding: 20px; text-align: center;">
    <p>路由加载中...</p>
  </div>
</template>

<style>

</style>
