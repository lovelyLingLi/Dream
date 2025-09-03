<!-- 左侧个人面板 -->
<template>
  <div class="sidebar-container relative">
    <!-- 头像卡片 -->
    <div ref="avatarCard" class="avatar-card relative">

      <!-- 头像容器 -->
      <div class="avatar-glow-layer relative h-[250px] overflow-hidden">
        <!-- 基础图片 -->
        <img src="@/assets/image/001.jpg"
             class="avatar-image absolute inset-0 w-full h-full object-cover rounded-t-xl">
      </div>


      <!-- 内容容器 -->
      <div class="avatar-info p-4 h-[104px] flex flex-col justify-between">
        <!-- 昵称区 -->
        <div>
          <p class="text-xl font-bold text-white drop-shadow-[0_0_8px_rgba(106,169,252,0.5)]">
            柠萌丶浅爱
          </p>
          <p ref="terminalText" class="terminal-text text-sm text-slate-400 truncate">
            {{ currentText }}
          </p>
        </div>

        <!-- 图标区 -->
        <div class="flex justify-center pt-2">
          <div class="flex space-x-2">
            <div v-for="(icon, index) in socialIcons" :key="index"
                 class="w-8 h-8 rounded-full bg-white/5 flex items-center justify-center
              border border-white/10 hover:bg-gradient-to-br hover:from-cyan-400/50 hover:to-blue-500/50
              hover:border-cyan-300/50 transition-all duration-300">
              <!-- 使用本地图片 -->
              <img :src="icon.imgSrc"
                   class="w-6 h-6 object-contain filter brightness-90 hover:brightness-110 transition-all"
                   :alt="icon.name">
            </div>
          </div>
        </div>

      </div>

    </div>

    <!-- 固定面板 -->
    <div ref="stickyPanel" class="sticky-panel" :style="{ height: `${panelHeight}px` }">

      <!-- 上部：社交信息 -->
      <div class="panel-section">
        <div class="social-icons">
          <GitHubIcon class="icon" />
          <TwitterIcon class="icon" />
          <MailIcon class="icon" />
        </div>
        <div class="social-list">
          <div v-for="i in 5" :key="'social'+i" class="social-item">
            <img src="@/assets/image/LY1.png" class="user-avatar">
            <div class="user-info">
              <p class="text-left">用户昵称 {{i}}</p>
              <div class="social-meta">
                <EyeIcon class="meta-icon" /> <span>1.2k</span>
                <HeartIcon class="meta-icon" /> <span>514</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中部：数据统计 -->
      <div class="panel-section">
        <h3 class="section-title">创作统计</h3>
        <div class="stats-list">
          <div v-for="(item, index) in stats" :key="'stat'+index" class="stats-item">
            <span>{{item.label}}</span>
            <div class="stats-badge">{{item.value}}</div>
          </div>
        </div>
      </div>

      <!-- 下部：标签云 -->
      <div class="panel-section">
        <h3 class="section-title">兴趣标签</h3>
        <div class="tags-container">
          <span v-for="tag in tags" :key="tag" class="tag">{{tag}}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'

// 图标引入
import {
  EyeIcon,
  HeartIcon
} from '@heroicons/vue/24/outline'

/* ==========================
   头像标签文字输入输出动画效果
   ========================== */

const terminalText = ref(null);
const originalText = "才不是喜欢你呢";
const alternateText = "其实最喜欢你啦";
const currentText = ref(originalText);
const isAnimating = ref(false);

// 删除文字的动画
function deleteText(text, callback) {
  let length = text.length;
  const interval = setInterval(() => {
    length--;
    currentText.value = text.substring(0, length);

    if (length <= 0) {
      clearInterval(interval);
      if (callback) callback();
    }
  }, 200);
}

// 输入文字的动画
function typeText(text, callback) {
  let length = 0;
  const interval = setInterval(() => {
    length++;
    currentText.value = text.substring(0, length);

    if (length >= text.length) {
      clearInterval(interval);
      if (callback) callback();
    }
  }, 200);
}

// 处理鼠标悬停事件
function handleHover() {
  if (isAnimating.value) return;
  isAnimating.value = true;

  // 删除原文字
  deleteText(originalText, () => {
    // 输入新文字
    typeText(alternateText, () => {
      // 3秒后恢复
      setTimeout(() => {
        deleteText(alternateText, () => {
          typeText(originalText, () => {
            isAnimating.value = false;
          });
        });
      }, 3000);
    });
  });
}

onMounted(() => {
  if (avatarCard.value) {
    avatarCard.value.addEventListener('mouseenter', handleHover);
  }
});


// 社交图标定义
const socialIcons = [
  {
    name: 'QQ',
    imgSrc: new URL('@/assets/icons/bilibili.png', import.meta.url).href
  },
  {
    name: '微信',
    imgSrc: new URL('@/assets/icons/bilibili.png', import.meta.url).href
  },
  {
    name: 'B站',
    imgSrc: new URL('@/assets/icons/bilibili.png', import.meta.url).href
  },
  {
    name: 'B站',
    imgSrc: new URL('@/assets/icons/bilibili.png', import.meta.url).href
  },
  {
    name: 'B站',
    imgSrc: new URL('@/assets/icons/bilibili.png', import.meta.url).href
  }
]

