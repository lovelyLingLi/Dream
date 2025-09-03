<template>
  <div class="starfield-container">
    <!-- 星空画布 -->
    <canvas ref="canvas" class="absolute inset-0"></canvas>
    <!-- 边缘暗角效果 - 增强深度感 -->
    <div class="vignette-effect"></div>
    <!-- 提示文字 - 5秒后自动消失 -->
    <div v-if="showHint" class="hint-text">移动鼠标改变方向</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

// ==================== 常量定义 ====================
const NUM_STARS = 4000;          // 星星的总数量
const MAX_ANGLE_Y = Math.PI / 3; // 最大水平旋转角度
const DEFAULT_SPEED = 0.03;      // 默认初始速度

// 星星颜色配置及出现概率
const STAR_COLORS = [
  { color: 'rgb(106,169,252)', prob: 0.6 }, // 蓝色星星 60%
  { color: 'rgb(255,150,220)', prob: 0.3 }, // 粉色星星 30%
  { color: 'rgb(255,255,255)', prob: 0.1 }  // 白色星星 10%
];

// ==================== 响应式状态 ====================
const canvas = ref(null);        // Canvas DOM引用
const showHint = ref(true);      // 是否显示提示文字
const baseSpeed = ref(DEFAULT_SPEED); // 当前速度

// ==================== 非响应式状态 ====================
let animationId = null;          // 动画帧ID，用于清理
let stars = [];                  // 星星数组
let targetAngleY = 0;            // 目标水平角度（由鼠标位置决定）
let currentAngleY = 0;           // 当前水平角度（平滑过渡）
let W, H, ctx, bgGradient, bgImage, bgImageLoaded = false;
let positionPool = Array(100).fill().map(() => ({ x:0, y:0, z:0 })); // 创建坐标对象池

// ==================== 星星类定义 ====================
class Star {
  /**
   * 星星构造函数
   * @param {boolean} isInitial - 是否是初始创建（影响初始位置分布）
   */
  constructor(isInitial = false) {
    // 初始化星星属性
    this.reset(isInitial);
    // 每颗星星有独特的移动速度，使星空显得更自然
    this.speedFactor = 0.4 + Math.random() * 3;
    // 轨迹相关属性
    this.trail = [];
    this.trailLength = 15 + Math.floor(Math.random() * 25);
    this.trailVisibility = 0;  // 初始轨迹不可见
    // 光晕效果属性
    this.glowIntensity = 0.6 + Math.random() * 0.8;
    this.glowSize = 2 + Math.random() * 2;
    // 特殊标记：新创建的星星需要一段时间才显示轨迹
    this.isNew = true;
    this.age = 0;
    // 光线散射效果的随机旋转角度
    this.rotationOffset = Math.random() * Math.PI * 2;
  }

  /**
   * 重置星星位置和属性
   * @param {boolean} isInitial - 是否是初始创建
   */
  reset(isInitial = false) {
    // 在球面上随机生成星星位置（球坐标系）
    const r = Math.random() * W * 10;
    const theta = Math.random() * Math.PI * 2;
    const phi = (Math.random() * 1.0 - 0.8) * Math.PI;

    // 转换为笛卡尔坐标系
    let x = r * Math.cos(phi) * Math.sin(theta);
    let y = r * Math.sin(phi);
    let z = r * Math.cos(phi) * Math.cos(theta);

    // 避免星星集中在中心区域
    const centerDist = Math.sqrt(x*x + y*y + z*z);
    if (centerDist < W * 0.5) {
      const scaleFactor = (W * 0.5 + Math.random() * W) / centerDist;
      x *= scaleFactor; y *= scaleFactor; z *= scaleFactor;
    }

    // 保存原始坐标（用于旋转变换）
    this.origX = x; this.origY = y;
    // 初始化时，星星可以分布在不同深度；重置时，星星放在远处
    this.origZ = isInitial ? z : (W * 2 + Math.random() * W);
    this.x = x; this.y = y; this.z = z;

    // 初始化轨迹数组，但所有点都在同一位置（不会产生可见轨迹）
    this.trail = Array(this.trailLength).fill().map(() => ({ x, y, z }));
    // 重置轨迹可见度为0
    this.trailVisibility = 0;
    this.baseSize = 0.2 + Math.random() * 1.2;

    // 按概率选择星星颜色
    let rnd = Math.random(), total = 0;
    for (const colorData of STAR_COLORS) {
      total += colorData.prob;
      if (rnd <= total) {
        this.color = colorData.color;
        break;
      }
    }

    // 闪烁效果参数
    this.twinkleSpeed = 0.03 + Math.random() * 0.05;
    this.twinklePhase = Math.random() * Math.PI * 2;
    this.twinkleAmount = 0.1 + Math.random() * 0.3;

    // 标记为新创建的星星，并重置年龄
    this.isNew = true;
    this.age = 0;
  }

