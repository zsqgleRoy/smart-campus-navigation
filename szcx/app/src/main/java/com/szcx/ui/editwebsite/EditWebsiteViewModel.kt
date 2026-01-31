package com.szcx.ui.editwebsite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.data.model.Website
import com.szcx.data.repository.CategoryRepository
import com.szcx.data.repository.WebsiteRepository
import com.szcx.util.FormatUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditWebsiteViewModel(
    private val websiteRepository: WebsiteRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    private val _website = MutableStateFlow<Website?>(null)
    val website: StateFlow<Website?> = _website.asStateFlow()
    
    val categories: StateFlow<List<com.szcx.data.model.Category>> = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun loadWebsite(id: String) {
        viewModelScope.launch {
            _website.value = websiteRepository.getWebsiteById(id)
        }
    }
    
    fun updateWebsite(
        website: Website,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // 如果图标URL为空，自动生成
                val finalIconUrl = if (website.iconUrl.isBlank()) {
                    FormatUtils.getWebsiteIcon(website.url)
                } else {
                    website.iconUrl
                }
                
                // 更新网站对象
                val updatedWebsite = website.copy(iconUrl = finalIconUrl)
                
                // 更新数据库
                websiteRepository.updateWebsite(updatedWebsite)
                
                callback(true, "修改成功")
            } catch (e: Exception) {
                callback(false, "修改失败：${e.message}")
            }
        }
    }
}






