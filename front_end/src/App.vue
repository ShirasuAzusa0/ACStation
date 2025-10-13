<!-- JavaScript放此处 -->
<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user.js'

const userStore = useUserStore()

onMounted(() => {
  // 确保用户状态已加载
  if (!userStore.token) {
    userStore.loadFromStorage()
  }
})
</script>

<!-- HTML放此处 -->
<template>
  <div class="rent">
    <main class="rent__main">
      <router-view />
    </main>
  </div>
</template>

<!-- CSS放此处 -->
<style scoped lang="scss">

</style>
