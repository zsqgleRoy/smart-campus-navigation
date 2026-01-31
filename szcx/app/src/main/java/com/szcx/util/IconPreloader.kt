package com.szcx.util

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import com.szcx.data.database.SzcxDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

object IconPreloader {
    /**
     * 预加载所有网站的图标到缓存
     * 在后台异步执行，不阻塞主线程
     */
    suspend fun preloadAllIcons(context: Context, database: SzcxDatabase) = withContext(Dispatchers.IO) {
        try {
            val websites = database.websiteDao().getAllWebsites().first()
            val imageLoader = ImageLoader(context)
            
            // 获取所有需要预加载的图标URL（去重）
            val iconUrls = websites.mapNotNull { website ->
                val iconUrl = website.iconUrl.ifEmpty { FormatUtils.getWebsiteIcon(website.url) }
                if (iconUrl.isNotEmpty()) iconUrl else null
            }.distinct()
            
            // 并发预加载图标，每批20个并发
            iconUrls.chunked(20).forEach { batch ->
                batch.map { iconUrl ->
                    async {
                        try {
                            val request = ImageRequest.Builder(context)
                                .data(iconUrl)
                                .memoryCacheKey(iconUrl)
                                .diskCacheKey(iconUrl)
                                .build()
                            
                            // 执行请求以触发缓存
                            imageLoader.execute(request)
                        } catch (e: Exception) {
                            // 忽略单个图标加载失败，继续加载其他图标
                            null
                        }
                    }
                }.awaitAll()
            }
        } catch (e: Exception) {
            // 预加载失败不影响应用启动
        }
    }
}

