<script setup>
// 声明当前组件会接收到一个名为 nav_card 的 prop，类型必须为 Object，并且是必传的
  defineProps({ nav_card: { type: Object, required: true } })
// 声明当前组件灰度外触发一个名为 click 的自定义事件
  defineEmits(["click"])
</script>

<template>
  <article class="nav_card" @click="$emit('click')">
    <div class="nav_card__img-wrap">
      <el-image class="nav_card__img" :src="nav_card.img" fit="cover" :alt="nav_card.name" />
    </div>
    <div class="nav_card__body">
      <h3 class="nav_card__name">{{ nav_card.name }}</h3>
      <p class="nav_card__info">{{ nav_card.info }}</p>
    </div>
  </article>
</template>

<style scoped lang="scss">
.nav_card {
  /* 整张卡片作为按钮可以点击 */
  cursor: pointer;
  /* 启用阴影溢出父容器显示 */
  overflow: visible;
  /* 主要阴影轮廓（外发光 + 轻微内阴影） */
  box-shadow:
      0 6px 18px rgba(0,0,0,0.10),       /* 主体阴影 */
      0 0 18px rgba(117,91,138,0.06);    /* 彩色微光轮廓（可删改为纯灰） */
  /* 明显的半透明边框 + 轻微内阴影，和模糊玻璃效果  */
  background: linear-gradient(180deg, rgba(255,255,255,0.02), rgba(255,255,255,0.01));
  /* 模糊玻璃效果 */
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 0.5rem;
  padding: 1rem;
  text-align: center;
  transition: transform 0.2s;
  /* 悬停提升效果 */
  &:hover { transform: translateY(-2px); }

  &__img { width: 100%; height: 160px; }
  &__name { font-size: 1.25rem; margin: 0.5rem 0; }
  &__info { font-size: 0.875rem; color: var(--clr-muted); }
}
</style>