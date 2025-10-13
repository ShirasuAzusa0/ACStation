<script setup>
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import axios from "axios";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import MarkdownIt from "markdown-it";
import markdownItMark from 'markdown-it-mark';
import {ArrowLeftBold, ArrowRightBold, Download} from "@element-plus/icons-vue";
import {useUserStore} from "@/stores/user.js";
import {useAlertStore} from "@/stores/alert.js";

const userStore = useUserStore()
const alertStore = useAlertStore()
const route = useRoute();
const router = useRouter();
const carModName = route.params.carModName;
const type = "cars"

// 点赞状态判断
const isLiked = ref(false);

const car = ref({
  carModName: "",
  description: "",
  images: [],
  likes: 0,
  views: 0,
  downloads: 0,
  createdAt: "",
});

const currentIndex = ref(0);
const loading = ref(true);
const error = ref("");
const descriptionHtml = ref("");

// Markdown 渲染器
const md = new MarkdownIt();
md.use(markdownItMark);

// 图片路径解析
function getPreviewPath(preview) {
  // 若为空，返回一个默认图片路径
  if (preview === 'none') {
    return '/NewestNotFound.jpg'
  }
  return `http://localhost:8080/${preview.replace(/^\/?/, '')}`
}

// 获取具体车辆模组资源信息
async function fetchCarDetail() {
  try {
    const res = await axios.get(`/api/source/cars/${encodeURIComponent(carModName)}`);
    if (res.data?.status === "success") {
      car.value = res.data.data;
      console.log(car.value);
      if (!Array.isArray(car.value.images)) car.value.images = [];
      descriptionHtml.value = md.render(car.value.description || "");
      // 修正 currentIndex 如果图片数减少导致越界
      if (car.value.images.length === 0) currentIndex.value = 0;
      else currentIndex.value = Math.min(currentIndex.value, car.value.images.length - 1);
    } else {
      error.value = res.data?.msg || "加载车辆模组详情失败";
    }
  } catch (err) {
    console.error("加载车辆模组详情出错:", err);
    error.value = err.message;
  } finally {
    loading.value = false;
  }
}

// 更新浏览量 views
const updateViews = async () => {
  try {
    await axios.post(`/api/source/cars/${encodeURIComponent(carModName)}/views`);
  } catch (err) {
    console.warn("更新浏览量失败:", err);
  }
}

// 概览图集切换
const nextImage = () => {
  if (!car.value?.images || car.value.images.length === 0) return;
  currentIndex.value = (currentIndex.value + 1) % car.value.images.length;
};
const prevImage = () => {
  if (!car.value?.images || car.value.images.length === 0) return;
  currentIndex.value = (currentIndex.value - 1 + car.value.images.length) % car.value.images.length;
};

// 点赞/取消点赞
const like = async () => {
  try {
    const token = userStore.token;
    if (!token) {
      console.warn("请先登录再点赞");
      alertStore.showAlertMessage("warning", "请先登录再点赞 ꒰ঌ(🎀 ᗜ`˰´ᗜ 🌸)໒꒱ ❌");
      await router.push("/Welcome/Sign-In")
      return;
    }
    const res = await axios.put(
        `/api/source/cars/${encodeURIComponent(carModName)}/like`,
        {},
        { headers: { Authorization: `${token}` } }
    );
    if (res.data?.status === "success") {
      // 点赞数量
      car.value.likes = res.data.likeCount;
      // 切换点赞状态
      isLiked.value = res.data.likeStatus;

      // 小动画反馈
      const btn = document.querySelector(".btn__like");
      if (btn) {
        btn.classList.add("animate");
        setTimeout(() => btn.classList.remove("animate"), 300);
      }

      alertStore.showAlertMessage("success", isLiked.value ? "点赞成功 ꒰ঌ(🎀 ᗜ`v´ᗜ 🌸)໒꒱ ✅" : "取消点赞  ꒰ঌ(🎀 ᗜ`˰´ᗜ 🌸)໒꒱ ❌");
      console.info(isLiked.value ? "点赞成功 👍" : "取消点赞 👎");
    } else {
      alertStore.showAlertMessage("error", "点赞失败 ꒰ঌ(🎀 ᗜ`˰´ᗜ 🌸)໒꒱ ❌");
      console.warn(res.data?.msg || "点赞失败");
    }
  } catch (err) {
    alertStore.showAlertMessage("error", "点赞出错 ꒰ঌ(🎀 ᗜ`˰´ᗜ 🌸)໒꒱ ❌");
    console.error("点赞出错:", err);
  }
};

