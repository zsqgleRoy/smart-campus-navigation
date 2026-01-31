package com.szcx.ui.addwebsite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.data.model.Website
import com.szcx.data.repository.CategoryRepository
import com.szcx.data.repository.WebsiteRepository
import com.szcx.util.FormatUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class AddWebsiteViewModel(
    private val websiteRepository: WebsiteRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    val categories: StateFlow<List<com.szcx.data.model.Category>> = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun addWebsite(
        name: String,
        url: String,
        iconUrl: String = "",
        description: String,
        categoryId: String,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 生成唯一ID
                val id = UUID.randomUUID().toString()
                
                // 如果提供了图标URL则使用，否则自动生成
                val finalIconUrl = if (iconUrl.isBlank()) {
                    FormatUtils.getWebsiteIcon(url)
                } else {
                    iconUrl.trim()
                }
                
                // 创建网站对象
                val website = Website(
                    id = id,
                    name = name.trim(),
                    url = url.trim(),
                    iconUrl = finalIconUrl,
                    category = categoryId,
                    description = description.trim(),
                    isHot = false,
                    isFavorite = false,
                    accessCount = 0,
                    lastAccess = 0
                )
                
                // 插入数据库
                websiteRepository.insertWebsite(website)
                
                callback(true, "添加成功")
            } catch (e: Exception) {
                callback(false, "添加失败：${e.message}")
            }
        }
    }
}

