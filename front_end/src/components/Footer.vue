<script setup>
  import { RouterLink } from "vue-router"
  import { ref } from "vue"
  import "@/styles/variables.scss"
</script>

<template>
  <footer class="rent-footer">
    <div class="rent-footer__container">
      <!-- 简介区 -->
      <div class="rent-footer__brand">
        <h3 class="rent-footer__title">ACStation</h3>
        <p class="rent-footer__subtitle">
          Practice and ready for race
        </p>
      </div>
      <!-- 链接区 -->
      <div class="rent-footer__nav">
        <h3 class="rent-footer__navTitle">友链</h3>
        <ul class="rent-footer__navList">
          <li class="rent-footer__navItem1">
            <a href="https://github.com/ShirasuAzusa0">我的GitHub主页</a>
          </li>
          <li class="rent-footer__navItem2">
            <a href="https://space.bilibili.com/290261723">我的b站主页</a>
          </li>
          <li class="rent-footer__navItem3">
            <a href="https://www.shirasu-azusa.com/">小梓的小站</a>
          </li>
          <li class="rent-footer__navItem4">
            <a href="#">来玩玩Azusa的小游戏吧</a>
          </li>
        </ul>
      </div>
      <!-- 版权声明 -->
      <div class="rent-footer__copyRight">
        <h3 class="rent-footer__copyRightTitle">Copy Right</h3>
        <p class="rent-footer__copyRightContent1">
          © 2025 ACStation.All rights reserved.
        </p>
        <p class="rent-footer__copyRightContent2">
          本网站归 Y.Z.Ben 本人所有
        </p>
        <p class="rent-footer__copyRightContent3">
          本站的所有资源均为免费开源资源，严禁商用，违者必究
        </p>
      </div>
    </div>
  </footer>
</template>

<style scoped lang="scss">
.rent-footer {
  margin-top: auto;
  background: var(--footer-bg);
  color: var(--footer-text);
  padding: var(--footer-px) 0;

  &__container {
    max-width: 80rem;
    margin: 0 auto;
    padding: 1rem 0;
    /* 横向 + 自动换行 */
    display: flex;
    gap: 2rem;
    flex-wrap: wrap;
    /* 左右拉开 */
    justify-content: space-between;
    align-items: flex-start;
  }

  &__brand,
  &__nav,
  &__copyRight {
    flex: 1 1 0;           /* 横向排布，强制三等分 */
    min-width: 0;          /* 防止内容撑宽 */
  }

  &__brand {
    .rent-footer__title {
      font-size: 1.25rem;
      font-weight: 700;
      color: var(--footer-primary);
      margin-bottom: 0.5rem;
    }
    .rent-footer__subtitle {
      font-size: 0.875rem;
      color: var(--footer-text-muted);
    }
  }

  &__nav {
    .rent-footer__navTitle {
      font-size: 1.125rem;
      font-weight: 600;
      margin-bottom: 0.75rem;
    }

    .rent-footer__navList {
      list-style: none;
      padding: 0;
      margin: 0;
    }

    .rent-footer__navItem1 {
      margin-bottom: 0.5rem;
    }
    .rent-footer__navItem2 {
      margin-bottom: 0.5rem;
    }
    .rent-footer__navItem3 {
      margin-bottom: 0.5rem;
    }
    .rent-footer__navItem4 {
      margin-bottom: 0.5rem;
    }
/*
    .rent-footer__navLink {
      font-size: 0.875rem;
      color: var(--footer-text-muted);
      text-decoration: none;
      transition: color 0.2s;

      &:hover { color: var(--footer-primary); }
    }

 */
  }

  &__copyright {
    border-top: 1px solid var(--footer-border);
    margin-top: 2rem;
    padding-top: 1.5rem;
    text-align: center;
    font-size: 0.875rem;
    color: var(--footer-text-muted);
  }
}
</style>