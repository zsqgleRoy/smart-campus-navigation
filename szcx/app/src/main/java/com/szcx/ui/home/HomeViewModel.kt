package com.szcx.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.data.model.Website
import com.szcx.data.repository.CategoryRepository
import com.szcx.data.repository.VisitRecordRepository
import com.szcx.data.repository.WebsiteRepository
import com.szcx.util.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val websiteRepository: WebsiteRepository,
    private val categoryRepository: CategoryRepository,
    private val visitRecordRepository: VisitRecordRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    val useAccessCountHot: StateFlow<Boolean> = preferencesManager.useAccessCountHot
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    
    val hotWebsites: StateFlow<List<Website>> = useAccessCountHot.flatMapLatest { useAccessCount ->
        if (useAccessCount) {
            websiteRepository.getHotWebsitesByAccessCount()
        } else {
            websiteRepository.getHotWebsites()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val categories = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val favorites: StateFlow<String> = preferencesManager.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
    
    val favoriteWebsites: StateFlow<List<Website>> = favorites.flatMapLatest { favoriteIds ->
        val ids = favoriteIds.split(",").filter { it.isNotEmpty() }
        if (ids.isEmpty()) {
            flowOf(emptyList())
        } else {
            websiteRepository.getFavoriteWebsites(ids)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun openWebsite(website: Website) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            websiteRepository.updateAccessInfo(website.id, currentTime)
            visitRecordRepository.insertVisitRecord(
                com.szcx.data.model.VisitRecord(
                    websiteId = website.id,
                    websiteName = website.name,
                    websiteUrl = website.url,
                    visitTime = currentTime
                )
            )
            if (!useAccessCountHot.value && visitRecordRepository.getDistinctWebsiteCount() >= 8) {
                preferencesManager.setUseAccessCountHot(true)
            }
        }
    }
    
    fun toggleFavorite(website: Website) {
        viewModelScope.launch {
            val currentFavorites = favorites.value.split(",").filter { it.isNotEmpty() }.toMutableList()
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
}

