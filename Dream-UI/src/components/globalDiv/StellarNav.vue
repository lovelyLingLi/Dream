<!-- 导航栏组件 -->
<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'



const route = useRoute()
const isHomePage = computed(() => route.path === '/')

const navItems = [
  { path: '/', name: '首页' },
  { path: '/profile', name: '个人主页' },
  { path: '/profile/wallpapers', name: '壁纸墙' },
  { path: '/contact', name: '关于我' }
]
</script>

<template>
  <nav class="fixed top-0 left-0 right-0 h-16 z-50
              bg-gradient-to-b from-black/80 to-transparent">
    <div class="container mx-auto px-10 h-full flex items-end">
      <div class="flex-1 flex justify-center space-x-8 pb-3">
        <router-link
            v-for="item in navItems"
            :key="item.path"
            :to="item.path"
            exact-active-class="router-link-exact-active"
            class="relative py-1 px-2 text-sm font-light tracking-widest
                 text-white/70 hover:text-white transition-all
                 after:content-[''] after:absolute after:bottom-0 after:left-1/2
                 after:h-px after:w-0 after:bg-gradient-to-r after:from-transparent
                 after:via-pink-400 after:to-transparent after:transition-all
                 hover:after:w-full hover:after:left-0">
          {{ item.name }}
        </router-link>
      </div>
    </div>
  </nav>
</template>

<style scoped>
/* 修改router-link样式 */
.router-link-exact-active {
  position: relative;
  color: theme('colors.pink.300') !important;
}

.router-link-exact-active::before {
  content: '';
  position: absolute;
  top: -10px;
  left: 50%;
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
  transform: translateX(-50%);
  filter: drop-shadow(0 0 5px #ff69b4);
  animation: twinkle 1.5s infinite alternate;
}

@keyframes twinkle {
  0% { opacity: 0.3; transform: translateX(-50%) scale(0.8); }
  100% { opacity: 1; transform: translateX(-50%) scale(1.2); }
}
</style>