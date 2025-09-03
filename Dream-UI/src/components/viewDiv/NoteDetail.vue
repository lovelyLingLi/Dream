<!-- 笔记卡片的详情内容组件，点击笔记卡片后使用该组件 -->
<template>
  <div class="space-y-6 w-full px-16">
    <!-- 面包屑 -->
    <Breadcrumb :items="breadcrumbItems" />

    <!-- 图片区域（与NoteCard同尺寸） -->
    <div class="group relative h-[300px] rounded-xl border border-white/10 overflow-hidden">
      <img
          src="../../assets/image/001.jpg"
          class="w-full h-full object-cover"
      >
      <!-- 点赞信息（位置调整到右下角） -->
      <div class="absolute bottom-4 right-4 bg-black/60 px-3 py-2 rounded-full">
        <div class="flex gap-4 text-white">
          <span class="flex items-center gap-1">
            ❤️ <span class="text-sm">114</span>
          </span>
          <span class="flex items-center gap-1">
            ⭐ <span class="text-sm">514</span>
          </span>
        </div>
      </div>
    </div>

    <!-- 改造后的内容区域 -->
    <div class="bg-gray-800/30 p-6 rounded-xl border border-white/10">
      <article
          class="article-content"
          v-html="htmlContent"
          @click="handleCodeBlockClick"
      ></article>
    </div>
  </div>

  <!-- 代码块复制提示窗口 -->
  <Transition name="fade">
    <div v-if="showToast"
         class="toast-message fixed bottom-8 left-1/2 -translate-x-1/2 bg-pink-500/90 text-white px-6 py-3 rounded-full text-sm">
      {{ toastMessage }}
    </div>
  </Transition>
</template>

<script setup>
import { computed, ref, onMounted, watch, nextTick } from 'vue'
import { useClipboard } from '@vueuse/core'
import { useRoute } from 'vue-router'
import Breadcrumb from '@/components/globalDiv/Breadcrumb.vue'
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';
import { parseMarkdown } from '@/utils/markdownParser';
// import { parseMarkdown } from '@/utils/parseMarkdown';

const route = useRoute()
const noteId = computed(() => route.params.id)

const breadcrumbItems = [
  { text: '个人主页', to: '/profile' },
  { text: `笔记${noteId.value}`, active: true }
]

// 模拟带代码的Markdown内容
const markdownContent = ref(`
# ${noteId.value} - 卡特的自我修养

## 卡特的历程
欢迎来到魔法世界！在开始前请确认已准备：
\`\`\`bash
npm install magic-core --save
\`\`\`

---

## 🎯 核心技能清单
### 1. 基础咒语
\`\`\`js
// 晨星召唤术
function summonMorningStar() {
  const power = calculatePower(9001);
  return new Promise((resolve) => {
    setTimeout(() => resolve('✨ 星之力量注入完成！'), 1000);
  });
}
\`\`\`

### 2. 装备配置
\`\`\`css
/* 魔杖样式配置 */
.magic-wand {
  element: star;       /* 核心元素 */
  glow-intensity: 3;   /* 发光强度 */
  length: 28cm;        /* 标准长度 */
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
\`\`\`

---

## 🌸 进阶技巧
### 元素融合公式
$$
\\text{融合能量} = \\frac{\\text{火元素} \\times \\text{水元素}}{\\sqrt{\\text{风元素}}}
$$

---

## 📚 常见问题解答
1. **Q：魔力不足怎么办？**
   - 食用魔法草莓 🍓
   - 进行月光冥想 🌕
   - 联系魔法客服：\`tel:131-1234-5678\`

2. **Q：变身失败提示**
   \`Error: 服装数据库连接失败\`
   尝试：
   \`\`\`bash
   magic-cli repair --costume --force
   \`\`\`

---

## 🎨 排版效果展示
> "魔法不是工具，而是心灵的回声"
> ——《魔法原理》第三章

| 属性       | 数值范围   | 推荐值 |
|------------|-----------|--------|
| 魔力值     | 0-10000   | ≥8000  |
| 敏捷度     | 0-100     | ≥90    |
| 可爱指数   | ★★★★★    | ★★★★☆ |
`);

const htmlContent = computed(() => parseMarkdown(markdownContent.value))


/* ==========================
   代码块点击后的复制功能
   ========================== */

const { copy, isSupported } = useClipboard()
const showToast = ref(false)
const toastMessage = ref('')

const handleCodeBlockClick = async (event) => {
  const preBlock = event.target.closest('pre')
  if (preBlock) {
    const code = preBlock.querySelector('code').innerText
    await copy(code)

    // 显示优雅提示
    toastMessage.value = '✨ 复制成功~'
    showToast.value = true
    setTimeout(() => showToast.value = false, 2000)

    // 动效反馈
    preBlock.style.transform = 'scale(0.98)'
    setTimeout(() => preBlock.style.transform = '', 200)
  }
}

/* ==========================
   代码块样式强制显示
   ========================== */

// 监听 htmlContent 变化，手动触发 Prism.js 高亮处理
watch(htmlContent, async () => {
  await nextTick()
  Prism.highlightAll()
})

// 页面挂载时也触发一次
onMounted(() => {
  Prism.highlightAll()
})

</script>

<style scoped>

/* ==========================
   代码块复制提示
   ========================== */

/* 提示动画 */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(20px);
}


/* 基础文章样式 */

:deep(h1) {
  font-size: 2.25rem !important;
  margin-bottom: 25px;
  @apply text-center;
}

:deep(h2) {
  font-size: 1.75rem !important;
  margin-bottom: 15px;
}

:deep(h3) {
  font-size: 1.25rem !important;
  margin-bottom: 5px;
}

/* 正文基准 */
:deep(p) {
  @apply text-base !important;  /* 16px */
  @apply text-left;
  font-size: 1rem !important;
}

/* 代码块点击事件 */
article :deep(pre) {
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 0 15px rgba(255,105,180,0.3);
    opacity: 1 !important; /* 强制显示 */
    right: 1rem !important;
    top: 1rem !important;
    background: rgba(0,0,0,0.5) !important;
    padding: 2px 8px;
    border-radius: 4px;
  }
}
</style>