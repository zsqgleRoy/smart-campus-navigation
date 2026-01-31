package com.szcx.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.data.model.Website
import com.szcx.data.repository.CategoryRepository
import com.szcx.data.repository.VisitRecordRepository
import com.szcx.data.repository.WebsiteRepository
import com.szcx.util.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val websiteRepository: WebsiteRepository,
    private val categoryRepository: CategoryRepository,
    private val visitRecordRepository: VisitRecordRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()
    
    val categories = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val websites: StateFlow<List<Website>> = _selectedCategory.flatMapLatest { categoryId ->
        websiteRepository.getWebsitesByCategory(categoryId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val favorites: StateFlow<String> = preferencesManager.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    
    fun selectCategory(categoryId: String) {
        _selectedCategory.value = categoryId
    }
    
    fun openWebsite(website: Website) {
        viewModelScope.launch {
            websiteRepository.updateAccessInfo(website.id, System.currentTimeMillis())
            visitRecordRepository.insertVisitRecord(
                com.szcx.data.model.VisitRecord(
                    websiteId = website.id,
                    websiteName = website.name,
                    websiteUrl = website.url,
                    visitTime = System.currentTimeMillis()
                )
            )
        }
    }
    
    fun toggleFavorite(website: Website) {
        viewModelScope.launch {
            val favString = favorites.first()
            val currentFavorites = favString.split(",").filter { it.isNotEmpty() }.toMutableList()
            val isFavorite = currentFavorites.contains(website.id)
            
            if (isFavorite) {
                currentFavorites.remove(website.id)
            } else {
                currentFavorites.add(website.id)
            }
            
            preferencesManager.setFavorites(currentFavorites.joinToString(","))
            websiteRepository.updateFavoriteStatus(website.id, !isFavorite)
        }
    }
    
    fun deleteWebsite(website: Website) {
        viewModelScope.launch {
            websiteRepository.deleteWebsite(website)
            // 如果删除的是收藏的网站，也要从收藏列表中移除
            val favString = favorites.first()
            val currentFavorites = favString.split(",").filter { it.isNotEmpty() }.toMutableList()
            if (currentFavorites.contains(website.id)) {
                currentFavorites.remove(website.id)
                preferencesManager.setFavorites(currentFavorites.joinToString(","))
            }
        }
    }
}

