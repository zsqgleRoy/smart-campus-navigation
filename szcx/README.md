# 数智成贤 Android 应用

这是一个专为移动端设计的校园网站导航应用，帮助师生快速访问常用网站。

## 技术栈

- **开发语言**: Kotlin
- **最小SDK**: API 21 (Android 5.0)
- **目标SDK**: API 34 (Android 14)
- **架构模式**: MVVM (Model-View-ViewModel)
- **UI框架**: Jetpack Compose
- **数据库**: Room Database
- **网络请求**: Retrofit + OkHttp
- **图片加载**: Coil
- **数据存储**: DataStore

## 项目结构

```
app/src/main/java/com/szcx/
├── data/
│   ├── database/          # Room 数据库和 DAO
│   ├── model/            # 数据模型
│   └── repository/       # 数据仓库层
├── ui/
│   ├── home/             # 首页
│   ├── category/         # 分类页面
│   ├── favorites/        # 收藏页面
│   ├── search/           # 搜索页面
│   ├── settings/         # 设置页面
│   ├── about/            # 关于页面
│   ├── webview/          # WebView 页面
│   ├── components/       # 共享组件
│   └── theme/            # 主题配置
└── util/                 # 工具类
```

## 功能特性

-  首页展示常用网站和分类
-  分类浏览网站
-  收藏功能
-  访问历史记录
-  搜索功能
-  主题切换（浅色/深色/护眼）
-  WebView 浏览网站
-  离线数据支持

## 构建说明

1. 使用 Android Studio 打开项目
2. 同步 Gradle 依赖
3. 连接 Android 设备或启动模拟器
4. 运行应用

## 注意事项

- 首次运行会自动初始化数据库数据
- 需要网络权限访问网站
- 图片资源已从原 Vue 项目复制






