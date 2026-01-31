package com.szcx.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.data.model.Website
import com.szcx.data.repository.VisitRecordRepository
import com.szcx.data.repository.WebsiteRepository
import com.szcx.util.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchViewModel(
    private val websiteRepository: WebsiteRepository,
    private val visitRecordRepository: VisitRecordRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()
    
    val searchResults: StateFlow<List<Website>> = _searchKeyword.flatMapLatest { keyword ->
        if (keyword.isBlank()) {
            flowOf(emptyList())
        } else {
            websiteRepository.searchWebsites(keyword)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val favorites: StateFlow<String> = preferencesManager.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    
    val searchHistory: StateFlow<List<String>> = preferencesManager.searchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setSearchKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }
    
    fun performSearch(keyword: String) {
        _searchKeyword.value = keyword
        if (keyword.isNotBlank()) {
            viewModelScope.launch {
                preferencesManager.addSearchHistory(keyword)
            }
        }
    }
    
    fun clearSearchHistory() {
        viewModelScope.launch {
            preferencesManager.clearSearchHistory()
        }
    }
    
    fun removeSearchHistoryItem(keyword: String) {
        viewModelScope.launch {
            preferencesManager.removeSearchHistoryItem(keyword)
        }
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

