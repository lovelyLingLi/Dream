<!-- 个人主页中右侧卡片的组件 -->
<template>
  <router-link
      :to="{ name: 'note-detail', params: { id: noteId } }"
      class="group relative block h-[300px] rounded-xl border border-white/10 overflow-hidden"
  >
    <img
        src="../../assets/image/001.jpg"
        class="w-full h-full object-cover transition-all duration-300 group-hover:blur-sm"
    >
    <div class="absolute inset-0 bg-gradient-to-t from-black/80 p-6 flex flex-col justify-end">
      <h3 class="text-2xl font-bold mb-2 text-white drop-shadow-lg">
        笔记标题 {{ noteId }}
      </h3>
      <p class="text-white/80 line-clamp-2">
        这里是笔记内容的概括文字
      </p>
    </div>
  </router-link>
</template>

<script setup>
defineProps({
  noteId: { type: Number, required: true }
})
</script>