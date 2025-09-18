import { createApp } from 'vue'
import router from '@/router'
import axios from 'axios'
import {createPinia} from "pinia";
import App from './App.vue'

const app = createApp(App)
const pinia = createPinia()

app.config.globalProperties.$axios = axios
app.use(router)
app.use(pinia)

app.mount('#app')
