<script setup>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import { ref, onMounted } from "vue";
import {VideoPlay, VideoPause} from "@element-plus/icons-vue";
import axios from "axios";

// 表单状态（排序规则、分类tag）
const form = ref({
  sort: "default",  // 默认按编号排序规则
  tag: null         // 当前分类（null则为全部）
})

// 更新 videos 数据后修改该值以实现动态加载效果
const listKey = ref(0)
// 控制是否触发过渡动画
const isTransitionEnabled = ref(false);

// 分类数据
const categories = ref([])
const categoriesLoading = ref(true)

// 视频数据
const videos = ref([])
const loading = ref(false)
const error = ref(null)

// 搜索关键字
const currentQuery = ref("")

// 几乎所有组件内逻辑函数都应该写成箭头函数
// 全局工具函数、对外暴露API和生命周期钩子里定义的函数适合用function常见

// 排序规则映射
function getChoiceFromSort(sort) {
  switch (sort) {
    case "default": return 1
    case "newest": return 2
    case "oldest": return 3
    case "most_popular": return 4
    default: return 5
  }
}

// 封面图片路径解析
function getPreviewPath(preview) {
  // 若为空，返回一个默认图片路径
  if (preview === 'none') {
    return '/NewestNotFound.jpg'
  }
  // 若图片路径以http或https开头，直接返回
  if (preview.startsWith('http') || preview.startsWith('https')) {
    // 如果是 B 站图片，使用更稳定的处理方式
    if (preview.includes('bilibili.com') || preview.includes('hdslb.com')) {
      // 使用images.weserv.nl代理
      return `https://images.weserv.nl?url=${encodeURIComponent(preview)}`;
    }
    return preview
  }
}

// 分类选择
const selectCatag = (cat) => {
  form.value.tag = cat.name
  fetchVideos()
  isTransitionEnabled.value = true;
  listKey.value = Date.now()
}

// 选择“全部”标签
const clearCategory = () => {
  form.value.tag = null
  fetchVideos()
  isTransitionEnabled.value = true;
  listKey.value = Date.now()
}

// 回车进行搜索
const onSearchEnter = () => {
  searchVideos()
  isTransitionEnabled.value = true;
  listKey.value = Date.now()
}

// 点search按钮进行搜索
const onSubmitForm = () => {
  searchVideos()
  isTransitionEnabled.value = true;
  listKey.value = Date.now()
}

// 获取分类（后端提供tag信息）
const fetchCategories = async (categoryType = "video") => {
  categoriesLoading.value = true
  try {
    const res = await axios.get("/api/categories", {params: {category: categoryType}})
    if (res.data?.status === "success" && Array.isArray(res.data.tags)) {
      categories.value = res.data.tags.map(tag => ({
        tagId: tag.tagId,
        name: tag.tagName
      }))
    } else {
      console.warn("后端返回格式异常:", res.data)
      categories.value = []
    }
  } catch (err) {
    console.error("获取标签失败:", err)
    error.value = err.message || "标签获取失败"
    categories.value = []
  } finally {
    categoriesLoading.value = false
  }
}

// 获取视频（通过分类和排序规则获取，后端提供视频信息）
const fetchVideos = async () => {
  loading.value = true
  error.value = null
  try {
    const params = {
      tag: form.value.tag ?? "",
      choice: getChoiceFromSort(form.value.sort)
    }
    const res = await axios.get("/api/source/get_videos", { params })
    if (res.data?.status === "success" && Array.isArray(res.data.data)) {
      videos.value = res.data.data.map((item, index) => ({
        id: index + 1,
        videoTitle: item.videoTitle,
        videoAvatar: getPreviewPath(item.videoAvatar),
        linkURL: item.linkURL,
        views: item.views,
        createdAt: item.createdAt
      }))
    } else {
      videos.value = []
      console.warn("get_videos 返回格式异常", res.data)
    }
  } catch (err) {
    console.error("获取视频失败:", err)
    error.value = err.message || "获取视频失败"
    videos.value = []
  } finally {
    loading.value = false
  }
}

// 搜索获取视频
const searchVideos = async () => {
  const keyword = currentQuery.value.trim()
  if (!keyword) {
    // 空搜索 → 显示默认视频
    await fetchVideos()
    return
  }
  loading.value = true
  error.value = null
  try {
    const payload = {
      tag: form.value.tag ?? "",
      choice: getChoiceFromSort(form.value.sort),
      search: keyword
    }
    const res = await axios.post("/api/source/search/video", payload)
    if (res.data?.status === "success" && Array.isArray(res.data.data)) {
      videos.value = res.data.data.map((item, index) => ({
        id: index + 1,
        videoTitle: item.videoTitle,
        videoAvatar: getPreviewPath(item.videoAvatar),
        linkURL: item.linkURL,
        views: item.views,
        createdAt: item.createdAt
      }))
    } else {
      videos.value = []
      console.warn("search 接口返回异常", res.data)
    }
  } catch (err) {
    console.error("搜索失败:", err)
    error.value = err.message || "搜索失败"
    videos.value = []
  } finally {
    loading.value = false
  }
}

