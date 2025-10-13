import { defineStore } from 'pinia'
import { ref, watch } from 'vue'

export const useDarkModeStore = defineStore('darkMode', () => {
    const isDark = ref(false)

    // 初始化时从 localStorage 读取或检测系统偏好
    const initializeDarkMode = () => {
        const saved = localStorage.getItem('dark-mode')
        if (saved !== null) {
            isDark.value = saved === 'true'
        } else {
            // 检测系统偏好
            isDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
        }
        applyDarkMode(isDark.value)
    }

    // 应用暗色模式
    const applyDarkMode = (dark) => {
        if (dark) {
            document.documentElement.classList.add('dark')
        } else {
            document.documentElement.classList.remove('dark')
        }
        localStorage.setItem('dark-mode', dark.toString())
    }

    // 切换暗色模式
    const toggleDark = () => {
        isDark.value = !isDark.value
        applyDarkMode(isDark.value)
    }

    // 立即初始化
    initializeDarkMode()

    return {
        isDark,
        toggleDark
    }
})