// 数据定义
const stats = [
  { label: '作品', value: 42 },
  { label: '获赞', value: 1024 },
  { label: '获赞', value: 1024 },
  { label: '获赞', value: 1024 },
  { label: '获赞', value: 1024 },

  { label: '收藏', value: 233 }
]

const tags = ['摄影', 'AI绘画', '赛博朋克', '星空', '概念艺术', 'AI绘画', '赛博朋克', '星空', '概念艺术']

const avatarCard = ref(null) // 头像卡片DOM
const stickyPanel = ref(null) // 固定面板DOM
const panelHeight = ref(800) // 面板固定高度（可调整）

// 滚动处理逻辑
const handleScroll = () => {
  if (!avatarCard.value || !stickyPanel.value) return

  // 获取头像卡片和视口位置
  const avatarRect = avatarCard.value.getBoundingClientRect()
  // 获取页面滚动条距离顶部的距离
  const scrollY = window.scrollY

  if(scrollY<=700){
    // 模式1：固定在头像下方
    stickyPanel.value.style.position = 'absolute'
    stickyPanel.value.style.top = `${avatarRect.height + 24}px`
    stickyPanel.value.style.transform = 'none'
  }else{
    // 模式2：恢复CSS默认居中样式
    stickyPanel.value.style.position = ''
    stickyPanel.value.style.top = ''
    stickyPanel.value.style.transform = ''
  }

}

// 生命周期钩子
onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  handleScroll() // 初始化位置
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>

/* ==========================
   头像发光效果样式
   ========================== */

.avatar-glow-layer {
  position: relative;
  overflow: visible !important;
  z-index: 1;
}

.avatar-image {
  filter: brightness(1.1) contrast(1.05); /* 减少亮度增强，更自然 */
  position: relative;
  z-index: 2;
  border-radius: inherit;
}

/* 柔和发散光效果 */
.avatar-glow-layer::before {
  content: "";
  position: absolute;
  top: -40px;
  left: -40px;
  right: -40px;
  bottom: -40px;
  background: radial-gradient(
      circle at center,
      rgba(255, 255, 255, 0.8) 0%,
      rgba(255, 255, 255, 0.6) 20%,
      rgba(255, 255, 255, 0.4) 40%,
      rgba(255, 255, 255, 0.2) 60%,
      transparent 80%
  );
  z-index: 1;
  filter: blur(50px); /* 添加模糊效果使光线更柔和 */
  pointer-events: none;
}

/* ==========================
   头像标签文字输入输出动画效果
   ========================== */

.terminal-text {
  position: relative;
  display: inline-block;
  overflow: hidden;
  white-space: nowrap;
  color: #94a3b8;
}


/* ==========================
   其他
   ========================== */


/* 侧边栏容器（必须设置relative） */
.sidebar-container {
  @apply w-[250px] flex-shrink-0; /* 固定宽度 */
}

/* 头像卡片样式 */
.avatar-card {
  @apply bg-gray-800/30 rounded-xl overflow-hidden mb-6;/* 圆角和背景透明度 */
  height: 354px;
  overflow: visible
}
.avatar-image {
  @apply w-full h-[250px] object-cover; /* 固定高度图片 */
}
.avatar-info {
  @apply p-6 pt-2;
}
.avatar-info h2 {
  @apply text-xl font-bold mb-3;
}

/* 固定面板容器 */
.sticky-panel {
  @apply bg-gray-800/30 rounded-xl; /* 背景+圆角 */
  position: fixed;
  width: 250px;
  max-height: calc(100vh - 2rem); /* 视口高度减去边距 */
  overflow-y: auto; /* 滚动条 */
  top: 50%; /* 初始居中 */
  transform: translateY(-50%);
}

/* 面板内部分区 */
.panel-section {
  @apply p-4 border-b border-white/10; /* 间距+底部边框 */
}

/* 社交图标区 */
.social-icons {
  @apply flex justify-around mb-4; /* 等间距布局 */
}
.icon {
  @apply w-5 h-5 text-gray-400 hover:text-white cursor-pointer; /* 悬停效果 */
}

/* 社交列表 */
.social-list {
  @apply space-y-3;
}
.social-item {
  @apply flex items-center gap-3;
}
.user-avatar {
  @apply w-8 h-8 rounded-full object-cover flex-shrink-0;
}
.user-info {
  @apply flex-1;
}
.social-meta {
  @apply flex gap-2 text-xs text-gray-400 mt-1;
}
.meta-icon {
  @apply w-3 h-3;
}

/* 数据统计区 */
.stats-list {
  @apply space-y-3;
}
.stats-item {
  @apply flex items-center;
}
.stats-badge {
  @apply ml-auto w-6 h-6 rounded-full bg-pink-500
  flex items-center justify-center text-xs;
}

/* 标签云 */
.tags-container {
  @apply flex flex-wrap gap-2;
}
.tag {
  @apply px-2 py-1 bg-gray-700/50 rounded-full text-sm;
}

/* 滚动条美化 */
.sticky-panel::-webkit-scrollbar {
  width: 4px;
}

.sticky-panel {
  @apply bg-gray-800/30 rounded-xl w-[250px] overflow-y-auto;
}
.sidebar-container {
  @apply w-[250px] flex-shrink-0;
  min-height: calc(100vh + 400px); /* 确保滚动空间 */
}

.sticky-panel::-webkit-scrollbar-thumb {
  @apply bg-gray-500 rounded-full;
}
</style>