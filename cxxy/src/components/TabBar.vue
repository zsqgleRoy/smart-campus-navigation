<template>
  <van-tabbar v-model="active" @change="handleChange" fixed placeholder>
    <van-tabbar-item icon="home-o" to="/">首页</van-tabbar-item>
    <van-tabbar-item icon="apps-o" to="/category">分类</van-tabbar-item>
    <van-tabbar-item icon="star-o" to="/favorites">收藏</van-tabbar-item>
    <van-tabbar-item icon="setting-o" to="/settings">设置</van-tabbar-item>
  </van-tabbar>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const active = computed(() => {
  const path = route.path
  if (path === '/') return 0
  if (path === '/category') return 1
  if (path === '/favorites') return 2
  if (path === '/settings') return 3
  return 0
})

const handleChange = (index: number) => {
  const paths = ['/', '/category', '/favorites', '/settings']
  if (paths[index]) {
    router.push(paths[index])
  }
}
</script>

<style scoped>
:deep(.van-tabbar-item) {
  min-height: 44px;
}
</style>

