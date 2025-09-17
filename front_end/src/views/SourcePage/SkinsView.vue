<script setup>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import {onMounted, reactive, ref} from "vue";
import { Collection } from "@element-plus/icons-vue";
import axios from "axios";

// 表单状态（排序规则 & 分类tags）
const form = reactive({
  sort: "default",
  categoryId: null
})

// 分类数据
const categories = ref([])
const categoriesLoading = ref(true)

// 涂装资源数据
const skins = ref([])
const loading = ref(false)
const error = ref(null)

// 搜索关键字
const currentQuery = ref("")

// 分类选择
const selectCatag = (cat) => {
  form.categoryId = cat.id
  fetchSkins()
}

const clearCategory = () => {
  form.categoryId = null
  fetchSkins()
}

// 回车进行搜索
const onSearchEnter = () => {
  fetchSkins()
}

// 点search按钮进行搜索
const onSubmitForm = () => {
  fetchSkins()
}

// 获取分类（后端提供tag信息）
const fetchCategories = async () => {
  categoriesLoading.value = true
  try {
    if (process.env.NODE_ENV === "development") {
      // 🟡 本地开发时：模拟分类
      categories.value = [
        { id: 1, name: "HER 新手体验赛" },
        { id: 2, name: "TTC 万元户赛道嘉年华" },
        { id: 3, name: "SSS TCR黄金冲刺赛" },
        { id: 4, name: "非赛事涂装" },
      ]
    } else {
      // 🟢 生产环境：调用后端
      const res = await axios.get("/api/skin/categories")
      categories.value = res.data
    }
  } catch (err) {
    error.value = err.message || "标签获取失败"
    categories.value = []
  } finally {
    categoriesLoading.value = false
  }
}

// 获取涂装资源（后端提供）
const fetchSkins = async () => {
  loading.value = true
  error.value = null
  try {
    if (process.env.NODE_ENV === "development") {
      // 模拟数据
      skins.value = [
        {
          id: 2,
          url: "/Skins/MX5_ND_ShirsuAzusa_track_day",
          thumb: "NewestNotFound.jpg",
          name: "模拟涂装资源 1",
          views: 123,
          likes: 114,
          download: 514,
          createAt: "2025-09-17"
        },
        {
          id: 1,
          url: "/Skins/example.com/2",
          thumb: "NewestNotFound.jpg",
          name: "模拟涂装资源 2",
          views: 456,
          likes: 1919,
          download: 810,
          createAt: "2025-09-16"
        }
      ]
    } else {
      // 真正请求后端
      const res = await axios.get("/api/skins", { params: { ...form } })
      skins.value = res.data
    }
  } catch (err) {
    error.value = err.message || "未知错误"
    skins.value = []
  } finally {
    loading.value = false
  }
}

// 初始化
onMounted( () => {
  fetchCategories()
  fetchSkins()
})
</script>

<template>
  <Header />
  <main class="Skin">
    <div class="Skin__container">
      <!-- 设定区 -->
      <section class="setting">
        <div class="setting__inner">
          <div class="setting__desc">
            <img src="../../components/icons/SkinP.JPG" alt="LOGO" class="setting__img" />
            <p>这里是由Y.Z.Ben自制的车辆涂装，所有涂装已获得授权，禁止商用</p>
          </div>
          <form class="setting__sv" name="skin sort" action="/Skin/Search">
            <div class="form_group">
              <label for="s">排序规则</label>
              <el-select v-model="form.sort" placeholder="排序规则" style="width: 100%" class="select-option" :teleported="false">
                <el-option label="默认" value="default"></el-option>
                <el-option label="按时间最新排列" value="newest"></el-option>
                <el-option label="按时间最早排列" value="oldest"></el-option>
                <el-option label="按最多观看排列" value="most_popular"></el-option>
              </el-select>
              <label for="c">标签</label>
              <!-- 后端驱动的分类tag区 -->
              <div class="categories">
                <button
                  type="button"
                  class="category-chip"
                  :class="{ active: form.categoryId === null }"
                  @click="clearCategory">
                  <el-icon><Collection /></el-icon>
                全部</button>
                <template v-if="categoriesLoading">
                  <span class="cat-loading">Azusa正在翻箱倒柜搜索中...</span>
                </template>
                <template v-else>
                  <button
                    v-for="cat in categories"
                    :key="cat.id"
                    type="button"
                    class="category-chip"
                    :class="{ active: form.categoryId === cat.id }"
                    @click="selectCatag(cat)">
                    <el-icon><Collection /></el-icon>
                    <span class="cat-name">{{ cat.name }}</span>
                  </button>
                </template>
              </div>
            </div>
          </form>
        </div>
      </section>

      <div class="Skin__right-part">
        <!-- 搜索栏 -->
        <section class="search">
          <div class="search__inner">
            <el-input v-model="currentQuery"
            placeholder="搜索涂装名或关键词，按回车或点击搜索按钮查找"
            clearable
            @keyup.enter="onSearchEnter"
            style="max-width: 1200px; width: 100%">
              <template #append>
                <el-button type="primary" height="100%" @click="onSubmitForm">Search</el-button>
              </template>
            </el-input>
          </div>
        </section>

        <!-- 涂装资源下载列表区 -->
        <section class="list">
          <div class="list__inner">
            <!-- 加载状态 -->
            <div v-if="loading" class="list__status list__loading">Azusa正在翻箱倒柜搜索中...</div>
            <!-- 错误状态 -->
            <div v-else-if="error" class="list__status list__error">出现错误：{{ error }}</div>
            <!-- 空数据状态 -->
            <div v-else-if="skins.length === 0" class="list__status list__empty">
              <p>呃啊，Azusa找不到你想要的涂装资源，请换个方式查询吧</p>
            </div>
            <!-- 正常涂装资源下载列表 -->
            <div v-else class="list__grid">
              <div v-for="skin in skins" :key="skin.id" class="skin__card">
                <!-- 缩略图 -->
                <a :href="skin.url" target="_blank">
                  <img :src="skin.thumb" :alt="skin.title" class="card__thumb" />
                </a>
                <a :href="skin.url" target="_blank" class="card__title">
                  {{ skin.name }}
                </a>
                <!-- 点击数、点赞数、下载数、发布时间等元数据 -->
                <div class="card__meta">
                  <span>{{ skin.views }} clicks</span>
                  <span>{{ skin.likes }} likes</span>
                  <span>{{ skin.download }} downloads</span>
                  <span>{{ skin.createAt }}</span>
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
.Skin {
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
    flex-direction: row;
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

/* skin-list区 */
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

    .skin__card {
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
        display: grid;
        grid-template-columns: 1fr 1fr; /* 两列平均分 */
        gap: 10px 28px; /* 行间距、列间距可调 */
        padding: 0 10px 10px 10px;
        font-size: 0.85rem;
        color: #666;
        text-align: center;
      }
    }
  }
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
  .Skin__container {
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