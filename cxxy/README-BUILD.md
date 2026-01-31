# 构建和预览说明

## 问题说明

**重要：不要直接双击打开 `dist/index.html` 文件！**

直接打开 HTML 文件会使用 `file://` 协议，这会导致：
- ❌ 资源文件无法加载（相对路径错误）
- ❌ Service Worker 无法工作
- ❌ ES 模块无法正常加载
- ❌ 页面显示空白

## 正确的预览方式

### 方法 1: 使用 Vite 预览（推荐）

```bash
# 构建项目
npm run build

# 预览构建结果（会自动启动本地服务器）
npm run preview
```

预览服务器会在 `http://localhost:4173` 启动，浏览器会自动打开。

### 方法 2: 使用其他本地服务器

#### 使用 Python（如果已安装）

```bash
cd dist
python -m http.server 8000
```

然后访问 `http://localhost:8000`

#### 使用 Node.js http-server

```bash
# 安装 http-server（如果未安装）
npm install -g http-server

# 启动服务器
cd dist
http-server -p 8000
```

然后访问 `http://localhost:8000`

#### 使用 VS Code Live Server

1. 安装 VS Code 扩展 "Live Server"
2. 右键点击 `dist/index.html`
3. 选择 "Open with Live Server"

## 部署说明

构建后的 `dist` 目录可以部署到任何静态文件服务器，例如：
- Nginx
- Apache
- GitHub Pages
- Netlify
- Vercel

**注意：** 如果部署到子目录（如 `/app/`），需要修改 `vite.config.ts` 中的 `base` 配置：

```typescript
export default defineConfig({
  base: '/app/', // 改为你的子目录路径
  // ...
})
```

## 常见问题

### Q: 为什么页面是空白的？
A: 可能是直接打开了 HTML 文件。请使用本地服务器预览。

### Q: 资源文件 404 错误？
A: 确保使用 HTTP 服务器预览，不要使用 `file://` 协议。

### Q: Service Worker 错误？
A: Service Worker 只能在 HTTP/HTTPS 协议下工作，不能使用 `file://` 协议。

