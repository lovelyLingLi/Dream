<template>
  <!-- 只在非首页时显示导航栏 -->
  <StellarNav v-if="$route.path !== '/'" />
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :is="Component" :key="$route.path" />
    </keep-alive>
  </router-view>
</template>

<script setup>
import StellarNav from './components/globalDiv/StellarNav.vue'
</script>