// 初始化
onMounted(() => {
  fetchCategories()
  fetchVideos()
})
</script>

<template>
  <Header />
  <main class="Video">
    <div class="Video__container">
      <!-- 设定区 -->
      <section class="setting">
        <div class="setting__inner">
          <div class="setting__desc">
            <img src="../../components/icons/VideoP.JPG" alt="LOGO" class="setting__img" />
            <p>欢迎来到ACStation，这里可以浏览 Azusa Racing 的所有视频</p>
          </div>
          <form class="setting__sv" name="video search" action="/Video/Search">
            <div class="form_group" >
              <label for="s">排序规则</label>
              <!--
                  teleported=true（默认）：下拉内容跑到 <body>，scoped 样式失效，要写全局样式
                  teleported=false：下拉内容留在组件里，可以用 scoped + ::v-deep 覆盖
              -->
              <el-select v-model="form.sort" placeholder="排序规则" style="width: 100%" class="select-option" :teleported="false">
                <el-option label="默认" value="default"></el-option>
                <el-option label="按时间最新排列" value="newest"></el-option>
                <el-option label="按时间最早排列" value="oldest"></el-option>
                <el-option label="按最多观看排列" value="most_popular"></el-option>
              </el-select>
              <label for="c">标签</label>
              <!-- 后端驱动的分类区 -->
              <div class="categories">
                <button
                    type="button"
                    class="category-chip"
                    :class="{ active: form.tag === null }"
                    @click="clearCategory">
                  <el-icon>
                    <video-pause v-if="form.tag === null" />
                    <video-play v-else />
                  </el-icon>
                  全部</button>
                <template v-if="categoriesLoading">
                  <span class="cat-loading">Azusa正在翻箱倒柜中...</span>
                </template>
                <template v-else>
                  <button
                      v-for="cat in categories"
                      :key="cat.tagId"
                      type="button"
                      class="category-chip"
                      :class="{ active: form.tag === cat.name }"
                      @click="selectCatag(cat)">
                    <el-icon>
                      <video-pause v-if="form.tag === cat.name" />
                      <video-play v-else />
                    </el-icon>
                    <span class="cat-name">{{ cat.name }}</span>
                  </button>
                </template>
              </div>
            </div>
          </form>
        </div>
      </section>

      <div class="Video__right-part">
        <!-- 搜索栏 -->
        <section class="search">
          <div class="search__inner">
            <el-input v-model="currentQuery"
                      placeholder="搜索视频标题、标签，按回车或点击搜索查找"
                      clearable
                      @keyup.enter="onSearchEnter"
                      style="max-width: 1200px; width: 100%">
              <template #append>
                <el-button type="primary" height="100%" @click="onSubmitForm">Search</el-button>
              </template>
            </el-input>
          </div>
        </section>

        <!-- 视频列表 -->
        <section class="list">
          <div class="list__inner">
            <!-- 加载状态 -->
            <div v-if="loading" class="list__status list__loading">Azusa正在翻箱倒柜搜索中...</div>
            <!-- 错误状态 -->
            <div v-else-if="error" class="list__status list__error">出现错误：{{ error }}</div>
            <!-- 空数据状态 -->
            <div v-else-if="videos.length === 0" class="list__status list__empty">
              <p>呃啊，Azusa找不到你想要的视频，请换个方式查询吧</p>
            </div>
            <!-- 正常视频列表 -->
            <div v-else class="list__grid" :key="listKey" :class="{'fade-in-animation': isTransitionEnabled}">
              <div v-for="video in videos" :key="video.id" class="video__card">
                <!-- 缩略图（可通过点击跳转新页面播放） -->
                <a :href="video.linkURL" target="_blank">
                  <img :src="video.videoAvatar" :alt="video.title" class="card__thumb" />
                </a>
                <!-- 标题（可通过点击跳转新页面播放） -->
                <a :href="video.videoTitle" target="_blank" class="card__title">
                  {{ video.videoTitle }}
                </a>
                <!-- 播放量、发布时间等元数据 -->
                <div class="card__meta">
                  <span>{{ video.views }} 次播放</span>
                  <span>{{ video.createdAt.split('T')[0] }}</span>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </main>
  <Footer />
</template>

