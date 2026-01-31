package com.szcx.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.data.model.VisitRecord
import com.szcx.data.model.Website
import com.szcx.data.repository.VisitRecordRepository
import com.szcx.data.repository.WebsiteRepository
import com.szcx.util.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val websiteRepository: WebsiteRepository,
    private val visitRecordRepository: VisitRecordRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
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
    
    val visitHistory: StateFlow<List<VisitRecord>> = visitRecordRepository.getVisitHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun openWebsite(website: Website) {
        viewModelScope.launch {
            websiteRepository.updateAccessInfo(website.id, System.currentTimeMillis())
            visitRecordRepository.insertVisitRecord(
                VisitRecord(
                    websiteId = website.id,
                    websiteName = website.name,
                    websiteUrl = website.url,
                    visitTime = System.currentTimeMillis()
                )
            )
        }
    }
    
    fun openHistory(record: VisitRecord) {
        viewModelScope.launch {
            visitRecordRepository.insertVisitRecord(
                VisitRecord(
                    websiteId = record.websiteId,
                    websiteName = record.websiteName,
                    websiteUrl = record.websiteUrl,
                    visitTime = System.currentTimeMillis()
                )
            )
        }
    }
    
    fun deleteHistory(record: VisitRecord) {
        viewModelScope.launch {
            visitRecordRepository.deleteVisitRecord(record.id)
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
    
    fun deleteWebsite(website: Website) {
        viewModelScope.launch {
            websiteRepository.deleteWebsite(website)
            // 如果删除的是收藏的网站，也要从收藏列表中移除
            val currentFavorites = favorites.value.split(",").filter { it.isNotEmpty() }.toMutableList()
            if (currentFavorites.contains(website.id)) {
                currentFavorites.remove(website.id)
                preferencesManager.setFavorites(currentFavorites.joinToString(","))
            }
        }
    }
}

