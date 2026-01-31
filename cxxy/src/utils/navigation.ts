// 导航工具函数 - 兼容 Android WebView

/**
 * 检测是否在 Android WebView 环境中
 */
export function isAndroidWebView(): boolean {
  if (typeof window === 'undefined') return false
  
  const ua = navigator.userAgent || ''
  const href = window.location.href || ''
  
  return (
    /Android/i.test(ua) ||
    href.includes('android_asset') ||
    href.includes('file:///android_asset') ||
    window.location.protocol === 'file:'
  )
}

/**
 * 安全地打开外部链接
 * 在 Android WebView 中使用 location.href，在普通浏览器中使用 window.open
 */
export function openExternalUrl(url: string, target: string = '_blank'): void {
  if (!url) {
    console.warn('URL is empty')
    return
  }

  // 确保 URL 是完整的
  let fullUrl = url
  if (!url.startsWith('http://') && !url.startsWith('https://') && !url.startsWith('file://')) {
    fullUrl = `https://${url}`
  }

  try {
    if (isAndroidWebView()) {
      // Android WebView 环境：使用 location.href
      // 注意：这会在当前窗口打开，而不是新窗口
      console.log('Opening URL in Android WebView:', fullUrl)
      window.location.href = fullUrl
    } else {
      // 普通浏览器环境：使用 window.open
      const newWindow = window.open(fullUrl, target)
      if (!newWindow) {
        // 如果弹出窗口被阻止，回退到 location.href
        console.warn('Popup blocked, using location.href instead')
        window.location.href = fullUrl
      }
    }
  } catch (error) {
    console.error('Failed to open URL:', error)
    // 最后的回退方案
    try {
      window.location.href = fullUrl
    } catch (e) {
      console.error('Failed to navigate:', e)
    }
  }
}

/**
 * 在新窗口/标签页中打开链接（如果支持）
 */
export function openInNewTab(url: string): void {
  openExternalUrl(url, '_blank')
}

/**
 * 在当前窗口打开链接
 */
export function openInCurrentWindow(url: string): void {
  if (!url) return
  
  let fullUrl = url
  if (!url.startsWith('http://') && !url.startsWith('https://') && !url.startsWith('file://')) {
    fullUrl = `https://${url}`
  }
  
  window.location.href = fullUrl
}