// 资源下载
const download = async () => {
  try {
    const token = userStore.token;
    if (!token) {
      console.error("请先登录再下载");
      alertStore.showAlertMessage("warning", "请先登录再下载 ꒰ঌ(🎀 ᗜ`˰´ᗜ 🌸)໒꒱ ❌");
      await router.push("/Welcome/Sign-In")
      return;
    }
    const res = await axios.get(`/api/source/${encodeURIComponent(type)}/${encodeURIComponent(carModName)}/download`, { responseType: "blob", headers: { Authorization: `${token}` } });
    // 创建下载链接
    const blob = new Blob([res.data], { type: "application/zip" });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = `${carModName}.zip`;
    link.click();
    window.URL.revokeObjectURL(url);
    alertStore.showAlertMessage("success", "开始下载 ꒰ঌ(🎀 ᗜ`v´ᗜ 🌸)໒꒱ ✅");
    console.info("开始下载");
  } catch (err) {
    alertStore.showAlertMessage("error", "下载失败 ꒰ঌ(🎀 ᗜ`˰´ᗜ 🌸)໒꒱ ❌");
    console.error("下载失败：" + (err.response?.data?.msg || err.message));
  }
}

onMounted(() => {
  fetchCarDetail();
  updateViews();
});
</script>

<template>
  <Header />
  <!-- 全局提示框 -->
  <transition name="fade">
    <el-alert
        v-if="alertStore.showAlert"
        :title="alertStore.alertMessage"
        :type="alertStore.alertType"
        show-icon
        effect="dark"
        class="global-alert"
    />
  </transition>
  <main class="carDetail">
    <div class="carDetail__container" v-if="!loading && car">
      <!-- 概览图集 -->
      <div class="carDetail__imageSet">
        <h2 class="carDetail__title">{{ car.carModName }}</h2>
        <div class="imageSet__wrapper">
          <button class="nav-btn left" @click="prevImage">
            <el-icon><ArrowLeftBold /></el-icon>
          </button>
          <img
              v-if="car.images && car.images.length > 0 && car.images[currentIndex]"
              :src="getPreviewPath(car.images[currentIndex])"
              alt="car preview"
              class="imageSet__image"
          />
          <div v-else class="no-image">暂无图片</div>
          <button class="nav-btn right" @click="nextImage">
            <el-icon><ArrowRightBold /></el-icon>
          </button>
        </div>
      </div>
      <!-- 介绍区 -->
      <div class="carDetail__description markdown-body" v-html="descriptionHtml"></div>
      <!-- 用户操作 -->
      <div class="carDetail__userOperate">
        <div class="btn">
          <div class="btn__container">
            <button class="btn__download" @click="download">
              <el-icon><Download /></el-icon>
              下载 Download</button>
            <button class="btn__like" :class="{ active: isLiked }" @click="like">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-hand-thumbs-up" viewBox="0 0 16 16">
                <path d="M8.864.046C7.908-.193 7.02.53 6.956 1.466c-.072 1.051-.23 2.016-.428 2.59-.125.36-.479 1.013-1.04 1.639-.557.623-1.282 1.178-2.131 1.41C2.685 7.288 2 7.87 2 8.72v4.001c0 .845.682 1.464 1.448 1.545 1.07.114 1.564.415 2.068.723l.048.03c.272.165.578.348.97.484.397.136.861.217 1.466.217h3.5c.937 0 1.599-.477 1.934-1.064a1.86 1.86 0 0 0 .254-.912c0-.152-.023-.312-.077-.464.201-.263.38-.578.488-.901.11-.33.172-.762.004-1.149.069-.13.12-.269.159-.403.077-.27.113-.568.113-.857 0-.288-.036-.585-.113-.856a2.144 2.144 0 0 0-.138-.362 1.9 1.9 0 0 0 .234-1.734c-.206-.592-.682-1.1-1.2-1.272-.847-.282-1.803-.276-2.516-.211a9.84 9.84 0 0 0-.443.05 9.365 9.365 0 0 0-.062-4.509A1.38 1.38 0 0 0 9.125.111L8.864.046zM11.5 14.721H8c-.51 0-.863-.069-1.14-.164-.281-.097-.506-.228-.776-.393l-.04-.024c-.555-.339-1.198-.731-2.49-.868-.333-.036-.554-.29-.554-.55V8.72c0-.254.226-.543.62-.65 1.095-.3 1.977-.996 2.614-1.708.635-.71 1.064-1.475 1.238-1.978.243-.7.407-1.768.482-2.85.025-.362.36-.594.667-.518l.262.066c.16.04.258.143.288.255a8.34 8.34 0 0 1-.145 4.725.5.5 0 0 0 .595.644l.003-.001.014-.003.058-.014a8.908 8.908 0 0 1 1.036-.157c.663-.06 1.457-.054 2.11.164.175.058.45.3.57.65.107.308.087.67-.266 1.022l-.353.353.353.354c.043.043.105.141.154.315.048.167.075.37.075.581 0 .212-.027.414-.075.582-.05.174-.111.272-.154.315l-.353.353.353.354c.047.047.109.177.005.488a2.224 2.224 0 0 1-.505.805l-.353.353.353.354c.006.005.041.05.041.17a.866.866 0 0 1-.121.416c-.165.288-.503.56-1.066.56z"/>
              </svg>
              点赞 like{{ car.likes }}</button>
          </div>
        </div>
      </div>
    </div>
    <div v-else-if="loading" class="loading">正在加载车辆模组详情...</div>
    <div v-else class="error">{{ error }}</div>
  </main>
  <Footer />
