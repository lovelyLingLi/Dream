<!-- 个人主页的页面，通过路由切换右侧内容 -->
<template>
  <div class="min-h-screen pt-96 text-white">
    <div class="mx-auto flex gap-6" style="width: 1200px; min-width: 1200px">
      <StarfieldBackground />
      <!-- 左侧固定区域 -->
      <PersonalSidebar />

      <!-- 必须要有router-view！ -->
      <router-view v-slot="{ Component }">
        <component :is="Component" />
      </router-view>
    </div>
  </div>
</template>

<script setup>
import PersonalSidebar from '@/components/imageDiv/PersonalSidebar.vue'
import StarfieldBackground from '@/components/background/StarfieldBackground.vue'
</script>

<style>

</style>