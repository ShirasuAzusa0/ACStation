# acstation

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```

# 项目架构
我们的项目架构如下：
```
acstation/
├── .idea/                 ← IDEA 项目配置
├── .vscode/               ← VSCode 配置文件（可能是导入留下的）
├── node_modules/          ← 依赖包（npm install 后生成）
├── public/                ← 静态资源目录，打包时会原样复制
│   └── favicon.ico        ← 网站图标
├── src/                   ← 项目主代码目录
│   ├── assets/            ← 图片、样式等资源
│   ├── components/        ← 可复用组件（目前为空）
│   ├── App.vue            ← 根组件
│   └── main.js            ← 应用入口文件
├── .gitignore
├── acstation.iml          ← IDEA模块配置
├── index.html             ← 页面入口模板，Vite 会注入 script
├── jsconfig.json
├── package.json           ← 项目描述、依赖、脚本
├── package-lock.json      ← 依赖锁定
├── README.md              ← 项目说明
└── vite.config.js         ← Vite 配置文件
```

下面的这种是vscode风格的项目架构，注意与我们当前使用的架构区分开来：
```
resource-site/
├── node_modules/         # 第三方依赖
├── public/
│   ├── index.html        # 应用主 HTML 模板
│   └── resources/        # 静态资源文件夹（可放置可直接下载的文件）
├── src/
│   ├── assets/           # 图片、样式等静态资源
│   ├── components/       # 可复用 UI 组件
│   ├── views/            # 路由页面视图
│   ├── router/           # 路由配置
│   ├── services/         # 与后台或文件列表交互的 API 封装
│   ├── store/            # Vuex 状态管理（可选）
│   ├── App.vue           # 根组件
│   └── main.js           # 入口脚本
├── .gitignore            # Git 忽略规则
├── package.json          # 项目信息 & 依赖
└── README.md             # 项目说明
```
