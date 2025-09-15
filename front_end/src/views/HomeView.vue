<script setup>
  import { ref, computed, onMounted } from "vue";
  import axios from "axios";
  import NavCards from '@/components/NavCards.vue'
  import Header from "@/components/Header.vue";
  import Footer from "@/components/Footer.vue";

  const navs = [
    {id: 1, name: "Video", info: "Get faster in your hot lap", img: "/CardOutLook/VideoOutLook.jpg"},
    {id: 2, name: "Plugin", info: "Your good practice partner", img: "/CardOutLook/PluginOutLook.png"},
    {id: 3, name: "Cars", info: "I drive", img: "/CardOutLook/CarOutLook.jpg"},
    {id: 4, name: "Skins", info: "Pick up your favorable painted", img: "/CardOutLook/SkinOutLook.jpg"},
    {id: 5, name: "Tracks", info: "I race", img: "/CardOutLook/TrackOutLook.png"}
  ]

  function scrollToNavDetails() {
    const el = document.getElementById('nav_details_section')
    if(!el) return
    el.scrollIntoView({behavior: 'smooth', block: 'start'})
  }

  function scrollToAboutUs() {
    const el = document.getElementById('description_section')
    if(!el) return
    el.scrollIntoView({behavior: 'smooth', block: 'start'})
  }

  function scrollToNewest() {
    const el = document.getElementById('newest_section')
    if(!el) return
    el.scrollIntoView({behavior: 'smooth', block: 'start'})
  }

  const newest = ref([])
  // 固定 5 张卡片（回退用）
  const fallbackNewest = [
    {id: 1, name: "Oh no!", info: "Azusa can't find the newest for you", img: "/NewestNotFound.jpg"},
    {id: 2, name: "Oh no!", info: "Azusa can't find the newest for you", img: "/NewestNotFound.jpg"},
    {id: 3, name: "Oh no!", info: "Azusa can't find the newest for you", img: "/NewestNotFound.jpg"},
    {id: 4, name: "Oh no!", info: "Azusa can't find the newest for you", img: "/NewestNotFound.jpg"},
    {id: 5, name: "Oh no!", info: "Azusa can't find the newest for you", img: "/NewestNotFound.jpg"}
  ]

  // 统一展示用的数据（优先使用后端，否则回退）
  const displayNewest = computed(() => {
    return newest.value.length > 0 ? newest.value : fallbackNewest
  })

  onMounted(async () => {
    try {
      const res = await axios.get("/api/newest-images")
      if (Array.isArray(res.data) && res.data.length > 0) {
        // 把后端返回的数组赋值
        newest.value = res.data
      }
    } catch (err) {
      console.error("获取轮播图失败：", err)
      console.warn("获取最新推荐失败，使用回退数据:", err)
      // 保持为空 → 自动走 fallback
      newest.value = []
    }
  })
</script>

