import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            // 引用组件，path为路径（"/"表示首页），name组件名，component是组件引用
            path: "/home",
            name: "home",
            component: () => import('@/views/HomeView.vue'),
            children : [
                {
                    path: "/login",
                    name: "home-login",
                    component: () => import('@/views/welcome/LoginPage.vue')
                }
            ]
        },
        {
            path: "/header_test",
            name: "header",
            component: () => import('@/components/Header.vue'),
            children: []
        },
        {
            path: "/footer_test",
            name: "footer",
            component: () => import('@/components/Footer.vue'),
            children: []
        }
    ]
})

export default router