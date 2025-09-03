<!-- 玻璃卡片组件（暂未使用） -->
<template>
  <!-- 玻璃卡片容器 -->
  <div class="
    rounded-2xl p-6
    backdrop-blur-lg bg-white bg-opacity-10
    border border-white border-opacity-20
    shadow-lg hover:shadow-xl
    transition-all duration-300
    hover:bg-opacity-20 hover:scale-[1.02]
  ">
    <!-- 卡片内容插槽 -->
    <slot></slot>
  </div>
</template>

<script>
export default {
  // 可添加props控制透明度/颜色等
  props: {
    blurLevel: {
      type: Number,
      default: 8 // backdrop-blur-lg对应的模糊值
    }
  }
}
</script>