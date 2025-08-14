import axios from "axios"

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE || 'http://localhost:8080',
    timeout: 5000,
})

// 请求失败统一处理
api.interceptors.response.use(
    res => res.data,
    err => {
        console.error('API Error:', err)
        return Promise.reject(err)
    }
)