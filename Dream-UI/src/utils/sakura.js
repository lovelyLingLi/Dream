// 樱花飞舞
export default class SakuraSystem {
    constructor(canvas) {
        this.canvas = canvas
        this.ctx = canvas.getContext('2d')
        this.sakuraArray = []
        this.mouseX = 0
        this.SAKURA_SUM = 30
        this.windDirection = 0 // 全局风场

        // 初始化樱花图片
        this.sakuraImg = new Image()
        this.sakuraImg.src = '/src/assets/image/sakura.png'
    }

    updateWind() {
        this.windDirection = Math.sin(Date.now() * 0.0005) * 0.02
    }

    init() {
        this.resizeCanvas()
        this.sakuraImg.onload = () => {
            for (let i = 0; i < this.SAKURA_SUM; i++) {
                this.sakuraArray.push(new this.Sakura(this))
            }
            this.render()
        }

        window.addEventListener('resize', this.resizeCanvas.bind(this))
        window.addEventListener('mousemove', this.touchHandler.bind(this))
        window.addEventListener('touchmove', this.touchHandler.bind(this))
    }

    // 樱花类（内部类）
    Sakura = class {
        constructor(sys) {
            this.system = sys
            this.windForce = Math.random() * 0.02 - 0.01 // 每个花瓣独立的风力系数
            this.speedVariation = Math.random() * 0.1 + 0.95
            this.reset()
        }

        reset() {
            this.x = Math.random() * this.system.canvas.width
            this.y = -this.height
            this.width = Math.random() * 10 + 15
            this.height = Math.random() * 8 + 12
            this.opacity = this.width / 50
            this.rotate = Math.random()
            this.xSpeed = Math.random() * -2 - 1
            this.ySpeed = Math.random() * 0.8 + 1
            this.rotateSpeed = Math.random() * 0.03
        }

        draw() {
            if (this.x > this.system.canvas.width || this.y > this.system.canvas.height) {
                this.reset()
            }

            this.system.ctx.save()
            this.system.ctx.globalAlpha = this.opacity
            this.system.ctx.translate(this.x, this.y)
            this.system.ctx.rotate(this.rotate)
            this.system.ctx.drawImage(
                this.system.sakuraImg,
                -this.width/2,
                -this.height/2,
                this.width,
                this.height
            )
            this.system.ctx.restore()
        }

        animate() {
            // 应用全局风场 + 局部随机风
            const currentWind = this.system.windDirection + this.windForce

            this.xSpeed = this.xSpeed * this.speedVariation + currentWind
            this.ySpeed = this.ySpeed * this.speedVariation + currentWind * 0.3

            // 限制速度范围
            this.xSpeed = Math.max(-3, Math.min(-0.5, this.xSpeed))
            this.ySpeed = Math.max(0.3, Math.min(1.8, this.ySpeed))

            this.x += this.xSpeed + this.system.mouseX * 3
            this.y += this.ySpeed + this.system.mouseX * 1
            this.rotate += this.rotateSpeed
            this.draw()
        }
    }

    render() {
        this.updateWind()
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height)
        this.sakuraArray.forEach(sakura => sakura.animate())
        requestAnimationFrame(this.render.bind(this))
    }

    resizeCanvas() {
        this.canvas.width = window.innerWidth
        this.canvas.height = window.innerHeight
    }

    touchHandler(e) {
        this.mouseX = (e.clientX || e.touches[0].clientX) / window.innerWidth
    }
}