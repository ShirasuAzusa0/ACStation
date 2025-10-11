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
            const user = localStorage.getItem('user')
            const token = localStorage.getItem('token')
            if (user && token) {
                this.user = JSON.parse(user)
                this.token = token
            }
        }
    }
})
