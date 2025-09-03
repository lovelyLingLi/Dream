<!-- 壁纸墙页面 -->
<template>
  <!-- 外层滚动容器 -->
  <div class="infinite-scroll-component__outerdiv ">
    <!-- 瀑布流主体 -->
    <div
        class="infinite-scroll-component"
        :style="{ height: 'auto', overflow: 'auto' }"
    >
      <!-- 四列容器 -->
      <div class="flex flex-wrap">
        <!-- 每列单独渲染 -->
        <WallpaperColumn
            v-for="(column, index) in columnData"
            :key="index"
            :wallpapers="column"
            class="w-full sm:w-1/2 md:w-1/4 px-1"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import WallpaperColumn from '@/components/imageDiv/WallpaperColumn.vue'

// 模拟数据（实际应从API获取）
const allWallpapers = Array(20).fill().map((_, i) => {
  const num = String(i+1).padStart(2, '0')
  return {
    id: i + 1,
    img: `/src/assets/wallpapers/${num}.jpg`, // 关键修改点
    title: `壁纸 ${i+1}`,
    height: 150 + Math.random() * 300
  }
})

// 将数据分配到四列
const columnData = computed(() => {
  const columns = [[], [], [], []]
  allWallpapers.forEach((item, index) => {
    columns[index % 4].push(item)
  })
  return columns
})
</script>

<style scoped>
.infinite-scroll-component__outerdiv {
  @apply w-full;
}
.infinite-scroll-component {
  @apply w-full;
}
</style>