<template>
  <Header />
  <main class="home">
    <!-- 核心区（hero区） -->
    <section class="hero">
      <div class="hero__bg">
        <div class="hero__image">
          <el-image style="width: 100%; height: 100%;" fit="cover"
                    src="Home.jpg"/>
        </div>
      </div>
      <div class="hero__container">
        <h1 class="hero__title">BWOAH!</h1>
        <p class="hero__subtitle">Welcome to ACStation!</p>
      </div>
      <!-- 按钮 -->
      <div class="btn">
        <div class="btn__container">
        <button class="btn__explore" @click="scrollToNavDetails">Explore</button>
        <button class="btn__about" @click="scrollToAboutUs">About Us</button>
        </div>
      </div>
    </section>

    <!-- 导航详细展示区 -->
    <section id="nav_details_section" class="nav_details">
      <div class="nav_details__bg">
        <div class="nav_details__image">
          <el-image style="width: 100%; height: 100%;" fit="contain"
                    src="cardsBG.png"/>
        </div>
      </div>
      <!-- 导航卡片容器 -->
      <div class="nav_details__container">
        <h1 class="nav_details__title">Search your liked</h1>
        <div class="nav_details__grid">
          <!-- 触发链接跳转 -->
          <NavCards v-for="nav in navs" :key="nav.id" :nav_card="nav" @click="$router.push(`/${nav.name}`)" />
        </div>
        <!-- 按钮 -->
        <div class="btn">
          <div class="btn__container">
            <button class="btn__newest" @click="scrollToNewest">view the newest</button>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新推荐区 -->
    <section id="newest_section" class="newest">
      <div class="newest__bg">
        <div class="newest__image">
          <el-image style="width: 100%; height: 100%;" fit="contain"
                    src="cardsBG.png"/>
        </div>
      </div>
      <div class="newest__container">
        <h1 class="newest__title">What is new currently?</h1>
        <!-- 轮播图 -->
        <div class="newest__carousel">
          <el-carousel class="custom-carousel" height="400px" interval="3000" type="card" v-if="displayNewest.length">
            <el-carousel-item v-for="item in displayNewest" :key="item.id">
              <div class="carousel__card">
                <!-- 把图片包一层figure，用flex实现“图在上，字在下”，并把悬浮层只盖在图片上 -->
                <!-- element plus的carousel等在渲染成HTML时，会生成额外的<div>包裹（container、track、item），即外层wrapper，这使CSS的 :hover在scoped下有时无法跨越wrapper正确匹配到内部的标签 -->
                <!-- 用 @mouseenter / @mouseleave 直接给 <img> 加类，就绕过了 wrapper 的影响，CSS可以直接匹配 -->
                <!-- 给 <img> 本身加/移除类，让 CSS 匹配直接作用在目标元素上 -->
                <div class="carousel__figure">
                  <img class="carousel__img"
                       :src="item.img" :alt="item.name"
                       @mouseenter="$event.currentTarget.classList.add('hovered')"
                       @mouseleave="$event.currentTarget.classList.remove('hovered')"
                  />
                  <!-- info不再常驻显示，改为hover显示 -->
                  <div class="carousel__overlay">
                    <p>{{ item.info }}</p>
                  </div>
                </div>
                <div class="carousel__name">{{ item.name }}</div>
              </div>
            </el-carousel-item>
          </el-carousel>
          <p v-else>正在加载最新推荐内容......</p>
        </div>
      </div>
    </section>

    <!-- 介绍区 -->
    <section id="description_section" class="description">
      <div class="description__bg">
        <div class="description__image">
          <el-image style="width: 100%; height: 100%;" fit="contain"
                    src="cardsBG.png"/>
        </div>
      </div>
      <!-- 介绍信息区 -->
      <div class="description__container">
        <div class="description__about">
          <h2 class="description__title">About Us</h2>

          <!-- 介绍图文 part 1 -->
          <div class="description__part1">
            <div class="description__content">
              <h3>ACStation</h3>
              <P>
                欢迎来到ACStation！这是一个由 Shirasu_Azusa 独立开发的资源网站，
                在这里你可以找到 Azusa Racing 团队（好吧目前只有 Y.Z.Ben 一人）自制的游戏插件、赛道和车辆mod以及各类涂装，
                也可以观看 Azusa Racing 日常训练的车载录像
              </P>
            </div>
            <div class="description__dp">
              <img class="description__website" src="/DescriptionPhotos/DP1.png" alt="website image" />
            </div>
          </div>

          <!-- 介绍图文 part 2 -->
          <div class="description__part2">
            <div class="description__content">
              <h3>Azusa Racing</h3>
              <p>
                Azusa Racing 是由 Y.Z.Ben 于2025年7月14日组建的模拟赛车队，主要参加Hipole举办的各项模拟赛车赛事，
                目前正在向TCR黄金联赛等更高级别的赛事进发
              </p>
            </div>
            <div class="description__dp">
              <img class="description__team" src="/DescriptionPhotos/DP2.png" alt="team image" />
            </div>
          </div>

          <!-- 介绍图文 part 3 -->
          <div class="description__part3">
            <div class="description__content">
              <h3>Y.Z.Ben</h3>
              <p>
                Azusa Racing 创始人，8个月模拟赛车新人，B站账号Sinkhorn，参赛用名Shirasu_Azusa
              </p>
            </div>
            <div class="description__dp">
              <img class="description__leader" src="/DescriptionPhotos/DP3.PNG" alt="leader image" />
            </div>
          </div>
        </div>

        <!-- 按钮 -->
        <div class="btn">
          <div class="btn__container">
            <button class="btn__explore" @click="scrollToNavDetails">Start your journey</button>
          </div>
        </div>
      </div>
    </section>
  </main>
  <Footer />
