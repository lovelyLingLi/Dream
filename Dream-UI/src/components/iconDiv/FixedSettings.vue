<!-- 首页中右上角的设置组件 -->
<template>
  <!-- 右上角按钮 -->
  <!--
    fixed: 将元素的定位设置为固定定位（position: fixed），意味着该元素的位置相对于浏览器窗口进行定位，而不是相对于父元素。
    top-6: 设置元素的上边距为 1.5rem (24px)，即距离浏览器窗口顶部 24px。
    right-6: 设置元素的右边距为 1.5rem (24px)，即距离浏览器窗口右侧 24px。
    p-2: 设置元素的内边距（padding）为 0.5rem (8px)，使元素的内容不会紧贴着边缘。
    bg-white: 设置元素的背景颜色为白色（background-color: white）。
    bg-opacity-10: 设置背景颜色的透明度为 10%，使背景变得非常透明。
    rounded-full: 将元素的边角设置为完全圆形，适用于圆形或圆角的元素。
    hover:bg-opacity-20: 当元素在悬停时（hover），背景透明度增加到 20%。
    transition-all: 设置所有属性的过渡效果，使元素在状态变化时平滑过渡。
    text-white: 设置文本颜色为白色（color: white）。
    text-opacity-80: 设置文本颜色的透明度为 80%，使文本变得有些透明。
    hover:text-opacity-100: 当元素在悬停时，文本的透明度变为 100%，即文本完全不透明。
   -->
  <button @click="toggleSettings" class="
    fixed top-6 right-6 p-2
    bg-white bg-opacity-10 rounded-full
    hover:bg-opacity-20 transition-all
    text-white text-opacity-80 hover:text-opacity-100
  ">
    <span class="text-xl">⚙️</span>
  </button>
</template>

<script>

export default {
  methods: {
    toggleSettings() {
      console.log('打开设置面板')
    }
  }
}
</script>