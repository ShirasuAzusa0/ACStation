<script setup>
import { ref } from 'vue'
// 不要把 useRouter, useRoute 错从 vue 中引入，这样是错误的会导致页面空白
import { useRouter, useRoute } from 'vue-router'
import { Sunny, Moon } from '@element-plus/icons-vue'
// 引入scss样式
import '@/styles/variables.scss'
import {useUserStore} from "@/stores/user.js";
import {useAlertStore} from "@/stores/alert.js";
import {useDarkModeStore} from "@/stores/darkMode.js";
// 移动端菜单开关
const isMenuOpen = ref(true)
const router = useRouter()
const route = useRoute()

const userStore = useUserStore()
const alertStore = useAlertStore()
const darkModeStore = useDarkModeStore()

// 导航列表
const navLinks = [
  {label: 'Videos', path: '/Videos'},
  {label: 'Plugins', path: '/Plugins'},
  {label: 'Cars', path: '/Cars'},
  {label: 'Skins', path: '/Skins'},
  {label: 'Tracks', path: '/Tracks'}
]

// 预先加一个 to 字段（纯对象，无函数）
navLinks.forEach(l => (l.to = { name: l.name }))

function goHome() {
  if (route.path === "/Home") {
    window.location.reload()
  }
  else {
    router.push("/Home")
  }
}

function SignUp() {
  router.push("/Welcome/Sign-Up")
}

function SignIn() {
  router.push("/Welcome/Sign-In")
}

function LogOut() {
  userStore.logout()
  alertStore.showAlertMessage("success", "已登出账号")
  router.push("/Home")
}
</script>

<template>
  <header class="rent-header">
    <div class="rent-header__container">
      <!-- logo -->
      <div class="rent-header__brand">
        <!--<h1 class="rent-header__logo" @click="goHome">ACStation</h1>-->
        <img src="../components/icons/Logo.png" alt="Logo" class="rent-header__logo" @click="goHome" />
      </div>

      <!-- 导航栏nav -->
      <nav class="rent-nav rent-nav--desktop">
        <router-link v-for="link in navLinks"
                     :key="link.name"
                     :to="{ path: link.path }"
                     class="rent-nav__link"
                     active-class="rent-nav__link--active"
        >{{ link.label }}</router-link>
      </nav>

      <button class="rent-btn theme-toggle-btn" @click="darkModeStore.toggleDark">
        <template v-if="darkModeStore.isDark">
          <el-icon><Moon /></el-icon> Dark
        </template>
        <template v-else>
          <el-icon><Sunny /></el-icon> Light
        </template>
      </button>

      <!-- 用户操作（登录/注册） -->
      <div class="rent-header__user_action">
        <!-- 登录状态 -->
        <template v-if="userStore.user && userStore.user.username">
          <img
              :src="userStore.user.avatar || 'https://avatars.githubusercontent.com/u/19370775'"
              alt="avatar"
              class="user-avatar"
          />
          <span class="user-name">{{ userStore.user.username }}</span>
          <button class="rent-btn rent-btn--SignUp" @click="LogOut">Logout</button>
        </template>

        <!-- 未登录状态 -->
        <template v-else>
          <button class="rent-btn rent-btn--SignUp" @click="SignUp">Sign Up</button>
          <button class="rent-btn rent-btn--SignIn" @click="SignIn">Sign In</button>
        </template>
      </div>
    </div>
  </header>
</template>

<style scoped lang="scss">
/* 通过 class 的值，定位到对应的标签，设置对应的CSS样式 */
.rent-header {
  /* 背景 */
  background-color: var(--color-bg);
  /* 地边框 */
  border-bottom: 1px solid var(--color-border);
  /* 阴影 */
  box-shadow: var(--shadow-sm);

  &__container {
    max-width: 80rem;               /* 1280px */
    margin: 0 auto;                 /* 水平居中 */
    padding: 0 1rem;                /* 响应式左右留白 */
    display: flex;
    align-items: center;            /* 垂直居中 */
    justify-content: space-between;
    height: 4rem;                   /* 64px 高度 */
  }

  &__brand { flex-shrink: 0; }      /* LOGO 区域不压缩 */

  &__logo  { width: 150px; cursor: pointer }

  &__actions {
    display: flex;
    align-items: center;
    gap: 0.75rem;                   /* 12px 间距 */
  }
}

/* 导航 */
.rent-nav {
  &--desktop {                      /* 生成 .rent-nav--desktop */

    display: flex;                  /* 始终可见，不再用 @media 隐藏 */
    gap: 0.5rem;
    overflow-x: auto;               /* 关键：超出时出现横向滚动条 */
    white-space: nowrap;            /* 防止文字换行 */
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;          /* Firefox 隐藏滚动条 */
    &::-webkit-scrollbar {
      display: none;                /* Chrome / Safari 隐藏滚动条 */
    }
  }

  &__link {
    font-size: 0.875rem;      /* 14px */
    font-weight: 500;
    color: var(--color-text-muted);
    padding: 0.5rem 0.75rem;  /* 8px 12px */
    border-radius: 0.375rem;  /* 6px 圆角 */
    text-decoration: none;
    transition: color 0.15s ease-in-out;

    &:hover { color: var(--color-primary); }

    &--active { color: var(--color-text); }   /* 当前路由高亮 */
  }
}

/* 按钮 */
.rent-btn {
  font-size: 0.875rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: all 0.15s ease-in-out;

  &--SignUp {            /* 幽灵按钮 */
    color: var(--color-text-muted);
    background: none;
    border: none;

    &:hover { color: var(--color-primary); }
  }

  &--SignIn {          /* 主按钮 */
    color: #fff;
    background-color: var(--color-primary);
    border: none;

    &:hover { background-color: var(--color-primary-hover); }
  }
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
}

.user-name {
  margin: 0 8px;
  font-weight: 500;
  color: var(--color-text);
  white-space: nowrap;
}

.theme-toggle-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 0.4rem 0.8rem;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  background-color: var(--color-bg);
  color: var(--color-primary);
  border: none;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;

  &:hover {
    color: var(--color-primary-hover);
  }

  svg {
    width: 16px;
    height: 16px;
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .rent-header__actions {
    gap: 0.5rem;
  }

  .theme-toggle-btn span {
    display: none;
  }
}
</style>