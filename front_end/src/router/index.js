import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            // 引用组件，path为路径（"/"表示首页），name组件名，component是组件引用
            path: "/",
            name: "home",
            component: () => import('@/views/HomeView.vue'),
            children : [
                {
                    path: "",
                    name: "home-login",
                    component: () => import('@/views/welcome/LoginPage.vue')
                }
            ]
        }
    ]
})

export default router