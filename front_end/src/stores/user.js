import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
    state: () => ({
        user: null,
        token: ''
    }),
    actions: {
        setUser(user, token) {
            this.user = user
            this.token = token
            localStorage.setItem('user', JSON.stringify(user))
            localStorage.setItem('token', token)
        },
        logout() {
            this.user = null
            this.token = ''
            localStorage.removeItem('user')
            localStorage.removeItem('token')
        },
        loadFromStorage() {
            try {
                const user = localStorage.getItem('user')
                const token = localStorage.getItem('token')
                if (user && token) {
                    this.user = JSON.parse(user)
                    this.token = token
                    console.log('用户状态已从存储加载')
                } else {
                    console.log('未找到存储的用户状态')
                }
            } catch (error) {
                console.error('加载用户状态失败:', error)
                // 清除可能损坏的存储
                this.logout()
            }
        }
    }
})
