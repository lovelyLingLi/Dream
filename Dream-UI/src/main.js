import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import StellarNav from '@/components/globalDiv/StellarNav.vue'


const app = createApp(App)

app.component('StellarNav', StellarNav)

import ProfileLayout from '@/layouts/ProfileLayout.vue'
app.component('ProfileLayout', ProfileLayout)


/* ==========================
   导入Prism主题，用于文章样式
   ========================== */
import 'prismjs/plugins/toolbar/prism-toolbar.css'
import '@/assets/css/code-theme.css' // 新建这个文件

app.use(router) // 注册路由
app.mount('#app')