</template>

<style scoped lang="scss">
.home {
  /* 根容器，让文字和按钮浮现在背景之上 */
  position: relative;
  margin: 0;
  display: block;
  flex-direction: column;
}

.hero {
  /* 文字层：绝对居中 */
  flex: 1;
  height: 100vh;
  position: relative;
  overflow: hidden;
  inset: 0;

  &__bg {
    position: absolute;
    inset: 0;
    z-index: 1;
    /* 淡入动画：opacity 0->1，持续1s */
    opacity: 0;
    animation: fadeInBg 0.8s ease-out 0s both;
    &::after {
      content: '';
      position: absolute;
      inset: 0;
      /* 45% 黑色蒙层 → 淡化背景 */
      background: rgba(0, 0, 0, 0.45);
    }
  }

  &__image {
    width: 100%;
    height: 100%;
  }

  &__title {
    position: absolute;
    left: 50%;
    z-index: 2;
    color: #fff;
    text-align: center;
    margin: 0;
    top: 50%;
    font-size: clamp(2.5rem, 6vw, 4rem);
    transform: translate(-50%, -100%);
    animation: fadeInTitle 0.8s ease-out 0.8s both;
  }

  &__subtitle {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    z-index: 2;
    color: #fff;
    text-align: center;
    margin: 0;
    top: calc(50% + 1.5rem);
    font-size: clamp(1.125rem, 2.5vw, 1.5rem);
    animation: fadeInSubtitle 0.8s ease-out 0.8s both;
  }
}

.btn {
  margin-top: 2rem;
  bottom: 2rem;
  text-align: center;

  &__container {
    position: absolute;
    left: 0;
    right: 0;
    top: calc(50% + 4.5rem);
    /* 整个容器水平居中 */
    /* transform: translateX(-50%); */
    display: flex;
    /* 按钮在容器内居中 */
    justify-content: center;
    gap: 1rem;
    z-index: 2;
  }

  &__newest, &__about, &__explore {
    /* 按钮的样式 */
    padding: 1rem 3rem;
    font-size: 1.25rem;
    background: #755b8a;
    color: #fff;
    border: none;
    border-radius: 0.5rem;
    cursor: pointer;
    animation: fadeInBtn 0.8s ease-out 1.6s both;
    transition: background 0.2s;
    &:hover { background: #5a2a92; }
  }
}

.nav_details {
  /* 让卡片区域紧跟在 Hero 之后，占剩余高度 */
  /* 第二屏高度 */
  min-height: 100vh;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: flex-start;
  position: relative;
  padding: 2rem 1rem;

  &__bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 0;
    &::after {
      content: '';
      position: absolute;
      inset: 0;
      /* 45% 黑色蒙层 → 淡化背景 */
      background: rgba(0, 0, 0, 0.45);
    }
  }

  &__image {
    width: 100%;
    height: 100%
  }

  &__container {
    z-index: 1;
    max-width: 80rem;
    margin: 0 auto;
  }

  &__title {
    font-size: 2rem;
    text-align: center;
    z-index: 2;
    color: #181818;
    margin-bottom: 2rem;
  }

  &__grid {
    display: grid;
    /* 5 列等宽 */
    grid-template-columns: repeat(5, 1fr);
    gap: 1.5rem;
  }
}

.newest {
  min-height: 100vh;   /* 至少一屏 */
  position: relative;  /* 让子元素的绝对定位基于它 */
  display: flex;       /* 可选：让内部内容居中 */
  align-items: center;
  justify-content: center;

  &__container {
    z-index: 1;
    display: block;
    width: 100%;
    max-width: 80rem;
    margin: 0 auto;
  }

  &__bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 0;
    &::after {
      content: '';
      position: absolute;
      inset: 0;
      /* 45% 黑色蒙层 → 淡化背景 */
      background: rgba(0, 0, 0, 0.45);
    }
  }

  &__image {
    width: 100%;
    height: 100%
  }

  &__title {
    font-size: 2rem;
    text-align: center;
    z-index: 2;
    color: #181818;
    margin-bottom: 2rem;
  }

  &__carousel {
    position: relative;
    max-width: 700px;
    /* 确保在背景图之上 */
    z-index: 1;
    width: 80%;
    margin: 0 auto;
    padding: 2rem 0;
    text-align: center;
  }
}

