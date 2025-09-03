<!-- 首页中的呼吸头像组件 -->
<template>

  <!-- 头像容器：相对定位+自动边距+响应式尺寸 -->
  <div class="relative group  mx-auto w-32 h-32 md:w-40 md:h-40 hover:-translate-y-2 transition-transform duration-300"  @click="goToSecretBase">
    <!-- 头像图片：圆形+边框+层级控制 -->
    <img
        :src="avatarSrc"
        class="rounded-full  z-10 relative w-full h-full object-cover "
        alt="主人の头像"
        @click="navigateToProfile"
    >

    <!-- 呼吸光效层：绝对定位+动画+颜色渐变 -->
    <div
        class="absolute inset-0 rounded-full bg-blue-500 opacity-70 z-0 animate-breathe-glow"
        :style="{
        'background': glowColor,
        'animation-duration': duration + 's' /* 动态控制时长 */
      }"
    ></div>

    <!-- 悬浮提示 -->
    <div class="
      absolute bottom-full left-1/2 transform -translate-x-1/2
      bg-black bg-opacity-70 text-white text-sm py-1 px-3 rounded
      opacity-0 group-hover:opacity-100 transition-opacity duration-200
      whitespace-nowrap my-2
    ">
      点击进入秘密基地
      <div class="absolute top-full left-1/2 -translate-x-1/2 w-0 h-0 border-l-4 border-r-4 border-b-0 border-t-4 border-t-black/70 border-l-transparent border-r-transparent"></div>
    </div>
  </div>

</template>

<script >
import { useRouter } from 'vue-router';

export default {
  props: {
    avatarSrc: {
      type: String,
      required: true
    },
    glowColor: {
      type: String,
      default: 'rgba(255, 105, 180, 0.7)' // Tailwind的blue-500
    },
    duration: {
      type: Number,
      default: 3 // 动画周期(秒)
    }
  },
  setup() {
    const router = useRouter(); // 获取路由实例

    const navigateToProfile = () => {
      router.push('/profile')
    }

    return {
      navigateToProfile
    };
  }
}
</script>

<style>
/* 自定义呼吸动画 */
@keyframes breathe-glow {
  0% {
    box-shadow:
        0 0 5px rgba(255, 255, 255, 0.4),
        0 0 15px rgba(255, 255, 255, 0.4);
    transform: scale(1.02);
  }
  50% {
    box-shadow:
        0 0 5px rgba(255, 182, 193, 0.4), /* 增加更柔和的光效 */
        0 0 15px rgba(255, 105, 180, 0.4);
    transform: scale(1.05);
  }
  100% {
    box-shadow:
        0 0 5px rgba(255, 255, 255, 0.4),
        0 0 15px rgba(255, 255, 255, 0.4);
    transform: scale(1.02);
  }
}
.animate-breathe-glow {
  animation: breathe-glow 5s ease-in-out infinite;
}
</style>