  /**
   * 更新星星位置和属性
   */
  update() {
    // 更新星星年龄
    this.age++;

    // 记录当前位置到轨迹数组
    const reusedPos = positionPool.pop() || { x:0, y:0, z:0 };
    reusedPos.x = this.x; reusedPos.y = this.y; reusedPos.z = this.z;

    if (this.trail.length >= this.trailLength) {
      positionPool.push(this.trail.shift()); // 回收旧对象
    }
    this.trail.push(reusedPos); // 使用池中对象

    // 轨迹可见度处理逻辑 - 新星星需要等待一定时间才显示轨迹
    if (this.isNew) {
      // 新星星轨迹可见度保持为0，直到年龄超过轨迹长度
      if (this.age > this.trailLength) {
        this.isNew = false;
      }
    } else {
      // 非新星星，基于当前速度调整轨迹可见度
      // 速度越快，轨迹越明显
      const speedFactor = baseSpeed.value / DEFAULT_SPEED;
      const targetVisibility = Math.min(1, speedFactor - 0.5);

      // 平滑过渡到目标可见度
      if (targetVisibility > this.trailVisibility) {
        this.trailVisibility = Math.min(targetVisibility, this.trailVisibility + 0.02);
      } else {
        this.trailVisibility = Math.max(targetVisibility, this.trailVisibility - 0.01);
      }
    }

    // 更新星星原始Z坐标（向观察者移动）- 速度会影响移动距离
    this.origZ -= baseSpeed.value * 50 * this.speedFactor;

    // 重置位置为原始坐标
    this.x = this.origX;
    this.y = this.origY;
    this.z = this.origZ;

    // 应用Y轴旋转变换（基于鼠标位置）
    const cosY = Math.cos(currentAngleY);
    const sinY = Math.sin(currentAngleY);
    this.x = this.origX * cosY - this.origZ * sinY;
    this.z = this.origZ * cosY + this.origX * sinY;

    // 如果星星太近，重置到远处
    if (this.origZ < 1) this.reset();
  }

