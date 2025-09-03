<script>

import BreatheAvatar from "../components/imageDiv/BreatheAvatar.vue";
import FixedSettings from "../components/iconDiv/FixedSettings.vue";
import SakuraSystem from '../utils/sakura.js'

export default {
  components: {
    FixedSettings,
    BreatheAvatar
  },
  data() {
    return {
      videoPoster: new URL('../assets/image/Liu_Ying.jpg', import.meta.url).href
    }
  },
  methods: {
    initSakura() {
      const sakura = new SakuraSystem(this.$refs.sakuraCanvas)
      sakura.init()
    }
  },
  mounted() {
    // 自动播放保险（兼容更多浏览器）
    const playVideo = async () => {
      try {
        await this.$refs.bgVideo.play()
      } catch (e) {
        console.log('自动播放被阻止:', e)
        document.addEventListener('click', () => {
          this.$refs.bgVideo.play()
        }, { once: true })
      }
    }
    playVideo()

    this.initSakura();
  }
}
</script>

<template>
  <!-- 主容器：相对定位 + 全屏高度 + 隐藏溢出 -->
  <div id="app" class="relative min-h-screen overflow-hidden">

    <!-- 视频背景层： 固定定位、四边的边距为0（紧贴填充父元素）、显示层级为-1（在其余内容的后面）-->
    <div class="fixed inset-0 z-[-1]">
      <!--   元素定位（同时不占用空间）、元素定位（原理没怎么懂）、宽度高度占满、保持图片视频的比例的同时尽可能填满容器、启动变换操作、向左向上平移自身长度50%、放大元素至125%、允许对元素应用图像效果、元素的亮度调整到原始亮度的 50%   -->
      <video
          ref="bgVideo"
          class="absolute top-1/2 left-1/2 w-full h-full object-cover transform -translate-x-1/2 -translate-y-1/2 scale-125 filter brightness-50"
          autoplay loop muted playsinline
          :poster="videoPoster"
      >
        <source src="../assets/video/LiuYing.mp4" type="video/mp4">
      </video>
      <canvas ref="sakuraCanvas" class="absolute inset-0"></canvas>
    </div>

    <!-- 固定设置按钮 -->
    <FixedSettings />

    <!-- 主内容区（独立滚动区域） -->
    <main class="relative z-10">

      <!-- 头像（居中偏上） -->
      <div class="mb-16 mt-[54vh]">
        <BreatheAvatar avatarSrc="/src/assets/image/LY1.png" />
      </div>

      <!-- 文字区（正中间） -->
      <div class="text-center  font-game glow-container">
        <!-- 每个字符单独控制 -->
        <h1 class="text-4xl md:text-6xl font-bold italic ">
          <span class="char mx-1" >[</span>
          <span class="char mx-1" style="--i:0">乘</span>
          <span class="char mx-1" style="--i:1">着</span>
          <span class="char mx-1" style="--i:2">梦</span>
          <span class="char mx-1" style="--i:3">想</span>
          <span class="char mx-1" style="--i:4">出</span>
          <span class="char mx-1" style="--i:5">发</span>
          <span class="char mx-1" style="--i:6">咯</span>
          <span class="char mx-1" >]</span>
        </h1>
      </div>

      <footer class="py-6 text-center text-white text-opacity-70">
        <p>© 我们的秘密基地</p>
      </footer>
    </main>
  </div>
</template>

<style scoped>

.font-game {
  font-family: 'GameFont', sans-serif;
}

/* 修复文字截断问题 */
.glow-container {
  padding: 20px 0; /* 增加上下空间 */
  overflow: visible; /* 允许溢出显示 */
}

.char {
  display: inline-block;
  font-style: italic;
  position: relative;
  color: #fff; /* 基础白色 */
  text-shadow:
      0 0 5px #fff,
      0 0 10px #ff69b4,
      0 0 20px #ff1493;
  transform: translateY(calc(var(--i) * 8px - 12px));
  animation: glow-pulse 1.5s ease-in-out infinite alternate;
  animation-delay: calc(var(--i) * 0.1s);
}


/* 强烈外发光 */
.char::after {
  content: attr(data-char);
  position: absolute;
  top: 0;
  left: 0;
  color: transparent;
  text-shadow:
      0 0 15px #fff,
      0 0 30px #ff69b4,
      0 0 45px #ff1493;
  z-index: -1;
  animation: glow-expand 2s ease-out infinite alternate;
}

/* 粉白光渐变核心 */
.char::before {
  content: attr(data-char);
  position: absolute;
  top: 0;
  left: 0;
  background: linear-gradient(to bottom, #ffb6c1, #ef7c8a);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  z-index: 1;
}



/* 起伏高度调整 */
.char:nth-child(odd) { transform: translateY(-10px); }
.char:nth-child(even) { transform: translateY(6px); }

/* 发光动画 */
@keyframes glow-pulse {
  0% { filter: brightness(1); }
  100% { filter: brightness(1.2); }
}

@keyframes glow-expand {
  0% { transform: scale(1); opacity: 0.8; }
  100% { transform: scale(1.2); opacity: 1; }
}

</style>