import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
    {
        path: '/',
        name: 'home',
        component: Home // 首页（含特效）
    },
    {
        path: '/profile',
        component: ()=> import('@/layouts/ProfileLayout.vue'),
        children: [
            {
                path: '',
                component: () => import('@/views/Profile.vue'),
                name: 'profile'
            },
            {
                path: 'note/:id',
                component: () => import('@/components/viewDiv/NoteDetail.vue'),
                name: 'note-detail'
            },
            {
                path: 'wallpapers',
                component: () => import('@/views/WallpaperWall.vue'),
                name: 'wallpapers'
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        // 像弹簧一样把页面弹回顶部 (๑•̀ㅂ•́)و✧
        return { top: 300, behavior: 'smooth'  }
    }
})

export default router