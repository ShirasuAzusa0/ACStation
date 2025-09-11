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
            path: "/cars",
            name: "cars",
            component: () => import('@/views/SourcePage/CarsView.vue'),
            children: []
        },
        {
            path: "/tracks",
            name: "tracks",
            component: () => import('@/views/SourcePage/TracksView.vue'),
            children: []
        },
        {
            path: "/skins",
            name: "skins",
            component: () => import('@/views/SourcePage/SkinsView.vue'),
            children: []
        },
        {
            path: "/plugins",
            name: "plugins",
            component: () => import('@/views/SourcePage/PluginView.vue'),
            children: []
        },
        {
            path: "/videos",
            name: "videos",
            component: () => import('@/views/SourcePage/VideoView.vue'),
            children: []
        }
    ]
})

export default router