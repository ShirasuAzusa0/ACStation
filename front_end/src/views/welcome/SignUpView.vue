<script setup>
import {User, Lock, Key, Message} from '@element-plus/icons-vue'
import {ref, onMounted} from 'vue'
import {useRouter} from "vue-router"
import axios from "axios"
import { useUserStore } from '@/stores/user'
import {ElMessage} from "element-plus";

const userName = ref('')
const email = ref('')
const password = ref('')
const captcha = ref('')

// 从后端获取验证码和对应编号
const captchaImage = ref('')
const captchaKey = ref('')

const userStore = useUserStore()
const router = useRouter()

// 动态加载 JSEncrypt （用于PKCS#1 v1.5加密）
function loadJSEncrypt() {
  return new Promise((resolve, reject) => {
    if (window.JSEncrypt) return resolve(window.JSEncrypt)
    const src = 'https://cdn.bootcdn.net/ajax/libs/jsencrypt/3.0.0-rc.1/jsencrypt.min.js'
    const s = document.createElement('script')
    s.src = src
    s.onload = () => {
      if(window.JSEncrypt) resolve(window.JSEncrypt)
      else reject(new Error('JSEncrypt not available after script load'))
    }
    s.onerror = (e) => reject(new Error('Failed to load JSEncrypt: ' + e.message))
    document.head.appendChild(s)
  })
}

// Base64 -> PEM
function b64TOpem(b64) {
  const chunks = b64.match(/.{1,64}/g) || [b64]
  return `-----BEGIN PUBLIC KEY-----\n${chunks.join('\n')}\n-----END PUBLIC KEY-----`
}

// 从后端获取公钥（Base64），返回PEM格式字符串
async function fetchPublicKeyPEM() {
  const res = await axios.get('/api/key')
  if(res.data && res.data.public_key) {
    return b64TOpem(res.data.public_key)
  } else {
    throw new Error('从 /api/key 获取公钥失败，返回数据：' + JSON.stringify(res.data))
  }
}

// 公钥加密，返回 base64 字符串
async function encryptPassword(rawPassword) {
  await loadJSEncrypt()
  const pem = await fetchPublicKeyPEM()
  const encryptor = new window.JSEncrypt()
  encryptor.setPublicKey(pem)
  const encrypted = encryptor.encrypt(rawPassword)
  if (!encrypted) throw new Error('密码加密失败')
  return encrypted
}

// 获取验证码（key+image）
async function fetchCaptcha() {
  try {
    const res = await axios.get('/api/captcha')
    if (res.data && res.data.key && res.data.image) {
      captchaImage.value = res.data.image.startsWith('data:') ? res.data.image : `data:image/png;base64,${res.data.image}`
      captchaKey.value = res.data.key
    } else {
      console.error('验证码接口返回异常：', res.data)
    }
  } catch (err) {
    console.error('获取验证码失败：', err)
  }
}

// 点击刷新验证码
const refreshCaptcha = async() => {
  await fetchCaptcha()
}

// 页面加载时先获取验证码
onMounted(() => {
  fetchCaptcha()
})

// 注册（发送userName, account, password, captcha, captchaKey）
const SignUp = async () => {
  try {
    if (!email.value || !userName.value || !password.value) {
      console.warn('账号、用户名或密码为空')
      ElMessage.error('账号、用户名或密码为空')
    }

    // 加密密码
    const encryptedPassword = await encryptPassword(password.value)

    // 发送登录请求到后端
    const payload = {
      userName: userName.value,
      account: email.value,
      password: encryptedPassword,
      captcha: captcha.value,
      captchaKey: captchaKey.value,
    }

    const res = await axios.post('/api/auth/register', payload)

    if (res.data && (res.data.status === 'success' || res.status === '200')) {
      const d = res.data.data || {}
      userStore.setUser({username: d.username, userId: d.userId, avatar: d.avatar}, d.token)
      await router.push("/Home")
      ElMessage.success('注册成功，welcome to ACStation!')
    } else {
      console.error('注册失败：', res.data)
      ElMessage.error('注册失败')
    }
  } catch (err) {
    console.error('注册流程异常：', err)
  }
}

// 登录（跳转）
const SignIn = async () => {
  try {
    await router.push("/Welcome/Sign-In")
  } catch (err) {
    console.error('注册跳转流程异常：', err)
  }
}
</script>

<template>
  <div class="SignUp__container">
    <div class="SignUp__desc">
      <h1>注册</h1>
      <p class="desc__dp1">“I just do my own thing, and what others say has nothing to do with me.”</p>
      <p class="desc__dp2">————Kimi Raikkonen</p>
    </div>
    <div class="SignUp__table">
      <el-input v-model="userName" placeholder="用户名">
        <template #prefix>
          <el-icon color="black"><User /></el-icon>
        </template>
      </el-input>
      <el-input v-model="email" style="margin-top: 10px" placeholder="邮箱">
        <template #prefix>
          <el-icon color="black"><Message /></el-icon>
        </template>
      </el-input>
      <el-input v-model="password" type="password" style="margin-top: 10px" placeholder="密码" show-password>
        <template #prefix>
          <el-icon color="black"><Lock /></el-icon>
        </template>
      </el-input>
      <div class="SignUp__captcha">
        <el-input v-model="captcha" class="captcha__input" placeholder="验证码">
          <template #prefix>
            <el-icon color="black"><Key /></el-icon>
          </template>
        </el-input>
        <img :src="captchaImage" alt="验证码" class="captcha__img" @click="refreshCaptcha" />
      </div>
    </div>
    <div class="SignUp__buttons">
      <div class="signUp__btn1">
        <el-button style="width: 400px" class="btn__success" plain @click="SignUp">立即注册</el-button>
      </div>
      <div class="SignUp__btn2">
        <el-button style="width: 400px" class="btn__warning" plain @click="SignIn">已有账号？跳转登录</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.SignUp {

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
    background: linear-gradient(135deg, rgba(90,42,146,0.2), rgba(0,0,0,0.2));
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
      background-color: rgba(90, 42, 146, 0.02);
      backdrop-filter: blur(2px);
      border-radius: 8px;
      border: 1px solid #181818;                /* 可选：加个黑色边框 */
      transition: background-color 0.3s ease;
    }

    ::v-deep(.el-input__inner) {
      position: relative;
      z-index: 1;
      color: #181818;
    }

    ::v-deep(.el-input__inner::placeholder) {
      color: #3f3f4f;
    }

    :deep(.el-input__wrapper.is-focus) {
      box-shadow: 0 0 0 1px #7e57c2 !important;
    }

    ::v-deep(.el-button) {
      background-color: transparent;             /* 完全透明 */
      border: 1px solid #181818;                /* 可选：加个黑色边框 */
      color: #666666;                          /* 按钮文字颜色 */
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
      background-color: rgba(90, 42, 146, 0.02);
      backdrop-filter: blur(2px);
      border-radius: 8px;
      transition: background-color 0.3s ease;
    }

    ::v-deep(.el-input__inner) {
      position: relative;
      z-index: 1;
      color: #181818;
    }

    ::v-deep(.el-input__inner::placeholder) {
      color: #3f3f4f;
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