.carousel {
  &__card {
    display: flex;
    flex-direction: column;
    /* 保证占满 carousel 高度 */
    height: 100%;
  }

  &__figure {
    flex: 1 1 auto;
    /* 图片固定高度 */
    min-height: 350px;
    position: relative;
    border-radius: 12px;
    overflow: hidden;
    cursor: pointer; /* 可选：提升交互感 */
  }

  &__figure img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: filter 0.25s ease;
  }

  /* 悬停时效果 */
  &__img.hovered {
    filter: brightness(50%);
    transition: filter 0.25s ease;
  }

  /* 相邻兄弟选择器：img.hovered + overlay */
  &__img.hovered + &__overlay {
    opacity: 1;
    transition: opacity 0.25s ease;
  }

  /* 默认隐藏 info */
  &__overlay {
    position: absolute;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    background: rgba(0,0,0,0.55);
    opacity: 0;
    pointer-events: none;
    z-index: 2;
    transition: opacity 0.25s ease;
    border-radius: 12px;
  }

  &__name {
    flex: 0 0 auto;
    margin-top: 0.5rem;
    font-size: 1.2rem;
    font-weight: bold;
    color: #fff;
    text-align: center;
    /* 半透明黑底，避免和背景混 */
    background: rgba(0, 0, 0, 0.6);
    border-radius: 6px;
    padding: 0.3rem 0.6rem;
    display: inline-block;
    margin-left: auto;
    margin-right: auto;
  }
}

/* 覆盖 Element Plus 内部样式：使用深度选择器 ::v-deep */
::v-deep .custom-carousel {
  /* 给指示器留出空间，并允许箭头溢出显示 */
  overflow: visible !important;
  padding-bottom: 44px; /* 给底部指示器腾出空间（可调） */
}

/* 箭头：放到轮播两侧外部、更明显、更大 */
::v-deep .custom-carousel .el-carousel__arrow {
  width: 44px;
  height: 44px;
  line-height: 44px;
  border-radius: 50%;
  background-color: rgba(0,0,0,0.65) !important;
  color: #fff !important;
  box-shadow: 0 8px 20px rgba(0,0,0,0.28);
  font-size: 18px !important;
  z-index: 50;
  opacity: 1 !important; /* 确保不透明 */
}

/* 鼠标悬停时更高亮 */
::v-deep .custom-carousel .el-carousel__arrow:hover {
  background-color: rgba(0,0,0,0.85) !important;
}

/* 把箭头移到轮播外侧（左/右） */
::v-deep .custom-carousel .el-carousel__arrow--left {
  left: -320px !important;   /* 挪到左侧外面 */
}
::v-deep .custom-carousel .el-carousel__arrow--right {
  right: -320px !important;  /* 挪到右侧外面 */
}

/* 指示器：下移到轮播外，且更醒目 */
::v-deep .custom-carousel .el-carousel__indicators {
  bottom: -5px !important;  /* 指示器整体下移 */
  z-index: 30;
}
::v-deep .custom-carousel .el-carousel__indicator button {
  width: 38px;
  height: 6px;
  border-radius: 4px;
  background-color: rgba(255,255,255,0.35) !important;
  border: none !important;
  transition: background-color .2s ease, transform .18s ease;
  margin: 0 6px !important;
}
::v-deep .custom-carousel .el-carousel__indicator.is-active button {
  background-color: #5a2a92 !important; /* 你的主色高亮 */
  transform: scale(1.05);
}

/* 如果你想把指示器改成点状小圆圈（可选）：
::v-deep .custom-carousel .el-carousel__indicator button { width:10px; height:10px; border-radius:50%; }
*/

/* 如果轮播项底部文字仍被背景干扰，保证名字区有背景 */
.carousel__name {
  display:inline-block;
  background: rgba(0,0,0,0.6);
  color: #fff;
  padding: .25rem .6rem;
  border-radius: 6px;
  margin-top: .5rem;
  z-index: 20;
}

