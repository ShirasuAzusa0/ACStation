<!-- JavaScript放此处 -->
<script setup>
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
</script>

<!-- HTML放此处 -->
<template>
  <div class="rent">
    <Header />
    <main class="rent__main">
      <router-view/>
    </main>
    <Footer />
  </div>
</template>

<!-- CSS放此处 -->
<style scoped lang="scss">
/* 让页面最少 100vh，footer 自动贴底 */
.rent {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.rent__main {
  /* 占满剩余空间，把 footer 推到底 */
  flex: 1 0 auto;
}
</style>
