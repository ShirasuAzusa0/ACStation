<script setup>
  import "@/styles/variables.scss"
  import "@/views/welcome/LogInView.vue"
  import "@/views/welcome/SignUpView.vue"
</script>

<template>
  <div class="welcome">
    <div class="welcome__container">
      <div class="welcome__left">
        <el-image fit="cover" src="/enterP1.jpg"/>
        <div class="welcome__txt">
          <h2 class="welcome__title">欢迎来到 ACStation！</h2>
          <p class="welcome__desc">let's start today's training</p>
        </div>
      </div>
      <div class="welcome__right">
        <el-image fit="cover" src="/enterP2.png"/>
        <div class="welcome__LogIn">
          <!-- 留一个插槽用来显示登录/注册窗口 -->
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
  .welcome {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    /* 填满整个页面高度 */
    height: 100vh;
    /* 隐藏超出元素盒子范围的内容，同时相当于禁用了滚动条 */
    overflow: hidden;

    &__container {
      flex: 1;
      /* 左右两列并排 */
      display: flex;
      min-height: 0;
    }

    &__left {
      width: 80%;
      flex-direction: column;
      min-height: 0;
      z-index: 1;
    }

    &__right {
      margin: auto;
      z-index: 0;
      &::after {
        content: '';
        position: absolute;
        inset: 0;
        /* 45% 黑色蒙层 → 淡化背景 */
        background: rgba(255, 255, 255, 0.55);
        z-index: 1;
      }
    }

    &__txt {
      /* 推到父容器底部 */
      margin-top: auto;
      width: 100%;
      height: 100%;
      background: var(--footer-bg);
      color: var(--footer-text);
      padding: var(--footer-px) 0;
    }

    &__title {
      font-size: 24px;
      font-weight: bold;
      margin: 0 0 10px 0;
      text-shadow: 0 0 10px black;
      transform: translate(1%, 0);
    }

    &__desc {
      font-size: 16px;
      margin: 0;
      text-shadow: 0 0 10px black;
      transform: translate(1%, 0);
    }

    &__LogIn {
      position: absolute;
      top: 50%;
      left: 50%;
      z-index: 2;
      transform: translate(15%, -50%);
      width: 500px;
      height: 500px;
      padding: 20px;
      border-radius: 10px;
    }
  }
</style>