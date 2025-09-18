// Pinia 用户仓库
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    const user = ref(null)      // { name, avatar }
    const token = ref(null)

    function setUser(u, t) {
        user.value = u
        token.value = t
        localStorage.setItem('user', JSON.stringify(u))
        localStorage.setItem('token', t)
    }

    function logout() {
        user.value = null
        token.value = null
        localStorage.removeItem('user')
        localStorage.removeItem('token')
    }

    // 页面刷新时自动恢复登录态
    if (localStorage.getItem('user')) {
        user.value = JSON.parse(localStorage.getItem('user'))
        token.value = localStorage.getItem('token')
    }

    return { user, token, setUser, logout }
})