</template>

<style scoped lang="scss">
.global-alert {
  position: fixed;
  top: 80px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  width: 400px;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

.carDetail {
  padding: 2rem;
  display: flex;
  justify-content: center;
  background: var(--color-detail-bg);

  &__container {
    max-width: 960px;
    width: 100%;
  }

  &__imageSet {
    position: relative;
    text-align: center;
    margin-bottom: 1rem;

    .carDetail__title {
      margin-bottom: 0.5rem;
      font-size: 1.5rem;
      font-weight: bold;
    }

    .imageSet__wrapper {
      width: 600px;
      height: 300px;
      position: relative;
      display: inline-block;


      .imageSet__image {
        width: 100%;
        height: 100%;
        object-fit: contain;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
      }

      .nav-btn {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        background: rgba(255, 255, 255, 0.8);
        border: none;
        border-radius: 50%;
        font-size: 1.5rem;
        cursor: pointer;
        padding: 0.3rem 0.7rem;
        transition: 0.2s;

        &:hover {
          background: #eee;
        }

        &.left {
          left: -2rem;
        }
        &.right {
          right: -2rem;
        }
      }
    }
  }

  &__description {
    margin-top: 1rem;
    padding: 1rem;
    background: var(--color-detail-bg);
    border-radius: 8px;
    transform: translateX(10%);
  }

  &__userOperate {
    margin-top: 1.5rem;
    text-align: center;

    .btn__container {
      display: flex;
      justify-content: center;
      gap: 1rem;
    }

    button {
      padding: 0.5rem 1rem;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 500;
      transition: 0.2s;
    }

    .btn__download {
      background: #755b8a;
      color: white;

      &:hover {
        background: #5a2a92;
      }
    }

    .btn__like {
      display: flex;
      align-items: center;
      gap: 6px;
      cursor: pointer;
      background: none;
      border: none;
      font-size: 16px;
      color: var(--color-detail-text);
      transition: all 0.2s ease;

      &.active {
        color: #5a2a92;           /* 自定义紫色为点赞高亮色 */
        transform: scale(1.05);
      }

      &.animate {
        animation: pop 0.3s ease;
      }

      svg {
        transition: transform 0.2s ease, fill 0.3s;
      }

      &:hover svg {
        transform: scale(1.2);
      }
    }
  }
}

@keyframes pop {
  0% { transform: scale(1); }
  50% { transform: scale(1.3); }
  100% { transform: scale(1); }
}
</style>