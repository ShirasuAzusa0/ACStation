import { createRouter, createWebHistory } from "vue-router";
import SkinDetailView from "@/views/SourcePage/DetailPage/SkinDetailView.vue";
import CarDetailView from "@/views/SourcePage/DetailPage/CarDetailView.vue";
import TrackDetailView from "@/views/SourcePage/DetailPage/TrackDetailView.vue";
import {useUserStore} from "@/stores/user.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path: "/",
            redirect: "/Home"
        },
        {
            // 引用组件，path为路径（"/"表示首页），name组件名，component是组件引用
            path: "/Home",
            name: "home",
            component: () => import('@/views/HomeView.vue')
        },
        {
            path: "/Welcome",
            name: "welcome",
            component: () => import('@/views/welcome/WelcomeView.vue'),
            children : [
                {
                    // 子路由的 path 如果以 / 开头，就会被当成根路径，不再拼接父路由的路径
                    // <router-view/> 一旦没有匹配到任何子路由，父路由页面就会变成空白的

                    // 登录子页面
                    path: "Sign-In",
                    name: "home-SignIn",
                    component: () => import('@/views/welcome/LogInView.vue')
                },
                {
                    // 注册子页面
                    path: "Sign-Up",
                    name: "home-SignUp",
                    component: () => import('@/views/welcome/SignUpView.vue')
                }
            ]
        },
        {
            path: "/Cars",
            name: "cars",
            component: () => import('@/views/SourcePage/CarsView.vue'),
            children: []
        },
        {
            path: "/Tracks",
            name: "tracks",
            component: () => import('@/views/SourcePage/TracksView.vue'),
            children: []
        },
        {
            path: "/Skins",
            name: "skins",
            component: () => import('@/views/SourcePage/SkinsView.vue'),
            children: []
        },
        {
            path: "/Plugins",
            name: "plugins",
            component: () => import('@/views/SourcePage/PluginView.vue'),
            children: []
        },
        {
            path: "/Videos",
            name: "videos",
            component: () => import('@/views/SourcePage/VideoView.vue'),
            children: []
        },
        {
            path: "/Skins/:skinName",
            name: "SkinDetail",
            component: SkinDetailView,
            props: true
        },
        {
            path: "/Cars/:carModName",
            name: "CarDetail",
            component: CarDetailView,
            props: true
        },
        {
            path: "/Tracks/:trackModName",
            name: "TrackDetail",
            component: TrackDetailView,
            props: true
        }

    ],
    scrollBehavior(to, from, savedPosition) {
        // 如果用户使用浏览器前进/后退，保留历史滚动位置
        if (savedPosition) {
            return savedPosition
        }

        // 如果 URL 有 hash（锚点），尝试滚到锚点
        if (to.hash) {
            return { el: to.hash, behavior: 'smooth' }
        }

        // 默认：回到页面顶端
        return { left: 0, top: 0 } // 可加 behavior: 'auto' 或 'smooth'
    }
})

/*
Vite dev server 有 URL 修正机制，会根据你的vue文件名命名方式修改URL（比如HomeView.vue会导致URL变成Home）
此时的结果就是你手动修改成小写开头访问照样会自动被转会大写开头
但有个例外，就是从其他页面直接指定跳转到小写开头的路径下，由于此过程不经过dev server文件名检查，小写开头的地址会原封不动地显示
 */

// 路由守卫/全局拦截器
// 保证用户手动输入小写也变成大写，确保路径显示统一
router.beforeEach((to, from, next) => {
    // 首字母大写，其余保持原样
    const formattedPath = to.path.replace(/^\/\w/, (c) => c.toUpperCase());
    if (to.path !== formattedPath) {
        next({ path: formattedPath, query: to.query, replace: true });
    } else {
        next();
    }
});

export default router