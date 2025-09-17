<script setup>
import {User, Lock, Key} from '@element-plus/icons-vue'
import { ref } from 'vue'

// 模拟验证码图片 URL
const captchaUrl = ref('/api/captcha?time' + Date.now())
// 点击刷新验证码
const refreshCaptcha = () => {
  captchaUrl.value = '/api/captcha?time' + Date.now()
}
</script>

<template>
  <div class="LogIn__container">
    <div class="LogIn__desc">
      <h1>登录</h1>
      <p class="desc__dp1">“I just do my own thing, and what others say has nothing to do with me.”</p>
      <p class="desc__dp2">————Kimi Raikkonen</p>
    </div>
    <div class="LogIn__table">
      <el-input type="text" placeholder="用户名/邮箱">
        <template #prefix>
          <el-icon><User /></el-icon>
        </template>
      </el-input>
      <el-input type="password" style="margin-top: 10px" placeholder="密码">
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
      <div class="LogIn__captcha">
        <el-input class="captcha__input" placeholder="验证码">
          <template #prefix>
            <el-icon><Key /></el-icon>
          </template>
        </el-input>
        <img :src="captchaUrl" alt="验证码" class="captcha__img" @click="refreshCaptcha" />
      </div>
    </div>
    <div class="LogIn__buttons">
      <div class="LogIn__btn1">
        <el-button style="width: 400px" class="btn__success" plain>立即登录</el-button>
      </div>
      <div class="LogIn__btn2">
        <el-button style="width: 400px" class="btn__warning" plain>没有账号？注册一个吧</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.LogIn {

  &__container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    z-index: 2;
    /* 可选：加个黑色边框 */
    border: 1px solid #181818;
    /* 明显的半透明渐变边框 + 轻微内阴影，和模糊玻璃效果  */
    background: linear-gradient(135deg, rgba(90,42,146,0.2), rgba(255,255,255,0.05));
    backdrop-filter: blur(15px);
    border-radius: 1rem;
    font-family: "Microsoft YaHei", sans-serif;
  }

  &__desc {
    margin-bottom: 40px;

    h1 {
      text-align: center;
      font-size: 32px;
      font-weight: bold;
      margin-bottom: 20px;
    }

    .desc__dp1 {
      font-size: 14px;
      color: #181818;
      line-height: 1.6;
      max-width: 400px;
      margin: 0 auto;
    }

    .desc__dp2 {
      text-align: right;
      font-size: 14px;
      color: #181818;
      line-height: 1.6;
      max-width: 400px;
      margin: 0 auto;
    }
  }

  &__table, &__buttons {
    display: flex;
    flex-direction: column;
    gap: 5px;
    width: 400px;
    margin-bottom: 25px;

    ::v-deep(.el-input__wrapper) {
      background-color: rgba(90, 42, 146, 0.2);
      backdrop-filter: blur(2px);
      border-radius: 8px;
      border: 1px solid #181818;                /* 可选：加个黑色边框 */
      transition: background-color 0.3s ease;
    }

    ::v-deep(.el-button) {
      background-color: transparent;             /* 完全透明 */
      border: 1px solid #181818;                /* 可选：加个黑色边框 */
      color: #fff;                             /* 按钮文字颜色 */
      transition: all 0.3s ease;
    }

    /* 悬停 */
    ::v-deep(.btn__success:hover) {
      background-color: rgba(103, 194, 58, 1);
      border-color: rgba(255,255,255,0.2);
      color: #fff;
      transform: translateY(-1px);
    }

    ::v-deep(.btn__warning:hover) {
      background-color: rgba(235, 181, 99, 1);
      border-color: rgba(255,255,255,0.2);
      color: #fff;
      transform: translateY(-1px);
    }
  }

  &__captcha {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-top: 10px;

    ::v-deep(.el-input__wrapper) {
      background-color: rgba(90, 42, 146, 0.2);
      backdrop-filter: blur(2px);
      border-radius: 8px;
      transition: background-color 0.3s ease;
    }

    .captcha__input {
      flex: 1;
      min-width: 0;
    }

    .captcha__img {
      width: 80px;
      height: 30px;
      max-width: 100%;
      /* contain 保持完整显示，cover 会裁剪 */
      object-fit: contain;
      border-radius: 4px;
      border: 1px solid #e6e6e6;
      cursor: pointer;
      display: block;
      /* 固定尺寸，不缩放 */
      flex: 0 0 auto;
    }
  }
}
</style>