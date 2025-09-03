<!-- 壁纸组件 -->
<template>
  <div>
    <!-- 单列中的壁纸项 -->
    <div
        v-for="wallpaper in wallpapers"
        :key="wallpaper.id"
        class="relative h-auto mb-4"
    >
      <!-- 图片容器 -->
      <div
          class="relative shadow-none rounded-small"
          :style="{ maxWidth: '4000px', height: `${wallpaper.height}px` }"
      >
        <img
            :src="wallpaper.img"
            class="absolute w-full h-full object-cover rounded-small"
            loading="lazy"
            :alt="wallpaper.title"
        >
      </div>

      <!-- 文字描述 -->
      <div class="p-1 mt-1 flex items-center justify-between w-full overflow-hidden">
        <span class="text-white truncate">{{ wallpaper.title }}</span>
        <button class="text-pink-400 text-sm">收藏</button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  wallpapers: { type: Array, required: true }
})
</script>