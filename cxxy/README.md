# 校园网站导航 - 移动端应用

一个专为移动端设计的校园网站导航应用，使用 Vue3 + TypeScript + Vant UI 构建。

## 功能特点

- 🎯 **移动端优化**：专为手机浏览器设计，适配各种屏幕尺寸
- 🔍 **快速搜索**：支持网站名称和描述搜索
- ⭐ **收藏功能**：收藏常用网站，快速访问
- 📱 **PWA支持**：支持添加到主屏幕，离线访问
- 🌓 **主题切换**：支持浅色/深色主题
- 📊 **访问历史**：记录最近访问的网站
- 🎨 **分类浏览**：按分类浏览网站，快速找到所需服务

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - 类型安全的 JavaScript
- **Vite** - 下一代前端构建工具
- **Vant UI** - 轻量、可靠的移动端 Vue 组件库
- **Vue Router** - Vue.js 官方路由管理器
- **Pinia** - Vue 的状态管理库
- **PWA** - 渐进式 Web 应用支持

## 项目结构

```
src/
├── components/          # 公共组件
│   ├── TabBar.vue      # 底部导航栏
│   └── WebsiteCard.vue # 网站卡片
├── views/              # 页面组件
│   ├── Home.vue        # 首页
│   ├── Category.vue    # 分类页
│   ├── Favorites.vue   # 收藏页
│   ├── Settings.vue    # 设置页
│   └── Search.vue      # 搜索页
├── stores/             # 状态管理
│   └── app.ts          # 应用状态
├── router/             # 路由配置
│   └── index.ts
├── utils/              # 工具函数
│   ├── storage.ts      # 本地存储
│   └── format.ts       # 格式化工具
├── data/               # 数据文件
│   └── websites.ts     # 网站数据
├── types/              # 类型定义
│   └── index.ts
└── styles/             # 样式文件
    └── common.css      # 全局样式
```

## 开发

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

### 类型检查

```bash
npm run type-check
```

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 移动端适配

- 使用 `postcss-px-to-viewport` 进行视口单位转换
- 基准宽度：375px（iPhone SE）
- 支持横屏和竖屏模式
- 优化触摸交互，防止300ms延迟

## 数据存储

使用 `localStorage` 存储：
- 用户主题偏好
- 收藏的网站列表
- 访问历史记录
- 搜索历史

## PWA 功能

- 支持添加到主屏幕
- 离线访问支持
- 自动更新机制
- 响应式图标

## 浏览器支持

- iOS Safari 12+
- Chrome (Android) 80+
- 微信内置浏览器
- 其他现代移动浏览器

## 自定义配置

### 修改网站数据

编辑 `src/data/websites.ts` 文件，添加或修改网站信息。

### 修改分类

编辑 `src/data/websites.ts` 中的 `categories` 数组。

### 修改主题颜色

编辑 `src/styles/common.css` 中的 CSS 变量。

## 许可证

MIT
