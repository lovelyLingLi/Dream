<!-- 面包屑 -->
<template>
  <div class="text-sm text-white/50 mb-6 flex items-center">
    <template v-for="(item, index) in items" :key="index">
      <!-- 可点击项 -->
      <router-link
          v-if="item.to"
          :to="item.to"
          class="hover:text-pink-400 transition-colors"
      >
        {{ item.text }}
      </router-link>

      <!-- 不可点击项 -->
      <span v-else>
        {{ item.text }}
      </span>

      <!-- 分隔符 -->
      <span v-if="index < items.length - 1" class="mx-2">></span>
    </template>
  </div>
</template>

<script setup>
defineProps({
  items: {
    type: Array,
    required: true,
    validator: (val) => {
      return val.every(item => 'text' in item)
    }
  }
})
</script>