<style scoped lang="scss">
.Video {
  /* 根容器，让文字和按钮浮现在背景之上 */
  position: relative;
  margin: 0;

  &__container {
    display: flex;
    flex-direction: row;
    min-height: 100vh;
  }

  &__right-part {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  &__layout {
    display: flex;
    flex-direction: row; /* 横向排列：左=内容，右=设定区 */
    align-items: flex-start;
    gap: 1rem;
  }

  &__content {
    flex: 1; /* 占据剩余空间 */
    padding: 1rem;
    display: flex;
    flex-direction: column;
    gap: 1rem; /* 搜索栏和列表间距 */
  }
}

/* setting区 */
.setting {
  width: 260px; /* 设定区宽度，可调 */
  transform: translate(0, 5%);
  background: #f8f8f8;
  border-left: 1px solid #ddd;
  padding: 1rem;
  flex-shrink: 0;
  height: 700px;

  &__inner {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    animation: fadeInLeft 0.3s ease-out 0.3s both;
  }

  &__desc {
    text-align: center;

    .setting__img {
      width: 125px;
      height: auto;
      margin-bottom: 0.5rem;
      border-radius: 50%;
      border: 2px solid #666666;  /* 轮廓颜色 + 粗细 */
      object-fit: cover;          /* 图片裁剪填充 */
    }
  }

  .form_group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;

    label {
      font-weight: bold;
      margin-top: 0.5rem;
    }

    .categories {
      display: flex;
      flex-direction: column; /* 垂直排列 */
      gap: 8px;              /* 标签之间的间距 */

      .category-chip {
        cursor: pointer;
        border-radius: 0 !important; /* 去掉圆角 */
        border: none !important;     /* 去掉边框 */
        background-color: #f5f5f5;
        color: #333;
        display: flex;
        align-items: center;
        gap: 6px; /* 图标和文字的间距 */
        width: 100%; /* 占满一行 */
        justify-content: flex-start;
        font-size: 14px;
        padding: 8px 12px;
        transition: transform 0.2s ease;

        /* 非选中时悬停效果 */
        &:hover {
          background-color: #e0e0e0;
          transform: translateX(5px)
        }

        &.active {
          background-color: #7e57c2;
          color: #fff;
          /* 选中时悬停效果 */
          &:hover {
            background-color: #5e35b1;
          }
        }
      }

      .cat-loading {
        font-size: 0.9rem;
        color: #666;
      }
    }
  }
}

/* search区 */
.search {
  padding: 20px 40px;
  background: #fff;
  border-bottom: 1px solid #eee;
  animation: fadeInTop 0.5s ease-out 0.5s both;

  &__inner {
    display: flex;
    justify-content: center;
    padding: 0.5rem;
  }
}

/* video-list区 */
.list {
  flex: 1;
  padding: 20px 40px;
  background: #fafafa;
  animation: fadeInBottom 0.5s ease-out 0.5s both;

  &__inner {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  &__status {
    text-align: center;
    font-size: 16px;
    color: #666;
    padding: 40px 0;
  }

  &__error {
    color: #c00;
  }

  &__grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 1rem;

    .video__card {
      border: 1px solid #ddd;
      border-radius: 8px;
      padding: 0.5rem;
      background: #fff;
      transition: transform 0.2s ease;

      &:hover {
        transform: translateY(-3px);
      }

      .card__thumb {
        width: 100%;
        height: 150px;
        object-fit: cover;
        display: block;
        border-radius: 6px;
      }

      .card__title {
        display: block;
        font-weight: bold;
        margin: 0.5rem 0;
        text-decoration: none;
        color: #333;

        &:hover {
          color: #5a2a92;
        }
      }

      .card__meta {
        padding: 0 10px 10px 10px;
        font-size: 0.85rem;
        color: #666;
        display: flex;
        justify-content: space-between;
      }
    }
  }
}

.fade-in-animation {
  animation: fadeInBottom 0.5s ease-out 0.5s both;
}

/* 自定义 el-select 的悬停和过渡 */
.select-option {
  .el-select-dropdown__item,
  .el-select-option,
  [role="option"] {
    transition: transform .18s ease, color .18s ease, background-color .18s ease;
    transform: translateX(0);
    will-change: transform, color, background-color;
  }

  .el-select-dropdown__item:hover,
  .el-select-option:hover,
  [role="option"]:hover {
    transform: translateX(6px);
    color: #5a2a92;
    background-color: rgba(90,42,146,0.06);
  }

  .el-select-dropdown__item.selected,
  .el-select-option.is-selected,
  [role="option"][aria-selected="true"] {
    background-color: #7e57c2;
    color: #fff;
  }
}
:deep(.el-select__wrapper.is-focused) {
  box-shadow: 0 0 0 1px #7e57c2 !important;
}

/* 自定义输入框选中效果 */
:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #7e57c2 !important;
  z-index: 1;
}

:deep(.el-button:hover) {
  border-radius: 0;
  box-shadow: 0 0 0 1px #7e57c2 !important;
  background-color: #7e57c2 !important;
  border-color: #666 !important;
  color: #fff !important;
}

/* 响应式：小屏幕时改为上下布局 */
@media (max-width: 1024px) {
  .Video__container {
    flex-direction: column;
  }

  .setting {
    width: 100%;
    border-left: none;
    border-top: 1px solid #ddd;
  }
}
@keyframes fadeInLeft {
  0%   { opacity: 0; transform: translate(0, 0) translateX(-0.5rem); }
  100% { opacity: 1; transform: translate(0, 0) translateX(0); }
}
@keyframes fadeInTop {
  0%   { opacity: 0; transform: translate(0, 0) translateY(-0.5rem); }
  100% { opacity: 1; transform: translate(0, 0) translateY(0); }
}
@keyframes fadeInBottom {
  0%   { opacity: 0; transform: translate(0, 0) translateY(0.5rem); }
  100% { opacity: 1; transform: translate(0, 0) translateY(0); }
}
</style>