/* 确保箭头不会被轮播卡片遮盖（提高 z-index） */
::v-deep .custom-carousel .el-carousel__arrow,
::v-deep .custom-carousel .el-carousel__indicators {
  pointer-events: auto;
}

/* 可选：在移动端缩小箭头与指示器尺寸 */
@media (max-width: 768px) {
  ::v-deep .custom-carousel .el-carousel__arrow { width:36px; height:36px; left: -40px !important; right: -40px !important; font-size:16px !important; }
  ::v-deep .custom-carousel .el-carousel__indicators { bottom: -26px !important; }
  ::v-deep .custom-carousel .el-carousel__indicator button { margin: 0 4px !important; width: 28px; height: 5px; }
}

.description {
  min-height: 100vh;   /* 至少一屏 */
  position: relative;  /* 让子元素的绝对定位基于它 */
  display: flex;       /* 可选：让内部内容居中 */
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  padding: 2.5rem 1rem 8rem;

  &__bg {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 0;
    &::after {
      content: '';
      position: absolute;
      inset: 0;
      /* 45% 黑色蒙层 → 淡化背景 */
      background: rgba(0, 0, 0, 0.45);
    }
  }

  &__image {
    width: 100%;
    height: 100%
  }

  &__container {
    position: relative;
    z-index: 1;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem;
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }

  &__about {
    position: relative;
    display: block;
    grid-template-columns: 1fr 420px; /* 左右两列 */
    /* 第一行为 About 标题，下面 3 行为对应文段/图片 */
    grid-template-rows: auto repeat(3, minmax(140px, auto));
    gap: 2rem;
    align-items: start; /* 顶部对齐 */
    z-index: 1;
    justify-content: center;
    max-width: 1200px;
    margin: -40px;
    padding: 2rem;
    /* 明显的半透明渐变边框 + 轻微内阴影，和模糊玻璃效果  */
    background: linear-gradient(135deg, rgba(90,42,146,0.2), rgba(255,255,255,0.05));
    /* 模糊玻璃效果 */
    backdrop-filter: blur(50px);
    border-radius: 1rem;
  }

  &__title {
    transform: translateY(-24px);
    margin-bottom: 1.25rem;
    transition: transform 180ms ease;
  }

  &__part1, &__part2, &__part3 {
    display: grid;
    grid-template-columns: 1fr 360px;
    gap: 1.25rem 2rem;
    align-items: start;
    margin-bottom: 1.5rem;
  }

  &__content {
    margin-top: -15px;
    grid-column: 1;
    align-self: start;
    padding-right: 0.5rem;
    border-radius: 0.75rem;
    object-fit: cover;
  }

  &__dp {
    display: flex;
    flex-direction: column;   /* 纵向排列 */
    gap: 1.5rem;              /* 图片之间间距 */
    align-items: center;      /* 居中对齐 */
  }

  &__website, &__team, &__leader {
    width: 100%;
    max-width: 350px;
    border-radius: 0.75rem;
    object-fit: cover;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  }

  .btn__container {
    position: absolute;
    left: 50%;
    top: 100%;               /* 放到 container 底部外侧 */
    transform: translateX(-50%);
    margin-top: 1rem;        /* 与 container 底部的距离（可调整） */
    display: flex;
    justify-content: center;
    gap: 1rem;
    z-index: 5;
  }
}

/* 小屏自适应：≥1200px 保持 5 列，<1200px → 自动换行 */
@media (max-width: 1200px) {
  .cars__grid {
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  }
}

/* 移动端展示介绍区改成上下排列 */
@media (max-width: 768px) {
  .description__about {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}

@keyframes fadeInBg { to { opacity: 1; } }
@keyframes fadeInTitle {
  0%   { opacity: 0; transform: translate(-50%, -100%) translateY(1rem); }
  100% { opacity: 1; transform: translate(-50%, -100%) translateY(0); }
}
@keyframes fadeInSubtitle {
  0%   { opacity: 0; transform: translate(-50%, 0) translateY(1rem); }
  100% { opacity: 1; transform: translate(-50%, 0) translateY(0); }
}
@keyframes fadeInBtn {
  0%   { opacity: 0; transform: translateY(1rem); }
  100% { opacity: 1; transform: translateY(0); }
}
</style>