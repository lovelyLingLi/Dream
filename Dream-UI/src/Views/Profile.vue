<!-- 个人主页右侧的笔记卡片，被用于layouts/ProfileLayout.vue路由到右侧-->
<template>
  <div class="flex-1 min-w-0 px-16 space-y-6">
    <NoteCard
        v-for="i in 10"
        :key="i"
        :note-id="i"
        class="cursor-pointer"
    />
  </div>
</template>

<script setup>
import NoteCard from '@/components/cardDiv/NoteCard.vue'
</script>