  /**
   * 绘制星星及其效果
   */
  draw() {
    // Z轴偏移量，避免被除数为0
    const zOffset = 200;
    // 计算透视投影比例
    const scale = W / Math.max(1, this.z + zOffset);
    // 投影到2D屏幕坐标
    const sx = this.x * scale + W / 2;
    const sy = this.y * scale + H / 2;

    // 如果星星不在可视范围内，跳过绘制
    if (sx < -100 || sx > W + 100 || sy < -100 || sy > H + 100) return;

    // 计算闪烁效果
    const time = Date.now() * 0.001;
    const twinkle = 1 - this.twinkleAmount + this.twinkleAmount * Math.sin(time * this.twinkleSpeed + this.twinklePhase);

    // 基于距离计算大小和透明度
    const distanceFactor = Math.max(0, 1 - this.z / (W * 3));
    const size = Math.max(this.baseSize, 0.5 + distanceFactor * 4) * twinkle;
    const alpha = Math.min(1, Math.max(0.3, 1.2 - this.z / (W * 2))) * twinkle;

    // ======== 绘制拖尾 ========
    // 只有非新星星并且可见度足够且速度足够快时才绘制
    if (!this.isNew && this.trail.length >= 2 && this.trailVisibility > 0.05) {
      // 计算所有轨迹点的屏幕坐标
      const points = this.trail.map(p => {
        const s = W / Math.max(1, p.z + zOffset);
        return { x: p.x * s + W/2, y: p.y * s + H/2 };
      });
      points.push({x: sx, y: sy});  // 添加当前位置作为轨迹终点

      // 确保轨迹有足够长度才绘制（防止微小运动产生不必要的轨迹）
      if (Math.hypot(points[points.length-1].x - points[0].x, points[points.length-1].y - points[0].y) > 3) {
        // 提取RGB颜色值
        const rgb = this.color.match(/\d+/g);
        // 创建渐变轨迹
        const gradient = ctx.createLinearGradient(points[0].x, points[0].y, points[points.length-1].x, points[points.length-1].y);
        gradient.addColorStop(0, 'rgba(0,0,0,0)');  // 起点完全透明
        gradient.addColorStop(0.3, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * 0.1 * this.trailVisibility})`);  // 中间点微弱可见
        gradient.addColorStop(1, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * this.trailVisibility})`);  // 终点最亮

        // 绘制平滑的贝塞尔曲线轨迹
        ctx.strokeStyle = gradient;
        ctx.lineWidth = size * 0.8;
        ctx.beginPath();
        ctx.moveTo(points[0].x, points[0].y);
        for (let i = 1; i < points.length; i++) {
          const xc = (points[i].x + points[i-1].x) / 2;
          const yc = (points[i].y + points[i-1].y) / 2;
          ctx.quadraticCurveTo(points[i-1].x, points[i-1].y, xc, yc);
        }
        ctx.stroke();
      }
    }

    // 提取RGB颜色值
    const rgb = this.color.match(/\d+/g);

    // ======== 多层光晕效果 ========

    // 1. 外部大光晕 - 柔和扩散
    const outerGlowSize = size * 8 * (1 + distanceFactor);
    const outerGlow = ctx.createRadialGradient(sx, sy, 0, sx, sy, outerGlowSize);
    outerGlow.addColorStop(0, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * 0.2})`);
    outerGlow.addColorStop(0.5, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * 0.05})`);
    outerGlow.addColorStop(1, 'rgba(0,0,0,0)');

    ctx.fillStyle = outerGlow;
    ctx.beginPath();
    ctx.arc(sx, sy, outerGlowSize, 0, Math.PI * 2);
    ctx.fill();

    // 2. 中间光晕 - 较强扩散
    const midGlowSize = size * 4;
    const midGlow = ctx.createRadialGradient(sx, sy, 0, sx, sy, midGlowSize);
    midGlow.addColorStop(0, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * 0.4})`);
    midGlow.addColorStop(0.7, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * 0.1})`);
    midGlow.addColorStop(1, 'rgba(0,0,0,0)');

    ctx.fillStyle = midGlow;
    ctx.beginPath();
    ctx.arc(sx, sy, midGlowSize, 0, Math.PI * 2);
    ctx.fill();

    // 3. 内部光晕 - 强光核心
    const innerGlowSize = size * 2;
    const innerGlow = ctx.createRadialGradient(sx, sy, 0, sx, sy, innerGlowSize);
    innerGlow.addColorStop(0, `rgba(255,255,255,${alpha * 0.9})`);
    innerGlow.addColorStop(0.3, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * 0.6})`);
    innerGlow.addColorStop(1, `rgba(${rgb[0]/2},${rgb[1]/2},${rgb[2]/2},0)`);

    ctx.fillStyle = innerGlow;
    ctx.beginPath();
    ctx.arc(sx, sy, innerGlowSize, 0, Math.PI * 2);
    ctx.fill();

    // 4. 星星核心
    ctx.fillStyle = `rgba(255,255,255,${alpha})`;
    ctx.beginPath();
    ctx.arc(sx, sy, size * 0.8, 0, Math.PI * 2);
    ctx.fill();

    // 5. 光线散射效果 (对于亮星)
    // 速度越快，光线越明显
    const speedFactor = Math.min(1, baseSpeed.value / DEFAULT_SPEED);
    if (size > 1.5 && alpha > 0.7 && speedFactor > 0.7) {
      const rays = 4;
      const rayLength = size * (10 + speedFactor * 10);

      ctx.save();
      ctx.translate(sx, sy);
      // 旋转效果 - 使用星星的特定旋转偏移和当前时间
      ctx.rotate(time * 0.2 + this.rotationOffset);

      const rayGradient = ctx.createLinearGradient(0, 0, rayLength, 0);
      rayGradient.addColorStop(0, `rgba(255,255,255,${alpha * 0.7 * speedFactor})`);
      rayGradient.addColorStop(0.1, `rgba(${rgb[0]},${rgb[1]},${rgb[2]},${alpha * 0.3 * speedFactor})`);
      rayGradient.addColorStop(1, 'rgba(0,0,0,0)');

      ctx.strokeStyle = rayGradient;
      ctx.lineWidth = size * 0.4;

      // 绘制十字光芒
      for (let i = 0; i < rays; i++) {
        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(rayLength, 0);
        ctx.stroke();
        ctx.rotate(Math.PI / (rays/2));
      }

      ctx.restore();
    }
  }
}

// 定期输出浏览器内存使用情况
function logMemoryUsage() {
  if (performance.memory) {
    const usedJSHeap = performance.memory.usedJSHeapSize / (1024 * 1024); // 转换成 MB
    const jsHeapSizeLimit  = performance.memory.jsHeapSizeLimit  / (1024 * 1024); // 转换成 MB
    const memoryUsagePercent = (usedJSHeap / jsHeapSizeLimit ) * 100;

    console.log(`1当前JavaScript堆内存使用率: ${memoryUsagePercent.toFixed(2)}%`);
    console.log(`当前星星期望数为: ${getOptimalStarCount()}`);
  } else {
    console.log("浏览器不支持 memory API");
  }
}

// 每2秒钟输出一次内存使用情况
// setInterval(logMemoryUsage, 2000);

// 根据屏幕性能调整星星数量
const getOptimalStarCount = () => {
  const perf = window.performance?.memory;
  return perf && perf.usedJSHeapSize > perf.jsHeapSizeLimit * 0.7
      ? NUM_STARS / 2
      : NUM_STARS;
};

// ==================== 核心功能函数 ====================

/**
 * 初始化函数：设置画布、背景和创建星星
 */
const init = () => {
  // 设置画布尺寸为窗口大小
  W = canvas.value.width = window.innerWidth;
  H = canvas.value.height = window.innerHeight;
  ctx = canvas.value.getContext('2d');

  // 创建背景渐变色
  bgGradient = ctx.createLinearGradient(0, 0, 0, H);
  bgGradient.addColorStop(0, '#010b18');
  bgGradient.addColorStop(0.5, '#050616');
  bgGradient.addColorStop(1, '#08071a');

  // 加载背景图片
  bgImage = new Image();
  bgImage.src = new URL('@/assets/image/img2.png', import.meta.url).href;
  bgImage.onload = () => bgImageLoaded = true;

  // 创建指定数量的星星对象
  stars = Array.from({ length: getOptimalStarCount() }, () => new Star(true));

  // 5秒后隐藏提示文字
  setTimeout(() => showHint.value = false, 5000);
}

/**
 * 动画循环函数
 */
let lastTime = 0;
let frameCount = 0;
let fps = 0;


const animate = (timestamp) => {

  // 限制帧率，避免动画过快
  /*if (timestamp - lastTime < 16) { // ~60fps
    animationId = requestAnimationFrame(animate);
    return;
  }
  lastTime = timestamp;*/

  // 每秒输出一次帧率
  /*frameCount++;
  if (timestamp - lastTime >= 1000) {
    fps = frameCount; // 帧率为过去1秒内的帧数
    frameCount = 0; // 重置帧数计数
    lastTime = timestamp; // 更新 lastTime 为当前时间
    console.log(`FPS: ${fps}`);
  }*/

  // 平滑过渡当前角度到目标角度
  currentAngleY += (targetAngleY - currentAngleY) * 0.1;

  // 绘制背景渐变
  ctx.fillStyle = bgGradient;
  ctx.fillRect(0, 0, W, H);

  // 如果背景图片已加载，绘制背景图片并添加视差效果
  if (bgImageLoaded) {
    const bgOffsetX = -currentAngleY * W / MAX_ANGLE_Y * 0.5;
    const bgWidth = W * 1.8;
    ctx.globalAlpha = 0.3;  // 设置背景透明度
    ctx.drawImage(bgImage, (W - bgWidth)/2 + bgOffsetX, 0, bgWidth, H);
    ctx.globalAlpha = 1.0;  // 恢复默认透明度
  }

  // 更新并绘制所有星星
  stars.forEach(star => {
    star.update();
    star.draw();
  });

  // 请求下一帧动画
  animationId = requestAnimationFrame(animate);
}

/**
 * 鼠标移动事件处理函数 - 根据鼠标水平位置更新目标角度
 */
const handleMouseMove = e => {
  targetAngleY = ((e.clientX / W) - 0.5) * MAX_ANGLE_Y;
}

/**
 * 窗口大小改变事件处理函数 - 更新画布尺寸和背景渐变
 */
const handleResize = () => {
  W = canvas.value.width = window.innerWidth;
  H = canvas.value.height = window.innerHeight;
  bgGradient = ctx.createLinearGradient(0, 0, 0, H);
  bgGradient.addColorStop(0, '#010b18');
  bgGradient.addColorStop(0.5, '#050616');
  bgGradient.addColorStop(1, '#08071a');
}

const handleTouchMove = (e) => {
  if (e.touches.length === 1) {
    const touch = e.touches[0]
    targetAngleY = ((touch.clientX / W) - 0.5) * MAX_ANGLE_Y
  }
}

// ==================== 生命周期钩子 ====================

/**
 * 组件挂载后的初始化
 */
onMounted(() => {
  init();
  animate();
  window.addEventListener('mousemove', handleMouseMove);
  window.addEventListener('resize', handleResize);

  // 触摸设备支持
  window.addEventListener('touchmove', handleTouchMove);
});

/**
 * 组件卸载前的清理
 */
onUnmounted(() => {
  // 取消动画循环
  if (animationId) {
    cancelAnimationFrame(animationId);
  }
  // 移除所有事件监听器，防止内存泄漏
  window.removeEventListener('mousemove', handleMouseMove);
  window.removeEventListener('resize', handleResize);
  window.removeEventListener('touchmove', handleTouchMove);
});
</script>

<style scoped>
.starfield-container {
  @apply fixed inset-0 overflow-hidden;
  z-index: -1; /* 确保背景在其他内容之后 */
}

.vignette-effect {
  @apply absolute inset-0;
  background: radial-gradient(circle, transparent 70%, rgba(0,0,0,0.7) 100%);
  pointer-events: none; /* 允许鼠标事件穿透 */
}

.hint-text {
  @apply fixed bottom-5 left-5 text-white/40 font-sans text-sm;
  transition: opacity 1s;
  pointer-events: none;
  opacity: v-bind('showHint ? 1 : 0');
}
</style>
