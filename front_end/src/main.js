import { createApp } from 'vue'
import router from '@/router'
import axios from 'axios'
import {createPinia} from "pinia";
import App from './App.vue'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.config.globalProperties.$axios = axios

import { useUserStore } from "@/stores/user.js";

// 再应用启动时加载本地存储的登录信息
const userStore = useUserStore()
userStore.loadFromStorage()
// 监听器同步，管理多个标签页登录/登出的同步更新
window.addEventListener('storage', () => {
    userStore.loadFromStorage()
})

app.mount('#app')
