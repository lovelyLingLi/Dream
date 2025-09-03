<!-- 图标超链接（暂未使用） -->
<template>
  <!-- 链接容器：水平排列+间距 -->
  <div class="flex justify-center space-x-6 mt-12">
    <!-- 单个链接 -->
    <a
        v-for="(link, index) in links"
        :key="index"
        :href="link.url"
        class="relative group w-12 h-12 flex items-center justify-center"
    >
      <!-- 圆形背景 -->
      <div class="
        absolute inset-0 rounded-full
        bg-white bg-opacity-10
        group-hover:bg-opacity-20
        transition-all duration-300
      "></div>

      <span class="text-2xl">{{ link.icon }}</span>

      <!-- 悬浮文字提示 -->
      <span class="
        absolute top-full mt-2 px-2 py-1 text-sm whitespace-nowrap
        bg-black bg-opacity-70 rounded
        opacity-0 group-hover:opacity-100
        transition-opacity duration-200
      ">
        {{ link.text }}
      </span>
    </a>
  </div>
</template>

<script>

const icons = {
  home: '🏠',
  code: '💻',
  mail: '✉️'
}

export default {
  props: {
    links: {
      type: Array,
      default: () => [
        { text: '主页', url: '#home', icon: icons.home },
        { text: '项目', url: '#projects', icon: icons.code },
        { text: '联系', url: '#contact', icon: icons.mail }
      ]
    }
  }
}
</script>