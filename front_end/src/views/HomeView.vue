<script setup>

</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden;display: flex">
    <div style="flex: 1; background-color: black">
      <el-image style="width: 100%; height: 100%;" fit="cover"
          src="Home.jpg"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px; font-weight: bold">欢迎来到AC_Station</div>
      <div style="margin-top: 10px">enjoy the game with MODs</div>
    </div>
    <div class="right-card">

    </div>
  </div>
  <router-view/>
</template>

<style scoped>
  .welcome-title {
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;
  }

  .right-card {
    width: 400px;
    z-index: 1;
    background-color: white;
  }
</style>