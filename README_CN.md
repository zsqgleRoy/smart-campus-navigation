# 数智成贤 (Smart Campus Navigation)

一个全面的校园网站导航解决方案，提供 Web 和原生 Android 两种应用形式，帮助师生快速访问校园常用网站。

## 📱 项目概述

本仓库包含校园导航系统的两个实现版本：

- **cxxy/** - 使用 Vue 3 + TypeScript + Vant UI 构建的 Web 应用
- **szcx/** - 使用 Kotlin + Jetpack Compose 构建的原生 Android 应用

两个应用提供相同的核心功能：帮助师生快速访问常用的校园网站。

## ✨ 功能特性

### 核心功能
- 🔍 **快速搜索** - 支持按网站名称或描述搜索
- ⭐ **收藏功能** - 收藏常用网站，快速访问
- 📊 **访问历史** - 记录最近访问的网站
- 🎨 **分类浏览** - 按分类浏览网站，快速找到所需服务
- 🌓 **主题切换** - 支持浅色/深色/护眼三种主题模式
- 📱 **响应式设计** - 专为移动端优化

### Web 应用 (cxxy)
- PWA 支持，支持离线访问
- 添加到主屏幕功能
- 适配各种屏幕尺寸的响应式布局

### Android 应用 (szcx)
- 原生 Android 体验
- 使用 Room Database 支持离线数据
- 图标预加载和缓存
- 集成 WebView 浏览网站

## 🛠️ 技术栈

### Web 应用 (cxxy)
- **框架**: Vue 3
- **语言**: TypeScript
- **构建工具**: Vite
- **UI 组件库**: Vant UI
- **状态管理**: Pinia
- **路由管理**: Vue Router
- **PWA**: 渐进式 Web 应用支持

### Android 应用 (szcx)
- **开发语言**: Kotlin
- **最小 SDK**: API 21 (Android 5.0)
- **目标 SDK**: API 34 (Android 14)
- **架构模式**: MVVM (Model-View-ViewModel)
- **UI 框架**: Jetpack Compose
- **数据库**: Room Database
- **图片加载**: Coil
- **数据存储**: DataStore
- **导航**: Navigation Compose

## 📁 项目结构

```
.
├── cxxy/                    # Web 应用 (Vue 3)
│   ├── src/
│   │   ├── components/      # 可复用组件
│   │   ├── views/           # 页面组件
│   │   ├── stores/          # 状态管理
│   │   ├── router/          # 路由配置
│   │   ├── utils/           # 工具函数
│   │   ├── data/            # 数据文件
│   │   └── styles/          # 全局样式
│   ├── package.json
│   └── vite.config.ts
│
└── szcx/                    # Android 应用 (Kotlin)
    ├── app/
    │   ├── src/main/
    │   │   ├── java/com/szcx/
    │   │   │   ├── data/     # 数据层 (数据库、模型、仓库)
    │   │   │   ├── ui/       # UI 层 (界面、组件、主题)
    │   │   │   └── util/     # 工具类
    │   │   └── res/          # 资源文件 (布局、图片、值)
    │   └── build.gradle.kts
    ├── build.gradle.kts
    └── settings.gradle.kts
```

## 🚀 快速开始

### 环境要求

#### Web 应用 (cxxy)
- Node.js 16+ 和 npm/yarn/pnpm

#### Android 应用 (szcx)
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK (API 21+)

### Web 应用设置

1. 进入 Web 应用目录：
```bash
cd cxxy
```

2. 安装依赖：
```bash
npm install
# 或
yarn install
# 或
pnpm install
```

3. 启动开发服务器：
```bash
npm run dev
# 或
yarn dev
# 或
pnpm dev
```

4. 构建生产版本：
```bash
npm run build
```

### Android 应用设置

1. 在 Android Studio 中打开项目：
   - 打开 Android Studio
   - 选择 "Open an Existing Project"
   - 导航到 `szcx` 目录

2. 同步 Gradle 依赖：
   - Android Studio 会自动同步 Gradle
   - 等待同步完成

3. 运行应用：
   - 连接 Android 设备或启动模拟器 (API 21+)
   - 点击 "Run" 按钮或按 `Shift+F10`

## 📖 开发指南

### Web 应用开发

- **开发服务器**: `npm run dev` (运行在 http://localhost:5173)
- **类型检查**: `npm run type-check`
- **构建**: `npm run build`
- **预览**: `npm run preview`

### Android 应用开发

- 在 Android Studio 中打开项目
- 同步 Gradle 依赖
- 在设备/模拟器上运行
- 首次运行会自动初始化数据库数据

## 🎯 核心功能实现

### 主题支持
两个应用都支持三种主题模式：
- **浅色模式** - 默认浅色主题
- **深色模式** - 适合低光环境的深色主题
- **护眼模式** - 暖色调配色，减少眼部疲劳

### 数据管理
- **Web 应用**: 使用 localStorage 存储收藏和历史记录
- **Android 应用**: 使用 Room Database 持久化存储，DataStore 存储偏好设置

### 网站图标
- **Web 应用**: 从网站 favicon 加载图标
- **Android 应用**: 应用启动时预加载并缓存所有网站图标

## 📝 注意事项

- Android 应用需要网络权限来访问网站
- Android 应用首次运行会自动初始化数据库数据
- Web 应用支持 PWA 功能，可离线访问
- 两个应用共享相同的网站数据结构

## 🤝 贡献

欢迎贡献代码！请随时提交 Pull Request。

## 📄 许可证

MIT License

## 👤 作者

- GitHub: [zsqgleRoy](https://github.com/zsqgleRoy)

## 🔗 相关链接

- [作者 GitHub 主页](https://github.com/zsqgleRoy/)

---

For English documentation, please see [README.md](README.md).

